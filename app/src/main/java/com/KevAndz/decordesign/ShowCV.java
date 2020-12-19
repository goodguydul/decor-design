package com.KevAndz.decordesign;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.KevAndz.decordesign.controller.PrefManager;

public class ShowCV extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_c_v);
        Bundle bundle = getIntent().getExtras();
        User user = PrefManager.getInstance(this).getUser();

        TextView cvName = (TextView) findViewById(R.id.cvName);
        WebView cv_view = (WebView) findViewById(R.id.cv_view);

        if ( bundle.getInt("id") == user.getId() ){
            cvName.setText("Your CV");
        }else{
            cvName.setText(bundle.getString("name")+"'s CV");
        }

        cv_view.getSettings().setJavaScriptEnabled(true);
        cv_view.getSettings().setPluginState(WebSettings.PluginState.ON);;
        cv_view.getSettings().setAllowFileAccess(true);
        cv_view.loadUrl("https://docs.google.com/gview?embedded=true&url=" + bundle.getString("cv_url"));

        findViewById(R.id.sdBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}