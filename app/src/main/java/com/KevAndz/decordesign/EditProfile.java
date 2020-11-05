package com.KevAndz.decordesign;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.KevAndz.decordesign.controller.PrefManager;

public class EditProfile extends AppCompatActivity {
    EditText editTextName, editTextUsername, editTextEmail, editPhoneNumber, editTextBirthDate;
    String gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        User user = PrefManager.getInstance(this.getApplicationContext()).getUser();

        Bundle bundle = getIntent().getExtras();

        if (user.getUserLevel() == 0){
            LinearLayout designerLayout = (LinearLayout) findViewById(R.id.designerLayoutEditProfile);
            designerLayout.setVisibility(View.GONE);
        }else{
            //TODO bikin url untuk portofolio
        }

        Spinner genderSpinner = (Spinner) findViewById(R.id.genderOptions);
        ArrayAdapter<String> genderAdapter= new ArrayAdapter<String>(EditProfile.this, android.R.layout.simple_list_item_1, getResources().getStringArray((R.array.gender)));
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        gender                  = genderSpinner.getSelectedItem().toString();
        editTextEmail           = findViewById(R.id.editTextEmail);
        editTextUsername        = findViewById(R.id.editTextUsername);
        editTextName            = findViewById(R.id.editTextName);
        editTextBirthDate       = findViewById(R.id.editTextBirthDate);
        editPhoneNumber         = findViewById(R.id.editPhoneNumber);

        editTextBirthDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int s       = monthOfYear+1;
                        String a    = dayOfMonth+"/"+s+"/"+year;
                        editTextBirthDate.setText(""+a);
                    }
                };

                Time date = new Time();
                DatePickerDialog d = new DatePickerDialog(EditProfile.this, dpd, date.year ,date.month, date.monthDay);
                d.show();
            }
        });

        if (bundle.getString("gender").equals("Male")){
            genderSpinner.setSelection(0);
        }else if (bundle.getString("gender").equals("Female")){
            genderSpinner.setSelection(1);
        }

        editTextName.setText(bundle.getString("names"));
        editTextUsername.setText(bundle.getString("username"));
        editTextEmail.setText(bundle.getString("email"));
        editPhoneNumber.setText(bundle.getString("phone"));
        editTextBirthDate.setText(bundle.getString("birthdate"));

        findViewById(R.id.sdBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}