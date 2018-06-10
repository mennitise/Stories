package com.fiuba.stories.stories;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class FriendsViewHolder extends RecyclerView.ViewHolder {

    TextView user;
    User userlogged;

    public FriendsViewHolder(View itemView, final User userlogged) {
        super(itemView);
        this.userlogged = userlogged;
        user = (TextView) itemView.findViewById(R.id.user_invitation);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Snackbar.make(v, user.getText(), Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
