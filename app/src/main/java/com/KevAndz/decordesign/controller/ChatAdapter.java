package com.KevAndz.decordesign.controller;

import com.KevAndz.decordesign.model.ChatModel;
import com.KevAndz.decordesign.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatAdapter extends ArrayAdapter<ChatModel> {
    List<ChatModel> listMessage;
    Context context;
    int layout;

    public ChatAdapter(Context context, int layout, List<ChatModel> listMessage) {
        super(context, layout, listMessage);
        this.context=context;
        this.layout=layout;
        this.listMessage=listMessage;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ChatHolder holder;

        if(v==null){
            LayoutInflater vi=((Activity)context).getLayoutInflater();
            v=vi.inflate(layout, parent,false);

            holder=new ChatHolder();
            holder.imgSenderUser=(ImageView) v.findViewById(R.id.imgSenderUser);
            holder.txtSenderMessage=(TextView) v.findViewById(R.id.txtSenderName);
            holder.txtSenderName=(TextView) v.findViewById(R.id.txtSenderMessage);

            v.setTag(holder);
        }
        else{
            holder=(ChatHolder) v.getTag();
        }

        ChatModel chat = listMessage.get(position);

        if(chat.getReceiverImageUrl() != null){
            Picasso.get().load(chat.getReceiverImageUrl()).into(holder.imgSenderUser);
        }else{
            holder.imgSenderUser.setImageResource(R.drawable.user);
        }

        holder.txtSenderName.setText(chat.getReceiverName());
        holder.txtSenderMessage.setText(chat.getMessage());
        return v;
    }

    static class ChatHolder{
        ImageView imgSenderUser;
        TextView txtSenderName;
        TextView txtSenderMessage;
    }
}
