package com.fiuba.stories.stories;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.stories.stories.utils.AppServerRequest;
import com.fiuba.stories.stories.utils.LocationHelper;
import com.fiuba.stories.stories.utils.UtilCallbacks;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class ProfileUpdate extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private StoriesApp app;
    public static final int GET_FROM_GALLERY = 3;

    // UI references.
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mBirthdayView;
    private EditText mGenderView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mUpdateButton;
    private ImageButton mMediaUpload;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String urlImage;
    private LinearLayout all;
    private ProgressBar loading;


    private Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        this.app = (StoriesApp) getApplicationContext();
        selectedImage = null;
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        // Set up the register form.
        //firstName
        mFirstNameView = findViewById(R.id.firstName);
        mFirstNameView.setText(app.userLoggedIn.firstName);

        //lastName
        mLastNameView = findViewById(R.id.lastName);
        mLastNameView.setText(app.userLoggedIn.lastName);

        //birthday
        mBirthdayView = findViewById(R.id.birthday);
        mBirthdayView.setText(app.userLoggedIn.birthday);

        //gender
        mGenderView = findViewById(R.id.gender);
        mGenderView.setText(app.userLoggedIn.gender);

        //upload photo
        mMediaUpload = findViewById(R.id.upload_media);
        mMediaUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        all = findViewById(R.id.all_update_profile);
        loading = findViewById(R.id.post_progress);
        //update button
        mUpdateButton = (Button) findViewById(R.id.update_button);

        if(mUpdateButton != null) {
            mUpdateButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadImage();
                }
            });
        }

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void uploadImage(){
        if (selectedImage != null) {
            all.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.VISIBLE);
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
                    makeRequest();
                }
            });

            /* ---------------- FIREBASE UPLOAD ---------------- */
        } else {
            // Not upload file
            Log.d("IMAGE NOT UPLOADED", "UPLOAD THE IMAGE");
        }
    }

    private void makeRequest(){
        final String firstName = mFirstNameView.getText().toString();
        final String lastName = mLastNameView.getText().toString();
        final String birthday = mBirthdayView.getText().toString();
        final String gender = mGenderView.getText().toString();
        final String profilePic = urlImage;

        app.userLoggedIn.setFirstName(firstName);
        app.userLoggedIn.setLastName(lastName);
        app.userLoggedIn.setBirthday(birthday);
        app.userLoggedIn.setGender(gender);
        app.userLoggedIn.setUrlProfilePicture(profilePic);

        Runnable runner200 = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), "Update Successful.", Toast.LENGTH_LONG).show();
            }
        };
        Runnable runner401 = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), "Can't register.", Toast.LENGTH_LONG).show();
                showProgress(false);
            }
        };
        Runnable runner400 = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), "The registration fails. Please try again.", Toast.LENGTH_LONG).show();
                showProgress(false);
            }
        };
        UtilCallbacks util = new UtilCallbacks();
        AppServerRequest.putProfileInformation(firstName, lastName, app.userLoggedIn.getEmail(), birthday, "99", gender, profilePic ,app.userLoggedIn.token,
                util.getCallbackRequestProfilePut(app.userLoggedIn.getEmail(), app, this, MainActivity.class, runner200, runner401, runner400));

        this.finish();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
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
                mMediaUpload.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

