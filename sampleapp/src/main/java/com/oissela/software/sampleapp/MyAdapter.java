package com.oissela.software.sampleapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oissela.software.multilevelexpindlistview.MultiLevelExpIndListAdapter;
import com.oissela.software.multilevelexpindlistview.Utils;
import com.oissela.software.sampleapp.model.MyComment;
import com.oissela.software.sampleapp.model.MyContent;

/**
 * An example of how to extend MultiLevelExpIndListAdapter.
 * Some of this code is copied from https://developer.android.com/training/material/lists-cards.html
 */
public class MyAdapter extends MultiLevelExpIndListAdapter {
    /**
     * View type of an item or group.
     */
    public static final int VIEW_TYPE_ITEM = 0;

    /**
     * View type of the content.
     */
    private static final int VIEW_TYPE_CONTENT = 1;

    /**
     * This is called when the user click on an item or group.
     */
    private final View.OnClickListener mListener;

    private final Context mContext;

    /**
     * Unit of indentation.
     */
    private final int mPaddingDP = 5;

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private View colorBand;
        public TextView authorTextView;
        public TextView commentTextView;
        public TextView hiddenCommentsCountTextView;
        private View view;

        private static final String[] indColors = {"#000000", "#3366FF", "#E65CE6",
                "#E68A5C", "#00E68A", "#CCCC33"};

        public CommentViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            authorTextView = (TextView) itemView.findViewById(R.id.author_textview);
            commentTextView = (TextView) itemView.findViewById(R.id.comment_textview);
            colorBand = itemView.findViewById(R.id.color_band);
            hiddenCommentsCountTextView = (TextView) itemView.findViewById(R.id.hidden_comments_count_textview);
        }

        public void setColorBandColor(int indentation) {
            int color = Color.parseColor(indColors[indentation]);
            colorBand.setBackgroundColor(color);
        }

        public void setPaddingLeft(int paddingLeft) {
            view.setPadding(paddingLeft,0,0,0);
        }
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        public TextView contentTextView;

        public ContentViewHolder(View itemView) {
            super(itemView);
            contentTextView = (TextView) itemView.findViewById(R.id.content_textview);
        }
    }

    public MyAdapter(Context context, View.OnClickListener listener) {
        super();
        mContext = context;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                int resource = R.layout.recyclerview_item;
                v = LayoutInflater.from(parent.getContext())
                        .inflate(resource, parent, false);
                viewHolder = new CommentViewHolder(v);
                break;
            case VIEW_TYPE_CONTENT:
                resource = R.layout.recyclerview_content;
                v = LayoutInflater.from(parent.getContext())
                        .inflate(resource, parent, false);
                viewHolder = new ContentViewHolder(v);
                break;
            default:
                throw new IllegalStateException("unknown viewType");
        }

        v.setOnClickListener(mListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                CommentViewHolder cvh = (CommentViewHolder) viewHolder;
                MyComment comment = (MyComment) getItemAt(position);

                cvh.authorTextView.setText(comment.author);
                cvh.commentTextView.setText(comment.comment);

                if (comment.getIndentation() == 0) {
                    cvh.colorBand.setVisibility(View.GONE);
                    cvh.setPaddingLeft(0);
                } else {
                    cvh.colorBand.setVisibility(View.VISIBLE);
                    cvh.setColorBandColor(comment.getIndentation());
                    int leftPadding = Utils.getPaddingPixels(mContext, mPaddingDP) * (comment.getIndentation() - 1);
                    cvh.setPaddingLeft(leftPadding);
                }

                if (comment.isGroup()) {
                    cvh.hiddenCommentsCountTextView.setVisibility(View.VISIBLE);
                    cvh.hiddenCommentsCountTextView.setText(Integer.toString(comment.getGroupSize()));
                } else {
                    cvh.hiddenCommentsCountTextView.setVisibility(View.GONE);
                }
                break;
            case VIEW_TYPE_CONTENT:
                ContentViewHolder contentVH = (ContentViewHolder) viewHolder;
                MyContent content = (MyContent) getItemAt(position);
                contentVH.contentTextView.setText(content.content);
                break;
            default:
                throw new IllegalStateException("unknown viewType");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return VIEW_TYPE_CONTENT;
        return VIEW_TYPE_ITEM;
    }
}
