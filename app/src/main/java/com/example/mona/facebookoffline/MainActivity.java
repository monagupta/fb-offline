package com.example.mona.facebookoffline;

import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

/**
 * Main activity, allows a user to login, create new posts, and access old posts
 */
public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject FacebookApiService mFacebookApiService;

    private Button mCreatePostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((FacebookOfflineApp) getApplication()).getAppComponent().inject(this);

        setContentView(R.layout.activity_main);

        mCreatePostButton = (Button) findViewById(R.id.create_post);
        mCreatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked 'Create Post' button");

                // TODO(mona): Remove hardcode or pull into constant
                Bundle params = new Bundle();
                params.putString("fields", "access_token");

                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/" + Constants.PAGE_ID,
                        params,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {

                            @Override
                            public void onCompleted(GraphResponse response) {
                                Log.d(TAG, "Response: " + response);
                                try {
                                    String pageToken = response.getJSONObject().getString("access_token");
                                    Log.d(TAG, "pageToken=" + pageToken);

                                    fooPost(pageToken);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                ).executeAsync();
            }
        });
    }

    private void fooPost(String pageAccessToken) {

        Bundle params = new Bundle();
        params.putString("message", "Testing...");

        // TODO(mona): OMG, this is super hacky. I hate it. Make it stop.
        AccessToken userToken = AccessToken.getCurrentAccessToken();
        AccessToken pageToken = new AccessToken(pageAccessToken, userToken.getApplicationId(),
                userToken.getUserId(), userToken.getPermissions(),
                userToken.getDeclinedPermissions(), userToken.getSource(),
                userToken.getExpires(), userToken.getLastRefresh());

        new GraphRequest(
                pageToken,
                "/" + Constants.PAGE_ID + "/feed",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, "Response from posting to feed: " + response);
                    }
                }
        ).executeAsync();
    }
}

