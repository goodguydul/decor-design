package com.KevAndz.decordesign.controller;

import com.KevAndz.decordesign.R;
import com.KevAndz.decordesign.User;
import com.KevAndz.decordesign.model.ChatModel;
import com.KevAndz.decordesign.model.ChatRoomModel;
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

public class ChatRoomAdapter extends ArrayAdapter<ChatRoomModel> {
    List<ChatRoomModel> listMessage;
    Context context;
    int layout;

    public ChatRoomAdapter(Context context, int layout, List<ChatRoomModel> listMessage) {
        super(context, layout, listMessage);
        this.context        =   context;
        this.layout         =   layout;
        this.listMessage    =   listMessage;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View v = convertView = LayoutInflater.from(context).inflate(R.layout.data_message_list_view, parent, false);;
        ChatHolder holder;
        User user = PrefManager.getInstance(context.getApplicationContext()).getUser();
        holder=new ChatHolder();

        holder.imgSenderUser=(ImageView) v.findViewById(R.id.imgSenderUser);
        holder.txtSenderMessage=(TextView) v.findViewById(R.id.txtMessage);
        holder.txtSenderName=(TextView) v.findViewById(R.id.txtName);
        v.setTag(holder);

        ChatRoomModel chat = listMessage.get(position);


        if ( ! chat.getReceiverId().equals(String.valueOf(user.getId()))){
            holder.txtSenderName.setText(user.getName());
        }else{
            holder.txtSenderName.setText(chat.getReceiverName());
        }

        if(chat.getReceiverImageUrl() != null ){
            Picasso.get().load(chat.getReceiverImageUrl()).into(holder.imgSenderUser);
        }else if(! user.getProf_img_url().equals("null")){
            Picasso.get().load(user.getProf_img_url()).into(holder.imgSenderUser);
        }else{
            holder.imgSenderUser.setImageResource(R.drawable.user);
        }

        holder.txtSenderMessage.setText(chat.getMessage());
        return v;
    }

    static class ChatHolder{
        ImageView imgSenderUser;
        TextView txtSenderName;
        TextView txtSenderMessage;
    }
}
