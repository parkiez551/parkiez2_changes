package com.parkiezmobility.parkiez.Entities;

public class VehicleTypes {
    String title;
    int cost;
    String duration;

    public VehicleTypes(String title, int cost, String duration) {
        this.title = title;
        this.cost = cost;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public int getCost() {
        return cost;
    }

    public String getDuration() {
        return duration;
    }
}
