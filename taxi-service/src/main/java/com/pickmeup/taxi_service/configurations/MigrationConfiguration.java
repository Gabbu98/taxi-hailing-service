package com.pickmeup.taxi_service.configurations;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import com.pickmeup.taxi_service.domain.models.Driver;
import com.pickmeup.taxi_service.domain.models.Location;
import com.pickmeup.taxi_service.domain.models.Rider;
import com.pickmeup.taxi_service.domain.repositories.DriverRepository;
import com.pickmeup.taxi_service.domain.repositories.RiderRepository;

@Configuration
public class MigrationConfiguration {

    private final DriverRepository driverRepository;
    private final RiderRepository riderRepository;

    public MigrationConfiguration(DriverRepository driverRepository,
            RiderRepository riderRepository) {
        this.driverRepository = driverRepository;
        this.riderRepository = riderRepository;
    }

    @EventListener
    public void seedData(ApplicationReadyEvent event) {
        // Seed some drivers
        driverRepository.save(new Driver("d1", "John", new Location(40.7128, -74.0060))); // NYC
        driverRepository.save(new Driver("d2", "Geoff", new Location(34.0522, -118.2437))); // LA
        driverRepository.save(new Driver("d3", "Marcos", new Location(51.5074, -0.1278))); // London

        // Seed some riders
        riderRepository.save(new Rider("r1", "Alice"));
        riderRepository.save(new Rider("r2", "Bob"));
        riderRepository.save(new Rider("r3", "Charlie"));
    }
}
