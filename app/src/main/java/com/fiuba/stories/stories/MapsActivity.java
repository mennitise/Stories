package com.fiuba.stories.stories;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.fiuba.stories.stories.utils.AppServerRequest;
import com.fiuba.stories.stories.utils.HttpCallback;
import com.fiuba.stories.stories.utils.LocationHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    StoriesApp app;
    private double longitude;
    private double latitude;
    private MapsActivity that = this;
    ArrayList<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.app = (StoriesApp) getApplicationContext();
        this.posts = new ArrayList<Post>();
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationHelper loc = new LocationHelper(this);
        if(!loc.canGetLocation()){
            loc.showSettingsAlert();
        }
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        AppServerRequest.getFeedStories(this.app.userLoggedIn.email, this.app.userLoggedIn.token, new MapsActivity.CallbackRequestGetMapStories());


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();

        return false;
    }

    public class CallbackRequestGetMapStories extends HttpCallback {
        @Override
        public void onResponse() {
            try{
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                JSONObject jsonResponse = getJSONResponse();

                if (getHTTPResponse().code() == 200) {
                    Log.d("RESPONSE: ", jsonResponse.toString());

                    JSONArray stories = (JSONArray) jsonResponse.get("feedStories"); // Array of posts
                    for(int i = 0; i < stories.length(); ++i){
                        JSONObject story = stories.getJSONObject(i);
                        String username = story.getString("username");
                        User ownerUser = new User();
                        ownerUser.setEmail(username);
                        JSONObject storyDetails = story.getJSONObject("storyDetail");
                        String id, title, description, url, state;
                        double latitude, longitude;
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
                            latitude = storyDetails.getDouble("lat");
                        }catch (JSONException e){
                            latitude = 0;
                        }
                        try {
                            longitude = storyDetails.getDouble("long");
                        }catch (JSONException e){
                            longitude = 0;
                        }
                        int privacity;
                        if (state == "Public"){
                            privacity = Post.privacity_public;
                        } else {
                            privacity = Post.privacity_private;
                        }

                        posts.add(new Post(id, title, description, R.drawable.stories_splash, ownerUser, privacity, url, latitude, longitude));
                    }
                    MapsActivity.this.runOnUiThread(new MapsActivity.CallbackRequestGetMapStories.SetResults());
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
                that.posts = posts;
                setMainContent();
            }
        }
    }

    public void setMainContent(){
        if (that.posts.size() == 0){
            Toast.makeText(getBaseContext(),"Your friends no post Stories with Location.", Toast.LENGTH_LONG).show();
        } else {
            for (int i = 0; i < that.posts.size() ; i++){
                Post item = that.posts.get(i);
                LatLng latLng = new LatLng(item.getLatitude(),item.getLongitude());
                MarkerOptions marker = new MarkerOptions().position(latLng).title(String.valueOf(i));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(marker);
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Post selected = that.posts.get(Integer.parseInt(marker.getTitle()));
                        Intent intent = new Intent(that, PostDetailActivity.class);
                        intent.putExtra(PostDetailActivity.ID_POST, selected.id);
                        intent.putExtra(PostDetailActivity.TITLE_POST, selected.title);
                        intent.putExtra(PostDetailActivity.DESCRIPTION_POST, selected.description);
                        intent.putExtra(PostDetailActivity.NAME_AUTHOR_POST, selected.ownerUser.email);
                        intent.putExtra(PostDetailActivity.IMAGE_POST, 0);
                        intent.putExtra(PostDetailActivity.URL_IMAGE_POST, selected.urlImage);
                        that.startActivity(intent);
                        return true;
                    }
                });
            }
        }
    }
}
