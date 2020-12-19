package com.KevAndz.decordesign;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.KevAndz.decordesign.controller.PrefManager;

public class MainActivity extends AppCompatActivity {
    String usernameText, emailProfileText, nameProfileText, profilePhoneText, profileimg_url, cv_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentPage(new Home());

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigationView);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                //Menentukan halaman Fragment yang akan tampil
                switch (item.getItemId()){
                    case R.id.home:
                        fragment = new Home();
                        break;

                    case R.id.profile:
                        fragment = new Profile();
                        break;

                    case R.id.search:
                        fragment = new Search();
                        break;

                    case R.id.chat:
                        fragment = new Chat();
                        break;
                }
                return getFragmentPage(fragment);
            }
        });

        User user = PrefManager.getInstance(this.getApplicationContext()).getUser();
        usernameText        = user.getUsername();
        emailProfileText    = user.getEmail();
        nameProfileText     = user.getName();
        profilePhoneText    = user.getPhonenumber();
        profileimg_url      = user.getProf_img_url();
        cv_url              = user.getCV_url();

        if (usernameText.equals("null") || emailProfileText.equals("null") || nameProfileText.equals("null") || nameProfileText.equals("") || profilePhoneText.equals("null") || profileimg_url.equals("null") || cv_url.equals("null")){
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            ShowDialog();
                        }
                    },
                    5000);
        }
    }

    private boolean getFragmentPage(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.page_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void ShowDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        alert.setMessage("Please complete your Profile, Thank you.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false);
        alert.show();
    }
}