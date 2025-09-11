package com.pickmeup.taxi_service.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.pickmeup.taxi_service.domain.models.Driver;
import com.pickmeup.taxi_service.domain.repositories.DriverRepository;

@Repository
public class InMemoryDriverRepository implements DriverRepository {
    private final ConcurrentHashMap<String, Driver> drivers = new ConcurrentHashMap<>();

    @Override
    public void save(Driver driver) {
        drivers.put(driver.getId(), driver);
    }

    @Override
    public Optional<Driver> findById(String driverId) {
        return Optional.ofNullable(drivers.get(driverId));
    }

    @Override
    public List<Driver> findAll() {
        return new ArrayList<>(drivers.values());
    }

    @Override
    public List<Driver> findAvailableDrivers() {
        return drivers.values().stream()
                .filter(Driver::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public List<Driver> findNonAvailableDrivers() {
        return drivers.values().stream()
                .filter(driver -> !driver.isAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String driverId) {
        drivers.remove(driverId);
    }
}
