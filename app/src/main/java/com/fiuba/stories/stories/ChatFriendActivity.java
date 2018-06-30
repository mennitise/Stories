package com.fiuba.stories.stories;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class ChatFriendActivity extends AppCompatActivity {

    public static final String USERNAME_RECEPTOR = "usernameReceptor";

    private ImageView friendImage;
    private TextView friendName;
    private RecyclerView recyclerView;
    private EditText message;
    private Button send;
    private String receptor;

    private MessageAdaptor adaptor;

    private StoriesApp app;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private DatabaseReference dbRefReceptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_friend);
        this.app = (StoriesApp) getApplicationContext();
        Intent intent = getIntent();
        receptor = intent.getStringExtra(USERNAME_RECEPTOR);

        this.friendImage = findViewById(R.id.friend_image);
        this.friendName = findViewById(R.id.friend_name);
        this.recyclerView = findViewById(R.id.recycler_view);
        this.message = findViewById(R.id.message);
        this.send = findViewById(R.id.send);



        this.db = FirebaseDatabase.getInstance();
        this.dbRef = this.db.getReference("chat/"+this.app.userLoggedIn.getEmail()+"_"+receptor); // Name where save messages -> should be usernames;
        this.dbRefReceptor = this.db.getReference("chat/"+receptor+"_"+this.app.userLoggedIn.getEmail());

        this.adaptor = new MessageAdaptor(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(l);
        this.recyclerView.setAdapter(this.adaptor);

        this.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef.push().setValue(new MessageSend(app.userLoggedIn.getEmail(), message.getText().toString(), ServerValue.TIMESTAMP));
                dbRefReceptor.push().setValue(new MessageSend(app.userLoggedIn.getEmail(), message.getText().toString(), ServerValue.TIMESTAMP));
                message.setText("");
            }
        });

        this.adaptor.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessageReceive m = dataSnapshot.getValue(MessageReceive.class);
                adaptor.addMessage(m);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setScrollbar(){
        this.recyclerView.scrollToPosition(this.adaptor.getItemCount()-1);
    }
}
