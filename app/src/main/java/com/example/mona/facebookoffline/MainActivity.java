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

    Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = LoginFragment.newInstance();
                loginFragment.show(getFragmentManager(), "dialog");
            }
        });
    }
}

