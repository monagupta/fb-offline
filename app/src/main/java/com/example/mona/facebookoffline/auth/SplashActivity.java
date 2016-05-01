package com.example.mona.facebookoffline.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mona.facebookoffline.MainActivity;
import com.example.mona.facebookoffline.R;
import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

/**
 * Splash screen for the app, which allows a user to login. This screen is bypassed if the
 * user is already logged into their fb account through this app.
 *
 * Created by mona on 4/29/16.
 */
public class SplashActivity extends Activity implements LoginFragment.LoginListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isLoggedIn()) {
            launchMainActivityAndFinish();
        } else {
            setContentView(R.layout.activity_splash);
        }
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        launchMainActivityAndFinish();
    }

    @Override
    public void onError(FacebookException error) {
        // Display a toast to the user
        Toast.makeText(this, R.string.unable_to_login, Toast.LENGTH_SHORT).show();
    }

    private boolean isLoggedIn() {
        // TODO(mona): Is a non-null token enough to say it's valid? Unsure from the docs
        return AccessToken.getCurrentAccessToken() != null;
    }

    private void launchMainActivityAndFinish() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
