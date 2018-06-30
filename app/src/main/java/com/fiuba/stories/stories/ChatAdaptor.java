package com.fiuba.stories.stories;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ChatAdaptor extends RecyclerView.Adapter<ChatViewHolder> {
    ArrayList<String> users;
    User userlogged;

    public ChatAdaptor(ArrayList<String> users, User userlogged) {
        this.users = users;
        this.userlogged = userlogged;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_friend, parent, false);
        return new ChatViewHolder(view, userlogged);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        holder.user.setText(this.users.get(position));
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }
}
