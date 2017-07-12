package com.socialnetworkintegration;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.socialnetworkintegration.javabasic.google.GooglePlusDash;
import com.socialnetworkintegration.javabasic.twitter.TwitterDash;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent=new Intent(SplashActivity.this, GooglePlusDash.class);
//                startActivity(intent);
                Intent twitterIntent=new Intent(SplashActivity.this, TwitterDash.class);
                startActivity(twitterIntent);
            }
        },5000);
    }
}
