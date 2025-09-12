package com.pickmeup.taxi_service.business;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickmeup.taxi_service.domain.models.Driver;
import com.pickmeup.taxi_service.domain.models.Location;
import com.pickmeup.taxi_service.domain.services.DriverService;
import com.pickmeup.taxi_service.domain.services.RideService;
import com.pickmeup.taxi_service.domain.transferobjects.AvailableDriver;
import com.pickmeup.taxi_service.domain.utils.DistanceCalculator;

@Service
public class DriverBusinessService {

    private final DriverService driverService;
    private final RideService rideService;

    public DriverBusinessService(DriverService driverService, RideService rideService) {
        this.driverService = driverService;
        this.rideService = rideService;
    }

    @Transactional
    public void completeRide(String rideId, String driverId, double latitude, double longitute) {
        rideService.updateRide(rideId);
        driverService.registerAvailability(driverId, new Location(latitude,longitute));
    }

    public List<AvailableDriver> getAvailableDrivers(Location location) {
        return DistanceCalculator.calculateAvailableDriverDistances(this.driverService.getDrivers(true), location.latitude(), location.longitude());
    }


}
