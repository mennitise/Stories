package com.fiuba.stories.stories.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AppServerRequest {

    private static final String BASE_URL = "https://radiant-gorge-17084.herokuapp.com";
    private static final String USER_LOGIN = "/api/users/login";
    private static final String USER_SIGNUP = "/api/users/signup";
    private static final String PROFILE_INFO = "/api/profile";
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
            json.put("firstname",firstName);
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

    public static void putProfileInformation(String firstName, String lastName, String email, String birthday, String age, String gender, String token, Callback callback){
        String credential = Credentials.basic(email, token);
        String json =   "{\"username\": \"" + email + "\"," +
                " \"firstname\": \"" + firstName + "\"," +
                " \"lastname\": \"" + lastName + "\"," +
                " \"gender\": \"" + gender + "\"," +
                " \"age\": \"" + age + "\"," +
                " \"birthday\": \"" + birthday + "\"" +
                "}";
        RequestBody request = RequestBody.create(AppServerRequest.JSON, json);
        AppServerRequest.putWithAuth(BASE_URL + PROFILE_INFO, credential, email, callback, request);
    }

    public static void getProfileInformation(String username, String token, Callback callback){
        String credential = Credentials.basic(username, token);
        Log.d("CREDENTIAL", credential);
        AppServerRequest.getWithAuth(BASE_URL + PROFILE_INFO, credential, username, callback);
    }

    //----------------------------------------------------------------------------------------------
    /*
    public static void sendTestRequest(Map<String,String> params, Callback callback) {
        String route = BASE_URL + "get?";
        for (Map.Entry param : params.entrySet()) {
            route += String.format("%s=%s&", param.getKey(), param.getValue());
        }
        get(route, callback);
    }

    public static void sendTestRequest(String params, Callback callback) {
        String route = BASE_URL + "get?" + params;

        Log.e("REQUEST URL:", route);

        get(route, callback);
    }

    public static void sendSignUpUser(String params, Callback callback, RequestBody request) {
        String route = BASE_URL + "/api/users/signup";
        Log.d("REQUEST URL: ", route);
        Log.d("REQUEST: ", request.toString());
        put(route, callback, request);
    }
    */
    //----------------------------------------------------------------------------------------------

    public static void post(String url, Callback callback, RequestBody body) {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
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
                .header("Autorization", credential)
                .header("username", username)
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
