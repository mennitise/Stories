package com.fiuba.stories.stories;

import android.app.Application;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.ProfileManager;
import com.fiuba.stories.stories.utils.AppServerRequest;
import com.fiuba.stories.stories.utils.HttpCallback;
import com.fiuba.stories.stories.utils.ResponseObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Splash extends AppCompatActivity {
    private final int DURATION_SPLASH = 3000;

    StoriesApp app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        AppServerRequest.upServer(new CallbackRequest());

        this.app = (StoriesApp) getApplicationContext();

        // If the User are already logged with Facebook
        if(AccessToken.getCurrentAccessToken() != null) {
            Profile currentProfile = Profile.getCurrentProfile();
            this.app.userLoggedIn = new User();
            Log.d("CurrentProfile", currentProfile.toString());
            Log.d("First name",currentProfile.getFirstName());
            Log.d("Last name",currentProfile.getLastName());
            Log.d("id",currentProfile.getId());
            Log.d("fb token",AccessToken.getCurrentAccessToken().getToken());
            this.app.userLoggedIn.setFirstName(currentProfile.getFirstName());
            this.app.userLoggedIn.setLastName(currentProfile.getLastName());
            this.app.userLoggedIn.setEmail(currentProfile.getId());
            this.app.userLoggedIn.setCurrentToken(AccessToken.getCurrentAccessToken().getToken(), true);
        }

        new Handler().postDelayed(new Runnable(){
            Boolean currentUser = true;
            public void run(){
                    startActivity(new Intent(Splash.this, MainActivity.class));
                    finish();
            };
        }, DURATION_SPLASH);
    }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    class CallbackRequest extends HttpCallback {
        @Override
        public void onResponse() {
        }
    }

    // ---------------------------------------------------------------------------------------------
}