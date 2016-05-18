package com.example.mona.facebookoffline.dagger;

import android.app.Application;
import android.content.Context;

import com.example.mona.facebookoffline.FacebookApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module that provides Dagger with the Application
 *
 * Created by mona on 4/29/16.
 */
@Module
public class AppModule {

    Application mApplication;
    Context mContext;

    public AppModule(Application application) {
        mApplication = application;
        mContext = application.getApplicationContext();
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    Context providesContext() {
        return mContext;
    }

    @Provides
    @Singleton
    FacebookApiService provideFacebookApiService(Context context) {
        return new FacebookApiService(context);
    }

}
