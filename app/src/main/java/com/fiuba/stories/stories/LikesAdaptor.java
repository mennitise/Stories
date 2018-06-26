package com.fiuba.stories.stories;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LikesAdaptor extends RecyclerView.Adapter<LikesViewHolder> {
    ArrayList<JSONObject> reactions;
    User userlogged;

    public LikesAdaptor(ArrayList<JSONObject> reactions, User userlogged) {
        this.reactions = reactions;
        this.userlogged = userlogged;
    }

    @Override
    public LikesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_reaction, parent, false);
        return new LikesViewHolder(view, this.userlogged);
    }

    @Override
    public void onBindViewHolder(LikesViewHolder holder, int position) {
        try{
            holder.user.setText(this.reactions.get(position).getString("reacter"));
            holder.like.setVisibility((reactions.get(position).getString("reaction").equals("like")) ? View.VISIBLE : View.GONE);
            holder.dislike.setVisibility((reactions.get(position).getString("reaction").equals("dislike")) ? View.VISIBLE : View.GONE);
            holder.facelike.setVisibility((reactions.get(position).getString("reaction").equals("facelike")) ? View.VISIBLE : View.GONE);
            holder.facedislike.setVisibility((reactions.get(position).getString("reaction").equals("facedislike")) ? View.VISIBLE : View.GONE);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.reactions.size();
    }
}