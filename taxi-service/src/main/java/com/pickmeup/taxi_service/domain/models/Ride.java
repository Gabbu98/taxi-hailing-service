package com.pickmeup.taxi_service.domain.models;

import com.pickmeup.taxi_service.domain.enums.RideStatus;

public class Ride {
    private String id;
    private Rider rider;
    private Driver driver; // Optional, will be assigned after a match
    private Location pickupLocation;
    private Location dropoffLocation;
    private RideStatus status;

    public Ride(String id, Rider rider, Location pickupLocation, Location dropoffLocation) {
        this.id = id;
        this.rider = rider;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.status = RideStatus.REQUESTED;
    }

    public String getId() {
        return id;
    }

    public Rider getRider() {
        return rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public Location getPickupLocation() {
        return pickupLocation;
    }

    public Location getDropoffLocation() {
        return dropoffLocation;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Ride{" +
                "id='" + id + '\'' +
                ", rider=" + rider +
                ", driver=" + driver +
                ", pickupLocation=" + pickupLocation +
                ", dropoffLocation=" + dropoffLocation +
                ", status=" + status +
                '}';
    }
}
