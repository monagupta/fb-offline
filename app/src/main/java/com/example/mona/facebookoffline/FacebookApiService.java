package com.example.mona.facebookoffline;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.mona.facebookoffline.util.FileUtil;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;

import java.io.IOException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.subjects.ReplaySubject;

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

    public Observable<GraphResponse> postMessageToPage(final String pageId, final String message) {
        Bundle params = new Bundle();
        params.putString(MESSAGE, message);
        String path = "/" + pageId + "/feed";
        return executeAsPage(pageId, path, params, HttpMethod.POST);
    }

    public Observable<GraphResponse> publishPhotoToPage(final String pageId, final String url, final String message) {
        Bundle params = new Bundle();
        params.putString("url", url);
        params.putString(MESSAGE, message);
        String path = "/" + pageId + "/photos";
        return executeAsPage(pageId, path, params, HttpMethod.POST);
    }

    public Observable<GraphResponse> publishPhotoToPage(final String pageId, final Uri uri, final String message) {
        // Get data from uri. TODO(mona): Should be done in background
        byte[] bytes;
        try {
            bytes = FileUtil.getBytesFromUri(mContext, uri);
        } catch (IOException e) {
            Log.e(TAG, "Unable to get bitmap from uri", e);
            return Observable.error(e);
        }

        Bundle params = new Bundle();
        params.putString(MESSAGE, message);
        params.putByteArray("picture", bytes);

        return executeAsPage(pageId, "/" + pageId + "/photos", params, HttpMethod.POST);
    }

    private Observable<GraphResponse> executeAsPage(final String pageId,
                                                    final String graphPath,
                                                    final Bundle parameters,
                                                    final HttpMethod httpMethod) {
        return getPageAccessToken(pageId)
                .observeOn(AndroidSchedulers.mainThread()) // TODO(mona) Check all threading everywhere
                .flatMap(new Func1<AccessToken, Observable<GraphResponse>>() {
                    @Override
                    public Observable<GraphResponse> call(AccessToken accessToken) {
                        return execute(accessToken, graphPath, parameters, httpMethod);
                    }
                });
    }

    private Observable<AccessToken> getPageAccessToken(final String pageId) {
        Bundle params = new Bundle();
        params.putString(FIELDS, ACCESS_TOKEN);

        // TODO(mona): Currently we're requesting a page token every time, which is probably not necessary
        return execute(AccessToken.getCurrentAccessToken(), "/" + pageId, params, HttpMethod.GET)
                .map(new Func1<GraphResponse, AccessToken>() {
                    @Override
                    public AccessToken call(GraphResponse graphResponse) {
                        try {
                            String pageToken = graphResponse.getJSONObject().getString(ACCESS_TOKEN);
                            Log.d(TAG, "pageToken=" + pageToken);
                            // Convert String representation to an AccessToken instance
                            return createAccessToken(pageToken,
                                    AccessToken.getCurrentAccessToken());
                        } catch (JSONException e) {
                            Log.w(TAG, "Unable to properly parse response: " + graphResponse, e);
                            return null;
                        }
                    }
                });
    }

    private AccessToken createAccessToken(String newTokenString, AccessToken modelToken) {
        // TODO(mona): OMG, this is super hacky. I hate it. Make it stop.
        return new AccessToken(newTokenString, modelToken.getApplicationId(),
                modelToken.getUserId(), modelToken.getPermissions(),
                modelToken.getDeclinedPermissions(), modelToken.getSource(),
                modelToken.getExpires(), modelToken.getLastRefresh());
    }

    private Observable<GraphResponse> execute(AccessToken accessToken,
                                      String graphPath,
                                      Bundle parameters,
                                      HttpMethod httpMethod) {
        final ReplaySubject<GraphResponse> obs = ReplaySubject.createWithSize(1);
        new GraphRequest(
                accessToken,
                graphPath,
                parameters,
                httpMethod,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response.getError() != null) {
                            obs.onError(response.getError().getException());
                        } else {
                            obs.onNext(response);
                            obs.onCompleted();
                        }
                    }
                }
        ).executeAsync();

        return obs;
    }
}
