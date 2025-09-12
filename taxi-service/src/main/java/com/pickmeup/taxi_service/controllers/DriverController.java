package com.pickmeup.taxi_service.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pickmeup.taxi_service.business.DriverBusinessService;
import com.pickmeup.taxi_service.domain.models.Location;
import com.pickmeup.taxi_service.domain.transferobjects.AvailableDriver;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    private final DriverBusinessService driverBusinessService;

    public DriverController(DriverBusinessService driverBusinessService) {
        this.driverBusinessService = driverBusinessService;
    }

    /**
     * GET /drivers?latitude={latitude}&longitude={longitude}
     * Find available drivers near a specific location.
     */
    @GetMapping
    public ResponseEntity<List<AvailableDriver>> getAvailableDrivers(
            @RequestParam double latitude,
            @RequestParam double longitude) {

        Location location = new Location(latitude, longitude);
        List<AvailableDriver> availableDrivers = driverBusinessService.getAvailableDrivers(location);

        return ResponseEntity.ok(availableDrivers);
    }
}
