package com.KevAndz.decordesign;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.KevAndz.decordesign.controller.URLS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity{

    EditText editTextName, editTextUsername, editTextEmail, editTextPassword, editTextPasswordConfirm, editTextBirthDate;
    String gender, registerAs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //set gender dropdown
        Spinner genderSpinner = (Spinner) findViewById(R.id.genderOptions);
        ArrayAdapter<String> genderAdapter= new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray((R.array.gender)));
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        gender                  = genderSpinner.getSelectedItem().toString();
        editTextEmail           = findViewById(R.id.editTextEmail);
        editTextUsername        = findViewById(R.id.editTextUsername);
        editTextName            = findViewById(R.id.editTextName);
        editTextBirthDate       = findViewById(R.id.editTextBirthDate);
        editTextPassword        = findViewById(R.id.editTextPassword);
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);
//        registerAs              = (getIntent().getStringExtra("registerAs") == "user") ? "user" : "designer";
        registerAs              = getIntent().getStringExtra("registerAs");

        TextView textView = (TextView) findViewById(R.id.headerRegisterTextView);
        textView.append(" AS " + registerAs.toUpperCase());

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
                DatePickerDialog d = new DatePickerDialog(RegisterActivity.this, dpd, date.year ,date.month, date.monthDay);
                d.show();
            }
        });

        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
                closeKeyBoard();
            }
        });

        findViewById(R.id.textViewLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void registerUser() {
        final String username           = editTextUsername.getText().toString().trim();
        final String name               = editTextName.getText().toString().trim();
        final String email              = editTextEmail.getText().toString().trim();
        final String password           = editTextPassword.getText().toString().trim();
        final String passwordConfirm    = editTextPasswordConfirm.getText().toString().trim();
        final String birthDate          = editTextBirthDate.getText().toString().trim();

        //validations

        if (TextUtils.isEmpty(name)) {
            editTextEmail.setError("Please enter your name");
            editTextEmail.requestFocus();
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
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter a password");
            editTextPassword.requestFocus();
            return;
        }
        if (!password.equals(passwordConfirm)){
            Toast.makeText(getApplicationContext(),"Password Not Match !", Toast.LENGTH_SHORT).show();
            return;
        }

        //if it passes all the validations
        //executing the async task
        RegisterUser ru = new RegisterUser(name, birthDate, gender, username, email, password, registerAs);
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

    private class RegisterUser extends AsyncTask<Void, Void, String> {
        private ProgressBar progressBar;
        private String name,birthdate,gender,username,email,password,registerAs;

        RegisterUser(String name, String birthdate, String gender, String username, String email, String password, String registerAs){
            this.username   = username;
            this.password   = password;
            this.email      = email;
            this.name       = name;
            this.gender     = gender;
            this.birthdate  = birthdate;
            this.registerAs = registerAs;
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
            params.put("name", name);
            params.put("gender", gender);
            params.put("birthdate", birthdate);
            params.put("username", username);
            params.put("email", email);
            params.put("password", password);
            params.put("level", registerAs);
            Log.d("BIRTHDATE_REGISTER",birthdate);
            //returing the response
            return requestHandler.sendPostRequest(URLS.URL_REGISTER, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("UserRegister","Status : "+s);
            //hiding the progressbar after completion
            progressBar.setVisibility(View.GONE);
            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);
                //if no error in response
                if (!obj.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Log.d("JSON ERROR", String.valueOf(e));
                e.printStackTrace();
            }
        }
    }
}
