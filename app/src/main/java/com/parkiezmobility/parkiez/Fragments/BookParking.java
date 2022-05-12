package com.parkiezmobility.parkiez.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parkiezmobility.parkiez.Activities.Login;
import com.parkiezmobility.parkiez.AppUtils;
import com.parkiezmobility.parkiez.BlurView;
import com.parkiezmobility.parkiez.Database.MyOpenHelper;
import com.parkiezmobility.parkiez.Entities.AddressEntities;
import com.parkiezmobility.parkiez.Entities.UserEntity;
import com.parkiezmobility.parkiez.R;
import com.parkiezmobility.parkiez.Services.MasterService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookParking extends Fragment implements AdapterView.OnItemSelectedListener {
    private Button btnDatePicker, btnTimePicker, btnBookParking;
    private EditText txtDate, txtTime;
    private Spinner Parking_hrs;
    private TextView ParkingAmount;
    private RadioGroup VehclType;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Calendar c;
    private AddressEntities Parking = null;
    private String VehicleType = "Car", bookDate, bookTime;
    private MyOpenHelper db;
    private UserEntity user;
    private MasterService task;

    public AlertDialog dialogWhichDisplayAlert;
    public Dialog fakeDialogUseToGetWindowForBlurEffect;

    public BookParking() {
    }

    public BookParking(AddressEntities Parking) {
        this.Parking = Parking;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_book_parking, container, false);

        DeclareVariables(v);

        VehclType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int amnt = 0;
                if(checkedId == R.id.bike) {
                    VehicleType = "Bike";
                    amnt = Integer.parseInt(Parking.getParking().getBikePrice());
                } else if(checkedId == R.id.car) {
                    VehicleType = "Car";
                    amnt = Integer.parseInt(Parking.getParking().getCarPrice());
                }
                ParkingAmount.setText("RS. " + String.valueOf(amnt * hrs));
            }

        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch Date Picker Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTime.setText((hourOfDay) + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        btnBookParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    if (isValidate()) {
                        fakeDialogUseToGetWindowForBlurEffect = new Dialog(getActivity());
                        new BlurAsyncTask().execute();
//                        String amnt = (String) ParkingAmount.getText();
//                        String PayableAmount = amnt.replace("RS. ", "");
//
//                        Intent intent = new Intent(getActivity(), PaymentSelection.class);
//                        intent.putExtra("CUSTOMER_NAME", user.getName());
//                        intent.putExtra("MOBILE_NUMBER", user.getMobileNo());
//                        intent.putExtra("EMAIL_ID", user.getEmail());
//                        intent.putExtra("PAYMENT_AMT", PayableAmount);
//                        intent.putExtra("USER_ID", user.getUserID());
//                        intent.putExtra("PARKING_ADD_ID", Parking.getAddressID());
//                        intent.putExtra("VEHICLE_TYPE", VehicleType);
//                        intent.putExtra("BOOKING_DATE", bookDate);
//                        intent.putExtra("BOOKING_TIME", bookTime);
//                        intent.putExtra("PARKING_NAME", Parking.getParkingName());
//                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getActivity(), "You are not logged in...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);
                }
            }
        });

        return v;
    }

    class BlurAsyncTask extends AsyncTask<Void, Integer, Bitmap> {

        protected Bitmap doInBackground(Void...arg0) {
            Bitmap map  = AppUtils.takeScreenShot(getActivity());
            Bitmap fast = new BlurView().fastBlur(map, 10);
            return fast;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        protected void onPostExecute(Bitmap result) {
            if (result != null){
                final Drawable draw=new BitmapDrawable(getResources(),result);
                Window window = fakeDialogUseToGetWindowForBlurEffect.getWindow();
                window.setBackgroundDrawable(draw);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                window.setGravity(Gravity.CENTER);
                fakeDialogUseToGetWindowForBlurEffect.show();

                // real one
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(R.layout.payment_dialog_layout);

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.payment_dialog_layout, null);

                // set values for custom dialog components - text, image and button
                LinearLayout payment_layout = (LinearLayout) view.findViewById(R.id.payment_layout);
                ImageView upi = (ImageView) view.findViewById(R.id.upi);
                final LinearLayout phone_pay_layout = (LinearLayout) view.findViewById(R.id.phonpay_layout);
                ImageView phone_pay = (ImageView) view.findViewById(R.id.phone_pay);
                final ImageView phone_pay_arrow = (ImageView) view.findViewById(R.id.phon_pay_arrow);
                final LinearLayout g_pay_layout = (LinearLayout) view.findViewById(R.id.gpay_layout);
                ImageView g_pay = (ImageView) view.findViewById(R.id.g_pay);
                final ImageView g_pay_arrow = (ImageView) view.findViewById(R.id.g_pay_arrow);
                final LinearLayout paytm_layout = (LinearLayout) view.findViewById(R.id.paytm_layout);
                ImageView paytm = (ImageView) view.findViewById(R.id.paytm);
                final ImageView paytm_arrow = (ImageView) view.findViewById(R.id.paytm_arrow);

                phone_pay_layout.setBackground(getResources().getDrawable(R.drawable.phonpay_border));
                phone_pay_arrow.setVisibility(View.VISIBLE);
                g_pay_layout.setBackground(null);
                g_pay_arrow.setVisibility(View.GONE);
                paytm_layout.setBackground(null);
                paytm_arrow.setVisibility(View.GONE);

                upi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "UPI Integration Still In Process...", Toast.LENGTH_SHORT).show();
                    }
                });

                phone_pay.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "PhonePay Integration Still In Process...", Toast.LENGTH_SHORT).show();
                        phone_pay_layout.setBackground(getResources().getDrawable(R.drawable.phonpay_border));
                        phone_pay_arrow.setVisibility(View.VISIBLE);
                        g_pay_layout.setBackground(null);
                        g_pay_arrow.setVisibility(View.GONE);
                        paytm_layout.setBackground(null);
                        paytm_arrow.setVisibility(View.GONE);
                    }
                });

                g_pay.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "Gpay Integration Still In Process...", Toast.LENGTH_SHORT).show();
                        phone_pay_layout.setBackground(null);
                        phone_pay_arrow.setVisibility(View.GONE);
                        g_pay_layout.setBackground(getResources().getDrawable(R.drawable.gpay_border));
                        g_pay_arrow.setVisibility(View.VISIBLE);
                        paytm_layout.setBackground(null);
                        paytm_arrow.setVisibility(View.GONE);
                    }
                });

                paytm.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "Paytm Integration Still In Process...", Toast.LENGTH_SHORT).show();
                        phone_pay_layout.setBackground(null);
                        phone_pay_arrow.setVisibility(View.GONE);
                        g_pay_layout.setBackground(null);
                        g_pay_arrow.setVisibility(View.GONE);
                        paytm_layout.setBackground(getResources().getDrawable(R.drawable.paytm_border));
                        paytm_arrow.setVisibility(View.VISIBLE);
                    }
                });

