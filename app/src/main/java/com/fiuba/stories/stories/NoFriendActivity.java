package com.fiuba.stories.stories;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.stories.stories.utils.AppServerRequest;
import com.fiuba.stories.stories.utils.HttpCallback;
import com.fiuba.stories.stories.utils.MyFirebaseCloudMessagingConfigure;
import com.fiuba.stories.stories.utils.UtilCallbacks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NoFriendActivity extends AppCompatActivity {

    public static final String FIRST_NAME = "FIRSTNAME";
    public static final String LAST_NAME = "LASTNAME";
    public static final String EMAIL = "EMAIL";
    public static final String BIRTHDAY = "BIRTHDAY";
    public static final String GENDER = "GENDER";
    public static final String AGE = "AGE";

    StoriesApp app;
    User user;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_friend);

        this.app = (StoriesApp) getApplicationContext();
        Intent intent = getIntent();
        this.user = new User();
        this.user.setFirstName(intent.getStringExtra(FIRST_NAME));
        this.user.setLastName(intent.getStringExtra(LAST_NAME));
        this.user.setEmail(intent.getStringExtra(EMAIL));
        this.user.setBirthday(intent.getStringExtra(BIRTHDAY));
        this.user.setGender(intent.getStringExtra(GENDER));
        this.user.setAge(intent.getStringExtra(AGE));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(this.user.getName());
        setSupportActionBar(toolbar);

        TextView profileName = findViewById(R.id.info_profile_name_view);
        profileName.setText(this.user.getName());
        TextView profileEmail = findViewById(R.id.info_profile_email_view);
        profileEmail.setText(this.user.email);
        TextView profileBirthday = findViewById(R.id.info_profile_birthday_view);
        profileBirthday.setText(this.user.birthday);
        TextView profileGender = findViewById(R.id.info_profile_gender_view);
        profileGender.setText(this.user.gender);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add to my Friends", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                UtilCallbacks util = new UtilCallbacks();
                AppServerRequest.postFriendInvitation(app.userLoggedIn.getEmail(), app.userLoggedIn.token, user.getEmail(),
                                    new CallbackRequestPostInvitation());
            }
        });
    }

    public void sendedInvitation(){
        (new SnackRunnable("Invitation sended...")).run();
        this.fab.setVisibility(View.INVISIBLE);
    }

    public class SnackRunnable implements Runnable {
        String message;

        public SnackRunnable(String message){
            this.message = message;
        }

        @Override
        public void run() {
            Snackbar.make(fab, this.message, Snackbar.LENGTH_LONG).show();
        }
    }

    public class CallbackRequestPostInvitation extends HttpCallback {
        @Override
        public void onResponse() {
            try{
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                if (getHTTPResponse().code() == 200) {

                    MyFirebaseCloudMessagingConfigure.sendFriendInvitation(app.userLoggedIn.getName(), user.getEmail());

                    NoFriendActivity.this.runOnUiThread(new NoFriendActivity.CallbackRequestPostInvitation.SetResults());
                /*
                } else if (getHTTPResponse().code() == 401){
                    Toast.makeText(getBaseContext(), "ERROR 401", Toast.LENGTH_LONG).show();
                } else if (getHTTPResponse().code() == 400){
                    Toast.makeText(getBaseContext(), "ERROR 400", Toast.LENGTH_LONG).show();
                */
                }
            } catch (Exception e) {
                Log.e("TEST REQUEST CALLBACK", "Error");
                e.printStackTrace();
            }
        }

        class SetResults implements Runnable{
            @Override
            public void run(){
                sendedInvitation();
            }
        }
    }
}
