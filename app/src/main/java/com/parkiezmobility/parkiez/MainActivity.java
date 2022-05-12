package com.parkiezmobility.parkiez;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parkiezmobility.parkiez.Activities.BaseActivity;
import com.parkiezmobility.parkiez.Activities.Login;
import com.parkiezmobility.parkiez.Activities.OTPVerification;
import com.parkiezmobility.parkiez.Activities.SplashActivity;
import com.parkiezmobility.parkiez.Database.DataHelp;
import com.parkiezmobility.parkiez.Database.MyOpenHelper;
import com.parkiezmobility.parkiez.Entities.UserEntity;
import com.parkiezmobility.parkiez.Fragments.AboutUs;
import com.parkiezmobility.parkiez.Fragments.BookingHistory;
import com.parkiezmobility.parkiez.Fragments.Parkings;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.parkiezmobility.parkiez.Interfaces.APIResponseInterface;
import com.parkiezmobility.parkiez.managers.ApplicationManager;
import com.parkiezmobility.parkiez.managers.ParkingManager;
import com.parkiezmobility.parkiez.utility.Constants;
import com.parkiezmobility.parkiez.utility.Utility;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import com.google.android.gms.location.places.ui.PlaceAutocomplete;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, PaymentResultWithDataListener {
    private GoogleMap mMap;
    private Fragment fragment = null;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ImageView profilePic;
    private TextView userName, userEmail;
    private MyOpenHelper db;
    private DataHelp dh;
    private NavigationView navigationView;
    private View mHeaderView;
    private Menu menu;
    private MenuItem nav_Login = null, nav_profile;
    private UserEntity user;
    private ActionBarDrawerToggle toggle;
    private ImageView menuBtn, searchBtn;

    static public double latitude, longitude;
    public final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    private ProgressDialog dialogProgress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         Log.d("*****","main");
        DeclareVariables();

        displaySelectedScreen(R.id.nav_parkings, true);
//        fragment = new Parkings(toolbar, menuBtn, searchBtn);
//        setFragment(fragment, true);

        mHeaderView = navigationView.getHeaderView(0);
        menu = navigationView.getMenu();
     //   String id = this.getIntent().getStringExtra("user_id1");
     //   Log.d("*****","iddd" +id);

        user = db.getUser();
        Log.d("*****","user"+user);
        nav_Login = menu.findItem(R.id.nav_login);
        if (user != null) {
            nav_Login.setTitle("LogOut");

            profilePic = (ImageView) mHeaderView.findViewById(R.id.profile_pic);
            userName = (TextView) mHeaderView.findViewById(R.id.user_name);
            userEmail = (TextView) mHeaderView.findViewById(R.id.user_mail);

            if (!user.getName().equals("")) {
                userName.setText(user.getName());
            }

            if (!user.getEmail().equals("")) {
                userEmail.setText(user.getEmail());    //*****(user.getMobileNo())
            }

          //  if (!user.getMobileNo().equals("")) {
            //    userEmail.setText(user.getMobileNo());    //*****(user.getMobileNo())
           //}
        }
        else {
            nav_Login.setTitle("Login");
        }

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });


        Places.initialize(getApplicationContext(), "AIzaSyB5bjAvjv3FD_ZOchIcC3acTC8e2nR8Gl0");
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                  //  mMap = googleMap;
                    List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                            Place.Field.LAT_LNG, Place.Field.NAME);
                //    Place.Field place=Place.Field.LAT_LNG;
                    Log.d("****","mainactivity" +fieldList);
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                            fieldList).build(MainActivity.this);
                  // LatLng sydney = new LatLng(-34, 151);
                   // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                    //Marker marker = mMap.addMarker(new MarkerOptions().position(sydney).title("search Location"));
                  //  marker.setTag("search Loc");
                  //  latitude = place.getLatLng().latitude;
                   // longitude = place.getLatLng().longitude;
                    //Marker marker = mMap.addMarker(new MarkerOptions().position(Place.Field.LAT_LNG).title("search Location"));
                    //marker.setTag("search Loc");
