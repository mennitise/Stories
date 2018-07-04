package com.fiuba.stories.stories;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MyAdaptor extends RecyclerView.Adapter<MyViewHolder> {
    List<Post> posts;
    FirebaseStorage storage;
    StoriesApp app;

    public MyAdaptor(List<Post> posts, StoriesApp app) {
        this.posts = posts;
        this.app = app;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        storage = FirebaseStorage.getInstance();
        String urlImage = posts.get(position).getUrlImage();

        if (posts.get(position).getType() == Post.TYPE_IMAGE) {
            holder.videoPost.setVisibility(View.INVISIBLE);
            holder.imagePost.setVisibility(View.VISIBLE);
            holder.imagePost.setImageResource(posts.get(position).getImagePost());
            if (urlImage != null && urlImage != ""){
                Log.d("image",urlImage);
                try {
                    StorageReference httpsReference = storage.getReferenceFromUrl(urlImage);
                    Glide.with(holder.itemView.getContext())
                            .using(new FirebaseImageLoader())
                            .load(httpsReference)
                            .into(holder.imagePost);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (posts.get(position).getType() == Post.TYPE_VIDEO) {
            if (urlImage != null && urlImage != ""){
                holder.imagePost.setVisibility(View.INVISIBLE);
                holder.videoPost.setVisibility(View.VISIBLE);
                Uri uri = Uri.parse(urlImage);
                holder.videoPost.setVideoURI(uri);
                holder.videoPost.start();
            }
        }

        holder.titlePost.setText(posts.get(position).getTitle());
        holder.descriptionPost.setText(posts.get(position).getDescription());
        holder.currentPost = posts.get(position);
        holder.app = app;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
