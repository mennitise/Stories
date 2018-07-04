package com.fiuba.stories.stories;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.fiuba.stories.stories.utils.AppServerRequest;
import com.fiuba.stories.stories.utils.HttpCallback;
import com.fiuba.stories.stories.utils.UtilCallbacks;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Address;
import okhttp3.Callback;
import okhttp3.internal.Util;

public class PostDetailActivity extends AppCompatActivity {

    public static final String ID_POST = "idPost";
    public static final String USERNAME_POST = "usernamePost";
    public static final String NAME_AUTHOR_POST = "nameAuthorPost";
    public static final String TITLE_POST = "titlePost";
    public static final String DESCRIPTION_POST = "descPost";
    public static final String IMAGE_POST = "imagePost";
    public static final String URL_IMAGE_POST = "urlImagePost";
    public static final String TYPE_POST = "typePost";

    FirebaseStorage storage;
    StoriesApp app;

    String id;
    String usernameAuthor;
    String nameAuthor;
    String title;
    String description;
    int type;
    int image;
    Button author;
    Button likes;
    ImageButton like;
    ImageButton dislike;
    ImageButton faceLike;
    ImageButton faceDislike;
    EditText comment;
    ImageButton sendComment;

    ImageView imageView;
    VideoView videoView;


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

        type = intent.getIntExtra(TYPE_POST, Post.TYPE_IMAGE);

