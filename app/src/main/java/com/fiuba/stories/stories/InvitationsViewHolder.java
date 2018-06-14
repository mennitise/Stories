package com.fiuba.stories.stories;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.stories.stories.utils.AppServerRequest;
import com.fiuba.stories.stories.utils.HttpCallback;
import com.fiuba.stories.stories.utils.UtilCallbacks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InvitationsViewHolder  extends RecyclerView.ViewHolder {

    View itemView;
    TextView user;
    ImageButton acept;
    User userlogged;

    public InvitationsViewHolder(final View itemView, final User userlogged) {
        super(itemView);
        this.itemView = itemView;
        this.userlogged = userlogged;
        user = (TextView) itemView.findViewById(R.id.user_invitation);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Snackbar.make(v, user.getText(), Snackbar.LENGTH_LONG).show();
                UtilCallbacks util = new UtilCallbacks();
                AppServerRequest.getAlienProfileInformation((String) user.getText(), userlogged.getEmail(), userlogged.token,
                        util.getCallbackRequestGetAlienProfileInvitation((String) user.getText(), AlienProfileActivity.class, v.getContext(), new ToastRunnable("401"), new ToastRunnable("400")));
            }
        });
        acept = itemView.findViewById(R.id.acept_invitation);
        acept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setVisibility(View.INVISIBLE);
                AppServerRequest.putAceptInvitation(userlogged.getEmail(),userlogged.token,(String) user.getText(),new CallbackRequestAceptInvitation());
            }
        });
    }

    public class ToastRunnable implements Runnable {
        String message;

        public ToastRunnable(String message){
            this.message = message;
        }

        @Override
        public void run() {
            Snackbar.make(itemView, this.message, Snackbar.LENGTH_LONG).show();
        }
    }

    public class CallbackRequestAceptInvitation extends HttpCallback {
        ArrayList<Post> posts;

        @Override
        public void onResponse() {
            try{
                Log.d("HTTP RESPONSE: ", getHTTPResponse().toString());
                JSONObject jsonResponse = getJSONResponse();

                if (getHTTPResponse().code() == 200) {
                    Log.d("RESPONSE: ", jsonResponse.toString());
                /*
                } else if (getHTTPResponse().code() == 401){
                    Toast.makeText(getBaseContext(), "ERROR 401", Toast.LENGTH_LONG).show();
                } else if (getHTTPResponse().code() == 400){
                    Toast.makeText(getBaseContext(), "ERROR 400", Toast.LENGTH_LONG).show();
                */
                }
            } catch (Exception e) {
                Log.e("TEST REQUEST CALLBACK", "Error");
                e.printStackTrace();
            }
        }
    }
}
