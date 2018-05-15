package com.fiuba.stories.stories;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PostDetailActivity extends AppCompatActivity {

    public static final String ID_POST = "idPost";
    public static final String TITLE_POST = "titlePost";
    public static final String DESCRIPTION_POST = "descPost";
    public static final String IMAGE_POST = "imagePost";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Intent intent = getIntent();

        String id = intent.getStringExtra(ID_POST);

        String title = intent.getStringExtra(TITLE_POST);
        TextView titleView = findViewById(R.id.title_post);
        titleView.setText(title);

        String description = intent.getStringExtra(DESCRIPTION_POST);
        TextView descriptionView  = findViewById(R.id.description_post);
        descriptionView.setText(description);

        int image = intent.getIntExtra(IMAGE_POST,0);
        ImageView imageView = findViewById(R.id.post_image);
        imageView.setImageResource(image);

        ImageButton like = findViewById(R.id.like_button);
        ImageButton dislike = findViewById(R.id.dislike_button);
        ImageButton faceLike = findViewById(R.id.like_face_button);
        ImageButton faceDislike = findViewById(R.id.not_like_face_button);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Like", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        faceLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Haha", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Dislike", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        faceDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Meh", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //post_image

    }
}
