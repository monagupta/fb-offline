package com.example.mona.facebookoffline;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mona.facebookoffline.models.Post;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import java.util.ArrayList;

import javax.inject.Inject;

public class EditActivity extends Activity {

    private static final String TAG = EditActivity.class.getSimpleName();

    @Inject FacebookApiService mFacebookApiService;

    private EditText mPostBody;
    private EditText mPostTitle;
    private Post mPost;

    // Key used for saving mPhotoUris across different instances
    private static final String PHOTO_URI_LIST = "uris";
    private ArrayList<Uri> mPhotoUris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((FacebookOfflineApp) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_edit);

        if (savedInstanceState != null) {
            mPhotoUris = (ArrayList) savedInstanceState.getSerializable(PHOTO_URI_LIST);
        }

        // Initialize mPhotoUris if not initialized through the savedInstanceState
        if (mPhotoUris == null) {
            mPhotoUris = new ArrayList<>();
        }

        Bundle b = getIntent().getExtras();
        if(b == null) {
            mPost = new Post();
        } else {
            mPost = Post.findById(Post.class, b.getInt("id"));
        }
        mPostBody = (EditText) findViewById(R.id.post_text);
        mPostTitle = (EditText) findViewById(R.id.post_title);
        mPostBody.setText(mPost.text);
        mPostTitle.setText(mPost.title);

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(getSaveButtonClickListener());

        Button postButton = (Button) findViewById(R.id.post_button);
        postButton.setOnClickListener(getPostButtonClickListener());

        Button addPhotosButton = (Button) findViewById(R.id.add_photos_button);
        addPhotosButton.setOnClickListener(getAddPhotoButtonClickListener());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "-------------Contents of mPhotoUris---------");
        for (Uri uri : mPhotoUris) {
            Log.d(TAG, "Photo uri:" + uri);
        }
        Log.d(TAG, "-------------END---------------");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Make sure to call the super method so that the states of our views are saved
        super.onSaveInstanceState(outState);

        outState.putSerializable(PHOTO_URI_LIST, mPhotoUris);
    }

    private View.OnClickListener getAddPhotoButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPhotoPicker();
            }
        };
    }

    private View.OnClickListener getPostButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked 'Create Post' button");
                savePost();
                String msg = mPostBody.getText().toString();
                GraphRequest.Callback cb = new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, "Response: " + response);
                    }
                };
                if (!mPhotoUris.isEmpty()) {
                    mFacebookApiService.publishPhotoToPage(Constants.PAGE_ID,
                            mPhotoUris.get(0), msg, cb);
                } else {
                    mFacebookApiService.postMessageToPage(Constants.PAGE_ID, msg, cb);
                }
                finish();
            }
        };
    }

    private View.OnClickListener getSaveButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePost();

                finish();
            }
        };
    }

    private void launchPhotoPicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.select_pictures)), ActivityRequestCode.SELECT_PHOTOS);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == ActivityRequestCode.SELECT_PHOTOS) {
                addUris(data);
            }
        }
    }

    private void addUris(Intent data) {
        if (Intent.ACTION_SEND_MULTIPLE.equals(data.getAction()) && data.hasExtra(Intent.EXTRA_STREAM)) {
            // retrieve a collection of selected images
            ArrayList<Parcelable> list = data.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
            // iterate over these images
            if (list != null) {
                for (Parcelable parcel : list) {
                    Uri uri = (Uri) parcel;
                    // TODO(mona): Is this code path ever triggered? Test Wes's phone
                    Log.d(TAG, "Selected uri: " + uri);
                    mPhotoUris.add(uri);
                }
            }
        } else {
            Uri selectedImageUri = data.getData();

            if (selectedImageUri != null) {
                // For Mona's Moto X 5.1, this code path is triggered when choosing
                // a single photo through the Gallery app. Choosing multiple photos
                // through the Gallery app is unsupported by Gallery
                Log.d(TAG, "Selected single uri: " + selectedImageUri);
                mPhotoUris.add(selectedImageUri);
            } else {
                // Attempt to retrieve Uri using clip data. For Mona's Moto X 5.1, this
                // code path is triggered when choosing a single or multiple images through
                // the Photos app
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri clipDataUri = clipData.getItemAt(i).getUri();
                    if (clipDataUri != null) mPhotoUris.add(clipDataUri);
                }
            }

        }
    }

    private void savePost() {
        mPost.text = mPostBody.getText().toString();
        mPost.title = mPostTitle.getText().toString();
        mPost.save();
    }
}
