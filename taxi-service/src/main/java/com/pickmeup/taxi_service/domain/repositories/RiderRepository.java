package com.pickmeup.taxi_service.domain.repositories;

import java.util.Optional;
import java.util.List;

import com.pickmeup.taxi_service.domain.models.Rider;

public interface RiderRepository {
    void save(Rider rider);
    Optional<Rider> findById(String riderId);
    List<Rider> findAll();
    void deleteById(String riderId);
}
