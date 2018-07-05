package com.fiuba.stories.stories;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.fiuba.stories.stories.utils.AppServerRequest;
import com.fiuba.stories.stories.utils.HttpCallback;
import com.fiuba.stories.stories.utils.MyFirebaseCloudMessagingConfigure;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    StoriesApp app;
    TextView nav_header_name;
    TextView nav_header_email;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyFirebaseCloudMessagingConfigure FCMConfigure;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.app = (StoriesApp) getApplicationContext();
        this.FCMConfigure = new MyFirebaseCloudMessagingConfigure(this.app);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        if (this.app.userLoggedIn == null) {
            goLoginScreen();
        } else {
            if (this.app.userLoggedIn.getEmail() != null){
                this.FCMConfigure.suscribeTopics();
            }
        }

        // Get token
        this.app.FCMtoken = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM TOKEN", this.app.FCMtoken);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Log.e(getClass().getSimpleName(), "refresh");
                setNowMainContent();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Create a story", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                goToCreatePostScreen();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        findViewById(R.id.action_search);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(this.app.userLoggedIn != null){ setNowMainContent(); }
    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void goToProfileScreen() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(intent);
    }

    private void goToFlashStoriesScreen(){
        Intent intent = new Intent(this, StoriesFlashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(intent);
    }

    private void goToCreatePostScreen(){
        Intent intent = new Intent(this, CreateStoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(intent);
    }

    private void goToInvitations(){
        Intent intent = new Intent(this, InvitationsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(intent);
    }

    private void goToMapActivitiesScreen(){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(intent);
    }

    public void goToSearchScreen(){
        Intent intent = new Intent(this, SearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(intent);
    }

    public void goToChatScreen(){
        Intent intent = new Intent(this, ChatSelectActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(intent);
    }

    public void logout() {
        this.FCMConfigure.unsuscribeTopics();
        ((StoriesApp) getApplicationContext()).userLoggedIn = null;

        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
        goLoginScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNowMainContent();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            goToSearchScreen();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_now) {
            setNowMainContent();
        } else if (id == R.id.nav_profile) {
            goToProfileScreen();
        } else if (id == R.id.nav_map_activity) {
            goToMapActivitiesScreen();
        } else if (id == R.id.nav_chat) {
            goToChatScreen();
        } else if (id == R.id.nav_invitations){
            goToInvitations();
        } else if (id == R.id.nav_flash_stories) {
            goToFlashStoriesScreen();
        } else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setNowMainContent(){
        AppServerRequest.getFeedStories(this.app.userLoggedIn.email, this.app.userLoggedIn.token, new MainActivity.CallbackRequestGetFeedStories());
    }

    public class CallbackRequestGetFeedStories extends HttpCallback {
        ArrayList<Post> posts;

        @Override
        public void onResponse() {
            try{
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                JSONObject jsonResponse = getJSONResponse();

                if (getHTTPResponse().code() == 200) {
                    Log.d("RESPONSE: ", jsonResponse.toString());
                    posts = new ArrayList<Post>();
                    JSONArray stories = (JSONArray) jsonResponse.get("feedStories"); // Array of posts
                    for(int i = 0; i < stories.length(); ++i){
                        JSONObject story = stories.getJSONObject(i);
                        String username = story.getString("username");
                        User ownerUser = new User();
                        ownerUser.setEmail(username);
                        JSONObject storyDetails = story.getJSONObject("storyDetail");
                        String id, title, description, url, state;
                        int type;
                        try {
                            id = story.getString("_id");
                        }catch (JSONException e){
                            id = "0";
                        }
                        try {
                            title = storyDetails.getString("title");
                        }catch (JSONException e){
                            title = "title";
                        }
                        try {
                            description = storyDetails.getString("description");
                        }catch (JSONException e){
                            description = "description";
                        }
                        try {
                            state = storyDetails.getString("state");
                        }catch (JSONException e){
                            state = "Private";
                        }
                        try {
                            url = storyDetails.getString("url");
                        }catch (JSONException e){
                            url = "Private";
                        }
                        try {
                            type = storyDetails.getInt("type");
                        }catch (JSONException e){
                            type = Post.TYPE_IMAGE;
                        }
                        int privacity;
                        if (state == "Public"){
                            privacity = Post.privacity_public;
                        } else {
                            privacity = Post.privacity_private;
                        }

                        posts.add(new Post(id, title, description, R.drawable.stories_splash, ownerUser, privacity, url, type));
                    }
                    MainActivity.this.runOnUiThread(new MainActivity.CallbackRequestGetFeedStories.SetResults());
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
                setMainContent(posts);
            }
        }
    }

    public void setMainContent(ArrayList<Post> posts){
        if (posts.size() == 0){
            Toast.makeText(getBaseContext(),"Don't have stories to show. Post one to start!", Toast.LENGTH_LONG).show();
        } else {
            RecyclerView container = (RecyclerView) findViewById(R.id.my_recycler_view);
            container.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.app);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);
            container.setAdapter(new MyAdaptor(posts, this.app));
            container.setLayoutManager(layoutManager);
        }
    }
}
