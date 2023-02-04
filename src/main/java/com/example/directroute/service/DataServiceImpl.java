package com.example.directroute.service;

import com.example.directroute.service.exception.InternalServerException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private static final Map<Integer, List<Integer>> BUS_STOP_TO_ROUTES_NUMBERS = new ConcurrentHashMap<>();
    private static final Map<Integer, List<Integer>> ROUTE_NUMBER_TO_ROUTE = new ConcurrentHashMap<>();

    @Value("${data.path}")
    private String dataPath;
    @Value("${data.line-size-for-parallel-load}")
    private Integer dataLineSizeForParallelLoad;
    @Value("${data.load-data-timeout-in-seconds}")
    private Integer loadDataTimeoutInSeconds;

    private final ExecutorService executorService;

    @PostConstruct
    private void postConstruct() {
        fillCacheFromDataFile();
    }

    @Override
    public List<Integer> findRoutesNumbersByBusStop(Integer busStop) {
        return BUS_STOP_TO_ROUTES_NUMBERS.get(busStop);
    }

    @Override
    public List<Integer> findRouteByRouteNumber(Integer routeNumber) {
        return ROUTE_NUMBER_TO_ROUTE.get(routeNumber);
    }

    private void fillCacheFromDataFile() {
        List<String> lines = readAllLinesFromData(Paths.get(dataPath));
        try {
            executorService.invokeAll(divideLinesIntoTasks(lines), loadDataTimeoutInSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new InternalServerException(e);
        }
    }

    private List<String> readAllLinesFromData(Path dataPath) {
        try {
            return Files.readAllLines(dataPath);
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }

    private List<Callable<Void>> divideLinesIntoTasks(List<String> lines) {
        List<Callable<Void>> tasks = new ArrayList<>();
        int processors = Runtime.getRuntime().availableProcessors();
        int size = lines.size();
        int sizeForThread = size > dataLineSizeForParallelLoad ? size / processors : size;
        int current = 0;
        for (int i = 0; i < processors && current < size; i++) {
            if (i == processors - 1) {
                tasks.add(new ParseLinesToCache(lines, current, lines.size()));
            } else {
                tasks.add(new ParseLinesToCache(lines, current, current + sizeForThread));
            }
            current += sizeForThread + 1;
        }
        return tasks;
    }

    @RequiredArgsConstructor
    private static class ParseLinesToCache implements Callable<Void> {

        private final List<String> lines;
        private final int from;
        private final int to;

        @Override
        public Void call() {
            for (int lineNumber = from; lineNumber < to; lineNumber++) {
                String[] route = lines.get(lineNumber).split(" ");
                Integer routeNumber = Integer.valueOf(route[0]);
                List<Integer> busStopsForRoute = new ArrayList<>();
                for (int i = 1; i < route.length; i++) {
                    Integer busStop = Integer.valueOf(route[i]);
                    busStopsForRoute.add(busStop);
                    List<Integer> routesNumbers = BUS_STOP_TO_ROUTES_NUMBERS.get(busStop);
                    if (routesNumbers == null) {
                        List<Integer> newRoutesNumbers = new CopyOnWriteArrayList<>();
                        newRoutesNumbers.add(routeNumber);
                        BUS_STOP_TO_ROUTES_NUMBERS.put(busStop, newRoutesNumbers);
                    } else {
                        routesNumbers.add(routeNumber);
                    }
                }
                ROUTE_NUMBER_TO_ROUTE.put(routeNumber, busStopsForRoute);
            }
            return null;
        }
    }
}
