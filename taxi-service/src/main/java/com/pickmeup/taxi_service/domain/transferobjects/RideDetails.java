package com.pickmeup.taxi_service.domain.transferobjects;

import com.pickmeup.taxi_service.domain.models.Ride;

public record RideDetails(Ride ride, double distance) {
}
