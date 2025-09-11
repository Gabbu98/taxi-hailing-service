package com.pickmeup.taxi_service.domain.models;

public class Rider {
    private String id;
    private String name;

    public Rider(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Rider{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
