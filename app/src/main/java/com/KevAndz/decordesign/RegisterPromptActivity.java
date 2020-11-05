package com.KevAndz.decordesign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegisterPromptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_prompt);


        findViewById(R.id.registerAsUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register as user screen
                Intent in = new Intent(getApplicationContext(), RegisterActivity.class);
                in.putExtra("registerAs","user");
                startActivity(in);
            }
        });
        findViewById(R.id.registerAsDesigner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register as designer screen
                Intent in = new Intent(getApplicationContext(), RegisterActivity.class);
                in.putExtra("registerAs","designer");
                startActivity(in);
            }
        });
    }
}
