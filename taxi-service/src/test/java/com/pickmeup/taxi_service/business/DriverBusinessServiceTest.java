package com.pickmeup.taxi_service.business;

import com.pickmeup.taxi_service.domain.models.Driver;
import com.pickmeup.taxi_service.domain.models.Location;
import com.pickmeup.taxi_service.domain.services.DriverService;
import com.pickmeup.taxi_service.domain.services.RideService;
import com.pickmeup.taxi_service.domain.exceptions.ResourceNotFoundException;
import com.pickmeup.taxi_service.domain.transferobjects.AvailableDriver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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

    @Test
    void getAvailableDrivers_returnsSortedByDistance() {
        // given: rider location at (0,0)
        Location riderLocation = new Location(0.0, 0.0);

        // and drivers with known positions
        Driver driver1 = new Driver("D1", "John", new Location(1.0, 1.0)); // distance ≈ 1.41
        Driver driver2 = new Driver("D2", "Geoff", new Location(3.0, 4.0)); // distance = 5.0
        Driver driver3 = new Driver("D3", "Marcos", new Location(0.0, 2.0)); // distance = 2.0

        when(driverService.getDrivers(true)).thenReturn(List.of(driver1, driver2, driver3));

        // when
        List<AvailableDriver> result = driverBusinessService.getAvailableDrivers(riderLocation);

        // then: drivers are sorted by ascending distance
        assertThat(result).extracting(AvailableDriver::driver)
                .containsExactly(driver1, driver3, driver2);

        assertThat(result).extracting(AvailableDriver::distance)
                .containsExactly(1.4142135623730951, 2.0, 5.0);

        // and service was invoked
        verify(driverService).getDrivers(true);
    }

    @Test
    void getAvailableDrivers_emptyListWhenNoDrivers() {
        // given
        when(driverService.getDrivers(true)).thenReturn(List.of());

        // when
        List<AvailableDriver> result = driverBusinessService.getAvailableDrivers(new Location(10.0, 10.0));

        // then
        assertThat(result).isEmpty();
        verify(driverService).getDrivers(true);
    }

    @Test
    void getAvailableDrivers_singleDriver() {
        // given
        Location riderLocation = new Location(0.0, 0.0);
        Driver driver = new Driver("D1", "John", new Location(0.0, 3.0)); // distance = 3

        when(driverService.getDrivers(true)).thenReturn(List.of(driver));

        // when
        List<AvailableDriver> result = driverBusinessService.getAvailableDrivers(riderLocation);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).driver()).isEqualTo(driver);
        assertThat(result.get(0).distance()).isEqualTo(3.0);
        verify(driverService).getDrivers(true);
    }
}