package com.example.mona.facebookoffline;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mona.facebookoffline.auth.LoginFragment;
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

        Button createPostButton = (Button) findViewById(R.id.create_post);
        createPostButton.setOnClickListener(getCreatePostButtonListener());

        Button editPostButton = (Button) findViewById(R.id.edit_post);
        editPostButton.setOnClickListener(getEditPostButtonListener());
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        Log.d(TAG, "Successfully logged in: " + loginResult);
    }

    @Override
    public void onError(FacebookException error) {
        // Display a toast to the user
        Toast.makeText(this, R.string.unable_to_login, Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener getCreatePostButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EditActivity.class));
            }
        };
    }

    private View.OnClickListener getEditPostButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EditActivity.class));
            }
        };
    }
}
