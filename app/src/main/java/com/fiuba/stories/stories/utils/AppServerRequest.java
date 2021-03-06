package com.fiuba.stories.stories.utils;

import android.util.Log;
import com.fiuba.stories.stories.Post;
import com.fiuba.stories.stories.PostDetailActivity;
import com.fiuba.stories.stories.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.sql.Timestamp;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AppServerRequest {

    private static final String BASE_URL = "https://radiant-gorge-17084.herokuapp.com";
    private static final String PING = "/api/ping";
    private static final String USER_LOGIN = "/api/users/login";
    private static final String USER_SIGNUP = "/api/users/signup";
    private static final String PROFILE_INFO = "/api/profile";
    private static final String STORIES = "/api/stories";
    private static final String FLASHSTORIES = "/api/flashstories";
    private static final String GEOLOCATION = "/api/geolocation/stories";
    private static final String INVITATIONS = "/api/invitations";
    private static final String FRIENDS = "/api/friends";
    private static final String COMMENTS = "/api/comments";
    private static final String REACTIONS = "/api/reactions";
    private static final String SEARCH = "/api/people";

    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";

    private static final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json");

    //----------------------------------------------------------------------------------------------

    public static void loginUser(String email, String password, Callback callback){
        Log.d("debug","");
        JSONObject json = new JSONObject();
        try {
            json.put("username", email);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JSON", json.toString());

        RequestBody request = RequestBody.create(AppServerRequest.JSON, json.toString());
        AppServerRequest.post(BASE_URL + USER_LOGIN, callback, request);
    }

    public static void registerUser(String firstName, String lastName, String email, String birthday, String age, String gender, String password, Callback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("username", email);
            json.put("password", password);
            json.put("fisrtname",firstName);
            json.put("lastname",lastName);
            json.put("gender",gender);
            json.put("age",age);
            json.put("birthday",birthday);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JSON", json.toString());

        RequestBody request = RequestBody.create(AppServerRequest.JSON, json.toString());
        AppServerRequest.post(BASE_URL + USER_SIGNUP, callback, request);
    }

    public static void registerFacebookUser(String firstName, String lastName, String email, String birthday, String age, String gender, String fbToken, Callback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("username", email);
            json.put("fbToken", fbToken);
            json.put("fisrtname",firstName);
            json.put("lastname",lastName);
            json.put("gender",gender);
            json.put("age",age);
            json.put("birthday",birthday);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JSON", json.toString());

        RequestBody request = RequestBody.create(AppServerRequest.JSON, json.toString());
        AppServerRequest.post(BASE_URL + USER_SIGNUP, callback, request);
    }

    public static void putProfileInformation(String firstName, String lastName, String email, String birthday, String age, String gender, String profilePic, String token, Callback callback){
        String credential = Credentials.basic(email, token);
        String json =   "{\"username\": \"" + email + "\"," +
                " \"fisrtname\": \"" + firstName + "\"," +
                " \"lastname\": \"" + lastName + "\"," +
                " \"gender\": \"" + gender + "\"," +
                " \"age\": \"" + age + "\"," +
                " \"birthday\": \"" + birthday + "\"," +
                " \"profilePic\": \"" + profilePic + "\"" +
                "}";
        RequestBody request = RequestBody.create(AppServerRequest.JSON, json);
        AppServerRequest.putWithAuth(BASE_URL + PROFILE_INFO, credential, email, callback, request);
    }

    public static void getProfileInformation(String username, String token, Callback callback){
        String credential = Credentials.basic(username, token);
        Log.d("CREDENTIAL", credential);
        AppServerRequest.getWithAuth(BASE_URL + PROFILE_INFO, credential, username, callback);
    }

    public static void getAlienProfileInformation(String alienUsername, String username, String token, Callback callback){
        String credential = Credentials.basic(username, token);
        AppServerRequest.getWithAuth(BASE_URL + PROFILE_INFO, credential, alienUsername, callback);
    }

    public static void getFeedStories(String username, String token, Callback callback){
        String credential = Credentials.basic(username, token);
        AppServerRequest.getWithAuth(BASE_URL + STORIES, credential, username, callback);
    }

    public static void getGeoStories(String username, String token, Callback callback){
        String credential = Credentials.basic(username, token);
        AppServerRequest.getWithAuth(BASE_URL + GEOLOCATION, credential, username, callback);
    }

    public static void postStory(String username, String token, Post story, Callback callback){
        JSONObject json = new JSONObject();
        try {
            json.put("title", story.getTitle());
            json.put("description", story.getDescription());
            json.put("username", username);
            json.put("state", "Public");
            json.put("type", story.getType());
            json.put("url", story.getUrlImage());
            json.put("lat",story.getLatitude());
            json.put("long",story.getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JSON", json.toString());

        String credential = Credentials.basic(username,token);

        RequestBody request = RequestBody.create(AppServerRequest.JSON, json.toString());
        AppServerRequest.postWithAuth(BASE_URL + STORIES, credential, username, callback, request);
    }

    public static void getUserStory(String username, String usernamelog, String token, Callback callback){
        String credential = Credentials.basic(usernamelog, token);
        AppServerRequest.getWithAuth(BASE_URL + STORIES + "/" + username, credential, username, callback);
    }

    public static void postFlashStory(String username, String token, Post story, Callback callback){
        JSONObject json = new JSONObject();
        try {
            json.put("description", story.getDescription());
            json.put("username", username);
            json.put("state", "Public");
            json.put("url", story.getUrlImage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JSON", json.toString());

        String credential = Credentials.basic(username,token);

        RequestBody request = RequestBody.create(AppServerRequest.JSON, json.toString());
        AppServerRequest.postWithAuth(BASE_URL + FLASHSTORIES, credential, username, callback, request);
    }

    public static void putComment(String username, String token, String storyId, String comment, Callback callback){
        JSONObject json = new JSONObject();
        try {
            json.put("comment", comment);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JSON", json.toString());
        String credential = Credentials.basic(username,token);
        RequestBody request = RequestBody.create(AppServerRequest.JSON, json.toString());
        AppServerRequest.putWithAuthAndStoryHeader(BASE_URL + COMMENTS, credential, username, storyId, callback, request);
    }

    public static void getComments(String username, String token, String storyId, Callback callback){
        String credential = Credentials.basic(username, token);
        AppServerRequest.getWithAuthAndStoryHeader(BASE_URL + COMMENTS, credential, username, storyId, callback);
    }

    public static void getFlashStory(String username, String token, Callback callback){
        String credential = Credentials.basic(username, token);
        AppServerRequest.getWithAuth(BASE_URL + FLASHSTORIES, credential, username, callback);
    }

    public static void getAlienStories(String alienUsername, String username, String token, Callback callback){
        String credential = Credentials.basic(username, token);
        AppServerRequest.getWithAuth(BASE_URL + STORIES + "/" + alienUsername, credential, alienUsername, callback);
    }

    public static void postFriendInvitation(String username, String token, String friend, Callback callback){
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("friend", friend);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JSON", json.toString());
        String credential = Credentials.basic(username,token);
        RequestBody request = RequestBody.create(AppServerRequest.JSON, json.toString());
        AppServerRequest.postWithAuth(BASE_URL + INVITATIONS, credential, username, callback, request);
    }

    public static void getFriendInvitations(String username, String token, Callback callback){
        String credential = Credentials.basic(username, token);
        AppServerRequest.getWithAuth(BASE_URL + INVITATIONS, credential, username, callback);
    }

    public static void putAceptInvitation(String username, String token, String friend, Callback callback){
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("friend", friend);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JSON", json.toString());
        String credential = Credentials.basic(username,token);
        RequestBody request = RequestBody.create(AppServerRequest.JSON, json.toString());
        AppServerRequest.putWithAuth(BASE_URL + INVITATIONS, credential, username, callback, request);
    }

    public static void getFriends(String username, String token, Callback callback){
        String credential = Credentials.basic(username, token);
        AppServerRequest.getWithAuth(BASE_URL + FRIENDS, credential, username, callback);
    }

    public static void getPeolpleSearch(String username, String token, String searching, Callback callback){
        String credential = Credentials.basic(username, token);
        AppServerRequest.getWithAuthAndSearchHeader(BASE_URL + SEARCH, credential, username, searching, callback);
    }

    public static void getStoryReactions(String username, String token, String storyId, Callback callback){
        String credential = Credentials.basic(username, token);
        AppServerRequest.getWithAuthAndStoryHeader(BASE_URL + REACTIONS, credential, username, storyId, callback);
    }

    public static void putStoryReaction(String username, String token, String storyId, String reaction, Callback callback) {
        String credential = Credentials.basic(username, token);
        RequestBody request = RequestBody.create(AppServerRequest.JSON, "");
        AppServerRequest.putWithAuthAndStoryHeaderAndReaction(BASE_URL + REACTIONS, credential, username, storyId, reaction, callback, request);
    }

    public static void upServer(Callback callback){
        AppServerRequest.get(BASE_URL + PING,callback);
    }

    //----------------------------------------------------------------------------------------------

    public static void postMessageToTopic(String title, String description, String topic, Callback callback){
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        try{
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            data.put("title",title);
            data.put("body",description);
            data.put("timestamp",timestamp.getTime());
            json.put("to", "/topics/"+topic);
            json.put("notification", data);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        Log.d("JSON", json.toString());

        RequestBody body = RequestBody.create(AppServerRequest.JSON, json.toString());

        Request request = new Request.Builder()
                .url(FCM_URL)
                .post(body)
                .header("Authorization", "key=AIzaSyBLUmgTz8Rw6RICWZDGTKMbiSwwjyjIXsM")
                .header("Content-Type","application/json")
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    //----------------------------------------------------------------------------------------------

    public static void post(String url, Callback callback, RequestBody body) {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void postWithAuth(String url, String credential, String username, Callback callback, RequestBody body) {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", credential)
                .header("username", username)
                .header("Content-Type","application/json")
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void put(String url, Callback callback, RequestBody body) {
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void putWithAuth(String url, String credential, String username, Callback callback, RequestBody body) {
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .header("Authorization", credential)
                .header("username", username)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void putWithAuthAndStoryHeader(String url, String credential, String username, String storyId, Callback callback, RequestBody body) {
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .header("Authorization", credential)
                .header("username", username)
                .header("story-id", storyId)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void putWithAuthAndStoryHeaderAndReaction(String url, String credential, String username, String storyId, String reaction, Callback callback, RequestBody body) {
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .header("Authorization", credential)
                .header("username", username)
                .header("story-id", storyId)
                .header("reaction", reaction)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void getWithAuthAndSearchHeader(String url, String credential, String username, String searching, Callback callback){
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Authorization", credential)
                .header("username", username)
                .header("searchedFor", searching)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void get(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void getWithAuth(String url, String credential, String username, Callback callback){
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Authorization", credential)
                .header("username", username)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void getWithAuthAndStoryHeader(String url, String credential, String username, String storyID, Callback callback){
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Authorization", credential)
                .header("username", username)
                .header("story-id", storyID)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    //----------------------------------------------------------------------------------------------

    private static String generateURL(Object... uris) {
        String url = BASE_URL;
        for (Object uri : uris)
            url += String.format("/%s", uri);
        return url;
    }

    private static String addParameters(String route, Object... params) {
        route += "?";
        for (int i = 0; i < params.length; i += 2)
            route += String.format("%s=%s", params[i], params[i + 1]);
        return route;
    }

    private static JSONObject generateJSON(Object... attributes) {
        JSONObject json = new JSONObject();
        try {
            for (int i = 0; i < attributes.length; i += 2)
                json.put(attributes[i].toString(), attributes[i+1]);
        } catch (JSONException e) {
            Log.e("JSON build", e.getMessage());
            e.printStackTrace();
        }
        return json;
    }

    private static class RequestConstants {

        class Routes {
            final static String LOGIN = "session";
            final static String USERS = "users";
            final static String CONTACTS = "contacts";
            final static String LOCATION = "location";
            final static String SKILLS = "skills";
            final static String JOB_POSITIONS = "job_positions";
        }
        class UserParams {

            final static String EMAIL = "email";
            final static String PASSWORD = "password";
            final static String FB_TOKEN = "token";
        }
    }
}
