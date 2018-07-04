package com.fiuba.stories.stories.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fiuba.stories.stories.AlienProfileActivity;
import com.fiuba.stories.stories.Login;
import com.fiuba.stories.stories.MainActivity;
import com.fiuba.stories.stories.NoFriendActivity;
import com.fiuba.stories.stories.PostDetailActivity;
import com.fiuba.stories.stories.ProfileUpdate;
import com.fiuba.stories.stories.RegisterActivity;
import com.fiuba.stories.stories.StoriesApp;
import com.fiuba.stories.stories.User;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilCallbacks{


    public CallbackRequestLogin getCallbackRequestLogin(String vUsername, StoriesApp app, Login loginActivity, Class<MainActivity> mainActivityClass, Runnable vRunner200, Runnable vRunner401, Runnable vRunner400){
        Log.d("DEbUG", "new CallbackRequestLogin");
        return new CallbackRequestLogin(vUsername, app, loginActivity, mainActivityClass, vRunner200, vRunner401, vRunner400);
    }

    public CallbackRequestRegister getCallbackRequestRegister(String vUsername, StoriesApp app, RegisterActivity registerActivity, Class<MainActivity> mainActivityClass, Runnable vRunner200, Runnable vRunner401, Runnable vRunner400){
        Log.d("DEbUG", "new CallbackRequestLogin");
        return new CallbackRequestRegister(vUsername, app, registerActivity, mainActivityClass, vRunner200, vRunner401, vRunner400);
    }

    public CallbackRequestProfilePut getCallbackRequestProfilePut(String vUsername, StoriesApp app, ProfileUpdate profileActivity, Class<MainActivity> mainActivityClass, Runnable vRunner200, Runnable vRunner401, Runnable vRunner400){
        Log.d("DEbUG", "new CallbackRequestLogin");
        return new CallbackRequestProfilePut(vUsername, app, profileActivity, vRunner200, vRunner401, vRunner400);
    }

    public CallbackRequestGetAlienProfile getCallbackRequestGetAlienProfile(String username, Class<AlienProfileActivity> profileActivityClass, PostDetailActivity postDetailActivity, Runnable vRunner401, Runnable vRunner400){
        return new CallbackRequestGetAlienProfile(username, profileActivityClass, postDetailActivity, vRunner401, vRunner400);
    }

    public CallbackRequestGetAlienProfileInvitation getCallbackRequestGetAlienProfileInvitation(String username, Class<AlienProfileActivity> profileActivityClass, Context activity, Runnable vRunner401, Runnable vRunner400){
        return new CallbackRequestGetAlienProfileInvitation(username, profileActivityClass, activity, vRunner401, vRunner400);
    }

    public CallbackRequestGetNoFriendProfile getCallbackRequestGetNoFriendProfile(String username, Class<NoFriendActivity> profileActivityClass, Context activity, Runnable vRunner401, Runnable vRunner400){
        return new CallbackRequestGetNoFriendProfile(username, profileActivityClass, activity, vRunner401, vRunner400);
    }

    public CallbackRequestRegisterFacebook getCallbackRequestRegisterFacebook(){
        return new CallbackRequestRegisterFacebook();
    }

    public CallbackRequestPostNotification getCallbackRequestPostNotification(){
        return new CallbackRequestPostNotification();
    }

    // ----------------------------------------------------------------

    public class CallbackRequestRegister extends HttpCallback {
        String username;
        StoriesApp app;
        RegisterActivity registerActivity;
        Class<MainActivity> mainActivityClass;
        Runnable runner200;
        Runnable runner401;
        Runnable runner400;
        public CallbackRequestRegister(String vUsername, StoriesApp app, RegisterActivity registerActivity, Class<MainActivity> mainActivityClass, Runnable vRunner200, Runnable vRunner401, Runnable vRunner400) {
            this.username = vUsername;
            this.app = app;
            this.registerActivity = registerActivity;
            this.mainActivityClass = mainActivityClass;
            this.runner200 = vRunner200;
            this.runner401 = vRunner401;
            this.runner400 = vRunner400;
        }

        @Override
        public void onResponse() {
            try {
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                Log.d("HTTP RESPONSE: ", "code = " + getHTTPResponse().code());
                if (getHTTPResponse().code() == 200) {
                    Log.d("RESPONSE: ", getJSONResponse().toString());

                    new Handler(Looper.getMainLooper()).post(this.runner200);
                    String token = jsonResponse.get("Token").toString();
                    User currentUser = new User("","",username,"");
                    currentUser.setCurrentToken(token, false);
                    currentUser.LOG_USER();
                    this.app.userLoggedIn = currentUser;
                    AppServerRequest.getProfileInformation(this.username, token, new UtilCallbacks.CallbackRequestGetProfileFromRegister(this.app, this.registerActivity, this.mainActivityClass, this.runner401, this.runner400));
                } else if (getHTTPResponse().code() == 401){
                    // The user is already registered
                    new Handler(Looper.getMainLooper()).post(this.runner401);
                } else {
                    // The registration fails
                    new Handler(Looper.getMainLooper()).post(this.runner400);
                }
            } catch (Exception e) {
                Log.e("TEST REQUEST CALLBACK", "Error");
                e.printStackTrace();
            }
        }
    }

    public class CallbackRequestProfilePut extends HttpCallback {
        String username;
        StoriesApp app;
        ProfileUpdate profileUpdate;
        Class<MainActivity> mainActivityClass;
        Runnable runner200;
        Runnable runner401;
        Runnable runner400;
        public CallbackRequestProfilePut(String vUsername, StoriesApp app, ProfileUpdate profileUpdate, Runnable vRunner200, Runnable vRunner401, Runnable vRunner400) {
            this.username = vUsername;
            this.app = app;
            this.profileUpdate = profileUpdate;
            this.runner200 = vRunner200;
            this.runner401 = vRunner401;
            this.runner400 = vRunner400;
        }

        @Override
        public void onResponse() {
            try {
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                Log.d("HTTP RESPONSE: ", "code = " + getHTTPResponse().code());
                if (getHTTPResponse().code() == 200) {
                    Log.d("RESPONSE: ", getHTTPResponse().body().string());

                    new Handler(Looper.getMainLooper()).post(this.runner200);
                } else if (getHTTPResponse().code() == 401){
                    // The user is already registered
                    new Handler(Looper.getMainLooper()).post(this.runner401);
                } else {
                    // The registration fails
                    new Handler(Looper.getMainLooper()).post(this.runner400);
                }
            } catch (Exception e) {
                Log.e("TEST REQUEST CALLBACK", "Error");
                e.printStackTrace();
            }
        }
    }

    public class CallbackRequestRegisterFacebook extends HttpCallback {
        String username;
        StoriesApp app;
        RegisterActivity registerActivity;
        Class<MainActivity> mainActivityClass;
        public CallbackRequestRegisterFacebook() {
        }

        @Override
        public void onResponse() {
            try {
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                Log.d("HTTP RESPONSE: ", "code = " + getHTTPResponse().code());
                /*
                if (getHTTPResponse().code() == 200) {
                    Log.d("RESPONSE: ", getJSONResponse().toString());

                    new Handler(Looper.getMainLooper()).post(this.runner200);
                    String token = jsonResponse.get("Token").toString();
                    User currentUser = new User("","",username,"");
                    currentUser.setCurrentToken(token, false);
                    currentUser.LOG_USER();
                    this.app.userLoggedIn = currentUser;
                    AppServerRequest.getProfileInformation(this.username, token, new UtilCallbacks.CallbackRequestGetProfileFromRegister(this.app, this.registerActivity, this.mainActivityClass, this.runner401, this.runner400));
                } else if (getHTTPResponse().code() == 401){
                    // The user is already registered
                    new Handler(Looper.getMainLooper()).post(this.runner401);
                } else {
                    // The registration fails
                    new Handler(Looper.getMainLooper()).post(this.runner400);
                }
                */
            } catch (Exception e) {
                Log.e("TEST REQUEST CALLBACK", "Error");
                e.printStackTrace();
            }
        }
    }

    public class CallbackRequestLogin extends HttpCallback {

        String response;
        String username;
        StoriesApp app;
        Login loginActivity;
        Class<MainActivity> mainActivityClass;
        Runnable runner200;
        Runnable runner401;
        Runnable runner400;

        public CallbackRequestLogin(String vUsername, StoriesApp app, Login loginActivity, Class<MainActivity> mainActivityClass, Runnable vRunner200, Runnable vRunner401, Runnable vRunner400) {
            this.username = vUsername;
            this.app = app;
            this.loginActivity = loginActivity;
            this.mainActivityClass = mainActivityClass;
            this.runner200 = vRunner200;
            this.runner401 = vRunner401;
            this.runner400 = vRunner400;
        }

        @Override
        public void onResponse() {
            try {
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                JSONObject jsonResponse = getJSONResponse();
                Log.d("RESPONSE: ", jsonResponse.toString());
                if (getHTTPResponse().code() == 200) {
                    String token = jsonResponse.get("Token").toString();
                    User currentUser = new User("","",username,"");
                    currentUser.setCurrentToken(token, false);
                    currentUser.LOG_USER();
                    this.app.userLoggedIn = currentUser;
                    AppServerRequest.getProfileInformation(this.username, token, new UtilCallbacks.CallbackRequestGetProfile(this.app, this.loginActivity, this.mainActivityClass, this.runner401, this.runner400));
                    new Handler(Looper.getMainLooper()).post(this.runner200);
                } else if (getHTTPResponse().code() == 401){
                    // The user is already registered
                    new Handler(Looper.getMainLooper()).post(this.runner401);
                } else {
                    // The log in fails
                    new Handler(Looper.getMainLooper()).post(this.runner400);
                }
            } catch (Exception e) {
                Log.e("TEST REQUEST CALLBACK", "Error");
                e.printStackTrace();
            }
        }
    }

    public class CallbackRequestGetProfile extends HttpCallback {
        String username;
        StoriesApp app;
        Login loginActivity;
        Class<MainActivity> mainActivityClass;
        Runnable runner401;
        Runnable runner400;

        public CallbackRequestGetProfile(StoriesApp app, Login loginActivity, Class<MainActivity> mainActivityClass, Runnable vRunner401, Runnable vRunner400) {
            this.app = app;
            this.loginActivity = loginActivity;
            this.mainActivityClass = mainActivityClass;
            this.runner401 = vRunner401;
            this.runner400 = vRunner400;
        }

        @Override
        public void onResponse() {
            try {
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                JSONObject jsonResponse = getJSONResponse();
                if (getHTTPResponse().code() == 200) {
                    Log.d("RESPONSE: ", jsonResponse.toString());
                    try{
                        this.app.userLoggedIn.setLastName(jsonResponse.get("lastname").toString());
                    } catch (JSONException e){
                        this.app.userLoggedIn.setLastName("");
                    }
                    try{
                        this.app.userLoggedIn.setFirstName(jsonResponse.get("firstname").toString());
                    } catch (JSONException e) {
                        this.app.userLoggedIn.setFirstName("");
                    }
                    try{
                        this.app.userLoggedIn.setBirthday(jsonResponse.get("birthday").toString());
                    } catch (JSONException e){
                        this.app.userLoggedIn.setBirthday("");
                    }
                    try{
                        this.app.userLoggedIn.setGender(jsonResponse.get("gender").toString());
                    } catch (JSONException e){
                        this.app.userLoggedIn.setGender("");
                    }
                    try{
                        this.app.userLoggedIn.setUrlProfilePicture(jsonResponse.get("profilePic").toString());
                    } catch (JSONException e){
                        this.app.userLoggedIn.setUrlProfilePicture("");
                    }
                    try{
                        this.app.userLoggedIn.setAge(jsonResponse.get("age").toString());
                    } catch (JSONException e){
                        this.app.userLoggedIn.setAge("");
                    }
                    this.app.userLoggedIn.LOG_USER();
                } else if (getHTTPResponse().code() == 401){
                    // Problem obtains the profile data
                    new Handler(Looper.getMainLooper()).post(this.runner401);
                } else if (getHTTPResponse().code() == 400){
                    // The log in fails
                    new Handler(Looper.getMainLooper()).post(this.runner400);
                }
                Log.d("Debug", "GO TO MAIN SCREEN");
                Intent intent = new Intent(this.loginActivity, this.mainActivityClass);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                this.loginActivity.startActivity(intent);
                //this.loginActivity.goMainScreen();
            } catch (Exception e) {
                Log.e("TEST REQUEST CALLBACK", "Error");
                e.printStackTrace();
            }
        }
    }

    public class CallbackRequestGetProfileFromRegister extends HttpCallback {
        StoriesApp app;
        RegisterActivity registerActivity;
        Class<MainActivity> mainActivityClass;
        Runnable runner401;
        Runnable runner400;

        public CallbackRequestGetProfileFromRegister(StoriesApp app, RegisterActivity registerActivity, Class<MainActivity> mainActivityClass, Runnable vRunner401, Runnable vRunner400) {
            this.app = app;
            this.registerActivity = registerActivity;
            this.mainActivityClass = mainActivityClass;
            this.runner401 = vRunner401;
            this.runner400 = vRunner400;
        }

        @Override
        public void onResponse() {
            try {
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                JSONObject jsonResponse = getJSONResponse();
                if (getHTTPResponse().code() == 200) {
                    try{
                        Log.d("RESPONSE: ", jsonResponse.toString());
                        Log.d("USER",jsonResponse.get("firstname").toString());
                        Log.d("USER",jsonResponse.get("lastname").toString());
                        Log.d("USER",jsonResponse.get("age").toString());
                        Log.d("USER",jsonResponse.get("gender").toString());
                        Log.d("USER",jsonResponse.get("profilePic").toString());
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                    try{
                        if (jsonResponse.get("lastname") != null){
                            this.app.userLoggedIn.setLastName(jsonResponse.get("lastname").toString());
                        }
                    } catch (JSONException e){
                        this.app.userLoggedIn.setLastName("Unknown");
                    }
                    try{
                        if(jsonResponse.get("firstname") != null) {
                            this.app.userLoggedIn.setFirstName(jsonResponse.get("firstname").toString());
                        }
                    } catch (JSONException e){
                        this.app.userLoggedIn.setFirstName("Unknown");
                    }
                    this.app.userLoggedIn.setEmail(this.app.userLoggedIn.getEmail());
                    try{
                        if (jsonResponse.get("birthday") != null){
                            this.app.userLoggedIn.setBirthday(jsonResponse.get("birthday").toString());
                        }
                    }catch (JSONException e){
                        this.app.userLoggedIn.setBirthday("Unknown");
                    }
                    try{
                        if (jsonResponse.get("gender") != null){
                            this.app.userLoggedIn.setGender(jsonResponse.get("gender").toString());
                        }
                    }catch (JSONException e){
                        this.app.userLoggedIn.setGender("Unknown");
                    }
                    try{
                        if (jsonResponse.get("age") != null){
                            this.app.userLoggedIn.setAge(jsonResponse.get("age").toString());
                        }
                    }catch (JSONException e){
                        this.app.userLoggedIn.setAge("Unknown");
                    }
                    try{
                        if (jsonResponse.get("profilePic") != null){
                            this.app.userLoggedIn.setUrlProfilePicture(jsonResponse.get("profilePic").toString());
                        }
                    }catch (JSONException e){
                        this.app.userLoggedIn.setUrlProfilePicture("");
                    }
                    this.app.userLoggedIn.LOG_USER();
                } else if (getHTTPResponse().code() == 401){
                    // Problem obtains the profile data
                    new Handler(Looper.getMainLooper()).post(this.runner401);
                } else if (getHTTPResponse().code() == 400){
                    // The log in fails
                    new Handler(Looper.getMainLooper()).post(this.runner400);
                }
                Log.d("Debug", "GO TO MAIN SCREEN");
                Intent intent = new Intent(this.registerActivity, this.mainActivityClass);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                this.registerActivity.startActivity(intent);
                //this.loginActivity.goMainScreen();
            } catch (Exception e) {
                Log.e("TEST REQUEST CALLBACK", "Error");
                e.printStackTrace();
            }
        }
    }

    public class CallbackRequestGetAlienProfile extends HttpCallback {
        String username;
        PostDetailActivity postDetailActivity;
        Class<AlienProfileActivity> profileActivityClass;
        Runnable runner401;
        Runnable runner400;

        public CallbackRequestGetAlienProfile(String username, Class<AlienProfileActivity> profileActivityClass, PostDetailActivity postDetailActivity, Runnable vRunner401, Runnable vRunner400) {
            this.username = username;
            this.profileActivityClass = profileActivityClass;
            this.postDetailActivity = postDetailActivity;
            this.runner401 = vRunner401;
            this.runner400 = vRunner400;
        }

        @Override
        public void onResponse() {
            try {
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                JSONObject jsonResponse = getJSONResponse();
                Intent intent = new Intent(this.postDetailActivity, this.profileActivityClass);
                if (getHTTPResponse().code() == 200) {
                    Log.d("RESPONSE: ", jsonResponse.toString());
                    try{
                        intent.putExtra(AlienProfileActivity.FIRST_NAME, jsonResponse.get("firstname").toString());
                    } catch (Exception e){
                        Log.e("TEST REQUEST CALLBACK", "Error");
                        e.printStackTrace();
                        intent.putExtra(AlienProfileActivity.FIRST_NAME, "");
                    }
                    try {
                        intent.putExtra(AlienProfileActivity.LAST_NAME, jsonResponse.get("lastname").toString());
                    } catch (Exception e){
                        Log.e("TEST REQUEST CALLBACK", "Error");
                        e.printStackTrace();
                        intent.putExtra(AlienProfileActivity.LAST_NAME, "");
                    }
                    intent.putExtra(AlienProfileActivity.EMAIL, this.username);
                    try{
                        intent.putExtra(AlienProfileActivity.BIRTHDAY, jsonResponse.get("birthday").toString());
                    } catch (Exception e){
                        Log.e("TEST REQUEST CALLBACK", "Error");
                        e.printStackTrace();
                        intent.putExtra(AlienProfileActivity.BIRTHDAY, "");
                    }
                    try{
                        intent.putExtra(AlienProfileActivity.GENDER, jsonResponse.get("gender").toString());
                    } catch (Exception e){
                        Log.e("TEST REQUEST CALLBACK", "Error");
                        e.printStackTrace();
                        intent.putExtra(AlienProfileActivity.GENDER, "");
                    }
                    try{
                        intent.putExtra(AlienProfileActivity.AGE, jsonResponse.get("age").toString());
                    } catch (Exception e){
                        Log.e("TEST REQUEST CALLBACK", "Error");
                        e.printStackTrace();
                        intent.putExtra(AlienProfileActivity.AGE, "");
                    }
                } else if (getHTTPResponse().code() == 401){
                    // Problem obtains the profile data
                    new Handler(Looper.getMainLooper()).post(this.runner401);
                } else if (getHTTPResponse().code() == 400){
                    // The get profile fails
                    new Handler(Looper.getMainLooper()).post(this.runner400);
                }
                Log.d("Debug", "GO TO ALIEN PROFILE");
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                this.postDetailActivity.startActivity(intent);
            } catch (Exception e) {
                Log.e("TEST REQUEST CALLBACK", "Error");
                e.printStackTrace();
            }
        }
    }

    public class CallbackRequestGetAlienProfileInvitation extends HttpCallback {
        String username;
        Context activity;
        Class<AlienProfileActivity> profileActivityClass;
        Runnable runner401;
        Runnable runner400;

        public CallbackRequestGetAlienProfileInvitation(String username, Class<AlienProfileActivity> profileActivityClass, Context activity, Runnable vRunner401, Runnable vRunner400) {
            this.username = username;
            this.profileActivityClass = profileActivityClass;
            this.activity = activity;
            this.runner401 = vRunner401;
            this.runner400 = vRunner400;
        }

        @Override
        public void onResponse() {
            try {
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                JSONObject jsonResponse = getJSONResponse();
                Intent intent = new Intent(this.activity, this.profileActivityClass);
                if (getHTTPResponse().code() == 200) {
                    Log.d("RESPONSE: ", jsonResponse.toString());
                    try{
                        intent.putExtra(AlienProfileActivity.FIRST_NAME, jsonResponse.get("firstname").toString());
                    } catch (Exception e){
                        Log.e("TEST REQUEST CALLBACK", "Error");
                        e.printStackTrace();
                        intent.putExtra(AlienProfileActivity.FIRST_NAME, "");
                    }
                    try {
                        intent.putExtra(AlienProfileActivity.LAST_NAME, jsonResponse.get("lastname").toString());
                    } catch (Exception e){
                        Log.e("TEST REQUEST CALLBACK", "Error");
                        e.printStackTrace();
                        intent.putExtra(AlienProfileActivity.LAST_NAME, "");
                    }
                    intent.putExtra(AlienProfileActivity.EMAIL, this.username);
                    try{
                        intent.putExtra(AlienProfileActivity.BIRTHDAY, jsonResponse.get("birthday").toString());
                    } catch (Exception e){
                        Log.e("TEST REQUEST CALLBACK", "Error");
                        e.printStackTrace();
                        intent.putExtra(AlienProfileActivity.BIRTHDAY, "");
                    }
                    try{
                        intent.putExtra(AlienProfileActivity.GENDER, jsonResponse.get("gender").toString());
                    } catch (Exception e){
                        Log.e("TEST REQUEST CALLBACK", "Error");
                        e.printStackTrace();
                        intent.putExtra(AlienProfileActivity.GENDER, "");
                    }
                    try{
                        intent.putExtra(AlienProfileActivity.AGE, jsonResponse.get("age").toString());
                    } catch (Exception e){
                        Log.e("TEST REQUEST CALLBACK", "Error");
                        e.printStackTrace();
                        intent.putExtra(AlienProfileActivity.AGE, "");
                    }
                } else if (getHTTPResponse().code() == 401){
                    // Problem obtains the profile data
                    new Handler(Looper.getMainLooper()).post(this.runner401);
                } else if (getHTTPResponse().code() == 400){
                    // The get profile fails
                    new Handler(Looper.getMainLooper()).post(this.runner400);
                }
                Log.d("Debug", "GO TO ALIEN PROFILE");
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                this.activity.startActivity(intent);
            } catch (Exception e) {
                Log.e("TEST REQUEST CALLBACK", "Error");
                e.printStackTrace();
            }
        }
    }

    public class CallbackRequestGetNoFriendProfile extends HttpCallback {
        String username;
        Context activity;
        Class<NoFriendActivity> noFriendProfileActivityClass;
        Runnable runner401;
        Runnable runner400;

        public CallbackRequestGetNoFriendProfile(String username, Class<NoFriendActivity> noFriendProfileActivityClass, Context activity, Runnable vRunner401, Runnable vRunner400) {
            this.username = username;
            this.noFriendProfileActivityClass = noFriendProfileActivityClass;
            this.activity = activity;
            this.runner401 = vRunner401;
            this.runner400 = vRunner400;
        }

        @Override
        public void onResponse() {
            try {
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                JSONObject jsonResponse = getJSONResponse();
                Intent intent = new Intent(this.activity, this.noFriendProfileActivityClass);
                if (getHTTPResponse().code() == 200) {
                    Log.d("RESPONSE: ", jsonResponse.toString());
                    try{
                        intent.putExtra(NoFriendActivity.FIRST_NAME, jsonResponse.get("firstname").toString());
                    } catch (Exception e){
                        Log.e("TEST REQUEST CALLBACK", "Error");
                        e.printStackTrace();
                        intent.putExtra(NoFriendActivity.FIRST_NAME, "");
                    }
                    try {
                        intent.putExtra(NoFriendActivity.LAST_NAME, jsonResponse.get("lastname").toString());
                    } catch (Exception e){
                        Log.e("TEST REQUEST CALLBACK", "Error");
                        e.printStackTrace();
                        intent.putExtra(NoFriendActivity.LAST_NAME, "");
                    }
                    intent.putExtra(NoFriendActivity.EMAIL, this.username);
                    try{
                        intent.putExtra(NoFriendActivity.BIRTHDAY, jsonResponse.get("birthday").toString());
                    } catch (Exception e){
                        Log.e("TEST REQUEST CALLBACK", "Error");
                        e.printStackTrace();
                        intent.putExtra(NoFriendActivity.BIRTHDAY, "");
                    }
                    try{
                        intent.putExtra(NoFriendActivity.GENDER, jsonResponse.get("gender").toString());
                    } catch (Exception e){
                        Log.e("TEST REQUEST CALLBACK", "Error");
                        e.printStackTrace();
                        intent.putExtra(NoFriendActivity.GENDER, "");
                    }
                    try{
                        intent.putExtra(NoFriendActivity.AGE, jsonResponse.get("age").toString());
                    } catch (Exception e){
                        Log.e("TEST REQUEST CALLBACK", "Error");
                        e.printStackTrace();
                        intent.putExtra(NoFriendActivity.AGE, "");
                    }
                } else if (getHTTPResponse().code() == 401){
                    // Problem obtains the profile data
                    new Handler(Looper.getMainLooper()).post(this.runner401);
                } else if (getHTTPResponse().code() == 400){
                    // The get profile fails
                    new Handler(Looper.getMainLooper()).post(this.runner400);
                }
                Log.d("Debug", "GO TO NO FRIEND PROFILE");
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                this.activity.startActivity(intent);
            } catch (Exception e) {
                Log.e("TEST REQUEST CALLBACK", "Error");
                e.printStackTrace();
            }
        }
    }

    public class CallbackRequestPostNotification extends HttpCallback {
        @Override
        public void onResponse() {
            String TAG = "FCM - Send notification";
            try {
                Log.d(TAG, getHTTPResponse().toString());
                Log.d(TAG, getHTTPResponse().code()+"");
                JSONObject jsonResponse = getJSONResponse();
                Log.d(TAG, jsonResponse.toString());

            } catch (Exception e) {
                Log.e(TAG, "ERROR");
                e.printStackTrace();
            }
        }
    }
}