package com.parkiezmobility.parkiez.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parkiezmobility.parkiez.AppUtils;
import com.parkiezmobility.parkiez.BlurView;
import com.parkiezmobility.parkiez.Entities.AddressEntities;
import com.parkiezmobility.parkiez.Entities.ParkingEntities;
import com.parkiezmobility.parkiez.Entities.VehicleTypes;
import com.parkiezmobility.parkiez.MainActivity;
import com.parkiezmobility.parkiez.R;
import com.parkiezmobility.parkiez.managers.ApplicationManager;
import com.parkiezmobility.parkiez.utility.Constants;
import com.parkiezmobility.parkiez.utility.Utility;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.RequiresApi;

public class TimeDurationDialog  {
    private final Parkings parkingFragment;
    private int hrs = 1;
    private int amnt = 0,amnt1=0;
    private boolean flag = true;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Calendar c;
    Activity activity;
    AddressEntities Parking;
    ParkingEntities parkingEntities;
    private ImageView car_icon;
    private ImageView vehicle_selected;
    private ImageView bike_icon;
    private EditText vehicle_no;
    private TextView vehicletype;
    private AlertDialog alertDialog;
    private Dialog dialog;
    private AlertDialog timeDurationAlert;
    String vehicle_type;
    // private AlertDialog.Builder builder;
    //private AlertDialog timeDurationAlert;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TimeDurationDialog(Parkings parkingFragment, ParkingEntities parkingEntities){
        this.activity = parkingFragment.getActivity();
        this.parkingEntities = parkingEntities;
        this.parkingFragment = parkingFragment;
        c = Calendar.getInstance();
        // Get Current Date
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Get Current Time
        mHour = c.get(Calendar.HOUR_OF_DAY) + 1;
        mMinute = c.get(Calendar.MINUTE);
        dialog = new Dialog(this.activity);
    }

    public void display(){
        new ParkingAsyncTask().execute();
    }
    class ParkingAsyncTask extends AsyncTask<Void, Integer, Bitmap> {

        protected Bitmap doInBackground(Void... arg0) {
            Bitmap fast = null;
            try {
                Bitmap map = AppUtils.takeScreenShot(TimeDurationDialog.this.activity);
                fast = new BlurView().fastBlur(map, 4);
            } catch (Exception e) {
                e.getMessage();
            }
            return fast;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                final Drawable draw = new BitmapDrawable(TimeDurationDialog.this.activity.getResources(), result);
                Window window = TimeDurationDialog.this.dialog.getWindow();
                window.setBackgroundDrawable(draw);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                window.setGravity(Gravity.CENTER);
                TimeDurationDialog.this.dialog.show();

                LayoutInflater inflater = TimeDurationDialog.this.activity.getLayoutInflater();
                View view = inflater.inflate(R.layout.time_duration_dialog4, null);
               // View view = inflater.inflate(R.layout.time_duration_dialog_layout2, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(TimeDurationDialog.this.activity);
                builder.setView(view);
                vehicletype = (TextView) view.findViewById(R.id.vehicletype);
                // set values for custom dialog components - text, image and button
                car_icon = (ImageView) view.findViewById(R.id.car_icon);
                final ImageView vehicle_selected = (ImageView) view.findViewById(R.id.selected_vehicle);
                bike_icon = (ImageView) view.findViewById(R.id.bike_icon);
                final EditText date_select = (EditText) view.findViewById(R.id.date_select);
                final EditText time_select = (EditText) view.findViewById(R.id.time_select);

                final EditText vehicle_no = (EditText) view.findViewById(R.id.vehicle_num);
                String vehicle = Utility.getPreference(ApplicationManager.getInstance().getContext(), Constants.LAST_VEHICLE_PREF_KEY);
                if(vehicle.compareToIgnoreCase("0") != 0) {
                    vehicle_no.setText(vehicle);
                }
                ImageView RemoveBtn = view.findViewById(R.id.remove_btn);
                final TextView parking_hrs_new = (TextView) view.findViewById(R.id.parking_hrs_new);
                ImageView AddBtn = view.findViewById(R.id.add_btn);

                final EditText parking_amnt = (EditText) view.findViewById(R.id.parking_amnt);
                Button bookParking = (Button) view.findViewById(R.id.bookParking);

                AddBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String parkingHrs = parking_hrs_new.getText().toString();
                        parking_hrs_new.setText(String.valueOf(Integer.parseInt(parkingHrs)+1));

                        String item = parking_hrs_new.getText().toString();

                        hrs = Integer.parseInt(item);
                        if (flag){
                            amnt = 0;
                            amnt = parkingEntities.getVehicle_types().get(0).getCost();
                        } else{
                            amnt = 0;
                            amnt = parkingEntities.getVehicle_types().get(1).getCost();  //  --array error
                        }
                        parking_amnt.setText("Rs. " + String.valueOf(amnt * hrs));
                    }
                });

                RemoveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String parkingHrs = parking_hrs_new.getText().toString();
                        if (!parkingHrs.equals("0")) {
                            parking_hrs_new.setText(String.valueOf(Integer.parseInt(parkingHrs)-1));

                            String item = parking_hrs_new.getText().toString();
                            hrs = Integer.parseInt(item);
                            if (flag){
                                amnt = 0;
                                amnt = parkingEntities.getVehicle_types().get(0).getCost();
                            } else{
                                amnt = 0;
                               amnt = parkingEntities.getVehicle_types().get(1).getCost(); //  array error
                            }
                            parking_amnt.setText("Rs. " + String.valueOf(amnt * hrs));
                        }
                    }
                });

                amnt = 0;
                amnt = parkingEntities.getVehicle_types().get(0).getCost();
              //  amnt1=0;
                //amnt1= parkingEntities.getVehicle_types().get(1).getCost();
              //  Log.d("****","amnt1="+amnt1);
                parking_amnt.setText("Rs. " + String.valueOf(amnt * hrs));

                String date_day = String.valueOf(mDay);
                String date_month = String.valueOf(mMonth + 1);
                String date_year = String.valueOf(mYear);
                if (mDay < 10) {
                    date_day = "0" + date_day;
                }
                if (mMonth < 9) {
                    date_month = "0" + date_month;
                }
                date_select.setText(date_day + "-" + date_month + "-" + date_year);

                String time_hrs = String.valueOf(mHour);
                String time_min = String.valueOf(mMinute);
                if (mHour < 10) {
                    time_hrs = "0" + time_hrs;
                }
                if (mMinute < 10) {
                    time_min = "0" + time_min;
                }
                time_select.setText(time_hrs + ":" + time_min);

                date_select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Launch Date Picker Dialog
                        DatePickerDialog datePickerDialog = new DatePickerDialog(TimeDurationDialog.this.activity,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {

                                        String date_day = String.valueOf(dayOfMonth);
                                        String date_month = String.valueOf(monthOfYear + 1);
                                        String date_year = String.valueOf(year);

                                        if (dayOfMonth < 10) {
                                            date_day = "0" + date_day;
                                        }

                                        if (monthOfYear < 9) {
                                            date_month = "0" + date_month;
                                        }

                                        date_select.setText(date_day + "-" + date_month + "-" + date_year);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                    }
                });

                time_select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(TimeDurationDialog.this.activity,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        String time_hrs = String.valueOf(hourOfDay);
                                        String time_min = String.valueOf(minute);

                                        if (hourOfDay < 10) {
                                            time_hrs = "0" + time_hrs;
                                        }

                                        if (minute < 10) {
                                            time_min = "0" + time_min;
                                        }

                                        time_select.setText(time_hrs + ":" + time_min);
                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();
                    }
                });
                car_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                        public void onClick(View view) {
                        amnt = 0;
                        vehicletype.setText("Car");
                       /* bike_icon.setImageDrawable(TimeDurationDialog.this.activity.getResources().getDrawable(R.drawable.ic_baseline_directions_bike_36));
                        car_icon.setImageDrawable(TimeDurationDialog.this.activity.getResources().getDrawable(R.drawable.ic_baseline_directions_car_selected_36));
                        vehicle_selected.setImageDrawable(TimeDurationDialog.this.activity.getResources().getDrawable(R.drawable.ic_baseline_toggle_off_36));*/
//
                       // bike_icon.setImageDrawable(TimeDurationDialog.this.activity.getResources().getDrawable(R.drawable.blackbike));
                        car_icon.setImageDrawable(TimeDurationDialog.this.activity.getResources().getDrawable(R.drawable.greencar));
                        //vehicle_selected.setImageDrawable(TimeDurationDialog.this.activity.getResources().getDrawable(R.drawable.ic_baseline_toggle_off_36));
//
//
//
                      amnt = parkingEntities.getVehicle_types().get(0).getCost();
                        //amnt=1;
                        parking_amnt.setText("Rs. " + String.valueOf(amnt * hrs));

                        flag = true;
                    }
                });

            /*   bike_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       amnt = 0;
                        vehicletype.setText("Bike");

                        bike_icon.setImageDrawable(TimeDurationDialog.this.activity.getResources().getDrawable(R.drawable.greenbike));
                        car_icon.setImageDrawable(TimeDurationDialog.this.activity.getResources().getDrawable(R.drawable.blackcar));
                        vehicle_selected.setImageDrawable(TimeDurationDialog.this.activity.getResources().getDrawable(R.drawable.ic_baseline_toggle_on_36));
                      //  parkingEntities.setVehicle_types(ArrayList<VehicleTypes>);
                           // parkingEntities.setVehicle_types( vehicletype)

              //        amnt = Integer.parseInt(parkingEntities.getParking().getBikePrice());
                     //   int a=parkingEntities.getVehicle_types().size();
                      //eeeeeeeee  Log.d("*****","size" +a);

                        //Log.d("*****","vehivle_type" +parkingEntities.getVehicle_types());
                        //parkingEntities.setVehicle_types(ArrayList<VehicleTypes> vehicle_type.add);
                       amnt =parkingEntities.getVehicle_types().get(1).getCost();  //  --array error
                       // parkingEntities.setVehicle_types(ArrayList<VehicleTypes> "");
                        parking_amnt.setText("Rs. " + String.valueOf(amnt * hrs));
                        flag = false;
                    }
                });*/
                if (parkingEntities.getNo_of_columns()<=0)
                    bike_icon.setEnabled(false);

                bookParking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (vehicle_no.getText().toString().isEmpty()) {
                            Toast.makeText(TimeDurationDialog.this.activity, "Kindly Enter Vehicle Number", Toast.LENGTH_SHORT).show();
                        } else if (parking_hrs_new.getText().toString().equals("0")) {
                            Toast.makeText(TimeDurationDialog.this.activity, "Kindly Select Hrs.", Toast.LENGTH_SHORT).show();
                        } else {

                            if(flag==true)
                            {
                                Log.d("***","car");
                                vehicle_type="4 wheeler";

                            }
                            else
                            {
                                Log.d("***","bike");
                               vehicle_type="2 wheeler";
                            }
                            Utility.setPreference(ApplicationManager.getInstance().getContext(), Constants.LAST_VEHICLE_PREF_KEY, vehicle_no.getText().toString().trim());
                            parkingFragment.generateOrder(parking_amnt.getText().toString().trim(), date_select.getText().toString().trim(),
                                    time_select.getText().toString().trim(), hrs, vehicle_no.getText().toString().trim(),vehicle_type);
                            TimeDurationDialog.this.timeDurationAlert.dismiss();
                            TimeDurationDialog.this.timeDurationAlert.cancel();


                        }
                    }
                });

                timeDurationAlert = builder.create();
                timeDurationAlert.setView(view);

                // position real dialogWhichDisplayAlert using Gravity.CENTER;
                timeDurationAlert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams wmlp = timeDurationAlert.getWindow().getAttributes();
                wmlp.gravity = Gravity.CENTER;
                timeDurationAlert.show();
                timeDurationAlert.getWindow().setBackgroundDrawable(null);
                timeDurationAlert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        TimeDurationDialog.this.dialog.dismiss();
                    }
                });
            }
        }
    }
}