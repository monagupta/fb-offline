package com.example.mona.facebookoffline.dagger;

import android.app.Application;

import com.example.mona.facebookoffline.FacebookApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *
 * Module that provides Dagger with the Application
 *
 * Created by mona on 4/29/16.
 */
@Module
public class AppModule {

    Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    FacebookApiService provideFacebookApiService() {
        return new FacebookApiService();
    }

}
