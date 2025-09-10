package com.pickmeup.taxi_service.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.pickmeup.taxi_service.domain.enums.RideStatus;
import com.pickmeup.taxi_service.domain.models.Ride;

public interface RideRepository {
    void save(Ride ride);
    Optional<Ride> findById(String rideId);
    List<Ride> findByStatus(RideStatus status);
    List<Ride> findAll();
}
