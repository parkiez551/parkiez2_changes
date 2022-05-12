package com.parkiezmobility.parkiez.Entities;

public class BookingDetailsEntities {
    int OrderId;
    int ParkingId;
    int UserId;
    int ParkingBlockId;
    String CarNumber;
    String PhoneNumber;
    int Cost;
    String Duration;
    String InTime;
    String OutTime;
    String PaymentMode;
    String OrderPlacedFrom;
    String OrderStatus;

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public int getParkingId() {
        return ParkingId;
    }

    public void setParkingId(int parkingId) {
        ParkingId = parkingId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getParkingBlockId() {
        return ParkingBlockId;
    }

    public void setParkingBlockId(int parkingBlockId) {
        ParkingBlockId = parkingBlockId;
    }

    public String getCarNumber() {
        return CarNumber;
    }

    public void setCarNumber(String carNumber) {
        CarNumber = carNumber;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public int getCost() {
        return Cost;
    }

    public void setCost(int cost) {
        Cost = cost;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getInTime() {
        return InTime;
    }

    public void setInTime(String inTime) {
        InTime = inTime;
    }

    public String getOutTime() {
        return OutTime;
    }

    public void setOutTime(String outTime) {
        OutTime = outTime;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getOrderPlacedFrom() {
        return OrderPlacedFrom;
    }

    public void setOrderPlacedFrom(String orderPlacedFrom) {
        OrderPlacedFrom = orderPlacedFrom;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }
}
