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

        AppServerRequest.sendTestRequest("a=1&b=2", new CallbackRequest());

        this.app = (StoriesApp) getApplicationContext();

        // If the User are already logged with Facebook
        if(AccessToken.getCurrentAccessToken() != null) {
            Profile currentProfile = Profile.getCurrentProfile();
            this.app.userLoggedIn = new User(currentProfile.getFirstName(), currentProfile.getLastName(),"", currentProfile.getProfilePictureUri(200,200).toString());
        }

        new Handler().postDelayed(new Runnable(){
            Boolean currentUser = true;
            public void run(){
                    startActivity(new Intent(Splash.this, MainActivity.class));
                    finish();
            };
        }, DURATION_SPLASH);
    }

    class CallbackRequest extends HttpCallback {

        ResponseObject resp;

        @Override
        public void onResponse() {
            try {
                JsonObject objJson = new JsonParser().parse(getJSONObject("args").toString()).getAsJsonObject();
                Log.e("RESPONSE: ", getJSONObject("args").toString());
                resp = ResponseObject.hydrate(objJson);
                Splash.this.runOnUiThread(new SetResults());
            } catch (Exception e) {
                Log.e("TEST REQUEST CALLBACK", "Error");
                e.printStackTrace();
            }
            Splash.this.runOnUiThread(new SetResults());
        }

        class SetResults implements Runnable {
            @Override
            public void run() {
                String text = String.format("Value A = %s\nValue B = %s",resp.getValueA(), resp.getValueB());
                Log.e("Response", text);

                //Login.this.response.setText(text);
            }
        }
    }

}