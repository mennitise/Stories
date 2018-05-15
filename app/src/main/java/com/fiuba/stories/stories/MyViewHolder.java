package com.fiuba.stories.stories;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
        button2 = (ImageButton) itemView.findViewById(R.id.share_button);
        button3 = (ImageButton) itemView.findViewById(R.id.favorite_button);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Coso", Snackbar.LENGTH_LONG).show();
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
                intent.putExtra(PostDetailActivity.TITLE_POST, currentPost.getTitle());
                intent.putExtra(PostDetailActivity.DESCRIPTION_POST, currentPost.getDescription());
                intent.putExtra(PostDetailActivity.IMAGE_POST, currentPost.getImagePost());

                v.getContext().startActivity(intent);
            }
        });
    }
}
