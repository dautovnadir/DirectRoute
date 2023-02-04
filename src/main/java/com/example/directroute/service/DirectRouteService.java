package com.example.directroute.service;

public interface DirectRouteService {

    /**
     * Имеется ли беспересадочный маршрут
     * @param from номер начальной автобусной остановки
     * @param to номер конечной автобусной остановки
     * @return true - если имеется, false - если отсутствует
     */
    boolean availableDirectRouteFromTo(Integer from, Integer to);
}
