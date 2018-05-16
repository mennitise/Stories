package com.fiuba.stories.stories;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fiuba.stories.stories.utils.AppServerRequest;
import com.fiuba.stories.stories.utils.HttpCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class CreateStoryActivity extends AppCompatActivity {

    public static final int GET_FROM_GALLERY = 3;
    StoriesApp app;
    private ImageButton mediaUpload;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    private EditText mTitle;
    private EditText mDescription;
    private RadioGroup mPrivacity;

    private String id;
    private String title;
    private String description;
    private String privacity;
    private String urlImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.app = (StoriesApp) getApplicationContext();
        setContentView(R.layout.activity_create_story);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        mediaUpload = findViewById(R.id.upload_media);
        mediaUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        mTitle = findViewById(R.id.title_create_post);
        mDescription = findViewById(R.id.description_create_post);
        mPrivacity = findViewById(R.id.radio_privacity);

        Button publish = findViewById(R.id.publish_story);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = "0";
                title = mTitle.getText().toString();
                description = mDescription.getText().toString();
                urlImage = "image";

                int priv;
                if (mPrivacity.getCheckedRadioButtonId() == R.id.public_radio){
                    privacity = "Public";
                    priv = Post.privacity_public;
                } else {
                    privacity = "Private";
                    priv = Post.privacity_private;
                }
                int url = 0;
                requestUploadStory(new Post("0", title, description, url, app.userLoggedIn, priv));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Log.d("Uri: ", selectedImage.toString());
            final StorageReference fileReference = storageRef.child("images/"+selectedImage.getLastPathSegment());
            UploadTask uploadTask = fileReference.putFile(selectedImage);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Log.d("URL:",taskSnapshot.getDownloadUrl().toString());
                }
            });

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                mediaUpload.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void requestUploadStory(Post story){
        AppServerRequest.postStory(this.app.userLoggedIn.email, this.app.userLoggedIn.token, story, new CallbackRequestPostStory());
    }

    public class CallbackRequestPostStory extends HttpCallback {
        @Override
        public void onResponse() {
            try{
                //Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                // GET THE ID
                //JSONObject jsonResponse = getJSONResponse();

                if (getHTTPResponse().code() == 200) {
                    CreateStoryActivity.this.runOnUiThread(new CreateStoryActivity.CallbackRequestPostStory.SetResults());
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
                Toast.makeText(getBaseContext(), "Published", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(), PostDetailActivity.class);

                intent.putExtra(PostDetailActivity.ID_POST, id);
                intent.putExtra(PostDetailActivity.TITLE_POST, title);
                intent.putExtra(PostDetailActivity.DESCRIPTION_POST, description);
                intent.putExtra(PostDetailActivity.IMAGE_POST, urlImage);

                getBaseContext().startActivity(intent);
                finish(); // FIX ME: GO TO STORY SCREEN .-
            }
        }
    }
}
