package com.example.mona.facebookoffline;

import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import javax.inject.Inject;

/**
 * Main activity, allows a user to login, create new posts, and access old posts
 */
public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject FacebookApiService mFacebookApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((FacebookOfflineApp) getApplication()).getAppComponent().inject(this);

        setContentView(R.layout.activity_main);

        Button createPostButton = (Button) findViewById(R.id.create_post);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked 'Create Post' button");
                mFacebookApiService.postMessageToPage(Constants.PAGE_ID, "More testing!",
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                Log.d(TAG, "Response: " + response);
                            }
                        });

            }
        });
    }
}

