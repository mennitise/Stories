package com.fiuba.stories.stories;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    private ImageView friendImage;
    private TextView friendName;
    private TextView message;
    private TextView messageTime;

    public MessageViewHolder(View itemView) {
        super(itemView);

        this.friendImage = itemView.findViewById(R.id.friend_image);
        this.friendName = itemView.findViewById(R.id.friend_name);
        this.message = itemView.findViewById(R.id.message);
        this.messageTime = itemView.findViewById(R.id.messageTime);
    }

    public ImageView getFriendImage() {
        return friendImage;
    }

    public void setFriendImage(ImageView friendImage) {
        this.friendImage = friendImage;
    }

    public TextView getFriendName() {
        return friendName;
    }

    public void setFriendName(TextView friendName) {
        this.friendName = friendName;
    }

    public TextView getMessage() {
        return message;
    }

    public void setMessage(TextView message) {
        this.message = message;
    }

    public TextView getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(TextView messageTime) {
        this.messageTime = messageTime;
    }
}
