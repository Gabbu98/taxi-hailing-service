package com.pickmeup.taxi_service.domain.models;

public record Location(double latitude, double longitude) {
    // A record implicitly provides a constructor, accessors, equals(), hashCode(), and toString().
    // We don't need to write them ourselves.
}
