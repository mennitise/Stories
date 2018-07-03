package com.fiuba.stories.stories;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.stories.stories.utils.AppServerRequest;
import com.fiuba.stories.stories.utils.UtilCallbacks;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class ProfileUpdate extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private StoriesApp app;

    // UI references.
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mBirthdayView;
    private EditText mGenderView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mUpdateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        this.app = (StoriesApp) getApplicationContext();

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

        //update button
        mUpdateButton = (Button) findViewById(R.id.update_button);

        if(mUpdateButton != null) {
            mUpdateButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeRequest();
                }
            });
        }

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void makeRequest(){
        final String firstName = mFirstNameView.getText().toString();
        final String lastName = mLastNameView.getText().toString();
        final String birthday = mBirthdayView.getText().toString();
        final String gender = mGenderView.getText().toString();
        app.userLoggedIn.setFirstName(firstName);
        app.userLoggedIn.setLastName(lastName);
        app.userLoggedIn.setBirthday(birthday);
        app.userLoggedIn.setGender(gender);

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
        AppServerRequest.putProfileInformation(firstName, lastName, app.userLoggedIn.getEmail(), birthday, "99", gender,app.userLoggedIn.token,
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

