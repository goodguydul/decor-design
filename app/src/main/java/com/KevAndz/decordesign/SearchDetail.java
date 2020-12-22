package com.KevAndz.decordesign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.KevAndz.decordesign.controller.PrefManager;
import com.squareup.picasso.Picasso;

public class SearchDetail extends AppCompatActivity {
    TextView nameDetailText, usernameDetailText, profileEmail, profilePhoneText, genderText, birthText;
    ImageView imageView;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_detail);
        final Bundle bundle = getIntent().getExtras();
        User user = PrefManager.getInstance(this).getUser();


        nameDetailText      = (TextView)findViewById(R.id.nameDetailText);
        usernameDetailText  = (TextView)findViewById(R.id.usernameDetailText);
        profileEmail        = (TextView)findViewById(R.id.profileEmail);
        profilePhoneText    = (TextView)findViewById(R.id.profilePhoneText);
        genderText          = (TextView)findViewById(R.id.genderText);
        birthText           = (TextView)findViewById(R.id.birthText);
        imageView           = (ImageView) findViewById(R.id.profilePict);
        TextView click_cv   = (TextView) findViewById(R.id.click_cv);

        nameDetailText.setText(bundle.getString("names"));
        usernameDetailText.setText(bundle.getString("username"));
        profileEmail.setText(bundle.getString("email"));

        if(bundle.getString("phone").equals("null")){
            profilePhoneText.setText("Not Set");
        }else{
            profilePhoneText.setText(bundle.getString("phone"));
        }

        genderText.setText(bundle.getString("gender"));
        birthText.setText(bundle.getString("birthdate"));

        if (!bundle.getString("profileimg_url").equals("null")){
            Picasso.get().load(bundle.getString("profileimg_url")).into(imageView);
        }

        if (!bundle.getString("cv_url").equals("null")){
            click_cv.setText("Click to show CV");
        }else{
            click_cv.setText("Not Available");
            findViewById(R.id.CvImageViewGo).setVisibility(View.GONE);
        }

        findViewById(R.id.CvImageViewGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("USER_ID", String.valueOf(bundle.getInt("id")));
                Intent intent = new Intent(view.getContext(), ShowCV.class);
                intent.putExtra("id", bundle.getInt("id"));
                intent.putExtra("name", bundle.getString("names"));
                intent.putExtra("cv_url", bundle.getString("cv_url"));
                startActivityForResult(intent, 10001);

            }
        });

        findViewById(R.id.sendMessageBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("USER_ID", String.valueOf(bundle.getInt("id")));
                Intent intent = new Intent(view.getContext(), ChatRoom.class);
                intent.putExtra("targetUser_id", bundle.getInt("id"));
                intent.putExtra("targetUser_name", bundle.getString("names"));
                startActivityForResult(intent, 10002);
            }
        });

        findViewById(R.id.sdBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}