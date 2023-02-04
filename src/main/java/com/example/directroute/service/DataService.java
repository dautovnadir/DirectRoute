package com.example.directroute.service;

import java.util.List;

public interface DataService {

    /**
     * Поиск номеров маршрутов по автобусной остановке
     * @param busStop автобусная остановка
     * @return список маршрутов проходящих через данную автобусную остановку
     */
    List<Integer> findRoutesNumbersByBusStop(Integer busStop);

    /**
     * Поиск маршрута по номеру
     * @param routeNumber номер маршрута
     * @return маршрут
     */
    List<Integer> findRouteByRouteNumber(Integer routeNumber);
}
