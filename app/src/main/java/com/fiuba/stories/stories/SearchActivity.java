package com.fiuba.stories.stories;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.KeyEvent;

import android.view.inputmethod.EditorInfo;

import com.fiuba.stories.stories.utils.AppServerRequest;
import com.fiuba.stories.stories.utils.HttpCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private StoriesApp app;
    private EditText searchText;
    private ImageButton searchSend;
    private RecyclerView recyclerView;
    private ArrayList<String> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.app = (StoriesApp) getApplicationContext();
        this.searchText = findViewById(R.id.search_text);
        this.searchSend = findViewById(R.id.search_send);
        searchSend.setVisibility(View.INVISIBLE);
        this.recyclerView = findViewById(R.id.search_view);

        AppServerRequest.getFriends(this.app.userLoggedIn.getEmail(), this.app.userLoggedIn.token, new SearchActivity.CallbackRequestGetFriends());

        this.searchSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                searchText.getText().toString();
            }
        });

        this.searchSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(searchText.getText().toString())) {
                    searchText.setError(getString(R.string.error_field_required));
                } else {
                    AppServerRequest.getPeolpleSearch(app.userLoggedIn.getEmail(), app.userLoggedIn.token, searchText.getText().toString(), new SearchActivity.CallbackRequestGetPeople());
                }
            }
        });

        TextView.OnEditorActionListener exampleListener = new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (TextUtils.isEmpty(searchText.getText().toString())) {
                        searchText.setError(getString(R.string.error_field_required));
                    } else {
                        AppServerRequest.getPeolpleSearch(app.userLoggedIn.getEmail(), app.userLoggedIn.token, searchText.getText().toString(), new SearchActivity.CallbackRequestGetPeople());
                    }
                }
                return true;
            }
        };

        this.searchText.setOnEditorActionListener(exampleListener);
    }

    private void setSearchContent(ArrayList<JSONObject> users){
        if (users.size() == 0){
            Toast.makeText(getBaseContext(),"No results for your criteria.", Toast.LENGTH_LONG).show();
        } else {
            RecyclerView container = (RecyclerView) findViewById(R.id.search_view);
            container.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.app);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);
            container.setAdapter(new SearchAdaptor(users, this.app.userLoggedIn));
            container.setLayoutManager(layoutManager);
        }
    }

    private Boolean isFriend(String username){
        return friends.contains(username);
    }

    public class CallbackRequestGetPeople extends HttpCallback {
        ArrayList<JSONObject> users;

        @Override
        public void onResponse() {
            try{
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                JSONObject jsonResponse = getJSONResponse();
                if (getHTTPResponse().code() == 200) {
                    Log.d("RESPONSE: ", jsonResponse.toString());
                    JSONArray usersJson = (JSONArray) jsonResponse.get("people"); // Array of people
                    users = new ArrayList<>();
                    for(int i = 0; i < usersJson.length(); ++i){
                        usersJson.getJSONObject(i).put("isFriend", isFriend(usersJson.getJSONObject(i).getString("username")));
                        users.add(usersJson.getJSONObject(i));
                    }
                    Log.d("SEARCH",users.toString());
                    SearchActivity.this.runOnUiThread(new SearchActivity.CallbackRequestGetPeople.SetResults());
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
                setSearchContent(users);
            }
        }
    }

    public class CallbackRequestGetFriends extends HttpCallback {
        ArrayList<String> getFriends;

        @Override
        public void onResponse() {
            try{
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                JSONObject jsonResponse = getJSONResponse();
                if (getHTTPResponse().code() == 200) {
                    Log.d("RESPONSE: ", jsonResponse.toString());
                    JSONArray usersJson = (JSONArray) jsonResponse.get("friends"); // Array of friends
                    getFriends = new ArrayList<>();
                    for(int i = 0; i < usersJson.length(); ++i){
                        getFriends.add(usersJson.getString(i));
                    }
                    SearchActivity.this.runOnUiThread(new SearchActivity.CallbackRequestGetFriends.SetResults());
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
                friends = getFriends;
                searchSend.setVisibility(View.VISIBLE);
            }
        }
    }
}
