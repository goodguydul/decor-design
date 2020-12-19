package com.KevAndz.decordesign;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.KevAndz.decordesign.controller.PrefManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Profile extends Fragment {
    TextView usernameText, emailProfileText, nameProfileText, profilePhoneText, genderText, birthdateText, click_cv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameText = (TextView) view.findViewById(R.id.usernameText);
        emailProfileText = (TextView) view.findViewById(R.id.emailProfileText);
        nameProfileText = (TextView) view.findViewById(R.id.nameProfileText);
        profilePhoneText = (TextView) view.findViewById(R.id.profilePhoneText);
        genderText = (TextView) view.findViewById(R.id.genderText);
        birthdateText = (TextView) view.findViewById(R.id.birthdateText);

        final User user = PrefManager.getInstance(getActivity().getApplicationContext()).getUser();
        if (user.getProf_img_url() != null){
            ImageView imageView = (ImageView) view.findViewById(R.id.profilePict);
            Picasso.get().load(user.getProf_img_url() ).into(imageView);
        }

        if(user.getUsername().equals("null")){
            usernameText.setText("not set");
        }else{
            usernameText.setText(user.getUsername());
        }

        if(user.getEmail().equals("null")){
            emailProfileText.setText("not set");
        }else{
            emailProfileText.setText(user.getEmail());
        }

        if(user.getName().equals("null")){
            nameProfileText.setText("not set");
        }else{
            nameProfileText.setText(user.getName());
        }

        if(user.getPhonenumber().equals("null")){
            profilePhoneText.setText("not set");
        }else{
            profilePhoneText.setText(user.getPhonenumber());
        }

        if(user.getBirthdate().equals("null") || user.getBirthdate().equals("0000-00-00")){
            birthdateText.setText("not set");
        }else{
            birthdateText.setText(user.getBirthdate());
        }

        if(user.getGenders().equals("null")){
            genderText.setText("not set");
        }else{
            genderText.setText(user.getGenders());
        }

        if (user.getUserLevel() == 0){
            LinearLayout designerLayout = (LinearLayout) view.findViewById(R.id.designerLayout1);
            designerLayout.setVisibility(View.GONE);
        }else{
            if(!user.getCV_url().equals("null") || user.getCV_url() != null){
                click_cv =  (TextView) view.findViewById(R.id.click_cv);
                click_cv.setText("Click to show CV");
            }
        }

        view.findViewById(R.id.CvImageViewGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), ShowCV.class);
                intent.putExtra("id", user.getId());
                intent.putExtra("name", user.getName());
                intent.putExtra("cv_url", user.getCV_url());
                startActivityForResult(intent, 10001);
            }
        });

        view.findViewById(R.id.profileMenuBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(getActivity().getApplicationContext(), view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.logoutMenuBtn:
                                PrefManager.getInstance(getActivity().getApplicationContext()).logout();
                                Toast.makeText(getActivity().getApplicationContext(), "You are successfully logout!", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                                break;
                        }
                        return true;
                    }
                });
                popup.inflate(R.menu.profile_menu);
                popup.show();
            }
        });

        view.findViewById(R.id.editProfileBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), EditProfile.class);
                intent.putExtra("names", user.getName());
                intent.putExtra("email", user.getEmail());
                intent.putExtra("username", user.getUsername());
                intent.putExtra("gender", user.getGenders());
                intent.putExtra("phone", user.getPhonenumber());
                intent.putExtra("birthdate", user.getBirthdate());
                startActivityForResult(intent, 10001);

            }
        });


        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 10001) && (resultCode == Activity.RESULT_OK)){
            // recreate your fragment here
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }
}