package com.fiuba.stories.stories;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MyAdaptor extends RecyclerView.Adapter<MyViewHolder> {
    List<Post> posts;

    public MyAdaptor(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.titlePost.setText(posts.get(position).getTitle());
        holder.descriptionPost.setText(posts.get(position).getDescription());
        holder.imagePost.setImageResource(posts.get(position).getImagePost());
        holder.currentPost = posts.get(position);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
