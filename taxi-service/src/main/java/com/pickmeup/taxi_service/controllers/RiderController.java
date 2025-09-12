package com.pickmeup.taxi_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickmeup.taxi_service.business.RideBusinessService;
import com.pickmeup.taxi_service.controllers.requests.RideRequest;
import com.pickmeup.taxi_service.domain.transferobjects.RideDetails;

@RestController
@RequestMapping("/rider")
public class RiderController {

    private final RideBusinessService rideBusinessService;

    public RiderController(RideBusinessService rideBusinessService) {
        this.rideBusinessService = rideBusinessService;
    }

    /**
     * POST /rider/{riderId}/ride
     * Request a new ride for a specific rider.
     */
    @PostMapping("/{riderId}/ride")
    public ResponseEntity<RideDetails> requestRide(
            @PathVariable String riderId, // this approach is insecure, the riderId should ideally be extracted from the JWT claim. However, this approach is RESTful.
            @RequestBody RideRequest request) {

        RideDetails details = rideBusinessService.requestRide(
                riderId,
                request.pickupLocation(),
                request.dropoffLocation()
        );

        if (details.ride()==null) {
            return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(details, HttpStatus.CREATED);
    }

//    /**
//     * GET /rider/{riderId}/rides/{rideId}
//     * Get the status and details of a specific ride for a rider.
//     */
//    @GetMapping("/ride/{rideId}")
//    public ResponseEntity<RideDetails> getRideDetails(
//            @PathVariable Long riderId,
//            @PathVariable Long rideId) {
//
//        RideDetails details = rideBusinessService.getRideDetails(riderId, rideId);
//        return ResponseEntity.ok(details);
//    }
}
