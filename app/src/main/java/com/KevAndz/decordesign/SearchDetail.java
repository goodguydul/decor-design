package com.KevAndz.decordesign;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.KevAndz.decordesign.controller.PrefManager;

public class SearchDetail extends AppCompatActivity {
    TextView nameDetailText, usernameDetailText, profileEmail, profilePhoneText, genderText, birthText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_detail);
        Bundle bundle = getIntent().getExtras();

        nameDetailText      = (TextView)findViewById(R.id.nameDetailText);
        usernameDetailText  = (TextView)findViewById(R.id.usernameDetailText);
        profileEmail        = (TextView)findViewById(R.id.profileEmail);
        profilePhoneText    = (TextView)findViewById(R.id.profilePhoneText);
        genderText          = (TextView)findViewById(R.id.genderText);
        birthText           = (TextView)findViewById(R.id.birthText);

        nameDetailText.setText(bundle.getString("names"));
        usernameDetailText.setText(bundle.getString("username"));
        profileEmail.setText(bundle.getString("email"));
        profilePhoneText.setText(bundle.getString("phone"));
        genderText.setText(bundle.getString("gender"));
        birthText.setText(bundle.getString("birthdate"));

        findViewById(R.id.sdBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}