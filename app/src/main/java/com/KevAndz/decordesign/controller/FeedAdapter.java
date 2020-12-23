package com.KevAndz.decordesign.controller;

import com.KevAndz.decordesign.R;
import com.KevAndz.decordesign.User;
import com.KevAndz.decordesign.model.FeedModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedAdapter extends ArrayAdapter<FeedModel> {
    List<FeedModel> listMessage;
    Context context;
    int layout;

    public FeedAdapter(Context context, int layout, List<FeedModel> listMessage) {
        super(context, layout, listMessage);
        this.context        =   context;
        this.layout         =   layout;
        this.listMessage    =   listMessage;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        FeedAdapter.FeedHolder holder;

        if(v==null){
            LayoutInflater vi=((Activity)context).getLayoutInflater();
            v=vi.inflate(layout, parent,false);

            holder=new FeedAdapter.FeedHolder();
            holder.imgAuthorUser   = v.findViewById(R.id.imgAuthorUser);
            holder.imgPost          = v.findViewById(R.id.imagePost);
            holder.txtMessage       = v.findViewById(R.id.txtMessage);
            holder.txtName          = v.findViewById(R.id.txtName);
            holder.likeCounter      = v.findViewById(R.id.likeCounter);
            holder.postDate         = v.findViewById(R.id.postDate);

            v.setTag(holder);
        }
        else{
            holder=(FeedAdapter.FeedHolder) v.getTag();
        }

        FeedModel feed = listMessage.get(position);

        if(feed.getAuthorImageUrl() != null && ! feed.getAuthorImageUrl().equals("null")){
            Picasso.get().load(feed.getAuthorImageUrl()).into(holder.imgAuthorUser);
        }else{
            holder.imgAuthorUser.setImageResource(R.drawable.user);
        }

        holder.txtName.setText(feed.getAuthorName());
        holder.txtMessage.setText(feed.getCaption());
        holder.likeCounter.setText(feed.getLike()+ " Likes");
        holder.postDate.setText(feed.getPostDate());
        Picasso.get().load(feed.getPostImageUrl()).into(holder.imgPost);

        return v;
    }

    static class FeedHolder{
        ImageView imgAuthorUser;
        ImageView imgPost;
        TextView txtName;
        TextView likeCounter;
        TextView txtMessage;
        TextView postDate;
    }
}
