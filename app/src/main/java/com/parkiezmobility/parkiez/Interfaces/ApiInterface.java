package com.parkiezmobility.parkiez.Interfaces;

import com.parkiezmobility.parkiez.Entities.AddressEntities;
import com.parkiezmobility.parkiez.Entities.BookedDetailsEntities;
import com.parkiezmobility.parkiez.Entities.BookingEntities;
import com.parkiezmobility.parkiez.Entities.MyResponce;
import com.parkiezmobility.parkiez.Entities.UserEntity;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("GetInfo")
    Call<ArrayList<AddressEntities>> getHomeContent(@Body JSONObject info);

    @POST("UserLogin")
    Call<UserEntity> LoginUser(@Body JSONObject info);

    @POST("saveUser")
    Call<MyResponce> saveUser(@Body UserEntity info);

    @POST("bookParking")
    Call<String> bookParking(@Body BookingEntities info);

    @POST("GetBookings")
    Call<ArrayList<BookedDetailsEntities>> GetBookings(@Body JSONObject info);

    @POST("CancelBooking")
    Call<String> cancelBooking(@Body BookedDetailsEntities info);

}
