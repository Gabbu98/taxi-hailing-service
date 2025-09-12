package com.pickmeup.taxi_service.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.pickmeup.taxi_service.configurations.transactions.TransactionalMapRepository;
import com.pickmeup.taxi_service.domain.models.Rider;
import com.pickmeup.taxi_service.domain.repositories.RiderRepository;

@Repository
public class InMemoryRiderRepository implements RiderRepository {
    private final TransactionalMapRepository<String, Rider> riders = new TransactionalMapRepository<>();

    @Override
    public void save(Rider rider) {
        riders.put(rider.getId(), rider);
    }

    @Override
    public Optional<Rider> findById(String riderId) {
        return Optional.ofNullable(riders.get(riderId));
    }

    @Override
    public List<Rider> findAll() {
        return new ArrayList<>(riders.values());
    }

    @Override
    public void deleteById(String riderId) {
        riders.remove(riderId);
    }
}
