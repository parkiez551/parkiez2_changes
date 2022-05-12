package com.parkiezmobility.parkiez.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parkiezmobility.parkiez.R;

public class PaymentSelection extends AppCompatActivity {
    private RadioButton paytem, freecharge, mobikwik, creditcard, jiomoney, sbibuddy, debitecard, netbanking, upi;
    private Button continueBtn;
    private TextView paymentAmount;
    private String getName, getMobileNo, getEmailID, getAmt, getPaymentName = null, getVehicleType, getBookingDate;
    private String getBookingTime, getParkingName;
    private int getUserID, getParkingAddrID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_selection);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        paytem = (RadioButton) findViewById(R.id.radio_paytm);
        freecharge = (RadioButton) findViewById(R.id.radio_freecharge);
        mobikwik = (RadioButton) findViewById(R.id.radio_mobikwik);
        jiomoney = (RadioButton) findViewById(R.id.radio_jiomoney);
        upi = (RadioButton) findViewById(R.id.radio_upi);
        sbibuddy = (RadioButton) findViewById(R.id.radio_sbibuddy);
        creditcard = (RadioButton) findViewById(R.id.radio_credit);
        debitecard = (RadioButton) findViewById(R.id.radio_debit);
        netbanking = (RadioButton) findViewById(R.id.radio_netbanking);

        Intent oIntent = getIntent();
        getName = oIntent.getExtras().getString("CUSTOMER_NAME");
        getMobileNo = oIntent.getExtras().getString("MOBILE_NUMBER");
        getEmailID = oIntent.getExtras().getString("EMAIL_ID");
        getAmt = oIntent.getExtras().getString("PAYMENT_AMT");
        getUserID = oIntent.getExtras().getInt("USER_ID");
        getParkingAddrID = oIntent.getExtras().getInt("PARKING_ADD_ID");
        getVehicleType = oIntent.getExtras().getString("VEHICLE_TYPE");
        getBookingDate = oIntent.getExtras().getString("BOOKING_DATE");
        getBookingTime = oIntent.getExtras().getString("BOOKING_TIME");
        getParkingName = oIntent.getExtras().getString("PARKING_NAME");

        continueBtn = (Button) findViewById(R.id.btn_continue);
        paymentAmount = (TextView) findViewById(R.id.paymentamt);
        paymentAmount.setText(getAmt);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPaymentName != null) {
                    Intent intent = new Intent(PaymentSelection.this, CCAvenueGateway.class);
                    intent.putExtra("CUSTOMER_NAME", getName);
                    intent.putExtra("MOBILE_NUMBER", getMobileNo);
                    intent.putExtra("EMAIL_ID", getEmailID);
                    intent.putExtra("PAYMENT_AMT", getAmt);
                    intent.putExtra("PAYMENT_NAME", getPaymentName);
                    intent.putExtra("USER_ID", getUserID);
                    intent.putExtra("PARKING_ADD_ID", getParkingAddrID);
                    intent.putExtra("VEHICLE_TYPE", getVehicleType);
                    intent.putExtra("BOOKING_DATE", getBookingDate);
                    intent.putExtra("BOOKING_TIME", getBookingTime);
                    intent.putExtra("PARKING_NAME", getParkingName);
                    startActivity(intent);
                } else {
                    Toast.makeText(PaymentSelection.this, "Select your payment gateway", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radio_paytm:
                if (checked) {
                    getPaymentName = "Paytm";
                    states(1);
                }
                break;
            case R.id.radio_freecharge:
                if (checked) {
                    getPaymentName = "FreeCharge";
                    states(2);
                }
                break;
            case R.id.radio_mobikwik:
                if (checked) {
                    getPaymentName = "MobiKwik";
                    states(3);
                }
                break;
            case R.id.radio_jiomoney:
                if (checked) {
                    getPaymentName = "JioMoney";
                    states(4);
                }
                break;
            case R.id.radio_sbibuddy:
                if (checked) {
                    getPaymentName = "SBIBuddy";
                    states(5);
                }
                break;
            case R.id.radio_upi:
                if (checked) {
                    getPaymentName = "UPI";
                    states(6);
                }
                break;
            case R.id.radio_credit:
                if (checked) {
                    getPaymentName = "Credit Card";
                    states(7);
                }
                break;
            case R.id.radio_debit:
                if (checked) {
                    getPaymentName = "Debit Card";
                    states(8);
                }
                break;
            case R.id.radio_netbanking:
                if (checked) {
                    getPaymentName = "Net Banking";
                    states(9);
                }
                break;
        }
    }

    private void states(int setflag) {
        if (setflag != 1) {
            paytem.setChecked(false);
        }
        if (setflag != 2) {
            freecharge.setChecked(false);
        }
        if (setflag != 3) {
            mobikwik.setChecked(false);
        }
        if (setflag != 4) {
            jiomoney.setChecked(false);
        }
        if (setflag != 5) {
            sbibuddy.setChecked(false);
        }
        if (setflag != 6) {
            upi.setChecked(false);
        }
        if (setflag != 7) {
            creditcard.setChecked(false);
        }
        if (setflag != 8) {
            debitecard.setChecked(false);
        }
        if (setflag != 9) {
            netbanking.setChecked(false);
        }
    }
}
