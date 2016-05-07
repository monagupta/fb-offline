package com.example.mona.facebookoffline;

import android.app.Application;

import com.example.mona.facebookoffline.dagger.AppComponent;
import com.example.mona.facebookoffline.dagger.AppModule;
import com.example.mona.facebookoffline.dagger.DaggerAppComponent;
import com.facebook.FacebookSdk;
import com.orm.SugarApp;

/**
 * Created by mona on 4/29/16.
 */
public class FacebookOfflineApp extends SugarApp {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        // Initialize FB sdk
        FacebookSdk.sdkInitialize(this);
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
