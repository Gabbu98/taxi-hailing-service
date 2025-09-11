package com.pickmeup.taxi_service.business;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pickmeup.taxi_service.domain.models.Driver;
import com.pickmeup.taxi_service.domain.models.Location;
import com.pickmeup.taxi_service.domain.models.Ride;
import com.pickmeup.taxi_service.domain.services.DriverService;
import com.pickmeup.taxi_service.domain.services.RideService;
import com.pickmeup.taxi_service.domain.services.RiderService;
import com.pickmeup.taxi_service.domain.transferobjects.AvailableDriver;
import com.pickmeup.taxi_service.domain.transferobjects.RideDetails;
import com.pickmeup.taxi_service.domain.utils.DistanceCalculator;

@Service
public class RideBusinessService {

    private DriverService driverService;
    private RideService rideService;
    private RiderService riderService;

    public RideDetails requestRide(String riderId, Location pickupLocation, Location dropoffLocation) {

        List<Driver> availableDrivers = driverService.getDrivers(true);
        AvailableDriver nearestDriver;

        // find nearest driver
        if(availableDrivers.isEmpty()) {
            return new RideDetails(null, 0);
        }

        nearestDriver = DistanceCalculator.calculateAvailableDriverDistances(availableDrivers, pickupLocation.latitude(), pickupLocation.longitude()).getFirst();
        // assign driver and mark as unavailable
        Driver updatedDriver = nearestDriver.driver().setAvailable(false);

        driverService.updateDriver(updatedDriver);

        // create the ride
        final Ride ride = rideService.createRide(updatedDriver, riderService.getRiderById(riderId), pickupLocation, dropoffLocation);
        // return driver and ride details

        return new RideDetails(ride, nearestDriver.distance());
    }
}
