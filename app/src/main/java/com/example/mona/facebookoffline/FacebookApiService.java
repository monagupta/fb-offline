package com.example.mona.facebookoffline;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.example.mona.facebookoffline.util.BitmapUtil;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Singleton class that handles requests to Facebook's Graph Api
 *
 * Created by mona on 4/29/16.
 */
public class FacebookApiService {
    private static final String TAG = FacebookApiService.class.getSimpleName();

    /** Constants used as parameters/fields for the Fb Graph Api */
    private static final String FIELDS = "fields";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String MESSAGE = "message";

    private Context mContext;

    public FacebookApiService(Context context) {
        mContext = context;
    }

    public void postMessageToPage(final String pageId, final String message,
                                  final GraphRequest.Callback cb) {
        executeAsPage(
                new ApiRequest() {
                    @Override
                    public void execute(AccessToken pageToken) {
                        Bundle params = new Bundle();
                        params.putString(MESSAGE, message);

                        new GraphRequest(
                                pageToken,
                                "/" + pageId + "/feed",
                                params,
                                HttpMethod.POST,
                                cb
                        ).executeAsync();
                    }
                }, pageId, cb);
    }

    public void publishPhotoToPage(final String pageId, final String url, final String message,
                                   final GraphRequest.Callback cb) {
        executeAsPage(new ApiRequest() {
            @Override
            public void execute(AccessToken pageToken) {
                Bundle params = new Bundle();
                params.putString("url", url);
                params.putString(MESSAGE, message);
                /* make the API call */
                new GraphRequest(
                        pageToken,
                        "/" + pageId + "/photos",
                        params,
                        HttpMethod.POST,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                cb.onCompleted(response);
                            }
                        }
                ).executeAsync();
            }
        }, pageId, cb);
    }

    public void publishPhotoToPage(final String pageId, final Uri uri, final String message,
                                   final GraphRequest.Callback cb) {
        executeAsPage(new ApiRequest() {
            @Override
            public void execute(AccessToken pageToken) {

                // Get data from uri. TODO(mona): Should be done in background
                byte[] bytes;
                try {
                    bytes = BitmapUtil.getBytesFromUri(mContext, uri);
                } catch (IOException e) {
                    Log.e(TAG, "Unable to get bitmap from uri", e);
                    notifyError(cb, null); // TODO(mona): Pass some info through
                    return;
                }

                Bundle params = new Bundle();
                params.putString(ACCESS_TOKEN, pageToken.getToken());
                params.putString(MESSAGE, message);
                params.putByteArray("picture", bytes);

                /* make the API call */
                new GraphRequest(
                        pageToken,
                        "/" + pageId + "/photos",
                        params,
                        HttpMethod.POST,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                cb.onCompleted(response);
                            }
                        }
                ).executeAsync();
            }
        }, pageId, cb);
    }

    private void notifyError(GraphRequest.Callback cb, GraphResponse response) {
        cb.onCompleted(response);
    }

    private void executeAsPage(final ApiRequest request, final String pageId,
                               final GraphRequest.Callback cb) {
        Bundle params = new Bundle();
        params.putString(FIELDS, ACCESS_TOKEN);

        // TODO(mona): Currently we're requesting a page token every time, which is probably not necessary
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + pageId,
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {

                    @Override
                    public void onCompleted(GraphResponse response) {

                        if (response.getError() != null) {
                            Log.w(TAG, "Error fetching page access token", response.getError().getException());
                            notifyError(cb, response);
                        } else {
                            try {
                                String pageToken = response.getJSONObject().getString(ACCESS_TOKEN);
                                Log.d(TAG, "pageToken=" + pageToken);
                                // Convert String representation to an AccessToken instance
                                AccessToken token = createAccessToken(pageToken,
                                        AccessToken.getCurrentAccessToken());
                                request.execute(token);
                            } catch (JSONException e) {
                                Log.w(TAG, "Unable to properly parse response: " + response, e);
                                notifyError(cb, response);
                            }
                        }

                    }
                }
        ).executeAsync();
    }

    private AccessToken createAccessToken(String newTokenString, AccessToken modelToken) {
        // TODO(mona): OMG, this is super hacky. I hate it. Make it stop.
        return new AccessToken(newTokenString, modelToken.getApplicationId(),
                modelToken.getUserId(), modelToken.getPermissions(),
                modelToken.getDeclinedPermissions(), modelToken.getSource(),
                modelToken.getExpires(), modelToken.getLastRefresh());
    }

    private interface ApiRequest {
        void execute(AccessToken pageToken);
    }
}
