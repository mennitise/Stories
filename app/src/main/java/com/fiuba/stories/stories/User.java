package com.fiuba.stories.stories;

import com.fiuba.stories.stories.utils.AppServerRequest;
import com.fiuba.stories.stories.utils.ResponseObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class User {
    @SerializedName("firstName") protected String firstName;
    @SerializedName("lastName") protected String lastName;
    @SerializedName("email")protected String email;
    @SerializedName("birthday")protected String birthday;
    protected String token;
    protected String fbToken;
    protected String urlProfilePicture;

    public User(String firstName, String lastName, String email, String urlProfilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.urlProfilePicture = urlProfilePicture;
    }

    public static User hydrate(JsonObject json) {
        return new Gson().fromJson(json, User.class);
    }

    public static void loginUser(String email, String password, Callback callback){
        String json = "{\"username\": \"" + email + "\", \"password\": \"" + password + "\"}";
        RequestBody request = RequestBody.create(AppServerRequest.JSON, json);
        AppServerRequest.post("https://radiant-gorge-17084.herokuapp.com/api/users/login", callback, request);
    }

    public static void registerUser(String firstName, String lastName, String email, String birthday, String password, Callback callback) {
        String json = "{\"username\": \"" + email + "\", \"password\": \"" + password + "\"}";
        RequestBody request = RequestBody.create(AppServerRequest.JSON, json);
        AppServerRequest.post("https://radiant-gorge-17084.herokuapp.com/api/users/signup", callback, request);
    }

    public static void registerFacebookUser(String firstName, String lastName, String email, String birthday, String fbToken, Callback callback) {
        String json = "{\"username\": \"" + email + "\", \"fbToken\": \"" + fbToken + "\"}";
        RequestBody request = RequestBody.create(AppServerRequest.JSON, json);
        AppServerRequest.post("https://radiant-gorge-17084.herokuapp.com/api/users/signup", callback, request);
    }

    public String getName(){
        return firstName+" "+lastName;
    }
    public String getEmail(){
        return email;
    }
}
