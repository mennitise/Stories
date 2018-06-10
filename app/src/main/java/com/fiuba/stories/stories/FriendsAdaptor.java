package com.fiuba.stories.stories;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class FriendsAdaptor extends RecyclerView.Adapter<FriendsViewHolder> {
    ArrayList<String> users;
    User userlogged;

    public FriendsAdaptor(ArrayList<String> users, User userlogged) {
        this.users = users;
        this.userlogged = userlogged;
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_friend, parent, false);
        return new FriendsViewHolder(view, userlogged);
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        holder.user.setText(this.users.get(position));
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }
}