package com.fiuba.stories.stories;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CommentsAdaptor extends RecyclerView.Adapter<CommentsViewHolder> {
    List<JSONObject> comments;

    public CommentsAdaptor(List<JSONObject> comments) {
        this.comments = comments;
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_comment, parent, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int position) {
        try {
            holder.authorComment.setText(this.comments.get(position).getString("author"));
            holder.comment.setText(this.comments.get(position).getString("comment"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.comments.size();
    }
}