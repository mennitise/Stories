package com.fiuba.stories.stories;

public class User {
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String urlProfilePicture;

    public User(String firstName, String lastName, String email, String urlProfilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.urlProfilePicture = urlProfilePicture;
    }
}
