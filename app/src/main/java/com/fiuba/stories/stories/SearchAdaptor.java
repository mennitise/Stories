package com.fiuba.stories.stories;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchAdaptor extends RecyclerView.Adapter<SearchViewHolder> {
    ArrayList<JSONObject> users;
    User userlogged;

    public SearchAdaptor(ArrayList<JSONObject> users, User userlogged) {
        this.users = users;
        this.userlogged = userlogged;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_friend, parent, false);
        return new SearchViewHolder(view, userlogged);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        try{
            holder.user.setText(this.users.get(position).getString("username"));
            holder.isFriend = this.users.get(position).getBoolean("isFriend");
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }
}