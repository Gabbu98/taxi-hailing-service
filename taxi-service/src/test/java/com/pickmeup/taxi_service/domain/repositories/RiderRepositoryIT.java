package com.pickmeup.taxi_service.domain.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import com.pickmeup.taxi_service.domain.models.Location;
import com.pickmeup.taxi_service.domain.models.Rider;

@SpringBootTest
public class RiderRepositoryIT {

    @Autowired
    private RiderRepository riderRepository;

    @Test
    void testSaveAndFindById() {
        Rider rider = new Rider("rider1", "Alice", new Location(38.8951, -77.0364));
        riderRepository.save(rider);

        Optional<Rider> foundRider = riderRepository.findById("rider1");
        assertTrue(foundRider.isPresent());
        assertEquals("Alice", foundRider.get().getName());
    }

    @Test
    void testFindAllRiders() {
        Rider rider1 = new Rider("rider1", "Alice", new Location(38.8951, -77.0364));
        Rider rider2 = new Rider("rider2", "Bob", new Location(35.6895, 139.6917));
        riderRepository.save(rider1);
        riderRepository.save(rider2);

        List<Rider> allRiders = riderRepository.findAll();
        assertEquals(2, allRiders.size());
    }

}
