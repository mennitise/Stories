package com.fiuba.stories.stories;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
public class LikesViewHolder extends RecyclerView.ViewHolder {

    TextView user;
    User userlogged;
    ImageButton like;
    ImageButton dislike;
    ImageButton facelike;
    ImageButton facedislike;


    public LikesViewHolder(View itemView, final User userlogged) {
        super(itemView);
        this.userlogged = userlogged;
        user = (TextView) itemView.findViewById(R.id.user_reaction);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Snackbar.make(v, user.getText(), Snackbar.LENGTH_LONG).show();
            }
        });
        this.like = itemView.findViewById(R.id.like_reaction);
        this.dislike = itemView.findViewById(R.id.dislike_reaction);
        //this.dislike.setVisibility((reaction.equals("")) ? View.VISIBLE : View.GONE);
        this.facelike = itemView.findViewById(R.id.like_face_reaction);
        //this.facelike.setVisibility((reaction.equals("")) ? View.VISIBLE : View.GONE);
        this.facedislike = itemView.findViewById(R.id.dislike_face_reaction);
        //this.facedislike.setVisibility((reaction.equals("")) ? View.VISIBLE : View.GONE);
    }
}
