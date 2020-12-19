package com.KevAndz.decordesign.controller;

import com.KevAndz.decordesign.R;
import com.KevAndz.decordesign.model.DesignerData;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DesignerAdapter extends ArrayAdapter<DesignerData> {
    List<DesignerData> listDesigner;
    Context context;
    int layout;

    public DesignerAdapter(Context context, int layout, List<DesignerData> listDesigner) {

        super(context, layout, listDesigner);
        this.context=context;
        this.layout=layout;
        this.listDesigner=listDesigner;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        DesignerHolder holder;

        if(v==null){
            LayoutInflater vi=((Activity)context).getLayoutInflater();
            v=vi.inflate(layout, parent,false);

            holder=new DesignerHolder();
            holder.txtName=(TextView) v.findViewById(R.id.txtName);
            holder.txtUsername=(TextView) v.findViewById(R.id.txtUsername);
            holder.txtEmail=(TextView) v.findViewById(R.id.txtEmail);
            holder.profileImg = (ImageView) v.findViewById(R.id.imgUser);
            v.setTag(holder);
        }
        else{
            holder=(DesignerHolder) v.getTag();
        }

        DesignerData designer = listDesigner.get(position);

        holder.txtName.setText(designer.getNames());
        holder.txtUsername.setText(designer.getUsername());
        holder.txtEmail.setText(String.valueOf(designer.getEmail()));

        if (!designer.getPictureimg_url().equals("null")){
            Picasso.get().load(designer.getPictureimg_url()).into(holder.profileImg);
        }
        return v;
    }

    static class DesignerHolder{
//        ImageView imageView;
        TextView txtName;
        TextView txtUsername;
        TextView txtEmail;
        ImageView profileImg;
    }
}
