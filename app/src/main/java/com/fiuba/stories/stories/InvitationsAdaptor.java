package com.fiuba.stories.stories;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InvitationsAdaptor extends RecyclerView.Adapter<InvitationsViewHolder> {
    ArrayList<String> users;
    User userlogged;

    public InvitationsAdaptor(ArrayList<String> users, User userlogged) {
        this.users = users;
        this.userlogged = userlogged;
    }

    @Override
    public InvitationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_invitation, parent, false);
        return new InvitationsViewHolder(view, userlogged);
    }

    @Override
    public void onBindViewHolder(InvitationsViewHolder holder, int position) {
        holder.user.setText(this.users.get(position));
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }
}