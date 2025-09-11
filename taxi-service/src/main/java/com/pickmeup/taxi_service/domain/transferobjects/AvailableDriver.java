package com.pickmeup.taxi_service.domain.transferobjects;

import com.pickmeup.taxi_service.domain.models.Driver;

public record AvailableDriver(Driver driver, double distance) {
}
