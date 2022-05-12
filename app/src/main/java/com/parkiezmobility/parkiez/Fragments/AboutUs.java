package com.parkiezmobility.parkiez.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parkiezmobility.parkiez.Entities.AddressEntities;
import com.parkiezmobility.parkiez.R;

import java.util.List;

public class AboutUs extends Fragment {
    private ImageView callBtn, emailBtn, linkedinBtn, instagramBtn;

    public AboutUs() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about_us, container, false);

        DeclareVariables(v);

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+91 7058012555"));
                startActivity(intent);
            }
        });

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "connect@parkiez.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Contact to Parkiez");
                intent.putExtra(Intent.EXTRA_TEXT, "Type here your query");
                startActivity(Intent.createChooser(intent, ""));
            }
        });

        linkedinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String linkedId = "https://in.linkedin.com/company/parkiez";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://add/%@" + linkedId));
                final PackageManager packageManager = getContext().getPackageManager();
                final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (list.isEmpty()) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("" + linkedId));
                }
                startActivity(intent);
            }
        });

        instagramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String instagram = "https://www.instagram.com/parkiez_mobility/";
                Uri uri = Uri.parse("" +instagram);
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/" +instagram)));
                }
            }
        });

        return v;
    }

    public void DeclareVariables(View v) {
        getActivity().setTitle("About Us");

        callBtn = v.findViewById(R.id.call);
        emailBtn = v.findViewById(R.id.email);
        linkedinBtn = v.findViewById(R.id.linkedin);
        instagramBtn = v.findViewById(R.id.instagram);
    }

}
