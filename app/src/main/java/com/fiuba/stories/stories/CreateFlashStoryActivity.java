package com.fiuba.stories.stories;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import java.io.FileNotFoundException;
import java.io.IOException;

public class CreateFlashStoryActivity extends AppCompatActivity {

    public static final int GET_FROM_GALLERY = 3;
    StoriesApp app;
    private ImageButton mediaUpload;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    private EditText mDescription;
    private RadioGroup mPrivacity;

    private String id;
    private String title;
    private String description;
    private String privacity;
    private String urlImage;
    private Uri selectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.app = (StoriesApp) getApplicationContext();
        setContentView(R.layout.activity_create_flash_story);
        selectedImage = null;
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        mediaUpload = findViewById(R.id.upload_media);
        mediaUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        mDescription = findViewById(R.id.description_create_post);
        mPrivacity = findViewById(R.id.radio_privacity);

        Button publish = findViewById(R.id.publish_story);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedImage != null) {
                    /* ---------------- FIREBASE UPLOAD ---------------- */

                    final StorageReference fileReference = storageRef.child("images/"+selectedImage.getLastPathSegment());
                    UploadTask uploadTask = fileReference.putFile(selectedImage);

                    // Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.d("IMAGE NOT UPLOADED", "Fails at upload");
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                            Log.d("URL:",taskSnapshot.getDownloadUrl().toString());
                            urlImage = taskSnapshot.getDownloadUrl().toString();
                            id = "0";
                            description = mDescription.getText().toString();

                            int priv;
                            if (mPrivacity.getCheckedRadioButtonId() == R.id.public_radio){
                                privacity = "Public";
                                priv = Post.privacity_public;
                            } else {
                                privacity = "Private";
                                priv = Post.privacity_private;
                            }
                            requestUploadFlashStory(new Post("0", "", description, 0, app.userLoggedIn, priv, urlImage));
                        }
                    });

                    /* ---------------- FIREBASE UPLOAD ---------------- */
                } else {
                    // Not upload file
                    Log.d("IMAGE NOT UPLOADED", "UPLOAD THE IMAGE");
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
            Log.d("Uri: ", selectedImage.toString());
            Bitmap bitmap;
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

    private void requestUploadFlashStory(Post story){
        AppServerRequest.postFlashStory(this.app.userLoggedIn.email, this.app.userLoggedIn.token, story, new CallbackRequestPostFlashStory());
    }

    public class CallbackRequestPostFlashStory extends HttpCallback {
        @Override
        public void onResponse() {
            try{
                Log.d("ERRRRRROOOOOOORRRRRR",getHTTPResponse().toString());

                if (getHTTPResponse().code() == 200) {
                    CreateFlashStoryActivity.this.runOnUiThread(new CreateFlashStoryActivity.CallbackRequestPostFlashStory.SetResults());
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
                finish();
            }
        }
    }
}
