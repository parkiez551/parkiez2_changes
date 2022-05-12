package com.parkiezmobility.parkiez.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.parkiezmobility.parkiez.Adapters.AllBookingList;
import com.parkiezmobility.parkiez.Database.MyOpenHelper;
import com.parkiezmobility.parkiez.Entities.BookedDetailsEntities;
import com.parkiezmobility.parkiez.Entities.BookingDetailsEntities;
import com.parkiezmobility.parkiez.Entities.ParkingEntities;
import com.parkiezmobility.parkiez.Entities.UserEntity;
import com.parkiezmobility.parkiez.Interfaces.ApiInterface;
import com.parkiezmobility.parkiez.Interfaces.OnCancelClick;
import com.parkiezmobility.parkiez.Interfaces.OnDirectionClick;
import com.parkiezmobility.parkiez.R;
import com.parkiezmobility.parkiez.Services.MasterService;
import com.parkiezmobility.parkiez.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class BookingHistory extends Fragment implements OnCancelClick, OnDirectionClick {
    private ListView list;
    private TextView msg;
    private MasterService task;
    private MyOpenHelper db;
    private UserEntity user;
    private ArrayList<BookedDetailsEntities> parkings , parkings1;
    private AllBookingList bookingAdap;

    private ProgressDialog dialogProgress = null;

    public BookingHistory() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        DeclareVariables(v);
        getData();

        return v;
    }

    public void DeclareVariables(View v) {
        getActivity().setTitle("Booking History");

        list = (ListView) v.findViewById(R.id.mylist);
        msg = (TextView) v.findViewById(R.id.msg);

        dialogProgress = new ProgressDialog(getContext());
        dialogProgress.setMessage("Please Wait...");
        dialogProgress.setCancelable(false);

        db = new MyOpenHelper(getActivity());
        user = db.getUser();
    }

    private void getData() {
        JSONObject info = new JSONObject();
        try {
            info.put("user_id", user.getUserID());
//            info.put("mobile_number", "9876543210");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLs.getAllParkingOrders, info,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonResponse) {
                        dialogProgress.dismiss();
                        try {
                            JSONArray jsonArray = jsonResponse.getJSONArray("orders");
                            ArrayList<BookingDetailsEntities> parkingEntities = new ArrayList<BookingDetailsEntities>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                BookingDetailsEntities parkingEntity = new BookingDetailsEntities();
                                parkingEntity.setOrderId(jsonObject.getInt("id"));
                                parkingEntity.setParkingId(jsonObject.getInt("parking_id"));
                                if (jsonObject.getString("user_id") == null ||
                                        jsonObject.getString("user_id").equals("null")) {
                                    parkingEntity.setUserId(1);
                                } else {
                                    parkingEntity.setUserId(jsonObject.getInt("user_id"));
                                }
                                parkingEntity.setParkingBlockId(jsonObject.getInt("parking_block_id"));
                                parkingEntity.setCarNumber(jsonObject.getString("car_number"));
                                parkingEntity.setPhoneNumber(jsonObject.getString("phone_number"));
                                parkingEntity.setCost(jsonObject.getInt("cost"));
                                if (jsonObject.getString("duration") == null ||
                                        jsonObject.getString("duration").equals("null")) {
                                    parkingEntity.setDuration("2");
                                } else {
                                    parkingEntity.setDuration(jsonObject.getString("duration"));
                                }
                                parkingEntity.setInTime(jsonObject.getString("in_time"));
                                parkingEntity.setOutTime(jsonObject.getString("out_time"));
                                if (jsonObject.getString("payment_mode") == null ||
                                        jsonObject.getString("payment_mode").equals("null")) {
                                    parkingEntity.setPaymentMode("Cash");
                                } else {
                                    parkingEntity.setPaymentMode(jsonObject.getString("payment_mode"));
                                }
                                parkingEntity.setOrderPlacedFrom(jsonObject.getString("order_placed_from"));
                                parkingEntity.setOrderStatus(jsonObject.getString("order_status"));

                                parkingEntities.add(parkingEntity);
                            }

                            setData(parkingEntities);
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialogProgress.dismiss();
                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(jsonObjectRequest);
    }

    private void setData(ArrayList<BookingDetailsEntities> parkings) {
        try {
            if (parkings.size() > 0) {
                list.setVisibility(View.VISIBLE);
                msg.setVisibility(View.GONE);
                bookingAdap = new AllBookingList(getActivity(), R.layout.custom_booking_list, parkings, 0, this, this);
                list.setAdapter(bookingAdap);
            } else {
                list.setVisibility(View.GONE);
                msg.setVisibility(View.VISIBLE);
                String text = "Currently no Bookings";
                msg.setText(text);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Some problem occured, please try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnCancelClick(BookedDetailsEntities BookingDetails) {
        Toast.makeText(getActivity(), "OnCancelClick", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnDirectionClick(BookedDetailsEntities BookingDetails) {
        Toast.makeText(getActivity(), "OnDirectionClick", Toast.LENGTH_SHORT).show();
    }
}