        imageView = findViewById(R.id.post_image);
        videoView = findViewById(R.id.post_video);
        if (type == Post.TYPE_IMAGE){
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.INVISIBLE);
        } else if (type == Post.TYPE_VIDEO){
            imageView.setVisibility(View.INVISIBLE);
            videoView.setVisibility(View.VISIBLE);
        }

        image = intent.getIntExtra(IMAGE_POST,0);
        imageView.setImageResource(image);

        storage = FirebaseStorage.getInstance();
        String urlImage = intent.getStringExtra(URL_IMAGE_POST);
        if (urlImage != null && urlImage != ""){
            if (type == Post.TYPE_IMAGE){

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

            if (type == Post.TYPE_VIDEO) {
                Log.d("WATCH VIDEO", "Can you see?");
                Log.d("WATCH VIDEO", urlImage);
                Uri uri = Uri.parse(urlImage);
                MediaController controller = new MediaController(this);
                videoView.setVideoURI(uri);
                controller.setMediaPlayer(videoView);
                videoView.start();
            }
        }

/*
        {"feedStories":[
                {"_id":"6c27e55d-91c3-49d5-8a46-06dd1c1b7aba","username":"mennitise@gmail.com","likes":3,"comments":2,
                        "storyDetail":{"description":"Take by me","title":"Upload a photo","state":"Public","url":"https:\/\/firebasestorage.googleapis.com\/v0\/b\/stories-tii.appspot.com\/o\/images%2FJPEG_20180703_220541_769937361.jpg?alt=media&token=089307e0-cc4e-408b-8a04-397de7d21e95","lat":-34.6869119,"long":-58.6587003},"createdAt":"2018-07-04 01:06:19.874885","updatedAt":"2018-07-04 01:06:19.874906","reactions":[{"reacter":"mennitise@gmail.com","reaction":"like"},{"reacter":"test@test.com","reaction":"like"},{"reacter":"a@gmail.com","reaction":"facelike"}],"importance":30.6},
                {"_id":"2beca12e-e009-48ae-b4f6-917fff38c4b1","username":"mennitise@gmail.com","likes":1,"comments":0,
                        "storyDetail":{"description":"Can you see?","title":"Uploading video!","state":"Public","url":"https:\/\/firebasestorage.googleapis.com\/v0\/b\/stories-tii.appspot.com\/o\/videos%2F1216491300?alt=media&token=b215f272-1d23-44aa-9a5d-039d66085263","lat":-34.6868776,"type":1,"long":-58.6586957},"createdAt":"2018-07-04 02:37:57.228813","updatedAt":"2018-07-04 02:37:57.228828","reactions":[{"reacter":"a@gmail.com","reaction":"like"}],"importance":28.2},
                {"_id":"a3734c55-8380-4a2a-beea-87b14ba1bfbd","username":"mennitise@gmail.com","likes":0,"comments":0,
                        "storyDetail":{"description":"It's really good","title":"Yeah!","state":"Public","url":"https:\/\/firebasestorage.googleapis.com\/v0\/b\/stories-tii.appspot.com\/o\/images%2F19326?alt=media&token=1747dad6-7284-4bd8-8b55-4c5c1528cde5","lat":-34.6869134,"type":0,"long":-58.6586997},"createdAt":"2018-07-04 02:35:17.422810","updatedAt":"2018-07-04 02:35:17.422832","reactions":[],"importance":27.5},
                {"_id":"1c8a9077-3811-4b1b-8773-77530c497409","username":"a@gmail.com","likes":2,"comments":13,
                        "storyDetail":{"description":"Le chat","title":"Macri","state":"Public","url":"https:\/\/firebasestorage.googleapis.com\/v0\/b\/stories-tii.appspot.com\/o\/images%2F1911929275?alt=media&token=f8e7526a-d909-444b-8eb8-57541c0ae29b","lat":null,"type":null,"long":null},"createdAt":"2018-07-04 02:47:14.613087","updatedAt":"2018-07-04 02:47:14.613104","reactions":[{"reacter":"mennitise@gmail.com","reaction":"facelike"},{"reacter":"a@gmail.com","reaction":"like"}],"importance":20.4}]}
*/
        author = findViewById(R.id.author_post);
        likes = findViewById(R.id.likes_button);

        if (usernameAuthor != null){
            author.setText(usernameAuthor);
        } else {
            author.setVisibility(View.INVISIBLE);
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
        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLikesScreen();
            }
        });

        AppServerRequest.getComments(this.app.userLoggedIn.getEmail(),this.app.userLoggedIn.token,id,new CallbackRequestGetComment());

        like = findViewById(R.id.like_button);
        dislike = findViewById(R.id.dislike_button);
        faceLike = findViewById(R.id.like_face_button);
        faceDislike = findViewById(R.id.not_like_face_button);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppServerRequest.putStoryReaction(app.userLoggedIn.getEmail(),app.userLoggedIn.token, id, "like", new CallbackRequestPutReaction(new SnackRunnable(view, "Like it"), new SnackRunnable(view,"Error, please try again.")));
            }
        });

        faceLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppServerRequest.putStoryReaction(app.userLoggedIn.getEmail(),app.userLoggedIn.token, id, "facelike", new CallbackRequestPutReaction(new SnackRunnable(view, "Haha!"), new SnackRunnable(view,"Error, please try again.")));
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppServerRequest.putStoryReaction(app.userLoggedIn.getEmail(),app.userLoggedIn.token, id, "dislike", new CallbackRequestPutReaction(new SnackRunnable(view, "Dislike it"), new SnackRunnable(view,"Error, please try again.")));
            }
        });

        faceDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppServerRequest.putStoryReaction(app.userLoggedIn.getEmail(),app.userLoggedIn.token, id, "facedislike", new CallbackRequestPutReaction(new SnackRunnable(view, "Meh..."), new SnackRunnable(view,"Error, please try again.")));
            }
        });

        comment = findViewById(R.id.comment_text);
        comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int idKey, KeyEvent keyEvent) {
                if (idKey == EditorInfo.IME_ACTION_SEND) {
                    if (TextUtils.isEmpty(comment.getText().toString())) {
                        comment.setError(getString(R.string.error_field_required));
                    } else {
                        AppServerRequest.putComment(app.userLoggedIn.getEmail(), app.userLoggedIn.token, id, comment.getText().toString(), new CallbackRequestPutComment());
                        comment.setText("");
                    }
                    return true;
                }
                return false;
            }
        });

        sendComment = findViewById(R.id.comment_send);
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(comment.getText().toString())) {
                    comment.setError(getString(R.string.error_field_required));
                } else {
                    Log.d("STORY-ID: ",id);
                    AppServerRequest.putComment(app.userLoggedIn.getEmail(), app.userLoggedIn.token, id, comment.getText().toString(), new CallbackRequestPutComment());
                    comment.setText("");
                }
            }
        });
    }

    private void setCommentsContent(ArrayList<JSONObject> comments){
        RecyclerView container = (RecyclerView) findViewById(R.id.comments_recycler_view);
        container.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.app);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        container.setAdapter(new CommentsAdaptor(comments));
        container.setLayoutManager(layoutManager);
        layoutManager.scrollToPosition(comments.size()-1);
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
                                                    util.getCallbackRequestGetAlienProfile(username, AlienProfileActivity.class, this, new ToastRunnable("401"), new ToastRunnable("400")));
    }

    private void goToLikesScreen() {
        Intent intent = new Intent(this, LikesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        intent.putExtra(LikesActivity.STORY_ID, this.id);
        startActivity(intent);
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

    public class SnackRunnable implements Runnable {

        String message;
        View view;

        public SnackRunnable(View v,String message){
            this.view = v;
            this.message = message;
        }

        @Override
        public void run() {
            Snackbar.make(this.view, this.message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    public class CallbackRequestPutReaction extends HttpCallback {

        Runnable callUI;
        Runnable callErrorUI;

        public CallbackRequestPutReaction(Runnable callUI, Runnable callErrorUI){
            this.callUI = callUI;
            this.callErrorUI = callErrorUI;
        }

        @Override
        public void onResponse() {
            try{
                if (getHTTPResponse().code() == 200) {
                    Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                    JSONObject jsonResponse = getJSONResponse();
                    Log.d("RESPONSE: ", jsonResponse.toString());
                    this.callUI.run();
                } else {

                }
            } catch (Exception e) {
                Log.e("TEST REQUEST CALLBACK", "Error");
                e.printStackTrace();
            }
        }
    }

    public class CallbackRequestPutComment extends HttpCallback {
        @Override
        public void onResponse() {
            try{
                if (getHTTPResponse().code() == 200) {
                    Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                    JSONObject jsonResponse = getJSONResponse();
                    Log.d("RESPONSE: ", jsonResponse.toString());
                    AppServerRequest.getComments(app.userLoggedIn.getEmail(),app.userLoggedIn.token,id,new CallbackRequestGetComment());
                }
            } catch (Exception e) {
                Log.e("TEST REQUEST CALLBACK", "Error");
                e.printStackTrace();
            }
        }
    }

    public class CallbackRequestGetComment extends HttpCallback {
        ArrayList<JSONObject> comments;

        @Override
        public void onResponse() {
            try{
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                JSONObject jsonResponse = getJSONResponse();
                if (getHTTPResponse().code() == 200) {
                    Log.d("RESPONSE: ", jsonResponse.toString());
                    JSONArray usersJson = (JSONArray) jsonResponse.get("comments"); // Array of comments
                    comments = new ArrayList<>();
                    for(int i = 0; i < usersJson.length(); ++i){
                        comments.add(usersJson.getJSONObject(i));
                    }
                    PostDetailActivity.this.runOnUiThread(new PostDetailActivity.CallbackRequestGetComment.SetResults());
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
                setCommentsContent(comments);
            }
        }
    }
}