//                    fragment = new Parkings(toolbar, menuBtn, searchBtn);
                    fragment.startActivityForResult(intent, 1);
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });
    }

    private void DeclareVariables() {

        Log.d("*****","declare variables");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);

        db = new MyOpenHelper(getApplicationContext());
        dh = new DataHelp(getApplicationContext());

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        menuBtn = (ImageView) findViewById(R.id.ic_menu);
        searchBtn = (ImageView) findViewById(R.id.ic_search);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        dialogProgress = new ProgressDialog(MainActivity.this);
        dialogProgress.setMessage("Please Wait...");
        dialogProgress.setCancelable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    /* @Override
    public void onBackPressed() {




       /*Intent i = new Intent(getApplicationContext(), Login.class);

   startActivity(i);
   }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do You want to close");
        builder.setCancelable(false);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        builder.show();
    }*/


      /*  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
            if (fragment == null || fragment.toString().contains("Parkings")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                } else {

                    finish();
                }
                //  System.exit(0);  //--main code
                /*  if (nav_Login.getTitle() != "Login") {
                    if (dh.LogOutUser(user.getUserID())) {
                        Toast.makeText(this, "You are successfully logout!!!", Toast.LENGTH_SHORT).show();
                        Intent Login_intent = new Intent(MainActivity.this, Login.class);
                        startActivity(Login_intent);
                        finish();
                    }
                }
                  //  Intent i = new Intent(getApplicationContext(), Login.class);

                  //  startActivity(i);
            } else {
                displaySelectedScreen(R.id.nav_parkings, false);
            }
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return false;
     }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
//            findPlace();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId(), false);
        return true;
    }

    private void displaySelectedScreen(int itemId, boolean first) {
        //creating fragment object
//        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_parkings:
                fragment = new Parkings(toolbar, menuBtn, searchBtn);
                Log.d("***","parking");
                break;
            case R.id.nav_booked:
                Toast.makeText(this, "In Process...", Toast.LENGTH_SHORT).show();
                Log.d("***","booked parking");
                break;
            case R.id.nav_history:
                Log.d("***","booking history");
                menuBtn.setVisibility(View.GONE);
                searchBtn.setVisibility(View.GONE);
                toolbar.setVisibility(View.VISIBLE);
                fragment = new BookingHistory();
                break;

            case R.id.nav_profile:
                Log.d("***","profile");
                Toast.makeText(this, "In Process...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about_us:
                Log.d("***","about us");
                        menuBtn.setVisibility(View.GONE);
                searchBtn.setVisibility(View.GONE);
                toolbar.setVisibility(View.VISIBLE);
                fragment = new AboutUs();
                break;
            case R.id.nav_share:
                Log.d("***"," share");
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Parkiez");
                intent.putExtra(Intent.EXTRA_TEXT, "The Digital Key to all your parking needs.  https://play.google.com/store/apps/details?id=com.parkiezmobility.parkiez");
                intent.setType("text/plain");
                startActivity(intent);
                break;
            case R.id.nav_login:
                Log.d("***","logout");
                if (nav_Login.getTitle() != "Login") {
                    if (dh.LogOutUser(user.getUserID())) {
                        Toast.makeText(this, "You are successfully logout!!!", Toast.LENGTH_SHORT).show();
                       // Intent Login_intent = new Intent(MainActivity.this, SplashActivity.class);
                        Intent Login_intent = new Intent(MainActivity.this, Login.class);
                        startActivity(Login_intent);
                        finish();
                    } else {
                        Toast.makeText(this, "You are not logout!!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent Login_intent = new Intent(MainActivity.this, Login.class);

                    startActivity(Login_intent);
                }
                break;
        }

        if (fragment != null) {
            Fragment currentFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
            if (currentFragment == null || currentFragment.getClass() != fragment.getClass()) {
                setFragment(fragment, first);
            } else {
                closeDrawer();
            }
        } else {
            closeDrawer();
        }
    }

    public void setFragment(Fragment fragment, boolean flag) {
        try {
            if (fragment != null) {
                try {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    if (flag) {
                        ft.add(R.id.content_main, fragment);
                    } else {
                        ft.replace(R.id.content_main, fragment, TAG_FRAGMENT);
                    }
                    ft.addToBackStack(null);
                    ft.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Some problem occured" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("MainActivity", "Error in creating content_frame");
            }
            closeDrawer();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Some problem occured" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void closeDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public static boolean isNetworkAvaliable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if ((connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                    || (connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .getState() == NetworkInfo.State.CONNECTED)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        JSONObject info = new JSONObject();
        try {
            info.put("razorpay_payment_id", paymentData.getPaymentId());
            info.put("razorpay_order_id", paymentData.getOrderId());
            info.put("razorpay_signature", paymentData.getSignature());


            ParkingManager.getInstance().onSaveOrderSuccessStatus(info, new APIResponseInterface<JSONObject>() {
                @Override
                public <T> void OnSuccess(T response) {
                    Toast.makeText(MainActivity.this, "Parking parked successfully.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnFailed(String response) {
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnError(VolleyError error) {
                    onHandleNetworkResponse(error);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String failString, PaymentData paymentData) {
        try {
            final String OrderId = Utility.getPreference(ApplicationManager.getInstance().getContext(), Constants.ORDER_ID_PREF_KEY);
            JSONObject info = new JSONObject();
            try {
                info.put("order_id", OrderId);
                info.put("payment_error", new JSONObject(failString).getJSONObject("error").getString("reason"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ParkingManager.getInstance().onSaveOrderCancelStatus(info, new APIResponseInterface<JSONObject>() {
                @Override
                public <T> void OnSuccess(T response) {
                    Toast.makeText(MainActivity.this, "Information Updated successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnFailed(String response) {
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnError(VolleyError error) {
                    onHandleNetworkResponse(error);
                }
            });
        } catch (Exception e) {
            Log.e("Parkiez", "Exception in onPaymentError", e);
        }
    }

    private void saveSuccessOrder(PaymentData paymentData) {

    }

}
