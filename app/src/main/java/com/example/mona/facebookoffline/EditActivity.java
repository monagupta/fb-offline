package com.example.mona.facebookoffline;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import javax.inject.Inject;

public class EditActivity extends Activity {

    private static final String TAG = EditActivity.class.getSimpleName();

    @Inject FacebookApiService mFacebookApiService;

    private EditText mPostBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((FacebookOfflineApp) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_edit);

        mPostBody = (EditText) findViewById(R.id.post_text);
        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button postButton = (Button) findViewById(R.id.post_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked 'Create Post' button");
                String msg = mPostBody.getText().toString();
                mFacebookApiService.postMessageToPage(Constants.PAGE_ID, msg,
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                Log.d(TAG, "Response: " + response);
                            }
                        });
                finish();
            }
        });
    }
}
