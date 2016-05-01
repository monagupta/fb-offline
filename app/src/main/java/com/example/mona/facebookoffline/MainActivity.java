package com.example.mona.facebookoffline;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mona.facebookoffline.auth.LoginFragment;
import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;

/**
 * Main activity, allows a user to login, create new posts, and access old posts
 */
public class MainActivity extends Activity implements LoginFragment.LoginListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize FB sdk
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
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

