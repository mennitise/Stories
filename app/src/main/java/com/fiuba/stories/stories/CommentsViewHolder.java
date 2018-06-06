package com.fiuba.stories.stories;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class CommentsViewHolder extends RecyclerView.ViewHolder {

        TextView authorComment;
        TextView comment;

        public CommentsViewHolder(View itemView) {
            super(itemView);

            authorComment = (TextView) itemView.findViewById(R.id.author_comment);
            comment = (TextView) itemView.findViewById(R.id.comment);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Snackbar.make(v, authorComment.getText(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
}
