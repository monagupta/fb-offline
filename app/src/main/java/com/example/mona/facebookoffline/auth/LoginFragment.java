package com.example.mona.facebookoffline.auth;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mona.facebookoffline.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * A fragment that handles login to Facebook
 */
public class LoginFragment extends DialogFragment {

    private static final String TAG = LoginFragment.class.getSimpleName();

    private LoginButton mLoginButton;
    private CallbackManager mCallbackManager;
    private LoginListener mLoginListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        mLoginButton = (LoginButton) view.findViewById(R.id.login_button);
        mLoginButton.setPublishPermissions("manage_pages", "publish_pages");
        mLoginButton.setFragment(this);

        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "Successful login: " + loginResult);
                if (mLoginListener != null) mLoginListener.onSuccess(loginResult);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Cancelled login");
            }

            @Override
            public void onError(FacebookException error) {
                Log.w(TAG, "Error on login", error);
                if (mLoginListener != null) mLoginListener.onError(error);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mLoginListener = (LoginListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement LoginListener");
        }

        // Initialize FB sdk
        FacebookSdk.sdkInitialize(getActivity());
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLoginListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public interface LoginListener {
        public void onSuccess(LoginResult loginResult);
        public void onError(FacebookException error);
    }

}
