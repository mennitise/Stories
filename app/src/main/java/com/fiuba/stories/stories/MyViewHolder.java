package com.fiuba.stories.stories;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MyViewHolder extends RecyclerView.ViewHolder {

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
    }
}
