package com.pickmeup.taxi_service.business;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pickmeup.taxi_service.domain.models.Ride;
import com.pickmeup.taxi_service.domain.repositories.RideRepository;

// To test Transactions
@SpringBootTest
public class RideBusinessServiceIT {
    @Autowired
    private RideBusinessService rideBusinessService;
    @Autowired
    private RideRepository rideRepository;


    @Test
    void test(){
        try {
            rideBusinessService.test();
        } catch (Exception e) {
            List<Ride> rides = rideRepository.findAll();
            System.out.println();
        }
    }
}
