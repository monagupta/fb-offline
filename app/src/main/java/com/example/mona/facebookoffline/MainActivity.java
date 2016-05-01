package com.example.mona.facebookoffline;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mona.facebookoffline.auth.LoginFragment;
import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

/**
 * Main activity, allows a user to login, create new posts, and access old posts
 */
public class MainActivity extends Activity implements LoginFragment.LoginListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button create_button = (Button) findViewById(R.id.create_post);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EditActivity.class));
            }
        });
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
