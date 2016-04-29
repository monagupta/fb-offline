package com.example.mona.facebookoffline;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mona.facebookoffline.auth.LoginFragment;

/**
 * Main activity, allows a user to login, create new posts, and access old posts
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
