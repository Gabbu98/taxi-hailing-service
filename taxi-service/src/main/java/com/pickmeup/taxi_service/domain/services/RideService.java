package com.pickmeup.taxi_service.domain.services;

import org.springframework.stereotype.Service;

import com.pickmeup.taxi_service.domain.enums.RideStatus;
import com.pickmeup.taxi_service.domain.exceptions.ResourceNotFoundException;
import com.pickmeup.taxi_service.domain.models.Driver;
import com.pickmeup.taxi_service.domain.models.Location;
import com.pickmeup.taxi_service.domain.models.Ride;
import com.pickmeup.taxi_service.domain.models.Rider;
import com.pickmeup.taxi_service.domain.repositories.RideRepository;

@Service
public class RideService {

    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    public Ride getRideById(String id){
        return rideRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ride not found with ID: %s".formatted(id)));
    }

    public void updateRide(String id) {
        Ride ride = getRideById(id);

        // Mark the ride as completed
        ride.setStatus(RideStatus.COMPLETED);
        rideRepository.save(ride);
    }

    public Ride createRide(Driver driver, Rider rider, Location pickupLocation, Location dropoffLocation){
        final Ride ride = new Ride(rider, driver, pickupLocation, dropoffLocation);
        rideRepository.save(ride);
        return ride;
    }
}
