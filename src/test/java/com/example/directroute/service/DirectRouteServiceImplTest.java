package com.example.directroute.service;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectRouteServiceImplTest {

    @Mock
    private DataService dataService;
    @InjectMocks
    private DirectRouteServiceImpl subj;

    @Test
    void availableDirectRouteFromTo() {
        int routeNumber = 1;
        int fromBusStop = 3;
        int toBusStop = 4;
        when(dataService.findRoutesNumbersByBusStop(fromBusStop)).thenReturn(Lists.list(routeNumber));
        when(dataService.findRoutesNumbersByBusStop(toBusStop)).thenReturn(Lists.list(routeNumber));
        when(dataService.findRouteByRouteNumber(routeNumber)).thenReturn(Lists.list(fromBusStop, toBusStop));

        assertTrue(subj.availableDirectRouteFromTo(fromBusStop, toBusStop));
    }
}
