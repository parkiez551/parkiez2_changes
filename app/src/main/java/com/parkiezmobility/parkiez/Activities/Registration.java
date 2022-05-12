package com.parkiezmobility.parkiez.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.parkiezmobility.parkiez.Interfaces.APIResponseInterface;
import com.parkiezmobility.parkiez.R;
import com.parkiezmobility.parkiez.managers.UserManager;
import com.parkiezmobility.parkiez.utility.Utility;

import org.json.JSONObject;

public class Registration extends BaseActivity {
    private EditText input_fname;
    private EditText input_lname;
    private EditText input_mobile;
    private EditText input_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        InitView();
    }

    private void InitView() {
        input_fname = findViewById(R.id.input_fname);
        input_lname = findViewById(R.id.input_lname);
        input_mobile = findViewById(R.id.mobile_no);
        input_email = findViewById(R.id.email);
        findViewById(R.id.link_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Registration.this, Login.class);
                startActivity(i);
                finish();
            }
        });
        findViewById(R.id.get_otp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validate()){
                    String fname = input_fname.getText().toString();
                    String lname = input_lname.getText().toString();
                    String mobile = input_mobile.getText().toString();
                    String email = input_email.getText().toString();
                    registerUser(view, fname, lname, mobile, email);
                }
            }
        });
    }


    private boolean Validate(){
        String fname = input_fname.getText().toString();
        String lname = input_lname.getText().toString();
        String mobile = input_mobile.getText().toString();
        String email = input_email.getText().toString();
        if (fname.length() == 0) {
            Toast.makeText(this, getResources().getString(R.string.emptyfirstname), Toast.LENGTH_LONG).show();
            return false;
        }
        if (lname.length() == 0){
            Toast.makeText(this, getResources().getString(R.string.emptylastname), Toast.LENGTH_LONG).show();
            return false;
        }
        if (mobile.length() != 10){
            Toast.makeText(this, getResources().getString(R.string.emptymobilenumber), Toast.LENGTH_LONG).show();
            return false;
        }
        if (email.length() == 0){
            Toast.makeText(this, getResources().getString(R.string.emptyemail), Toast.LENGTH_LONG).show();
            return false;
        }
        return fname.length() != 0 && lname.length() != 0 && mobile.length() != 0 && email.length() != 0;
    }


    private void registerUser(final View v, final String fname, final String lname, final String mobile, final String email){
        Utility.CloseKeyBoard(this, v);
        try {
            JSONObject jobject = new JSONObject();
            jobject.put("mobile_no", mobile);
            jobject.put("type", "registration"); //registration or login

            UserManager.getInstance().generateOTP(jobject, new APIResponseInterface<JSONObject>() {
                @Override
                public <T> void OnSuccess(T response) {
                    try {
                        JSONObject obj = (JSONObject)response;
                        Toast.makeText(Registration.this, obj.toString(), Toast.LENGTH_LONG).show();
                        if (obj.has("success")) {
                            String otp = obj.getJSONObject("success").getString("otp");

                            Log.d("****","response"+response);
                         //   int id = obj.getJSONObject("success").getInt("user_id");
                           //Log.d("*****","user_id"+ id);
                            JSONObject jobject = new JSONObject();
                            jobject.put("firstname", fname);
                            jobject.put("lastname", lname);
                            jobject.put("mobile_no", mobile);
                            jobject.put("email", email);
                            String requestBody = jobject.toString();
                            Intent i = new Intent(Registration.this, OTPVerification.class);
                            i.putExtra("FROM", "REGISTRATION");
                            i.putExtra("OTP", otp);
                            i.putExtra("USERINFO", requestBody);


                        //    i.putExtra("user_id",id);
                            startActivity(i);
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
                        Registration.this.showSnackBar("Mobile number is already registered. Please use other.", v, R.color.ColorPrimary, null);
                    } else {
                        Registration.this.onHandleNetworkResponse(error);
                    }
                }
            });
        }
        catch (Exception ex){

        }
    }
}
