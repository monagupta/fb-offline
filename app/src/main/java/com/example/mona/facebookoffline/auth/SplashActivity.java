package com.example.mona.facebookoffline.auth;

import android.app.Activity;
import android.os.Bundle;

import com.example.mona.facebookoffline.R;

/**
 * Splash screen for the app, which allows a user to login. This screen is bypassed if the
 * user is already logged into their fb account through this app.
 *
 * Created by mona on 4/29/16.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
}
