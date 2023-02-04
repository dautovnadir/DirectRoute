package com.example.directroute.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class DirectRouteServiceImpl implements DirectRouteService {

    private final DataService dataService;

    @Override
    public boolean availableDirectRouteFromTo(Integer from, Integer to) {
        List<Integer> sourceFromRoutesNumbers = dataService.findRoutesNumbersByBusStop(from);
        if (sourceFromRoutesNumbers == null) {
            return false;
        }
        List<Integer> fromRoutesNumbers = new ArrayList<>(sourceFromRoutesNumbers);
        List<Integer> sourceToRoutesNumbers = dataService.findRoutesNumbersByBusStop(to);
        if (sourceToRoutesNumbers == null) {
            return false;
        }
        fromRoutesNumbers.retainAll(sourceToRoutesNumbers);

        return fromRoutesNumbers.stream().anyMatch(routeNumber -> {
            List<Integer> route = dataService.findRouteByRouteNumber(routeNumber);
            int start = route.indexOf(from);
            if (start != -1) {
                int end = route.indexOf(to);
                if (end != -1) {
                    return start < end;
                }
            }
            return false;
        });
    }

}
