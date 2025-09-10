package com.pickmeup.taxi_service.business;

import com.pickmeup.taxi_service.domain.models.Driver;
import com.pickmeup.taxi_service.domain.models.Location;
import com.pickmeup.taxi_service.domain.services.DriverService;
import com.pickmeup.taxi_service.domain.services.RideService;
import com.pickmeup.taxi_service.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DriverBusinessServiceTest {

    @Mock
    private DriverService driverService;

    @Mock
    private RideService rideService;

    @InjectMocks
    private DriverBusinessService driverBusinessService;

    private String driverId;
    private String rideId;
    private Location driverLocation;
    private Driver driver;

    @BeforeEach
    void setUp() {
        driverId = "driver1";
        rideId = "ride1";
        driverLocation = new Location(40.7128, -74.0060);
        driver = new Driver(driverId, "John Doe", driverLocation);
    }

    @Test
    void testRegisterAvailability_updatesExistingDriver() {
        // Arrange
        when(driverService.findDriverById(driverId)).thenReturn(driver);

        // Act
        driverBusinessService.registerAvailability(driverId, driverLocation);

        // Assert
        verify(driverService, times(1)).findDriverById(driverId);
        verify(driverService, times(1)).updateDriver(driver);
    }

    @Test
    void testRegisterAvailability_throwsException_ifDriverNotFound() {
        // Arrange
        when(driverService.findDriverById(driverId)).thenThrow(new ResourceNotFoundException("Driver not found"));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            driverBusinessService.registerAvailability(driverId, driverLocation);
        });

        verify(driverService, never()).updateDriver(any(Driver.class));
    }

    @Test
    void testCompleteRide_updatesRideAndDriverAvailability() {
        // Arrange
        Location endLocation = new Location(34.0522, -118.2437);
        when(driverService.findDriverById(driverId)).thenReturn(driver);

        // Act
        driverBusinessService.completeRide(rideId, driverId, endLocation.latitude(), endLocation.longitude());

        // Assert
        verify(rideService, times(1)).updateRide(rideId);
        verify(driverService, times(1)).findDriverById(driverId);
        verify(driverService, times(1)).updateDriver(any(Driver.class));
    }

    @Test
    void testCompleteRide_throwsException_ifDriverNotFound() {
        // Arrange
        when(driverService.findDriverById(driverId)).thenThrow(new ResourceNotFoundException("Driver not found"));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            driverBusinessService.completeRide(rideId, driverId, 0, 0);
        });

        verify(rideService).updateRide(anyString());
    }
}