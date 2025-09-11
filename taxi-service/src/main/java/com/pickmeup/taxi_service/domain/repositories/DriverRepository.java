package com.pickmeup.taxi_service.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.pickmeup.taxi_service.domain.models.Driver;

public interface DriverRepository {
    void save(Driver driver);
    Optional<Driver> findById(String driverId);
    List<Driver> findAll();
    List<Driver> findAvailableDrivers();
    List<Driver> findNonAvailableDrivers();
    void deleteById(String driverId);
}
