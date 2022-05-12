package com.parkiezmobility.parkiez.Activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parkiezmobility.parkiez.Database.DataHelp;
import com.parkiezmobility.parkiez.Database.MyOpenHelper;
import com.parkiezmobility.parkiez.Entities.BookingEntities;
import com.parkiezmobility.parkiez.Entities.UserEntity;
import com.parkiezmobility.parkiez.Interfaces.ApiInterface;
import com.parkiezmobility.parkiez.MainActivity;
import com.parkiezmobility.parkiez.R;
import com.parkiezmobility.parkiez.Services.MasterService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentComplete extends AppCompatActivity {
    private String getTransId, getPaidAmt, getPaymentName, getVehicleType, getBookingDate, getStatus, getBookingStatus;
    private String getBookingTime, getParkingName;
    private boolean getIsDone, getOld;
    private int getUserID, getParkingAddrID;
    private TextView textCustName, textTransId, textAmt, textShop;
    private RelativeLayout layout, layoutFail, layoutRetry;
    private Button done, retry, tryagain;
    private DataHelp dh;
    private MyOpenHelper db;
    private MasterService task;
    private UserEntity user;
    private BookingEntities bookData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment_complete);

        DeclareVariables();

        Intent intent = getIntent();
        getTransId = intent.getExtras().getString("TRANSEID");
        getIsDone = intent.getExtras().getBoolean("DONE");
        getOld = intent.getExtras().getBoolean("OLD");
        getPaidAmt = intent.getExtras().getString("PAIDAMT");
        getPaymentName = intent.getExtras().getString("PAYMENT_NAME");
        getUserID = intent.getExtras().getInt("USER_ID");
        getParkingAddrID = intent.getExtras().getInt("PARKING_ADD_ID");
        getVehicleType = intent.getExtras().getString("VEHICLE_TYPE");
        getBookingDate = intent.getExtras().getString("BOOKING_DATE");
        getBookingTime = intent.getExtras().getString("BOOKING_TIME");
        getParkingName = intent.getExtras().getString("PARKING_NAME");
        getStatus = intent.getExtras().getString("STATUS");
        getBookingStatus = intent.getExtras().getString("BOOK_STATUS");

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(PaymentComplete.this, MainActivity.class));
            }
        });

        bookData = new BookingEntities();
        bookData.setUserID(getUserID);
        bookData.setParkingAddrID(getParkingAddrID);
        bookData.setVehicleType(getVehicleType);
        bookData.setBookingDate(getBookingDate);
        bookData.setBookingTime(getBookingTime);
        bookData.setPaidAmount(Double.valueOf(getPaidAmt));
        bookData.setPaymentName(getPaymentName);
        bookData.setTransactionID(getTransId);
        bookData.setPaymentMode("Online");
        bookData.setPaymentStatus(getStatus);
        bookData.setBookingStatus(getBookingStatus);
        getData(bookData);
    }

    public void DeclareVariables() {
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        layout = (RelativeLayout) findViewById(R.id.payHide);
        layoutFail = (RelativeLayout) findViewById(R.id.payFail);
        layoutRetry = (RelativeLayout) findViewById(R.id.refresh);
        textCustName = (TextView) findViewById(R.id.payCustName);
        textTransId = (TextView) findViewById(R.id.payTrans);
        textAmt = (TextView) findViewById(R.id.payAmt);
        textShop = (TextView) findViewById(R.id.payShop);
        done = (Button) findViewById(R.id.back);
        tryagain = (Button) findViewById(R.id.tryagin);
        retry = (Button) findViewById(R.id.retry);

        dh = new DataHelp(this);
        db = new MyOpenHelper(getApplicationContext());

        user = db.getUser();
    }

    private void getError() {
        layoutRetry.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        layoutFail.setVisibility(View.GONE);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(bookData);
            }
        });
    }

    private void getData(final BookingEntities bookData) {
        try {
            if (MainActivity.isNetworkAvaliable(getApplicationContext())) {
                if (bookData != null) {
                    ApiInterface apiService = MasterService.getCaller().create(ApiInterface.class);
                    Call<String> call = apiService.bookParking(bookData);
                    task = new MasterService();
                    task.showProgress(this);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            try {
                                String str = response.body();
                                if (str.equals("Success")) {
                                    textCustName.setText(user.getName());
                                    textTransId.setText(bookData.getTransactionID());
                                    textAmt.setText(String.valueOf("Rs. " + bookData.getPaidAmount()));
                                    textShop.setText(getParkingName);

                                    if (getIsDone & getOld) {
                                        layout.setVisibility(View.VISIBLE);
                                        layoutFail.setVisibility(View.GONE);
                                        layoutRetry.setVisibility(View.GONE);
                                    } else if (getIsDone == false & getOld == true) {
                                        layoutFail.setVisibility(View.VISIBLE);
                                        layout.setVisibility(View.GONE);
                                        layoutRetry.setVisibility(View.GONE);
                                        tryagain.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(new Intent(PaymentComplete.this, MainActivity.class));
                                            }
                                        });
                                        Toast.makeText(getApplicationContext(), "Transaction failed", Toast.LENGTH_LONG).show();
                                    } else if (getIsDone == false & getOld == false) {
                                        getError();
                                    }
                                } else {
                                    getError();
                                }
                                task.dismissProgress(PaymentComplete.this);
                            } catch (Exception e) {
                                task.dismissProgress(PaymentComplete.this);
                                Toast.makeText(getApplicationContext(), "Some problem occure", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            task.dismissProgress(PaymentComplete.this);
                            Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
