package com.fiuba.stories.stories;

import java.util.ArrayList;

public class Post {

    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;

    String id;
    String title;
    String description;
    int imagePost;
    int type = TYPE_IMAGE;
    User ownerUser;
    int privacity;
    String urlImage;
    double latitude;
    double longitude;

    public int getImagePost() {
        return imagePost;
    }

    public void setImagePost(int imagePost) {
        this.imagePost = imagePost;
    }

    public Post(String id, String title, String description, int imagePost, User ownerUser, int privacity, String urlImage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imagePost = imagePost;
        this.ownerUser = ownerUser;
        this.privacity = privacity;
        this.urlImage = urlImage;
    }

    public Post(String id, String title, String description, int imagePost, User ownerUser, int privacity, String urlImage, int type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imagePost = imagePost;
        this.ownerUser = ownerUser;
        this.privacity = privacity;
        this.urlImage = urlImage;
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Post(String id, String title, String description, int imagePost, int type, User ownerUser, int privacity, String urlImage, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imagePost = imagePost;
        this.type = type;
        this.ownerUser = ownerUser;
        this.privacity = privacity;
        this.urlImage = urlImage;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static final int privacity_public = 0;
    public static final int privacity_private = 1;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public String getUrlImage() {
        return this.urlImage;
    }

    public void setType(int type) {
        this.type = type;
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
            posts.add(new Post(String.valueOf(i),
                    "Title "+i,
                    "Description "+i,
                    R.drawable.stories_no_image,
                    user,
                    Post.privacity_public,
                    "")
            );
        }
        return posts;
    }
}
