package com.parkiezmobility.parkiez.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parkiezmobility.parkiez.Activities.Login;
import com.parkiezmobility.parkiez.Database.DataHelp;
import com.parkiezmobility.parkiez.Database.MyOpenHelper;
import com.parkiezmobility.parkiez.Entities.MyResponce;
import com.parkiezmobility.parkiez.Entities.UserEntity;
import com.parkiezmobility.parkiez.Interfaces.ApiInterface;
import com.parkiezmobility.parkiez.MainActivity;
import com.parkiezmobility.parkiez.R;
import com.parkiezmobility.parkiez.Services.MasterService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends Fragment {
    private EditText name, address, email, mobileno;
    private String name_str, address_str, email_str, mobileno_str;
    private Button btn_update;
    private MyOpenHelper db;
    private DataHelp dh;
    private UserEntity user;
    private MasterService task;

    public Profile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        DeclareVariables(v);
        name.setText(user.getName());
        address.setText(user.getAddress());
        email.setText(user.getEmail());
        email.setEnabled(false);
        mobileno.setText(user.getMobileNo());

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate()) {
                    UserEntity userData = new UserEntity();
                    userData.setUserID(user.getUserID());
                    userData.setName(name_str);
                    userData.setEmail(email_str);
                    userData.setMobileNo(mobileno_str);
                    userData.setAddress(address_str);
                    getData(userData);
                }
            }
        });

        return v;
    }

    public void DeclareVariables(View v) {
        getActivity().setTitle("Profile");

        name = (EditText) v.findViewById(R.id.name);
        address = (EditText) v.findViewById(R.id.addr);
        email = (EditText) v.findViewById(R.id.email);
        mobileno = (EditText) v.findViewById(R.id.mobileno);
        btn_update = (Button) v.findViewById(R.id.update_profile);

        dh = new DataHelp(getActivity());
        db = new MyOpenHelper(getActivity());
        user = db.getUser();
    }

    public boolean isValidate() {
        boolean valid = true;
        name_str = name.getText().toString();
        email_str = email.getText().toString();
        address_str = address.getText().toString();
        mobileno_str = mobileno.getText().toString();

        if (name_str.isEmpty()) {
            requestFocus(name);
            name.setError("Please enter a name!");
            valid = false;
        } else if (address_str.isEmpty()){
            requestFocus(address);
            address.setError("Please enter a address!");
            valid = false;
        } else if (email_str.isEmpty()) {
            requestFocus(email);
            email.setError("Please enter an email address!");
            valid = false;
//        } else if (!Login.validateEmail(email_str)) {
//            requestFocus(email);
//            email.setError("Please enter a valid email address!");
//            valid = false;
//        } else if (mobileno_str.isEmpty()) {
            mobileno.setError("Please enter a mobile number!");
            valid = false;
        } else if (mobileno_str.length() < 10) {
            mobileno.setError("Please enter 10 digit mobile number!");
            valid = false;
        } else {
            return valid;
        }
        return valid;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void getData(UserEntity userData) {
        try {
            if (MainActivity.isNetworkAvaliable(getActivity())) {
                ApiInterface apiService = MasterService.getCaller().create(ApiInterface.class);
                Call<MyResponce> call = apiService.saveUser(userData);
                task = new MasterService();
                task.showProgress(getActivity());
                call.enqueue(new Callback<MyResponce>() {
                    @Override
                    public void onResponse(Call<MyResponce> call, Response<MyResponce> response) {
                        task.dismissProgress(getActivity());
                        MyResponce responceObj = response.body();
                        if (responceObj != null) {
                            if (responceObj.getResult().equals("Email Found")) {
                                requestFocus(email);
                                email.setError("Mail ID already exist!");
                            } else {
                                Gson gson = new Gson();
                                UserEntity user = gson.fromJson(responceObj.getResult(), UserEntity.class);
                                if (dh.putUser(user)) {
                                    Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(Registration.this, MainActivity.class);
//                                startActivity(intent);
                                } else {
                                    Toast.makeText(getActivity(), "local database error", Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "Some problem occure", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponce> call, Throwable t) {
                        task.dismissProgress(getActivity());
                        Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "some problem occure", Toast.LENGTH_LONG).show();
        }
    }

}
