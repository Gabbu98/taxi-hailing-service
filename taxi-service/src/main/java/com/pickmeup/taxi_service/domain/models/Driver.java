package com.pickmeup.taxi_service.domain.models;

public class Driver {
    private String id;
    private String name;
    private Location currentLocation;
    private boolean isAvailable;

    public Driver(String id, String name, Location currentLocation) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.isAvailable = false; // Drivers are unavailable by default
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Driver setId(String id) {
        this.id = id;
        return this;
    }

    public Driver setName(String name) {
        this.name = name;
        return this;
    }

    public Driver setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
        return this;
    }

    public Driver setAvailable(boolean available) {
        isAvailable = available;
        return this;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", currentLocation=" + currentLocation +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
