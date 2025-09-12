package com.pickmeup.taxi_service.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickmeup.taxi_service.domain.exceptions.ResourceNotFoundException;
import com.pickmeup.taxi_service.domain.models.Driver;
import com.pickmeup.taxi_service.domain.models.Location;
import com.pickmeup.taxi_service.domain.repositories.DriverRepository;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Driver findDriverById(String driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: %s".formatted(driverId)));
    }

    public void updateDriver(Driver driver) {
        driverRepository.save(driver);
    }

    public List<Driver> getDrivers(@Nullable Boolean isAvailable) {
        if (Boolean.TRUE.equals(isAvailable)) {
            return this.driverRepository.findAvailableDrivers();
        }
        return Boolean.FALSE.equals(isAvailable) ? this.driverRepository.findNonAvailableDrivers() : this.driverRepository.findAll();
    }

    public void registerAvailability(String driverId, Location location) {
        // Find if the driver already exists
        Driver driver = findDriverById(driverId);

        driver.setCurrentLocation(location);
        driver.setAvailable(true);

        updateDriver(driver);
    }
}
