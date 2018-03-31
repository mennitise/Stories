package com.fiuba.stories.stories;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.pm.ActivityInfo;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends AppCompatActivity {
    private final int DURATION_SPLASH = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            Boolean currentUser = true;
            public void run(){
                    startActivity(new Intent(Splash.this, MainActivity.class));
                    finish();
            };
        }, DURATION_SPLASH);
    }
}