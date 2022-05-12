package com.parkiezmobility.parkiez.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parkiezmobility.parkiez.Entities.BookedDetailsEntities;
import com.parkiezmobility.parkiez.Entities.BookingDetailsEntities;
import com.parkiezmobility.parkiez.Interfaces.OnCancelClick;
import com.parkiezmobility.parkiez.Interfaces.OnDirectionClick;
import com.parkiezmobility.parkiez.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AllBookingList extends ArrayAdapter<BookingDetailsEntities> {
    Context context;
    private ArrayList<BookingDetailsEntities> SubsList;
    OnCancelClick OnCallBack;
    OnDirectionClick OnCallBack1;
    int flag;

    public AllBookingList(Activity activity, int userlist, ArrayList<BookingDetailsEntities> subList, int flag, OnCancelClick listner,
                          OnDirectionClick listner1) {
        super(activity, userlist, subList);
        this.context = activity.getApplicationContext();
        this.SubsList = new ArrayList<BookingDetailsEntities>();
        this.SubsList.addAll(subList);
        this.flag = flag;
        this.OnCallBack = listner;
        this.OnCallBack1 = listner1;
    }

    private class ViewHolder {
        TextView BookingDate, BookingTime, ParkingName, ParkingAddress, ParkingHrs, ParkingCost, BookAgain;
        BookingDetailsEntities data;
    }

    ViewHolder holder = null;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            try {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.custom_booking_list, null);

                holder = new ViewHolder();
                holder.BookingDate = convertView.findViewById(R.id.book_date);
                holder.BookingTime = convertView.findViewById(R.id.book_time);
                holder.ParkingName = convertView.findViewById(R.id.parking_name);
                holder.ParkingAddress = convertView.findViewById(R.id.parking_address);
                holder.ParkingHrs = convertView.findViewById(R.id.parking_hrs);
                holder.ParkingCost = convertView.findViewById(R.id.parking_cost);
                holder.BookAgain = convertView.findViewById(R.id.book_again);

                final ViewHolder finalViewholder = holder;
//                holder.Cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
////                        OnCallBack.OnCancelClick(finalViewholder.data);
//                    }
//                });

            } catch (Exception e) {
                Toast.makeText(getContext(), "Some problem occured, please try again", Toast.LENGTH_SHORT).show();
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            holder.data = SubsList.get(position);
            SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = formatter6.parse(holder.data.getInTime());

            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            String strDate = dateFormat.format(date1);

            String[] strs = strDate.split(" ");
            String[] Datestrs = strs[0].split("-");
            String[] Timestrs = strs[1].split(":");

            holder.BookingDate.setText(Datestrs[1]+" "+Datestrs[0]);
            if (Integer.parseInt(Timestrs[0]) > 12) {
                int timeStr = Integer.parseInt(Timestrs[0])-12;
                if (timeStr > 9) {
                    holder.BookingTime.setText(String.valueOf(timeStr)+":"+Timestrs[1]+" pm");
                } else {
                    holder.BookingTime.setText("0"+String.valueOf(timeStr)+":"+Timestrs[1]+" pm");
                }
            } else {
                holder.BookingTime.setText(Timestrs[0]+":"+Timestrs[1]+" am");
            }
            holder.ParkingHrs.setText(holder.data.getDuration()+" hours");
            holder.ParkingCost.setText(String.valueOf(holder.data.getCost()) + "/-");

        } catch (Exception e) {
            Toast.makeText(getContext(), "Some problem occured, please try again", Toast.LENGTH_SHORT).show();
        }

        return convertView;
    }

}