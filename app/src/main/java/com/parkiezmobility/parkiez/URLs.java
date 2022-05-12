package com.parkiezmobility.parkiez;

public class URLs {
    private static final String ROOT_URL = "http://api.parkiez.com/api/";

    public static final String sendOTP = ROOT_URL + "validate-mobile-no";
    public static final String registeruser = ROOT_URL + "register-user";
    public static final String getAllParkings = ROOT_URL + "get-all-parkings";
    public static final String getMerchantId = ROOT_URL + "get-pg-token";
    public static final String generateOrder = ROOT_URL + "create-parking-order";

    public static final String getAllParkingOrders = ROOT_URL + "get-all-parking-orders";
    public static final String getUserDetails = ROOT_URL + "get-user-profile";

    //public static final String generateOrder = "https://api.parkiez.com/api/create-parking-order";



    public static final String savePaymentFailure = ROOT_URL + "payment-failure";
    public static final String savePaymentSuccess = ROOT_URL + "payment-success";


}
