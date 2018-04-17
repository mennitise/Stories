package com.fiuba.stories.stories.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AppServerRequest {

    private static final String BASE_URL = "https://radiant-gorge-17084.herokuapp.com";
    private static final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json");

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

    public static void get(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

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

    /*******************************************************************************/

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
