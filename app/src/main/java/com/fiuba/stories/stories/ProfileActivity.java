package com.fiuba.stories.stories;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    StoriesApp app;
    FirebaseStorage storage;
    Intent intent = getIntent();

    private LinearLayout scrollProfile;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static final String URL_IMAGE_POST = "urlImagePost";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.app = (StoriesApp) getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(this.app.userLoggedIn.getName());
        setSupportActionBar(toolbar);
        TextView profileName = findViewById(R.id.info_profile_name_view);
        profileName.setText(this.app.userLoggedIn.getName());
        TextView profileEmail = findViewById(R.id.info_profile_email_view);
        profileEmail.setText(this.app.userLoggedIn.email);
        TextView profileBirthday = findViewById(R.id.info_profile_birthday_view);
        profileBirthday.setText(this.app.userLoggedIn.birthday);
        TextView profileGender = findViewById(R.id.info_profile_gender_view);
        profileGender.setText(this.app.userLoggedIn.gender);

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
        requestProfileMainContent();
        this.scrollProfile.scrollTo(0,0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFriendsScreen();
            }
        });

        FloatingActionButton edit = (FloatingActionButton) findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUpdateScreen();
            }
        });
    }

    public static Bitmap loadBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;

        try {
            return BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void goToFriendsScreen(){
        Intent intent = new Intent(this, FriendsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(intent);
    }

    private void goToUpdateScreen(){
        Intent intent = new Intent(this, ProfileUpdate.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(intent);
    }

    private void requestProfileMainContent(){
        AppServerRequest.getUserStory(this.app.userLoggedIn.email, this.app.userLoggedIn.email, this.app.userLoggedIn.token, new CallbackRequestGetStory());
    }

    public class CallbackRequestGetStory extends HttpCallback {
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
                        int privacity;
                        if (state == "Public"){
                            privacity = Post.privacity_public;
                        } else {
                            privacity = Post.privacity_private;
                        }
                        int type;
                        try {
                            type = storyDetails.getInt("type");
                        }catch (JSONException e){
                            type = Post.TYPE_IMAGE;
                        }
                        posts.add(new Post(id, title, description, R.drawable.stories_splash, ownerUser, privacity, url, type));
                    }
                    ProfileActivity.this.runOnUiThread(new SetResults());
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