//                builder.setTitle("Lets Blur");
//                builder.setMessage("This is Blur Demo");
//                builder.setCancelable(false);
//                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        fakeDialogUseToGetWindowForBlurEffect.dismiss();
//                        dialog.cancel();
//                    }
//                });
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        fakeDialogUseToGetWindowForBlurEffect.dismiss();
//                        dialog.cancel();
//                    }
//                });
                dialogWhichDisplayAlert = builder.create();
                dialogWhichDisplayAlert.setView(view);

                // position real dialogWhichDisplayAlert using Gravity.CENTER;
                dialogWhichDisplayAlert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams wmlp = dialogWhichDisplayAlert.getWindow().getAttributes();
                wmlp.gravity = Gravity.CENTER;
                dialogWhichDisplayAlert.show();
                dialogWhichDisplayAlert.getWindow().setBackgroundDrawable(null);
                dialogWhichDisplayAlert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        fakeDialogUseToGetWindowForBlurEffect.dismiss();
                    }
                });
            }

        }
    }


    public void DeclareVariables(View v) {
        getActivity().setTitle("Book Parking");

        VehclType = (RadioGroup) v.findViewById(R.id.vehicleGrp);
        btnDatePicker = (Button) v.findViewById(R.id.btn_date);
        btnTimePicker = (Button) v.findViewById(R.id.btn_time);
        txtDate = (EditText) v.findViewById(R.id.in_date);
        txtTime = (EditText) v.findViewById(R.id.in_time);
        btnBookParking = (Button) v.findViewById(R.id.btn_book);
        Parking_hrs = (Spinner) v.findViewById(R.id.parking_hrs);
        ParkingAmount = (TextView) v.findViewById(R.id.parking_amnt);

        Parking_hrs.setOnItemSelectedListener(this);
        if (VehclType.getCheckedRadioButtonId() == R.id.car)
            ParkingAmount.setText(Parking.getParking().getCarPrice());
        else
            ParkingAmount.setText(Parking.getParking().getBikePrice());

        List<String> list = new ArrayList<String>();
        for (int i=1; i<=24; i++) {
            list.add(String.valueOf(i));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Parking_hrs.setAdapter(dataAdapter);

        db = new MyOpenHelper(getActivity());
        user = db.getUser();

        c = Calendar.getInstance();
        // Get Current Date
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Get Current Time
        mHour = c.get(Calendar.HOUR_OF_DAY) + 1;
        mMinute = c.get(Calendar.MINUTE);

        txtDate.setText(mDay + "-" + (mMonth+1) + "-" +mYear);
        txtTime.setText(mHour + ":" + mMinute);
    }

    public boolean isValidate() {
        boolean valid = true;
        bookDate = txtDate.getText().toString();
        bookTime = txtTime.getText().toString();

        if (bookDate.isEmpty()) {
            requestFocus(txtDate);
            txtDate.setError("Please enter a date!");
            valid = false;
        } else if (bookTime.isEmpty()){
            requestFocus(txtTime);
            txtTime.setError("Please enter a address!");


            valid = false;
        }

        return valid;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

//    private void getData(BookingEntities bookData) {
//        try {
//            if (MainActivity.isNetworkAvaliable(getActivity())) {
//                ApiInterface apiService = MasterService.getCaller().create(ApiInterface.class);
//                Call<String> call = apiService.bookParking(bookData);
//                task = new MasterService();
//                task.showProgress(getActivity());
//                call.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        task.dismissProgress(getActivity());
//                        String str = response.body();
//                        if (str.equals("Success")) {
//                            Toast.makeText(getActivity(), "Parking is booked successfully...", Toast.LENGTH_LONG).show();
//                            Fragment fragment = new Parkings();
//                            getFragmentManager().beginTransaction()
//                                    .replace(((ViewGroup) getView().getParent()).getId(), fragment)
//                                    .addToBackStack(null)
//                                    .commit();
//                        } else {
//                            Toast.makeText(getActivity(), "Parking is not booked please try again...", Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        task.dismissProgress(getActivity());
//                        Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();
//                    }
//                });
//
//            } else {
//                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(getActivity(), "some problem occure", Toast.LENGTH_LONG).show();
//        }
//    }

    private int hrs = 1;
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        int amnt = 0;

        if (VehclType.getCheckedRadioButtonId() == R.id.car)
            amnt = Integer.parseInt(Parking.getParking().getCarPrice());
        else
            amnt = Integer.parseInt(Parking.getParking().getBikePrice());

        hrs = Integer.parseInt(item);
        ParkingAmount.setText("RS. " + String.valueOf(amnt * hrs));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
