package com.pickmeup.taxi_service.domain.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import com.pickmeup.taxi_service.domain.enums.RideStatus;
import com.pickmeup.taxi_service.domain.models.Location;
import com.pickmeup.taxi_service.domain.models.Ride;
import com.pickmeup.taxi_service.domain.models.Rider;

@SpringBootTest
public class RideRepositoryIT {

    @Autowired
    private RideRepository rideRepository;
    private Rider rider;
    private Location pickup;
    private Location dropoff;

    public RideRepositoryIT() {
        rider = new Rider("rider1", "Alice", new Location(38.8951, -77.0364));
        pickup = new Location(38.8951, -77.0364);
        dropoff = new Location(38.9051, -77.0500);
    }

    @Test
    void testSaveAndFindById() {
        Ride ride = new Ride("ride1", rider, pickup, dropoff);
        rideRepository.save(ride);

        Optional<Ride> foundRide = rideRepository.findById("ride1");
        assertTrue(foundRide.isPresent());
        assertEquals(RideStatus.REQUESTED, foundRide.get().getStatus());
    }

    @Test
    void testFindByStatus() {
        Ride ride1 = new Ride("ride1", rider, pickup, dropoff);
        Ride ride2 = new Ride("ride2", rider, pickup, dropoff);
        ride1.setStatus(RideStatus.COMPLETED);
        rideRepository.save(ride1);
        rideRepository.save(ride2);

        List<Ride> requestedRides = rideRepository.findByStatus(RideStatus.REQUESTED);
        assertEquals(1, requestedRides.size());
        assertEquals("ride2", requestedRides.get(0).getId());
    }

}