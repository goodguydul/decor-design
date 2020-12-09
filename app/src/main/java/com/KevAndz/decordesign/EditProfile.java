package com.KevAndz.decordesign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.KevAndz.decordesign.controller.PrefManager;
import com.KevAndz.decordesign.controller.URLS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditProfile extends AppCompatActivity {
    EditText editTextName, editTextUsername, editTextEmail, editPhoneNumber, editTextBirthDate;
    String gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final User user = PrefManager.getInstance(this.getApplicationContext()).getUser();

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
                        String d    = "";
                        String m    = "";

                        if (s < 10){
                            m = "0" + s;
                        }
                        if (dayOfMonth < 10){
                            d = "0" + dayOfMonth;
                        }else{
                            d = "" + dayOfMonth;
                        }
                        String a    = year+"/"+m+"/"+d;
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

        findViewById(R.id.profilePict).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), UploadPhotoProfile.class);
                intent.putExtra("user_id", user.getId());
                startActivityForResult(intent, 10001);

            }
        });

        findViewById(R.id.editProfileBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
                closeKeyBoard();
            }
        });

    }

    private void updateUser() {
        final String username           = editTextUsername.getText().toString().trim();
        final String name               = editTextName.getText().toString().trim();
        final String email              = editTextEmail.getText().toString().trim();
        final String birthDate          = editTextBirthDate.getText().toString().trim();
        final String phonenumber        = editPhoneNumber.getText().toString().trim();

        //validations

        if (TextUtils.isEmpty(name)) {
            editTextEmail.setError("Please enter your name");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phonenumber)) {
            editPhoneNumber.setError("Please enter your phone number!");
            editPhoneNumber.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(gender)) {
            editTextUsername.setError("Please set your gender!");
            editTextUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(birthDate)) {
            editTextEmail.setError("Please enter your birthdate!");
            editTextEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter username");
            editTextUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter your email");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }
        //if it passes all the validations
        //executing the async task
        User user = PrefManager.getInstance(this.getApplicationContext()).getUser();
        EditProfile.UpdateUser ru = new EditProfile.UpdateUser(user.getId(), name, birthDate, gender, username, email, phonenumber);

        User updatedData = new User(
                user.getId(),
                username,
                email,
                name,
                birthDate,
                gender,
                phonenumber,
                user.getProf_img_url(),
                user.getUserLevel()
        );
        //storing new updated data in shared preferences
        PrefManager.getInstance(getApplicationContext()).setUserLogin(updatedData);

        ru.execute();
    }

    private void closeKeyBoard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private class UpdateUser extends AsyncTask<Void, Void, String> {
        private ProgressBar progressBar;
        private String name, birthdate, gender, username, email, phonenumber;
        private  int id;

        UpdateUser(int id, String name, String birthdate, String gender, String username, String email, String phonenumber){
            this.id  = id;
            this.username   = username;
            this.phonenumber= phonenumber;
            this.email      = email;
            this.name       = name;
            this.gender     = gender;
            this.birthdate  = birthdate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(id));
            params.put("name", name);
            params.put("gender", gender);
            params.put("birthdate", birthdate);
            params.put("username", username);
            params.put("email", email);
            params.put("phonenumber", phonenumber);

            //returing the response
            return requestHandler.sendPostRequest(URLS.URL_UPDATE, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("UpdateProfile","Status : "+s);
            //hiding the progressbar after completion
            progressBar.setVisibility(View.GONE);
            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);
                //if no error in response
                if (!obj.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();
//                    startActivity(new Intent(getApplicationContext(), Profile.class));
                } else {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}