package com.pickmeup.taxi_service.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.pickmeup.taxi_service.domain.enums.RideStatus;
import com.pickmeup.taxi_service.domain.models.Ride;
import com.pickmeup.taxi_service.domain.repositories.RideRepository;

@Repository
public class InMemoryRideRepository implements RideRepository {
    private final ConcurrentHashMap<String, Ride> rides = new ConcurrentHashMap<>();

    @Override
    public void save(Ride ride) {
        rides.put(ride.getId(), ride);
    }

    @Override
    public Optional<Ride> findById(String rideId) {
        return Optional.ofNullable(rides.get(rideId));
    }

    @Override
    public List<Ride> findByStatus(RideStatus status) {
        return rides.values().stream()
                .filter(ride -> ride.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ride> findAll() {
        return new ArrayList<>(rides.values());
    }
}