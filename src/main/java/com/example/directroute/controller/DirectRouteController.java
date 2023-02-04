package com.example.directroute.controller;


import com.example.directroute.controller.dto.DirectRouteResponse;
import com.example.directroute.service.DirectRouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor

public class DirectRouteController{

    private final DirectRouteService directRouteService;
    @PostMapping("/direct")
    public ResponseEntity<DirectRouteResponse> directRouteFromTo(Integer from, Integer to) {
        DirectRouteResponse response = new DirectRouteResponse();
        response.setFrom(from);
        response.setTo(to);
        response.setDirect(directRouteService.availableDirectRouteFromTo(from, to));
        return ResponseEntity.ok(response);
    }
}
