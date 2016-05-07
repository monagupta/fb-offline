package com.example.mona.facebookoffline.dagger;

/**
 * Created by mona on 4/30/16.
 */

import com.example.mona.facebookoffline.EditActivity;
import com.example.mona.facebookoffline.FacebookApiService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={AppModule.class})
public interface AppComponent {
    void inject(EditActivity activity);
    void inject(FacebookApiService service);
}
