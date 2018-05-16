package com.fiuba.stories.stories;

import android.support.annotation.NonNull;
import android.util.Log;

import com.fiuba.stories.stories.utils.AppServerRequest;
import com.fiuba.stories.stories.utils.ResponseObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class User {
    @SerializedName("firstName") protected String firstName;
    @SerializedName("lastName") protected String lastName;
    @SerializedName("email")protected String email;
    @SerializedName("birthday")protected String birthday;
    @SerializedName("gender")protected String gender;
    @SerializedName("age")protected String age;
    protected Boolean fbUser;
    protected String token;
    protected String fbToken;
    protected String urlProfilePicture;

    public User(String firstName, String lastName, String email, String urlProfilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.urlProfilePicture = urlProfilePicture;
    }

    public User(){

    }

    public static User hydrate(JsonObject json) {
        return new Gson().fromJson(json, User.class);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setGender(String gender){
        this.gender = gender;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName(){
        if (firstName == null && lastName == null){
            return null;
        } else if (firstName == null){
            return lastName;
        } else if (lastName == null){
            return firstName;
        }
        return firstName+" "+lastName;
    }

    public String getEmail(){
        return email;
    }

    public void setCurrentToken(String token, Boolean fbUser){
        if (fbUser) {
            this.fbToken = token;
        } else {
            this.token = token;
        }
    }

    // FOR DEBUGGING
    public void LOG_USER(){
        Log.d("USER: ", "FIRST NAME: " + this.firstName + "\n"
                                + "LAST NAME: " + this.lastName + "\n"
                                + "EMAIL: " + this.email + "\n"
                                + "BIRTHDAY: " + this.birthday + "\n"
                                + "GENDER: " + this.gender + "\n"
                                + "TOKEN: " + this.token + "\n"
                                + "FB_TOKEN: " + this.fbToken + "\n"
                                + "FB_USER: " + this.fbUser);
    }
}
