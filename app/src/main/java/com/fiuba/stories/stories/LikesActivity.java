package com.fiuba.stories.stories;

import android.content.Intent;
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

public class LikesActivity extends AppCompatActivity {

    StoriesApp app;
    RecyclerView mRecyclerView;
    String storyId;

    public static final String STORY_ID = "storyId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        this.app = (StoriesApp) getApplicationContext();
        Intent intent = getIntent();
        this.storyId = intent.getStringExtra(STORY_ID);
        mRecyclerView = (RecyclerView) findViewById(R.id.likes_view);
        AppServerRequest.getStoryReactions(this.app.userLoggedIn.email, this.app.userLoggedIn.token, storyId, new LikesActivity.CallbackRequestGetReactions());
    }

    private void setReactionsContent(ArrayList<JSONObject> reactions){
        if (reactions.size() == 0){
            Toast.makeText(getBaseContext(),"Don't have Reactions.", Toast.LENGTH_LONG).show();
        } else {
            RecyclerView container = (RecyclerView) findViewById(R.id.likes_view);
            container.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.app);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);
            container.setAdapter(new LikesAdaptor(reactions, this.app.userLoggedIn));
            container.setLayoutManager(layoutManager);
        }
    }

    public class CallbackRequestGetReactions extends HttpCallback {
        ArrayList<JSONObject> reactions;

        @Override
        public void onResponse() {
            try{
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                JSONObject jsonResponse = getJSONResponse();
                if (getHTTPResponse().code() == 200) {
                    Log.d("RESPONSE: ", jsonResponse.toString());
                    JSONArray reactionsJson = (JSONArray) jsonResponse.get("reactions"); // Array of comments
                    reactions = new ArrayList<>();
                    for(int i = 0; i < reactionsJson.length(); ++i){
                        reactions.add(reactionsJson.getJSONObject(i));
                    }
                    LikesActivity.this.runOnUiThread(new LikesActivity.CallbackRequestGetReactions.SetResults());
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
                setReactionsContent(reactions);
            }
        }
    }
}
