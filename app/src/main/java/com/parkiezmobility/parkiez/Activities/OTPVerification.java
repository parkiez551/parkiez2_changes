package com.parkiezmobility.parkiez.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parkiezmobility.parkiez.Database.DataHelp;
import com.parkiezmobility.parkiez.Entities.UserEntity;
import com.parkiezmobility.parkiez.Interfaces.APIResponseInterface;
import com.parkiezmobility.parkiez.MainActivity;
import com.parkiezmobility.parkiez.R;
import com.parkiezmobility.parkiez.URLs;
import com.parkiezmobility.parkiez.managers.UserManager;
import com.parkiezmobility.parkiez.utility.Constants;
import com.parkiezmobility.parkiez.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.view.KeyEvent.KEYCODE_DEL;

public class OTPVerification extends BaseActivity {
    EditText[] otpETs = new EditText[6];
    String MobileNo, OTP, EnteredOTP="", Token,Token1,mobile;
    int userId,userId1;
    TextView mobile_no;
    Button Submit;
    private DataHelp dh;
    UserEntity user;
    private ProgressDialog dialogProgress = null;
    private String FROM;
    private String userinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
      //  Toast.makeText(getApplicationContext(),
        //        "otp" + OTP,
          //      Toast.LENGTH_LONG);
        DeclareVariables();
        OTP = this.getIntent().getStringExtra("OTP");
        Toast t= Toast.makeText(getApplicationContext(),
                "otp" +OTP,
                Toast.LENGTH_LONG);

        t.setGravity(Gravity.BOTTOM | Gravity.RIGHT, 0, 0);
        t.show();

       // MobileNo = getIntent().getStringExtra("mobile_no");
       // mobile_no.setText(MobileNo);

        //OTP = getIntent().getStringExtra("otp");
        //Token = getIntent().getStringExtra("token");*/
        try {
            FROM = this.getIntent().getStringExtra("FROM");
            userinfo = this.getIntent().getStringExtra("USERINFO");
         //   OTP = this.getIntent().getStringExtra("OTP");

          //  Toast t= Toast.makeText(getApplicationContext(),
            //        "otp" +OTP,
              //      Toast.LENGTH_LONG);

            //t.setGravity(Gravity.BOTTOM | Gravity.RIGHT, 0, 0);
            //t.show();

           // Toast.makeText(getApplicationContext(),
             //       "otp" + OTP,
               //     Toast.LENGTH_LONG);
            Token1 = this.getIntent().getStringExtra("token");
       //    userId=this.getIntent().getIntExtra("user_id",0);
           Log.d("****","user_id" +userId);
            JSONObject jobject = new JSONObject(userinfo);
             mobile = jobject.getString("mobile_no");
             Log.d("****","mobile_jobje+" +mobile);
            mobile_no.setText(mobile);
        }
        catch (Exception ex){
//            showSnackBar(getResources().getString(R.string.somethingiswrong), findViewById(android.R.id.content), R.color.redcolor, new SnackBarCallBackHandler() {
//                @Override
//                public void OnDismissed() {
//                    finish();
//                }
//            });
        }
        userId=this.getIntent().getIntExtra("user_id",0);
        /*SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Token",Token);
        editor.commit();*/

