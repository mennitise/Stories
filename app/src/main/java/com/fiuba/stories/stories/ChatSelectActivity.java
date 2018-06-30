package com.fiuba.stories.stories;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.fiuba.stories.stories.utils.AppServerRequest;
import com.fiuba.stories.stories.utils.HttpCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatSelectActivity extends AppCompatActivity {

    StoriesApp app;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_select);
        this.app = (StoriesApp) getApplicationContext();
        mRecyclerView = (RecyclerView) findViewById(R.id.friends_view);

        AppServerRequest.getFriends(this.app.userLoggedIn.email, this.app.userLoggedIn.token, new ChatSelectActivity.CallbackRequestGetFriends());
    }

    private void setFriendsContent(ArrayList<String> users){
        if (users.size() == 0){
            Toast.makeText(getBaseContext(),"Don't have friends to chat.", Toast.LENGTH_LONG).show();
        } else {
            RecyclerView container = (RecyclerView) findViewById(R.id.friends_view);
            container.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.app);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);
            container.setAdapter(new ChatAdaptor(users, this.app.userLoggedIn));
            container.setLayoutManager(layoutManager);
        }
    }

    public class CallbackRequestGetFriends extends HttpCallback {
        ArrayList<String> users;

        @Override
        public void onResponse() {
            try{
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                JSONObject jsonResponse = getJSONResponse();
                if (getHTTPResponse().code() == 200) {
                    Log.d("RESPONSE: ", jsonResponse.toString());
                    JSONArray usersJson = (JSONArray) jsonResponse.get("friends"); // Array of friends
                    users = new ArrayList<>();
                    for(int i = 0; i < usersJson.length(); ++i){
                        users.add(usersJson.getString(i));
                    }
                    ChatSelectActivity.this.runOnUiThread(new ChatSelectActivity.CallbackRequestGetFriends.SetResults());
                /*
                } else if (getHTTPResponse().code() == 401){
                    Toast.makeText(getBaseContext(), "ERROR 401", Toast.LENGTH_LONG).show();
                } else if (getHTTPResponse().code() == 400){
                    Toast.makeText(getBaseContext(), "ERROR 400", Toast.LENGTH_LONG).show();
                */
                }

            } catch (Exception e) {
                Log.e("TEST REQUEST CALLBACK", "Error");
                e.printStackTrace();
            }
        }

        class SetResults implements Runnable{
            @Override
            public void run(){
                setFriendsContent(users);
            }
        }
    }
}
