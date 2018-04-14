package com.fiuba.stories.stories;

import com.fiuba.stories.stories.utils.ResponseObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class User {
    @SerializedName("firstName") protected String firstName;
    @SerializedName("lastName") protected String lastName;
    @SerializedName("email")protected String email;
    @SerializedName("birthday")protected String birthday;
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

    public static void registerUser(String firstName, String lastName, String email, String birthday, String password) {

    }

    public String getName(){
        return firstName+" "+lastName;
    }
    public String getEmail(){
        return email;
    }
}
