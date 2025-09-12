package com.pickmeup.taxi_service.controllers.requests;

import com.pickmeup.taxi_service.domain.models.Location;

/**
 * Request payload for POST /rides
 */
public record RideRequest(
        String riderId,
        Location pickupLocation,
        Location dropoffLocation
) {}
