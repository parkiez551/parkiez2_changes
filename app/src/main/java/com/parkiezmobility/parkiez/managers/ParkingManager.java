package com.parkiezmobility.parkiez.managers;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.parkiezmobility.parkiez.Interfaces.APIResponseInterface;
import com.parkiezmobility.parkiez.URLs;
import com.parkiezmobility.parkiez.networkhandler.JsonObjectRequestHandler;
import com.parkiezmobility.parkiez.networkhandler.SingletonRequestQueue;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ParkingManager {
    private static ParkingManager manager = null;

    public static ParkingManager getInstance(){
        if (manager == null){
            manager = new ParkingManager();
        }
        return manager;
    }

    public void getAllParkings(final JSONObject body, final APIResponseInterface handler){
        Log.e("data" ,body.toString());
        JsonObjectRequestHandler jsonObjectRequest = new JsonObjectRequestHandler(Request.Method.GET, URLs.getAllParkings, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                handler.OnSuccess(response);
                Log.d("**********","parking details" +response);
                Log.d("#################","success get all parking");
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

    public void getPaymentMerchantId(final APIResponseInterface handler){
        JsonObjectRequestHandler jsonObjectRequest = new JsonObjectRequestHandler(Request.Method.GET, URLs.getMerchantId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.has("success")) {

                    Log.d("#############","success get payment gateway merchant id");
                    handler.OnSuccess(response);
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

    public void generateParkingOrder(final JSONObject body, final APIResponseInterface handler){

            JsonObjectRequestHandler jsonObjectRequest = new JsonObjectRequestHandler(Request.Method.POST, URLs.generateOrder, body, new Response.Listener<JSONObject>() {


                @Override
                public void onResponse(JSONObject response) {
                    if (response.has("success")) {
                        handler.OnSuccess(response);
                      //  response.getStatusLine().getStatusCode();
                        Log.d("############","sucess create parking order");
                        Log.d("*****", "response from generate_parking_0rder api" + response);
                    } else {
                        handler.OnFailed(response.toString());
                        Log.d("*****", "error from generate_parking_0rder api");
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    handler.OnError(error);


                    error.printStackTrace();
                    Log.d("*******", " volleerror");


                }

               public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                  headers.put("Content-Type", "application/json; charset=utf-8");

              //     headers.put("Content-Type","application/x-www-form-urlencoded");
                   //headers.put("Accept", "application/json");
               //   headers.put( " AppleWebKit","537.36");
                    //headers.put("User-Agent","Chrome/99.0.4844.82");
                    //headers.put("User-Agent","Safari/537.36");
                   // headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.82 Safari/537.36");
                   // headers["User-Agent"]="Mozilla/5.0";
                 // headers.put("User-Agent", "Mozilla/5.0");
                    //headers.put("User-Agent", "Nintendo Gameboy");
                    //headers.put("Accept-Language", "fr");
                    return headers;
                }


            });

        //jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 10, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
       // jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
         //       DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
           //     DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
             //   DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
     //  jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
       //         (int) TimeUnit.SECONDS.toMillis(20),
         //       DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
           //     DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //int socketTimeout = 30000;
       // RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        //RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
          //      DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            //    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        //jsonObjectRequest.setRetryPolicy(policy);

        //jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,-1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
      //jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
       // jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
       // jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
         //       DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
           //     DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
       // System.setProperty("http.keepAlive", "false");
        //jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         //    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue queue = SingletonRequestQueue.getInstance(ApplicationManager.getInstance().getContext()).getRequestQueue();
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // queue.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonObjectRequest);
            Log.d("***********", "request for creating order" + jsonObjectRequest);
            Log.d("***********", "parameter" + body);



    }

    public void onSaveOrderCancelStatus(final JSONObject body, final APIResponseInterface handler){
        JsonObjectRequestHandler jsonObjectRequest = new JsonObjectRequestHandler(Request.Method.POST, URLs.savePaymentFailure, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.has("success")) {
                    handler.OnSuccess(response);
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

    public void onSaveOrderSuccessStatus(final JSONObject body, final APIResponseInterface handler){
        JsonObjectRequestHandler jsonObjectRequest = new JsonObjectRequestHandler(Request.Method.POST, URLs.savePaymentSuccess, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.has("success")) {
                    handler.OnSuccess(response);

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
}
