package com.fiuba.stories.stories;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.fiuba.stories.stories.utils.AppServerRequest;
import com.fiuba.stories.stories.utils.UtilCallbacks;


public class SearchViewHolder extends RecyclerView.ViewHolder {

    View itemView;
    TextView user;
    Boolean isFriend;
    User userlogged;

    public SearchViewHolder(final View itemView, final User userlogged) {
        super(itemView);
        this.itemView = itemView;
        this.userlogged = userlogged;
        user = (TextView) itemView.findViewById(R.id.user_invitation);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (userlogged.getEmail().equals(user.getText())){
                    Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    v.getContext().startActivity(intent);
                } else if (isFriend){
                    UtilCallbacks util = new UtilCallbacks();
                    AppServerRequest.getAlienProfileInformation((String) user.getText(), userlogged.getEmail(), userlogged.token,
                            util.getCallbackRequestGetAlienProfileInvitation((String) user.getText(), AlienProfileActivity.class, v.getContext(), new SearchViewHolder.SnackRunnable("401"), new SearchViewHolder.SnackRunnable("400")));
                } else {
                    UtilCallbacks util = new UtilCallbacks();
                    AppServerRequest.getAlienProfileInformation((String) user.getText(), userlogged.getEmail(), userlogged.token,
                            util.getCallbackRequestGetNoFriendProfile((String) user.getText(), NoFriendActivity.class, v.getContext(), new SearchViewHolder.SnackRunnable("401"), new SearchViewHolder.SnackRunnable("400")));
                }
            }
        });
    }

    public class SnackRunnable implements Runnable {
        String message;

        public SnackRunnable(String message){
            this.message = message;
        }

        @Override
        public void run() {
            Snackbar.make(itemView, this.message, Snackbar.LENGTH_LONG).show();
        }
    }

}
