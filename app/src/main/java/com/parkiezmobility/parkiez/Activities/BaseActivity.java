package com.parkiezmobility.parkiez.Activities;

import android.net.ConnectivityManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.parkiezmobility.parkiez.Interfaces.SnackBarCallBackHandler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public void showSnackBar(final String message, View view, final int color, @Nullable final SnackBarCallBackHandler snackBarCallBackHandler){
        Snackbar sb = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setAction("Action", null);
        sb.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if(snackBarCallBackHandler != null)
                    snackBarCallBackHandler.OnDismissed();
            }
        });
        sb.setBackgroundTint(color);
        sb.show();
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {


            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            return false;
        }
        return false;
    }

    public final void onHandleNetworkResponse(final VolleyError error){
        if (error instanceof TimeoutError) {
            Toast.makeText(this, "Session Time out, Please Login Again...", Toast.LENGTH_LONG).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(this, "Server Authorization Failed", Toast.LENGTH_LONG).show();
          //  Log.d("*****","time_Duartion_dialog");
        } else if (error instanceof ServerError) {
            Toast.makeText(this, "Server Error, please try after some time later", Toast.LENGTH_LONG).show();
        } else if (error instanceof NetworkError) {
            Toast.makeText(this, "Network not Available", Toast.LENGTH_LONG).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(this, "JSONArray Problem", Toast.LENGTH_LONG).show();
        }
    }
}