        //userId = getIntent().getIntExtra("id", 0);
      //****
       // Token1 = getIntent().getStringExtra("token");
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (isValidate()) {
                    if (OTP.equals(EnteredOTP)) {
                        if (FROM.compareToIgnoreCase("LOGIN")==0){
                            getUserDetails();
                        } else {
                            try {
                                UserManager.getInstance().registerUser(new JSONObject(userinfo), new APIResponseInterface<JSONObject>() {
                                    @Override
                                    public void OnSuccess(Object response) {
                                        try {
                                            JSONObject obj = (JSONObject) response;
                                            if (obj.has("success")) {
                                                String token = obj.getJSONObject("success").getString("token");
                                                Log.d("***********","token generated" +token);

                                               // userId= obj.getJSONObject("success").getInt("user_id1");
                                             //   Log.d("*****","id"+userId);
                                              // saveUser(response);
                                                Utility.setPreference(OTPVerification.this, Constants.TOKEN_PREF_KEY, token);
                                               Intent i = new Intent(OTPVerification.this, MainActivity.class);

                                            //    Intent i = new Intent(OTPVerification.this, Third_party.class);



                                               // Intent i = new Intent(OTPVerification.this, SplashActivity.class);
                                               // getUserDetails();
                                              //  i.putExtra("user_id",userId);

                                                startActivity(i);
                                                finish();
                                            }
                                        }
                                        catch (Exception ex){

                                        }
                                    }

                                    @Override
                                    public void OnFailed(String response) {

                                    }

                                    @Override
                                    public void OnError(VolleyError error) {
                                        if (error.networkResponse.statusCode == 401){
                                            OTPVerification.this.showSnackBar("User is already registered.", view, R.color.ColorPrimary, null);
                                        } else {
                                            OTPVerification.this.onHandleNetworkResponse(error);
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } else {
                        OTPVerification.this.showSnackBar("Incorrect OTP. Please try again.", view, R.color.ColorPrimary,null);
                    }
                }
            }
        });
    }

    private void DeclareVariables() {
        getWindow().setBackgroundDrawableResource(R.drawable.otp_bground);

        dialogProgress = new ProgressDialog(OTPVerification.this);
        dialogProgress.setMessage("Authenticating...");
        dialogProgress.setCancelable(false);

        otpETs[0] = (EditText) findViewById(R.id.otpET1);
        otpETs[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otpETs[0].getText().toString().length()>=1){
                    otpETs[0].clearFocus();
                    otpETs[1].requestFocus();
                    otpETs[1].setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otpETs[1] = (EditText) findViewById(R.id.otpET2);
        otpETs[1].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otpETs[1].getText().toString().length()>=1){
                    otpETs[1].clearFocus();
                    otpETs[2].requestFocus();
                    otpETs[2].setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otpETs[2] = (EditText) findViewById(R.id.otpET3);
        otpETs[2].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otpETs[2].getText().toString().length()>=1){
                    otpETs[2].clearFocus();
                    otpETs[3].requestFocus();
                    otpETs[3].setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otpETs[3] = (EditText) findViewById(R.id.otpET4);
        otpETs[3].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otpETs[3].getText().toString().length()>=1){
                    otpETs[3].clearFocus();
                    otpETs[4].requestFocus();
                    otpETs[4].setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otpETs[4] = (EditText) findViewById(R.id.otpET5);
        otpETs[4].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otpETs[4].getText().toString().length()>=1){
                    otpETs[4].clearFocus();
                    otpETs[5].requestFocus();
                    otpETs[5].setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otpETs[5] = (EditText) findViewById(R.id.otpET6);
        otpETs[5].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otpETs[5].getText().toString().length()>=1){
                    otpETs[5].clearFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mobile_no = (TextView) findViewById(R.id.mobile_no);
        Submit = (Button) findViewById(R.id.submit);

        dh = new DataHelp(getApplicationContext());
    }

    public boolean isValidate() {
        boolean valid = true;

        EnteredOTP = "";
        for (int i = 0; i < otpETs.length; i++) {
            EditText tempET = otpETs[i];
            if (tempET.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                EnteredOTP = EnteredOTP + tempET.getText().toString();
            }
        }

        return valid;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == 7 || keyCode == 8 ||
                keyCode == 9 || keyCode == 10 ||
                keyCode == 11 || keyCode == 12 ||
                keyCode == 13 || keyCode == 14 ||
                keyCode == 15 || keyCode == 16 ||
                keyCode == 67) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KEYCODE_DEL) {
                    int index = checkWhoHasFocus();
                    if (index != 123) {
                        if (Helpers.rS(otpETs[index]).equals("")) {
                            if (index != 0) {
                                otpETs[index - 1].requestFocus();
                            }
                        } else {
                            return super.dispatchKeyEvent(event);
                        }
                    }
                } else {
                    int index = checkWhoHasFocus();
                    if (index != 123) {
                        if (Helpers.rS(otpETs[index]).equals("")) {
                            return super.dispatchKeyEvent(event);
                        } else {
                            if (index != 5) {
                                otpETs[index + 1].requestFocus();
                            }
                        }
                    }
                    return super.dispatchKeyEvent(event);
                }
            }
        } else {
            return super.dispatchKeyEvent(event);
        }
        return true;
    }

    private int checkWhoHasFocus() {
        for (int i = 0; i < otpETs.length; i++) {
            EditText tempET = otpETs[i];
            if (tempET.hasFocus()) {
                return i;
            }
        }
        return 123;
    }

    public static class Helpers {

        public static String rS(EditText editText) {
            return editText.getText().toString().trim();
        }
    }

    private void getUserDetails() {
        JSONObject parameters = new JSONObject();
        try {
           //**
            // parameters.put("user_id", String.valueOf(userId));
        //    parameters.put("token",String.valueOf(Token1));
            parameters.put("user_id", String.valueOf(userId));
           // parameters.put("token",Token1);
          //  parameters.put("token",String.valueOf())
            Log.d("******","userid"+userId);
        } catch (Exception e) {
            e.getMessage();
        }

        dialogProgress.show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URLs.getUserDetails, parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialogProgress.dismiss();
                Log.d("******","*******"+response);
                if (response.has("success")) {
                    saveUser(response);
                    Log.d("############","success get user profile");
                    Log.d("****","api_response");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogProgress.dismiss();
                try{


                if (error instanceof TimeoutError) {
                    Toast.makeText(getApplicationContext(), "Session Time out, Please Try Again...", Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), "Server Authorization Failed", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), "Server Error, please try after some time later", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), "Network not Available", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), "JSONArray Problem", Toast.LENGTH_LONG).show();
                }
                }catch (Exception e){

                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + Token1);
                Log.d("*****","token===" +Token1);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
        Log.d("*****","user_id_otp="+userId);
        Log.d("*****","requset"+request);
        Log.d("*****","parameter" +parameters);
       Log.d("*****","otp_token===" +Token1);


    }

    public void saveUser(JSONObject response) {
        try {
            Log.d("***","save_user_response" +response);
            user = new UserEntity();
            user.setUserID(userId);
            String name = "";
            if (response.getJSONObject("success").getString("firstname") != null &&
                    !response.getJSONObject("success").getString("firstname").equals("null")) {
                name = response.getJSONObject("success").getString("firstname");
            }
            Log.d("*****","if1");
            if (response.getJSONObject("success").getString("lastname") != null &&
                    !response.getJSONObject("success").getString("lastname").equals("null")) {
                name = name + " " +response.getJSONObject("success").getString("lastname");
            }
            Log.d("*****","if2");
            user.setName(name);
            if (response.getJSONObject("success").getString("email") != null &&
                    !response.getJSONObject("success").getString("email").equals("null")) {
                user.setEmail(response.getJSONObject("success").getString("email"));
            } else {
                user.setEmail("");
            }
           // user.setMobileNo();
            //user.setMobileNo(MobileNo);
           user.setMobileNo(mobile);
            Log.d("******","mobileno"+mobile);
            user.setPassword("");
            user.setAddress("");
            user.setEntryDate("");
            if (response.getJSONObject("success").getString("profile_image_url") != null &&
                    !response.getJSONObject("success").getString("profile_image_url").equals("null")) {
                user.setProfileImg(response.getJSONObject("success").getString("profile_image_url"));
            } else {
                user.setProfileImg("");
            }
            Log.d("*****","if3");
          //  if (response.getJSONObject("success").getString("vehicles") != null &&
          //          !response.getJSONObject("success").getString("vehicles").equals("null")) {
          //      user.setVehicle(response.getJSONObject("success").getString("vehicles"));
           // } else {
           //     user.setVehicle("");
           // }
            Log.d("*****","if4");
            if (dh.putUser(user)) {
                Log.d("******","intent");
                Intent i = new Intent(OTPVerification.this, MainActivity.class);
            //    Intent i = new Intent(OTPVerification.this, Third_party.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(OTPVerification.this, "local db error", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.getMessage();
            Log.d("*****","exception");
        }
    }

}
