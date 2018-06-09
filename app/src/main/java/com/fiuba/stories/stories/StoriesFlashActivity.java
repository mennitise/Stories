package com.fiuba.stories.stories;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.fiuba.stories.stories.utils.AppServerRequest;
import com.fiuba.stories.stories.utils.HttpCallback;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StoriesFlashActivity extends AppCompatActivity {

    StoriesApp app;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories_flash);
        this.app = (StoriesApp) getApplicationContext();
        AppServerRequest.getFlashStory(this.app.userLoggedIn.getEmail(), this.app.userLoggedIn.token, new CallbackRequestGetFlashStories());
        FloatingActionButton addFlashStory = findViewById(R.id.add_flashstory);
        addFlashStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Create a flash story", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                goToCreateFlashStoryScreen();
            }
        });
    }

    private void goToCreateFlashStoryScreen(){
        Intent intent = new Intent(this, CreateFlashStoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stories_flash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String DESCRIPTION = "section_description";
        private static final String NAME = "section_name";
        private static final String URL = "section_url";
        private FirebaseStorage storage;


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, Post story) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(NAME, story.getOwnerUser().email);
            args.putString(DESCRIPTION, story.getDescription());
            args.putString(URL,story.getUrlImage());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_stories_flash, container, false);
            TextView name_author = (TextView) rootView.findViewById(R.id.name_flash);
            name_author.setText(getArguments().getString(NAME));
            TextView title_story = (TextView) rootView.findViewById(R.id.title_flash);
            title_story.setText(getArguments().getString(DESCRIPTION));
            ImageView imagePost = rootView.findViewById(R.id.image_flash);

            storage = FirebaseStorage.getInstance();
            String urlImage = getArguments().getString(URL);
            if (urlImage != null && urlImage != ""){
                Log.d("image",urlImage);
                try {
                    StorageReference httpsReference = storage.getReferenceFromUrl(urlImage);
                    Glide.with(container.getContext())
                            .using(new FirebaseImageLoader())
                            .load(httpsReference)
                            .into(imagePost);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<Post> stories;

        public SectionsPagerAdapter(FragmentManager fm, List<Post> flashStories) {
            super(fm);
            this.stories = flashStories;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1, this.stories.get(position));
        }

        @Override
        public int getCount() {
            return this.stories.size();
        }
    }

    public class CallbackRequestGetFlashStories extends HttpCallback {
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
                        try {
                            id = story.getString("_id");
                        }catch (JSONException e){
                            id = "0";
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
                        int privacity;
                        if (state == "Public"){
                            privacity = Post.privacity_public;
                        } else {
                            privacity = Post.privacity_private;
                        }

                        posts.add(new Post(id, null, description, R.drawable.stories_splash, ownerUser, privacity, url));
                    }
                    StoriesFlashActivity.this.runOnUiThread(new SetResults());
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
                setFlashStoriesContent(posts);
            }
        }
    }

    public void setFlashStoriesContent(List<Post> posts){
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), posts);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }
}
