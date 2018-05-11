package com.fiuba.stories.stories.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.fiuba.stories.stories.Login;
import com.fiuba.stories.stories.MainActivity;
import com.fiuba.stories.stories.RegisterActivity;
import com.fiuba.stories.stories.StoriesApp;
import com.fiuba.stories.stories.User;

import org.json.JSONObject;

public class UtilCallbacks{


    public CallbackRequestLogin getCallbackRequestLogin(String vUsername, StoriesApp app, Login loginActivity, Class<MainActivity> mainActivityClass, Runnable vRunner200, Runnable vRunner401, Runnable vRunner400){
        Log.d("DEbUG", "new CallbackRequestLogin");
        return new CallbackRequestLogin(vUsername, app, loginActivity, mainActivityClass, vRunner200, vRunner401, vRunner400);
    }

    private void goToScreen(Activity context, Class activityClass){
        Intent intent = new Intent(context, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void getToastHandler(Context context, String message){
         Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static class CallbackRequestRegister extends HttpCallback {

        RegisterActivity registerActivity;
        Class<MainActivity> mainActivityClass;
        public CallbackRequestRegister(RegisterActivity registerActivity, Class<MainActivity> mainActivityClass) {
            this.registerActivity = registerActivity;
            this.mainActivityClass = mainActivityClass;
        }

        @Override
        public void onResponse() {
            try {
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                Log.d("HTTP RESPONSE: ", "code = " + getHTTPResponse().code());
                if (getHTTPResponse().code() == 200) {
                    Log.d("RESPONSE: ", getJSONResponse().toString());
                    Intent intent = new Intent(this.registerActivity, this.mainActivityClass);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.registerActivity.startActivity(intent);
                } else if (getHTTPResponse().code() == 401){
                    // The user is already registered
                    //getToastHandler(this.registerActivity,"The user is already registered.");
                } else if (getHTTPResponse().code() == 400){
                    // The registration fails
                    //getToastHandler(this.registerActivity,"The registration has failed.");
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

                    // FIXME: Move this goMainScreen to CallBackRequestGetProfile
                    //goMainScreen();
                } else if (getHTTPResponse().code() == 401){
                    // The user is already registered
                    new Handler(Looper.getMainLooper()).post(this.runner401);

                } else if (getHTTPResponse().code() == 400){
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
                    /*
                    if(jsonResponse.get("email") != null){
                        // Not necessary, set in the previous api callback
                        // app.userLoggedIn.setEmail(jsonResponse.get("email").toString());
                    }
                    */
                    if (jsonResponse.get("birthday") != null){
                        this.app.userLoggedIn.setBirthday(jsonResponse.get("birthday").toString());
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

}