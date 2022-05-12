package com.parkiezmobility.parkiez.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.parkiezmobility.parkiez.R;
import com.parkiezmobility.parkiez.managers.ApplicationManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MediaPlayer mediaPlayer=MediaPlayer.create(this,R.raw.car_sound);
        ApplicationManager.getInstance().Init(getApplicationContext());
        mediaPlayer.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
            //   Intent i = new Intent(SplashActivity.this, Third_party.class);
               Intent i = new Intent(SplashActivity.this, Login.class);

                startActivity(i);
                finish();
            }
        }, 2000);
    }
}
