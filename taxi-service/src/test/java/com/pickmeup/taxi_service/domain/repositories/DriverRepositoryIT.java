package com.pickmeup.taxi_service.domain.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import com.pickmeup.taxi_service.domain.models.Driver;
import com.pickmeup.taxi_service.domain.models.Location;

@SpringBootTest
public class DriverRepositoryIT {

    @Autowired
    private DriverRepository driverRepository;

    @Test
    void testSaveAndFindById() {
        Driver driver = new Driver("driver1", "John Doe", new Location(40.7128, -74.0060));
        driverRepository.save(driver);

        Optional<Driver> foundDriver = driverRepository.findById("driver1");
        assertTrue(foundDriver.isPresent());
        assertEquals("John Doe", foundDriver.get().getName());
    }

    @Test
    void testFindAllDrivers() {
        Driver driver1 = new Driver("driver1", "John Doe", new Location(40.7128, -74.0060));
        Driver driver2 = new Driver("driver2", "Jane Smith", new Location(34.0522, -118.2437));
        driverRepository.save(driver1);
        driverRepository.save(driver2);

        List<Driver> allDrivers = driverRepository.findAll();
        assertEquals(2, allDrivers.size());
    }

}
