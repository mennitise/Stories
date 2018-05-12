package com.fiuba.stories.stories;

import android.app.Application;

import java.util.ArrayList;

public class Post {
    String title;
    String description;
    int imagePost;
    User ownerUser;
    int privacity;

    public Post(String title, String description, int imagePost, User ownerUser, int privacity) {
        this.title = title;
        this.description = description;
        this.imagePost = imagePost;
        this.ownerUser = ownerUser;
        this.privacity = privacity;
    }

    public static final int privacity_public = 0;
    public static final int privacity_private = 1;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImagePost() {
        return imagePost;
    }

    public void setImagePost(int imagePost) {
        this.imagePost = imagePost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrivacity() {
        return privacity;
    }

    public void setPrivacity(int privacity) {
        this.privacity = privacity;
    }

    public User getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(User ownerUser) {
        this.ownerUser = ownerUser;
    }

    public static ArrayList<Post> getHistoryPosts(User currentUser) {
        ArrayList<Post> posts = new ArrayList<Post>();

        User user = new User("Sebas", "Menniti", "mennitise@gmail.com", "image");
        for (int i = 1; i < 6; i++) {
            posts.add(new Post("Title "+i,
                    "Description "+i,
                    R.drawable.stories_no_image,
                    user,
                    Post.privacity_public));
        }
        return posts;
    }

    public static ArrayList<Post> getOwnHistoryPosts(User currentUser) {
        ArrayList<Post> posts = new ArrayList<Post>();

        User user = new User("Sebas", "Menniti", "mennitise@gmail.com", "image");
        for (int i = 1; i < 10; i++) {
            posts.add(new Post("Profile post "+i,
                    "Profile Description "+i,
                    R.drawable.stories_splash,
                    user,
                    Post.privacity_public));
        }
        return posts;
    }
}