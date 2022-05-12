package com.parkiezmobility.parkiez.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parkiezmobility.parkiez.Entities.AddressEntities;
import com.parkiezmobility.parkiez.R;

public class ParkingInfo extends Fragment {
    private TextView ParkingName, ParkingDetails, ReviewCount, ParkingTime, CarCount, CarPrice, BikeCount, BikePrice, ThreeHrsCarPrice;
    private Button bookParking;
    private AddressEntities Parking = null;
    private LinearLayout First, Second, Third;
    private BottomNavigationView navigation;

    public ParkingInfo() {
    }

    public ParkingInfo(AddressEntities Parking) {
        this.Parking = Parking;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_parking_info, container, false);

        DeclareVariables(v);

        if (Parking != null) {
            ParkingName.setText(Parking.getParkingName());
            CarCount.setText(Parking.getParking().getCar_ParkAvailable());
            CarPrice.setText(Parking.getParking().getCarPrice());
            BikeCount.setText(Parking.getParking().getBike_ParkAvailable());
            BikePrice.setText(Parking.getParking().getBikePrice());
            ThreeHrsCarPrice.setText("Total Price \n Rs. " +String.valueOf(3*Double.valueOf(Parking.getParking().getCarPrice())));
        } else {

        }



        bookParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(Parking.getParking().getCar_ParkAvailable()) == 0 &&
                        Integer.parseInt(Parking.getParking().getCar_ParkAvailable()) == 0) {
                    Toast.makeText(getActivity(), "This parking is full", Toast.LENGTH_SHORT).show();
                } else {
                    Fragment fragment = new BookParking(Parking);
                    getFragmentManager().beginTransaction()
                            .replace(((ViewGroup) getView().getParent()).getId(), fragment)
                            .addToBackStack(null)
                            .commit();
                }

            }
        });

        
        return v;
    }

    public void DeclareVariables(View v) {
        getActivity().setTitle("Parking Info");

        ParkingName = (TextView) v.findViewById(R.id.parking_name);
       // ParkingDetails = (TextView) v.findViewById(R.id.parking_detail);
        ReviewCount = (TextView) v.findViewById(R.id.review_count);
        ParkingTime = (TextView) v.findViewById(R.id.parking_time);
    //    CarCount = (TextView) v.findViewById(R.id.car_count);
     //   CarPrice = (TextView) v.findViewById(R.id.carPrice);
     //   BikeCount = (TextView) v.findViewById(R.id.bike_count);
      //  BikePrice = (TextView) v.findViewById(R.id.bikePrice);
       // ThreeHrsCarPrice = (TextView) v.findViewById(R.id.three_hrs_car_price);

        bookParking = (Button) v.findViewById(R.id.bookParking);

       // navigation = (BottomNavigationView) v.findViewById(R.id.navigation);
      //  navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        First = (LinearLayout) v.findViewById(R.id.first);
        Second = (LinearLayout) v.findViewById(R.id.second);
       // Third = (LinearLayout) v.findViewById(R.id.third);
    }

 /*   private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_details:
                    First.setVisibility(View.VISIBLE);
                    Second.setVisibility(View.GONE);
                    Third.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_images:
                    First.setVisibility(View.GONE);
                    Second.setVisibility(View.VISIBLE);
                    Third.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_review:
                    Toast.makeText(getActivity(), "In Process...", Toast.LENGTH_SHORT).show();
//                    First.setVisibility(View.GONE);
//                    Second.setVisibility(View.GONE);
//                    Third.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };   */

}
