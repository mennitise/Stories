package com.fiuba.stories.stories;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class ChatViewHolder extends RecyclerView.ViewHolder {

    View itemView;
    TextView user;
    User userlogged;

    public ChatViewHolder(final View itemView, final User userlogged) {
        super(itemView);
        this.itemView = itemView;
        this.userlogged = userlogged;
        user = (TextView) itemView.findViewById(R.id.user_invitation);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(v.getContext(),ChatFriendActivity.class);
                intent.putExtra(ChatFriendActivity.USERNAME_RECEPTOR, user.getText().toString());
                v.getContext().startActivity(intent);
            }
        });
    }

}
