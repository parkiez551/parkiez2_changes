package com.parkiezmobility.parkiez.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parkiezmobility.parkiez.Adapters.AllBookingList;
import com.parkiezmobility.parkiez.Database.MyOpenHelper;
import com.parkiezmobility.parkiez.Entities.BookedDetailsEntities;
import com.parkiezmobility.parkiez.Entities.UserEntity;
import com.parkiezmobility.parkiez.Interfaces.ApiInterface;
import com.parkiezmobility.parkiez.Interfaces.OnCancelClick;
import com.parkiezmobility.parkiez.Interfaces.OnDirectionClick;
import com.parkiezmobility.parkiez.MainActivity;
import com.parkiezmobility.parkiez.R;
import com.parkiezmobility.parkiez.Services.MasterService;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookedParkings extends Fragment implements OnCancelClick, OnDirectionClick {
    private ListView list;
    private TextView msg;
    private MasterService task;
    private MyOpenHelper db;
    private UserEntity user;
    private ArrayList<BookedDetailsEntities> parkings , parkings1;
    private AllBookingList bookingAdap;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private Calendar c;
    private String date_str, time_str;

    public BookedParkings() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_booked_parkings, container, false);

        DeclareVariables(v);

        getData();
        return v;
    }

    public void DeclareVariables(View v) {
        getActivity().setTitle("Booked Parkings");

        list = (ListView) v.findViewById(R.id.mylist);
        msg = (TextView) v.findViewById(R.id.msg);

        c = Calendar.getInstance();

        // Get Current Date
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        date_str = mDay + "-" + mMonth + "-" +mYear;

        // Get Current Time
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        time_str = mHour + ":" + mMinute;

        db = new MyOpenHelper(getActivity());
        user = db.getUser();
    }

    private void getData() {
        try {
            ApiInterface apiService = MasterService.getCaller().create(ApiInterface.class);

            JSONObject user1 = new JSONObject();
            user1.put("UserID", user.getUserID());

            Call<ArrayList<BookedDetailsEntities>> call = apiService.GetBookings(user1);
            task = new MasterService();
            task.showProgress(getActivity());

            call.enqueue(new Callback<ArrayList<BookedDetailsEntities>>() {
                @Override
                public void onResponse(Call<ArrayList<BookedDetailsEntities>> call, Response<ArrayList<BookedDetailsEntities>> response) {
                    task.dismissProgress(getActivity());
                    parkings = response.body();
                    if (parkings != null) {
                        parkings1 = new ArrayList<BookedDetailsEntities>();
                        for(int i=0; i<parkings.size(); i++) {
                            if (!parkings.get(i).getBookingStatus().equals("Cancelled")) {
                                try {
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                    String date = parkings.get(i).getBookedDate();
                                    Date CurrentDate = formatter.parse(date_str);
                                    Date BookingDate = formatter.parse(date);

                                    if (CurrentDate.compareTo(BookingDate) <= 0) {
                                        if (CurrentDate.compareTo(BookingDate) == 0) {
                                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                            String time = parkings.get(i).getBoookedTime();
                                            Date CurrentTime = sdf.parse(time_str);
                                            Date BookingTime = sdf.parse(time);
                                            long elapsed = CurrentTime.getTime() - BookingTime.getTime();

                                            if(elapsed <= 0) {
                                                parkings1.add(parkings.get(i));
                                            }
                                        } else {
                                            parkings1.add(parkings.get(i));
                                        }
                                    }

                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }
                        }
                        setData();
                    } else {
                        Toast.makeText(getActivity(), "Some problem occured, please try again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<BookedDetailsEntities>> call, Throwable t) {
                    task.dismissProgress(getActivity());
                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData() {

        try {
            if (parkings1.size() > 0) {
                list.setVisibility(View.VISIBLE);
                msg.setVisibility(View.GONE);
//                bookingAdap = new AllBookingList(getActivity(), R.layout.custom_booking_list, parkings1, 1, this, this);
                list.setAdapter(bookingAdap);
            } else {
                list.setVisibility(View.GONE);
                msg.setVisibility(View.VISIBLE);
                String text = "Currently no Bookings";
                msg.setText(text);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Some problem occured, please try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnCancelClick(BookedDetailsEntities BookingDetails) {
        ShowAlert(BookingDetails);
    }

    public void ShowAlert(final BookedDetailsEntities data) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle("Cancel Booking");
            alertDialogBuilder.setMessage("You Want to Cancel Booking?");

            alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    data.setBookingStatus("Cancelled");
                    getData(data);
                    dialog.cancel();
                }
            });

            alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void OnDirectionClick(BookedDetailsEntities BookingDetails) {
        MainActivity m = new MainActivity();

        if (m.longitude == 0 && m.latitude == 0) {
            Toast.makeText(getActivity(), "Current Location Can't Get", Toast.LENGTH_SHORT).show();
        } else {
            String sLat = String.valueOf(m.latitude);
            String sLong = String.valueOf(m.longitude);
            String dLat = BookingDetails.getParking().getLat();
            String dLong = BookingDetails.getParking().getLong();

            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr=" + sLat + "," + sLong + "&daddr=" + dLat + "," + dLong));
            startActivity(intent);
        }
    }

    private void getData(final BookedDetailsEntities bookData) {
        try {
            if (MainActivity.isNetworkAvaliable(getActivity())) {
                if (bookData != null) {
                    ApiInterface apiService = MasterService.getCaller().create(ApiInterface.class);
                    Call<String> call = apiService.cancelBooking(bookData);
                    task = new MasterService();
                    task.showProgress(getActivity());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            try {
                                String str = response.body();
                                if (str.equals("Cancelled")) {
                                    Toast.makeText(getActivity(), "Cancelled Successful", Toast.LENGTH_LONG).show();
                                    Fragment fragment = new BookedParkings();
                                    getFragmentManager().popBackStack();
                                    getFragmentManager().beginTransaction()
                                            .replace(((ViewGroup) getView().getParent()).getId(), fragment)
                                            .addToBackStack(null)
                                            .commit();

//                                    getData();
                                } else {
                                    Toast.makeText(getActivity(), "Some problem occure", Toast.LENGTH_LONG).show();
                                }
                                task.dismissProgress(getActivity());
                            } catch (Exception e) {
                                task.dismissProgress(getActivity());
                                Toast.makeText(getActivity(), "Some problem occure", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            task.dismissProgress(getActivity());
                            Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
