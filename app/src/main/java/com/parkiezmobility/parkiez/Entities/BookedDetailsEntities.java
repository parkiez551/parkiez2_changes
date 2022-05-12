package com.parkiezmobility.parkiez.Entities;

public class BookedDetailsEntities {
    int BookingID;
    int ParkingAddrID;
    String ParkingName;
    AddressEntities Parking;
    String VehicleType;
    String BookedDate;
    String BoookedTime;
    String BookingStatus;

    public int getBookingID() {
        return BookingID;
    }

    public void setBookingID(int bookingID) {
        BookingID = bookingID;
    }

    public int getParkingAddrID() {
        return ParkingAddrID;
    }

    public void setParkingAddrID(int parkingAddrID) {
        ParkingAddrID = parkingAddrID;
    }

    public String getParkingName() {
        return ParkingName;
    }

    public void setParkingName(String parkingName) {
        ParkingName = parkingName;
    }

    public AddressEntities getParking() {
        return Parking;
    }

    public void setParking(AddressEntities parking) {
        Parking = parking;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public String getBookedDate() {
        return BookedDate;
    }

    public void setBookedDate(String bookedDate) {
        BookedDate = bookedDate;
    }

    public String getBoookedTime() {
        return BoookedTime;
    }

    public void setBoookedTime(String boookedTime) {
        BoookedTime = boookedTime;
    }

    public String getBookingStatus() {
        return BookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        BookingStatus = bookingStatus;
    }
}
