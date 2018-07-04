package com.fiuba.stories.stories;

import android.app.Application;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class StoriesApp extends Application{

    public User userLoggedIn;
    public String passFirebase;
    public String FCMtoken;

    @Override
    public void onCreate(){
        super.onCreate();
        this.passFirebase = "123456";
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
