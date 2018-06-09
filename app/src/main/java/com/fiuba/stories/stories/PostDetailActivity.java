package com.fiuba.stories.stories;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.fiuba.stories.stories.utils.AppServerRequest;
import com.fiuba.stories.stories.utils.UtilCallbacks;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;

public class PostDetailActivity extends AppCompatActivity {

    public static final String ID_POST = "idPost";
    public static final String USERNAME_POST = "usernamePost";
    public static final String NAME_AUTHOR_POST = "nameAuthorPost";
    public static final String TITLE_POST = "titlePost";
    public static final String DESCRIPTION_POST = "descPost";
    public static final String IMAGE_POST = "imagePost";
    public static final String URL_IMAGE_POST = "urlImagePost";

    FirebaseStorage storage;
    StoriesApp app;

    String id;
    String usernameAuthor;
    String nameAuthor;
    String title;
    String description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        this.app = (StoriesApp) getApplicationContext();
        Intent intent = getIntent();

        id = intent.getStringExtra(ID_POST);
        usernameAuthor = intent.getStringExtra(USERNAME_POST);
        nameAuthor = intent.getStringExtra(NAME_AUTHOR_POST);

        title = intent.getStringExtra(TITLE_POST);
        TextView titleView = findViewById(R.id.title_post);
        titleView.setText(title);

        description = intent.getStringExtra(DESCRIPTION_POST);
        TextView descriptionView  = findViewById(R.id.description_post);
        descriptionView.setText(description);

        int image = intent.getIntExtra(IMAGE_POST,0);
        ImageView imageView = findViewById(R.id.post_image);
        imageView.setImageResource(image);

        storage = FirebaseStorage.getInstance();
        String urlImage = intent.getStringExtra(URL_IMAGE_POST);
        if (urlImage != null && urlImage != ""){
            Log.d("image",urlImage);
            try {
                StorageReference httpsReference = storage.getReferenceFromUrl(urlImage);
                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(httpsReference)
                        .into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Button author = findViewById(R.id.author_post);
        if (usernameAuthor != null){
            author.setText(usernameAuthor);
        }
        author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usernameAuthor.equals(app.userLoggedIn.getEmail())){
                    Snackbar.make(view, "Your Profile", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    goToProfileScreen();
                } else {
                    Snackbar.make(view, "Author Profile", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    goToAuthorProfileScreen(usernameAuthor);
                }
            }
        });

        ImageButton like = findViewById(R.id.like_button);
        ImageButton dislike = findViewById(R.id.dislike_button);
        ImageButton faceLike = findViewById(R.id.like_face_button);
        ImageButton faceDislike = findViewById(R.id.not_like_face_button);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Like", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        faceLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Haha", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Dislike", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        faceDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Meh", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //post_image


        List<JSONObject> comments = getComments();
        RecyclerView container = (RecyclerView) findViewById(R.id.comments_recycler_view);
        container.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.app);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        container.setAdapter(new CommentsAdaptor(comments));
        container.setLayoutManager(layoutManager);
    }

    private List<JSONObject> getComments(){
        ArrayList<JSONObject> result = new ArrayList<>();
        JSONObject comment1 = new JSONObject();
        JSONObject comment2 = new JSONObject();
        JSONObject comment3 = new JSONObject();
        try {
            comment1.put("author", "Brad Pitt");
            comment1.put("comment", "Excelent! I love this story!");
            comment2.put("author", "Diego Maradona");
            comment2.put("comment", "Ehhh... Yo pienso... Ehhhh... Lo... Ehhh... Mismo...");
            comment3.put("author", "Guido Kaczka");
            comment3.put("comment", "Noo! Pasame la repe!");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        result.add(comment1);
        result.add(comment2);
        result.add(comment3);

        return result;
    }

    private void goToProfileScreen() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(intent);
    }

    private void goToAuthorProfileScreen(String username) {
        UtilCallbacks util = new UtilCallbacks();
        AppServerRequest.getAlienProfileInformation(username,
                                                    this.app.userLoggedIn.email,
                                                    this.app.userLoggedIn.token,
                                                    util.getCallbackRequestGetAlienProfile(this.app, username, AlienProfileActivity.class, this, new ToastRunnable("401"), new ToastRunnable("400")));
    }

    public class ToastRunnable implements Runnable {

        String message;

        public ToastRunnable(String message){
            this.message = message;
        }

        @Override
        public void run() {
            Toast.makeText(getBaseContext(), this.message, Toast.LENGTH_LONG).show();
        }
    }
}
