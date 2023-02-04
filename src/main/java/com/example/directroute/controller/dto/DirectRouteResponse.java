package com.example.directroute.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirectRouteResponse {
    private int from;
    private int to;
    boolean direct;
}
