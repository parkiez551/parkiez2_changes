package com.parkiezmobility.parkiez.Entities;

import java.util.ArrayList;

public class ParkingEntities {
    int id;
    String title;
    String description;
    String address;
    String latitude;
    String longitude;
    String city;
    String state;
    String country;
    String zipcode;
    String image_url;
    String slots;
    int no_of_rows;
    int no_of_columns;
    int cost;
    String unit;
    String radius;
    int status;
    String created_at;
    String updated_at;
    String distance;

  //ArrayList<VehicleTypes> vehicle_types;





 ArrayList<VehicleTypes> vehicle_types=new ArrayList<>();





    public int getParkingID() {
        return id;
    }

    public void setParkingID(int parkingID) {
        id = parkingID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        zipcode = zipcode;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        image_url = image_url;
    }

    public String getSlots() {
        return slots;
    }

    public void setSlots(String slots) {
        slots = slots;
    }

    public int getNo_of_rows() {
        return no_of_rows;
    }

    public void setNo_of_rows(int no_of_rows) {
        no_of_rows = no_of_rows;
    }

    public int getNo_of_columns() {
        return no_of_columns;
    }

    public void setNo_of_columns(int no_of_columns) {
        no_of_columns = no_of_columns;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        cost = cost;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        unit = unit;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        radius = radius;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        updated_at = updated_at;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public ArrayList<VehicleTypes> getVehicle_types() {

        return vehicle_types;
    }

    public void setVehicle_types(ArrayList<VehicleTypes> vehicle_types) {
        this.vehicle_types = vehicle_types;
    }

}
