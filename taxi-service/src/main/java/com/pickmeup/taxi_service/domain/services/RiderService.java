package com.pickmeup.taxi_service.domain.services;

import org.springframework.stereotype.Service;

import com.pickmeup.taxi_service.domain.exceptions.ResourceNotFoundException;
import com.pickmeup.taxi_service.domain.models.Rider;
import com.pickmeup.taxi_service.domain.repositories.RiderRepository;

@Service
public class RiderService {

    private final RiderRepository riderRepository;

    public RiderService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

    public Rider getRiderById(final String id) {
        return this.riderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rider with ID [%s] not found.".formatted(id)));
    }
}
