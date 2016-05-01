package com.example.mona.facebookoffline;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;

/**
 * Created by mona on 4/29/16.
 */
public class FacebookApiService {
    private static final String TAG = FacebookApiService.class.getSimpleName();

    /** Constants used as parameters/fields for the Fb Graph Api */
    private static final String FIELDS = "fields";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String MESSAGE = "message";

    public void postMessageToPage(final String pageId, final String message,
                                  final GraphRequest.Callback cb) {
        Bundle params = new Bundle();
        params.putString(FIELDS, ACCESS_TOKEN);

        // TODO(mona): Currently we're requesting a page token every time, which is probably not necessary
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
                            String pageToken = response.getJSONObject().getString(ACCESS_TOKEN);
                            Log.d(TAG, "pageToken=" + pageToken);

                            executeRequestToPost(pageId, pageToken, message, cb);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

        ).executeAsync();
    }

    private void executeRequestToPost(String pageId, String pageAccessToken, String message,
                                  GraphRequest.Callback cb) {

        Bundle params = new Bundle();
        params.putString(MESSAGE, message);

        AccessToken pageToken = createAccessToken(pageAccessToken, AccessToken.getCurrentAccessToken());

        new GraphRequest(
                pageToken,
                "/" + pageId + "/feed",
                params,
                HttpMethod.POST,
                cb
        ).executeAsync();
    }

    private AccessToken createAccessToken(String newTokenString, AccessToken modelToken) {
        // TODO(mona): OMG, this is super hacky. I hate it. Make it stop.
        return new AccessToken(newTokenString, modelToken.getApplicationId(),
                modelToken.getUserId(), modelToken.getPermissions(),
                modelToken.getDeclinedPermissions(), modelToken.getSource(),
                modelToken.getExpires(), modelToken.getLastRefresh());
    }
}
