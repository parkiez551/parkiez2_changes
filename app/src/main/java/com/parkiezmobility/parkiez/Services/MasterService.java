package com.parkiezmobility.parkiez.Services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.parkiezmobility.parkiez.R;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class MasterService  {

    public static final String BASE_URL = "https://carparkingservice.azurewebsites.net/carparking/CarParking/";
    private static Retrofit retrofit = null;
    private ProgressDialog progressDialog;

    private static OkHttpClient okClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public static Retrofit getCaller() {
        try {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(okClient())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retrofit;
    }

    public void showProgress(Activity context) {
        try {
            progressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Some problem occured in progress bar", Toast.LENGTH_SHORT).show();
        }
    }

    public void dismissProgress(Activity context) {
        try {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Some problem occured in progress bar", Toast.LENGTH_SHORT).show();
        }
    }
}
