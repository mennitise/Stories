package com.fiuba.stories.stories;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyViewHolder extends RecyclerView.ViewHolder {



    public Post currentPost;
    TextView titlePost;
    TextView descriptionPost;
    ImageView imagePost;
    Button button1;
    ImageButton button2;
    ImageButton button3;

    public MyViewHolder(View itemView) {
        super(itemView);

        titlePost = (TextView) itemView.findViewById(R.id.title_post);
        descriptionPost = (TextView) itemView.findViewById(R.id.description_post);
        imagePost = (ImageView) itemView.findViewById(R.id.card_image);
        button1 = (Button) itemView.findViewById(R.id.owner_button);
        button2 = (ImageButton) itemView.findViewById(R.id.like_post_button);
        button3 = (ImageButton) itemView.findViewById(R.id.dislike_post_button);



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Stories App", Snackbar.LENGTH_LONG).show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(v.getContext(), PostDetailActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.putExtra(PostDetailActivity.EXTRA_POSITION, currentPost);
                intent.putExtra(PostDetailActivity.ID_POST, currentPost.id);
                intent.putExtra(PostDetailActivity.USERNAME_POST, currentPost.ownerUser.email);
                intent.putExtra(PostDetailActivity.NAME_AUTHOR_POST, currentPost.ownerUser.getName());
                intent.putExtra(PostDetailActivity.TITLE_POST, currentPost.getTitle());
                intent.putExtra(PostDetailActivity.DESCRIPTION_POST, currentPost.getDescription());
                intent.putExtra(PostDetailActivity.IMAGE_POST, currentPost.getImagePost());
                intent.putExtra(PostDetailActivity.URL_IMAGE_POST, currentPost.getUrlImage());


                v.getContext().startActivity(intent);
            }
        });
    }
}
