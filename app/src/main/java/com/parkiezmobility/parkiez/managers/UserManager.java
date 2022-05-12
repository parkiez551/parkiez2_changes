package com.parkiezmobility.parkiez.managers;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.parkiezmobility.parkiez.Interfaces.APIResponseInterface;
import com.parkiezmobility.parkiez.URLs;
import com.parkiezmobility.parkiez.networkhandler.JsonObjectRequestHandler;
import com.parkiezmobility.parkiez.networkhandler.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class UserManager {
    private static UserManager manager = null;

    public static UserManager getInstance(){
        if (manager == null){
            manager = new UserManager();
        }
        return manager;
    }

    public void registerUser(final JSONObject body, final APIResponseInterface handler){
        JsonObjectRequestHandler jsonObjectRequest = new JsonObjectRequestHandler(Request.Method.POST, URLs.registeruser, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.has("success")) {
                    Log.d("###########","sucess register user after OTP validation");
                    handler.OnSuccess(response);
                    Log.d("*****","registeruser" +response);
                } else {
                    handler.OnFailed(response.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.OnError(error);
            }
        });
        RequestQueue queue = SingletonRequestQueue.getInstance(ApplicationManager.getInstance().getContext()).getRequestQueue();
        queue.add(jsonObjectRequest);
    }

    public void loginUser(){

    }

    public void updateUser(){

    }

    public void generateOTP(final JSONObject body, final APIResponseInterface handler){
        JsonObjectRequestHandler jsonObjectRequest = new JsonObjectRequestHandler(Request.Method.POST, URLs.sendOTP, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.has("success")) {

                    Log.d("#######" ,"success response generate otp");
                    handler.OnSuccess(response);
                } else {
                    handler.OnFailed(response.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.OnError(error);
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject data = new JSONObject(responseBody);
                    JSONArray errors = data.getJSONArray("errors");
                    JSONObject jsonMessage = errors.getJSONObject(0);
                    String message = jsonMessage.getString("message");
                    Log.d("**********","message"+message);
                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                } catch (Exception  errorr) {
                }
            }
        });
        RequestQueue queue = SingletonRequestQueue.getInstance(ApplicationManager.getInstance().getContext()).getRequestQueue();
        queue.add(jsonObjectRequest);
        Log.d("**********","generate otp request"+jsonObjectRequest);
        Log.d("**********","body"+body);

    }
    }

