package com.parkiezmobility.parkiez.Entities;

public class ParkingInfoEntities {
    int ParkingInfoID;
    OwnerEntities Owner;
    String ParkingSlot;
    String Car_ParkAvailable;
    String CarPrice;
    String Bike_ParkAvailable;
    String BikePrice;

    public int getParkingInfoID() {
        return ParkingInfoID;
    }

    public void setParkingInfoID(int parkingInfoID) {
        ParkingInfoID = parkingInfoID;
    }

    public OwnerEntities getOwner() {
        return Owner;
    }

    public void setOwner(OwnerEntities owner) {
        Owner = owner;
    }

    public String getParkingSlot() {
        return ParkingSlot;
    }

    public void setParkingSlot(String parkingSlot) {
        ParkingSlot = parkingSlot;
    }

    public String getCar_ParkAvailable() {
        return Car_ParkAvailable;
    }

    public void setCar_ParkAvailable(String car_ParkAvailable) {
        Car_ParkAvailable = car_ParkAvailable;
    }

    public String getCarPrice() {
        return CarPrice;
    }

    public void setCarPrice(String carPrice) {
        CarPrice = carPrice;
    }

    public String getBike_ParkAvailable() {
        return Bike_ParkAvailable;
    }

    public void setBike_ParkAvailable(String bike_ParkAvailable) {
        Bike_ParkAvailable = bike_ParkAvailable;
    }

    public String getBikePrice() {
        return BikePrice;
    }

    public void setBikePrice(String bikePrice) {
        BikePrice = bikePrice;
    }
}
