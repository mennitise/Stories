package com.fiuba.stories.stories.utils;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.fiuba.stories.stories.AlienProfileActivity;
import com.fiuba.stories.stories.Login;
import com.fiuba.stories.stories.MainActivity;
import com.fiuba.stories.stories.PostDetailActivity;
import com.fiuba.stories.stories.RegisterActivity;
import com.fiuba.stories.stories.StoriesApp;
import com.fiuba.stories.stories.User;
import com.google.gson.JsonObject;

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

    public CallbackRequestGetAlienProfile getCallbackRequestGetAlienProfile(StoriesApp app, String username, Class<AlienProfileActivity> profileActivityClass, PostDetailActivity postDetailActivity, Runnable vRunner401, Runnable vRunner400){
        return new CallbackRequestGetAlienProfile(app, username, profileActivityClass, postDetailActivity, vRunner401, vRunner400);
    }

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
                    Log.d("RESPONSE: ", jsonResponse.toString());
                    Log.d("USER",jsonResponse.get("firstname").toString());
                    Log.d("USER",jsonResponse.get("lastname").toString());
                    Log.d("USER",jsonResponse.get("age").toString());
                    Log.d("USER",jsonResponse.get("gender").toString());
                    if (jsonResponse.get("lastname") != null){
                        this.app.userLoggedIn.setLastName(jsonResponse.get("lastname").toString());
                    }
                    if(jsonResponse.get("firstname") != null) {
                        this.app.userLoggedIn.setFirstName(jsonResponse.get("firstname").toString());
                    }
                    this.app.userLoggedIn.setEmail(this.app.userLoggedIn.getEmail());
                    if (jsonResponse.get("birthday") != null){
                        this.app.userLoggedIn.setBirthday(jsonResponse.get("birthday").toString());
                    }
                    if (jsonResponse.get("gender") != null){
                        this.app.userLoggedIn.setGender(jsonResponse.get("gender").toString());
                    }
                    if (jsonResponse.get("age") != null){
                        this.app.userLoggedIn.setAge(jsonResponse.get("age").toString());
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
        StoriesApp app;
        PostDetailActivity postDetailActivity;
        Class<AlienProfileActivity> profileActivityClass;
        Runnable runner401;
        Runnable runner400;

        public CallbackRequestGetAlienProfile(StoriesApp app, String username, Class<AlienProfileActivity> profileActivityClass, PostDetailActivity postDetailActivity, Runnable vRunner401, Runnable vRunner400) {
            this.app = app;
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

}