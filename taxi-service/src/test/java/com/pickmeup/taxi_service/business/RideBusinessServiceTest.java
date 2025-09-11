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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

    @Test
    void requestRide_returnsEmptyWhenNoDriversAvailable() {
        // given
        when(driverService.getDrivers(true)).thenReturn(List.of());

        // when
        RideDetails result = rideBusinessService.requestRide(
                "rider1",
                new Location(0.0, 0.0),
                new Location(1.0, 1.0)
        );

        // then
        assertThat(result.ride()).isNull();
        assertThat(result.distance()).isEqualTo(0);

        verify(driverService).getDrivers(true);
        verifyNoInteractions(rideService, riderService);
    }

    @Test
    void requestRide_assignsNearestDriverAndCreatesRide() {
        // given
        Driver driver1 = new Driver("D1", "John", new Location(1.0, 1.0));
        Driver driver2 = new Driver("D2", "Geoff", new Location(5.0, 5.0));
        Rider rider = new Rider("rider1", "Tom");

        when(driverService.getDrivers(true)).thenReturn(List.of(driver1, driver2));
        when(riderService.getRiderById("rider1")).thenReturn(rider);

        Ride expectedRide = new Ride(rider, driver1, new Location(0.0, 0.0), new Location(2.0, 2.0));
        when(rideService.createRide(eq(driver1), eq(rider), any(Location.class), any(Location.class)))
                .thenReturn(expectedRide);

        // when
        RideDetails result = rideBusinessService.requestRide(
                "rider1",
                new Location(0.0, 0.0),
                new Location(2.0, 2.0)
        );

        // then
        assertThat(result.ride()).isEqualTo(expectedRide);
        assertThat(result.distance()).isLessThan(2.0); // driver1 is closer

        // verify driver was marked unavailable and updated
        ArgumentCaptor<Driver> captor = ArgumentCaptor.forClass(Driver.class);
        verify(driverService).updateDriver(captor.capture());
        assertThat(captor.getValue().isAvailable()).isFalse();

        verify(rideService).createRide(eq(driver1), eq(rider), any(Location.class), any(Location.class));
    }

    @Test
    void requestRide_picksNearestDriver() {
        Location pickup = new Location(0.0, 0.0);
        Location dropoff = new Location(10.0, 10.0);

        Driver closeDriver = new Driver("close", "John", new Location(1.0, 1.0)); // distance ~1.41
        Driver farDriver = new Driver("far", "Tom", new Location(10.0, 10.0));   // distance ~14.14
        Rider rider = new Rider("riderX", "Sam");

        when(driverService.getDrivers(true)).thenReturn(List.of(farDriver, closeDriver));
        when(riderService.getRiderById("riderX")).thenReturn(rider);
        when(rideService.createRide(any(), any(), any(), any()))
                .thenReturn(new Ride(rider, closeDriver, pickup, dropoff));

        RideDetails result = rideBusinessService.requestRide("riderX", pickup, dropoff);

        assertThat(result.ride().getDriver()).isEqualTo(closeDriver);
        assertThat(result.distance()).isCloseTo(1.41, within(0.01));

        verify(driverService).updateDriver(closeDriver);
    }
}
