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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.fiuba.stories.stories.utils.AppServerRequest;
import com.fiuba.stories.stories.utils.HttpCallback;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AlienProfileActivity extends AppCompatActivity {

    public static final String FIRST_NAME = "FIRSTNAME";
    public static final String LAST_NAME = "LASTNAME";
    public static final String EMAIL = "EMAIL";
    public static final String BIRTHDAY = "BIRTHDAY";
    public static final String GENDER = "GENDER";
    public static final String AGE = "AGE";

    StoriesApp app;
    FirebaseStorage storage;
    User user;
    private LinearLayout scrollProfile;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alien_profile);
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

        ImageView picView = (ImageView) this.findViewById(R.id.profile_pic);

        storage = FirebaseStorage.getInstance();
        String urlImage = this.app.userLoggedIn.urlProfilePicture;
        if (urlImage != null && urlImage != ""){
            Log.d("image",urlImage);
            try {
                StorageReference httpsReference = storage.getReferenceFromUrl(urlImage);
                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(httpsReference)
                        .into(picView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.scrollProfile = findViewById(R.id.scroll_profile);
        mRecyclerView = findViewById(R.id.profile_recycler_view);
        requestAlienProfileMainContent();
        this.scrollProfile.scrollTo(0,0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Go to Chat", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void requestAlienProfileMainContent(){
        AppServerRequest.getAlienStories(this.user.email, this.app.userLoggedIn.email, this.app.userLoggedIn.token, new AlienProfileActivity.CallbackRequestGetALienStories());
    }

    public class CallbackRequestGetALienStories extends HttpCallback {
        ArrayList<Post> posts;

        @Override
        public void onResponse() {
            try{
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                JSONObject jsonResponse = getJSONResponse();

                if (getHTTPResponse().code() == 200) {
                    Log.d("RESPONSE: ", jsonResponse.toString());
                    posts = new ArrayList<Post>();
                    JSONArray stories = (JSONArray) jsonResponse.get("userStories"); // Array of posts
                    for(int i = 0; i < stories.length(); ++i){
                        JSONObject story = stories.getJSONObject(i);
                        String username = story.getString("username");
                        User ownerUser = new User();
                        ownerUser.setEmail(username);
                        JSONObject storyDetails = story.getJSONObject("storyDetail");
                        String id, title, description, url, state;
                        try {
                            id = story.getString("_id");
                        }catch (JSONException e){
                            id = "0";
                        }
                        try {
                            title = storyDetails.getString("title");
                        }catch (JSONException e){
                            title = "title";
                        }
                        try {
                            description = storyDetails.getString("description");
                        }catch (JSONException e){
                            description = "description";
                        }
                        try {
                            state = storyDetails.getString("state");
                        }catch (JSONException e){
                            state = "Private";
                        }
                        try {
                            url = storyDetails.getString("url");
                        }catch (JSONException e){
                            url = "Private";
                        }
                        int type;
                        try {
                            type = storyDetails.getInt("type");
                        }catch (JSONException e){
                            type = Post.TYPE_IMAGE;
                        }
                        int privacity;
                        if (state == "Public"){
                            privacity = Post.privacity_public;
                        } else {
                            privacity = Post.privacity_private;
                        }

                        posts.add(new Post(id, title, description, R.drawable.stories_splash, ownerUser, privacity, url, type));
                    }
                    AlienProfileActivity.this.runOnUiThread(new AlienProfileActivity.CallbackRequestGetALienStories.SetResults());
                }

            } catch (Exception e) {
                Log.e("TEST REQUEST CALLBACK", "Error");
                e.printStackTrace();
            }
        }

        class SetResults implements Runnable{
            @Override
            public void run(){
                setProfileMainContent(posts);
            }
        }
    }

    public void setProfileMainContent(ArrayList<Post> posts){
        if (posts.size() == 0){
            Toast.makeText(getBaseContext(),"Don't have stories to show. Post one to start!", Toast.LENGTH_LONG).show();
        } else {
            RecyclerView container = (RecyclerView) findViewById(R.id.profile_recycler_view);
            container.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.app);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);
            container.setAdapter(new MyAdaptor(posts, this.app));
            container.setLayoutManager(layoutManager);
        }
    }
}
