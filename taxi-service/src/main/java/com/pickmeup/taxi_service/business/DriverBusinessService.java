package com.pickmeup.taxi_service.business;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

import com.pickmeup.taxi_service.domain.enums.RideStatus;
import com.pickmeup.taxi_service.domain.models.Driver;
import com.pickmeup.taxi_service.domain.models.Location;
import com.pickmeup.taxi_service.domain.repositories.DriverRepository;
import com.pickmeup.taxi_service.domain.services.DriverService;
import com.pickmeup.taxi_service.domain.services.RideService;

@Service
public class DriverBusinessService {

    private final DriverService driverService;
    private final RideService rideService;

    public DriverBusinessService(DriverService driverService, RideService rideService) {
        this.driverService = driverService;
        this.rideService = rideService;
    }

    public void registerAvailability(String driverId, Location location) {
        // Find if the driver already exists
        Driver driver = driverService.findDriverById(driverId);

        driver.setCurrentLocation(location);
        driver.setAvailable(true);

        driverService.updateDriver(driver);
    }
// rollback
    public void completeRide(String rideId, String driverId, double latitude, double longitute) {
        rideService.updateRide(rideId);
        registerAvailability(driverId, new Location(latitude,longitute));
    }

}
