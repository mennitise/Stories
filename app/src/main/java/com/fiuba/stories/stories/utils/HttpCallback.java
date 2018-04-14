package com.fiuba.stories.stories.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class HttpCallback implements Callback {

        Activity activity;
        JSONObject jsonResponse;
        Response httpResponse;
        Call call;
        int statusCode;
        String responseBody;


        public HttpCallback () {}

        public HttpCallback (Activity a) {
            activity = a;
        }

        public JSONObject getJSONResponse() {
            return jsonResponse;
        }

        public JSONObject getJSONObject(String key) throws JSONException {
            return jsonResponse.getJSONObject(key);
        }

        public String getErrorMessage() throws JSONException {
            return jsonResponse != null ? jsonResponse.getString(JSONConstants.ERROR_MESSAGE) : "Internal error";
        }

        public String getToken() throws JSONException {
            return jsonResponse.getString(JSONConstants.TOKEN);
        }

        public Response getHTTPResponse() { return httpResponse; }

        public Call getCall() { return call; }

        public int getStatusCode() { return statusCode; }

        public Boolean statusIs(int code) { return statusCode == code; }


        @Override
        public void onFailure(Call call, IOException e) {
            Log.w("Request failure", e.getMessage());
            String message;
            if (e instanceof SocketTimeoutException) {
                message = "Connection failed. Please, check your Internet connection.";
            } else {
                message = "There was an error in the connection. Please try again later.";
            }
            e.printStackTrace();
            announceError(message);
        }

        @Override
        public final void onResponse(Call call, Response response) throws IOException {
            try {
                httpResponse = response;
                statusCode = response.code();
                this.call = call;
                responseBody = response.body().string();
                if (response.header("Content-Type") != null &&
                        response.header("Content-Type").startsWith("application/json"))
                    jsonResponse = new JSONObject(responseBody);
            } catch (JSONException e) {
                Log.e("JSON ResponseObject", e.getMessage());
                e.printStackTrace();
            }
            onResponse();
        }

        public abstract void onResponse();


        /******************************************************************************/

        protected void announceError(String message) {
            showLongToast(message);
        }

        protected void showLongToast (final String message) {
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Log.w("No activity set", "Implement activity constructor");
            }
        }
}
