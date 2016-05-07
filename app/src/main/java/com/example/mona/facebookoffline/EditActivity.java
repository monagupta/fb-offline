package com.example.mona.facebookoffline;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mona.facebookoffline.models.Post;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import javax.inject.Inject;

public class EditActivity extends Activity {

    private static final String TAG = EditActivity.class.getSimpleName();

    @Inject FacebookApiService mFacebookApiService;

    private EditText mPostBody;
    private EditText mPostTitle;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((FacebookOfflineApp) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_edit);

        Bundle b = getIntent().getExtras();
        if(b == null) {
            post = new Post();
        } else {
            post = Post.findById(Post.class, b.getInt("id"));
        }
        mPostBody = (EditText) findViewById(R.id.post_text);
        mPostTitle = (EditText) findViewById(R.id.post_title);
        mPostBody.setText(post.text);
        mPostTitle.setText(post.title);

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePost();

                finish();
            }
        });

        Button postButton = (Button) findViewById(R.id.post_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked 'Create Post' button");
                savePost();
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

    private void savePost() {
        post.text = mPostBody.getText().toString();
        post.title = mPostTitle.getText().toString();
        post.save();
    }
}
