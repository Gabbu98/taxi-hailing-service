package com.pickmeup.taxi_service.business;

import com.pickmeup.taxi_service.domain.models.Driver;
import com.pickmeup.taxi_service.domain.models.Location;
import com.pickmeup.taxi_service.domain.models.Ride;
import com.pickmeup.taxi_service.domain.models.Rider;
import com.pickmeup.taxi_service.domain.services.DriverService;
import com.pickmeup.taxi_service.domain.services.RideService;
import com.pickmeup.taxi_service.domain.services.RiderService;
import com.pickmeup.taxi_service.domain.transferobjects.RideDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(MockitoExtension.class)
class RideBusinessServiceTest {

    @Mock
    private DriverService driverService;

    @Mock
    private RideService rideService;

    @Mock
    private RiderService riderService;

    @InjectMocks
    private RideBusinessService rideBusinessService;

    private PodamFactory factory;

    @BeforeEach
    void setUp() {
        factory = new PodamFactoryImpl();
    }

    @Test
    void requestRide_returnsEmptyWhenNoDriversAvailable() {
        when(driverService.getDrivers(true)).thenReturn(List.of());

        RideDetails result = rideBusinessService.requestRide(
                "rider1",
                new Location(0.0, 0.0),
                new Location(1.0, 1.0)
        );

        assertThat(result.ride()).isNull();
        assertThat(result.distance()).isEqualTo(0);

        verify(driverService).getDrivers(true);
        verifyNoInteractions(rideService, riderService);
    }

    @Test
    void requestRide_assignsNearestDriverAndCreatesRide() {
        Driver driver1 = factory.manufacturePojo(Driver.class);
        driver1.setAvailable(true);
        driver1.setCurrentLocation(new Location(1.0, 1.0));

        Driver driver2 = factory.manufacturePojo(Driver.class);
        driver2.setAvailable(true);
        driver2.setCurrentLocation(new Location(5.0, 5.0));

        Rider rider = factory.manufacturePojo(Rider.class);

        when(driverService.getDrivers(true)).thenReturn(List.of(driver1, driver2));
        when(riderService.getRiderById(rider.getId())).thenReturn(rider);

        Ride expectedRide = new Ride(rider, driver1, new Location(0.0, 0.0), new Location(2.0, 2.0));
        when(rideService.createRide(eq(driver1), eq(rider), any(Location.class), any(Location.class)))
                .thenReturn(expectedRide);

        RideDetails result = rideBusinessService.requestRide(
                rider.getId(),
                new Location(0.0, 0.0),
                new Location(2.0, 2.0)
        );

        assertThat(result.ride().getDriver()).isEqualTo(driver1);
        assertThat(result.ride().getRider()).isEqualTo(rider);
        assertThat(result.distance()).isLessThan(2.0);

        verify(driverService).updateDriver(driver1);
        verify(rideService).createRide(eq(driver1), eq(rider), any(Location.class), any(Location.class));
    }


    @Test
    void requestRide_picksNearestDriver() {
        Location pickup = new Location(0.0, 0.0);
        Location dropoff = new Location(10.0, 10.0);

        Driver closeDriver = factory.manufacturePojo(Driver.class);
        closeDriver.setCurrentLocation(new Location(1.0, 1.0));
        closeDriver.setAvailable(true);

        Driver farDriver = factory.manufacturePojo(Driver.class);
        farDriver.setCurrentLocation(new Location(10.0, 10.0));
        farDriver.setAvailable(true);

        Rider rider = factory.manufacturePojo(Rider.class);

        when(driverService.getDrivers(true)).thenReturn(List.of(farDriver, closeDriver));
        when(riderService.getRiderById(rider.getId())).thenReturn(rider);
        when(rideService.createRide(any(), any(), any(), any()))
                .thenReturn(new Ride(rider, closeDriver, pickup, dropoff));

        RideDetails result = rideBusinessService.requestRide(rider.getId(), pickup, dropoff);

        assertThat(result.ride().getDriver()).isEqualTo(closeDriver);
        assertThat(result.distance()).isCloseTo(1.41, within(0.01));

        verify(driverService).updateDriver(closeDriver);
    }

    @Test
    void completeRide_updatesRideAndDriverAvailability() {
        Ride ride = factory.manufacturePojo(Ride.class);
        Driver driver = factory.manufacturePojo(Driver.class);
        driver.setAvailable(true);

        when(rideService.updateRide(ride.getId())).thenReturn(ride);
        when(driverService.findDriverById(driver.getId())).thenReturn(driver);

        double latitude = 34.0522;
        double longitude = -118.2437;

        RideDetails result = rideBusinessService.completeRide(ride.getId(), driver.getId(), latitude, longitude);

        assertThat(result.ride()).isEqualTo(ride);
        assertThat(driver.getCurrentLocation().latitude()).isEqualTo(latitude);
        assertThat(driver.getCurrentLocation().longitude()).isEqualTo(longitude);
        assertThat(driver.isAvailable()).isTrue();

        verify(rideService).updateRide(ride.getId());
        verify(driverService).findDriverById(driver.getId());
        verify(driverService).updateDriver(driver);
    }

    @Test
    void completeRide_driverNotFound_throwsException() {
        Ride ride = factory.manufacturePojo(Ride.class);

        when(rideService.updateRide(ride.getId())).thenReturn(ride);
        when(driverService.findDriverById("driver123")).thenThrow(new RuntimeException("Driver not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            rideBusinessService.completeRide(ride.getId(), "driver123", 34.0, -118.0);
        });

        assertThat(exception).hasMessage("Driver not found");

        verify(rideService).updateRide(ride.getId());
        verify(driverService).findDriverById("driver123");
        verify(driverService, never()).updateDriver(any());
    }
}


