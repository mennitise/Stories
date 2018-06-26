package com.fiuba.stories.stories;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageAdaptor extends RecyclerView.Adapter<MessageViewHolder>{

    private List<MessageReceive> listMessages = new ArrayList<>();
    private Context c;

    public MessageAdaptor(Context c) {
        this.c = c;
    }

    public void addMessage(MessageReceive m){
        listMessages.add(m);
        notifyItemInserted(listMessages.size());
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.content_message, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.getFriendName().setText(listMessages.get(position).getUsername());
        holder.getMessage().setText(listMessages.get(position).getMessage());

        Long timeCode = listMessages.get(position).getTime();
        Date date = new Date(timeCode);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
        holder.getMessageTime().setText(sdf.format(date));
    }

    @Override
    public int getItemCount() {
        return listMessages.size();
    }
}
