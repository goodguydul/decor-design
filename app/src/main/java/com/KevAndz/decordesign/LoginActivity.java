package com.KevAndz.decordesign;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.KevAndz.decordesign.controller.PrefManager;
import com.KevAndz.decordesign.controller.URLS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PrefManager prefManager = PrefManager.getInstance(LoginActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(prefManager.isLoggedIn()) {

            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }else{
            editTextEmail = findViewById(R.id.editTextEmail);
            editTextPassword = findViewById(R.id.editTextPassword);

            //if user presses on login calling the method login
            findViewById(R.id.cirLoginButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userLogin();
                    closeKeyBoard();
                }
            });

            //if user presses on not registered
            findViewById(R.id.textViewSignUp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //open register screen
                    startActivity(new Intent(getApplicationContext(), RegisterPromptActivity.class));

                }
            });

            findViewById(R.id.textViewForgotPass).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //open forgotpass screen
                    startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));

                }
            });
        }
    }
    private void closeKeyBoard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)
            getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void userLogin() {
        //first getting the values
        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();
        //validating inputs
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter email!");
            editTextEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter password!");
            editTextPassword.requestFocus();
            return;
        }
        //if everything is fine
        UserLogin ul = new UserLogin(email,password);
        ul.execute();
    }
    class UserLogin extends AsyncTask<Void, Void, String> {
        ProgressBar progressBar;
        String email, password;
        UserLogin(String email,String password) {
            this.email = email;
            this.password = password;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);

            try {
                //converting response to json object

                JSONObject obj = new JSONObject(s);

                //if no error in response
                if (!obj.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                    //getting the user from the response
                    JSONObject userJson = obj.getJSONObject("user");
                    //creating a new user object
                    User user = new User(
                            userJson.getInt("id"),
                            userJson.getString("username"),
                            userJson.getString("email"),
                            userJson.getString("name"),
                            userJson.getString("birthdate"),
                            userJson.getString("gender"),
                            userJson.getString("phonenumber"),
                            userJson.getString("profileimage_url"),
                            userJson.getString("cv_url"),
                            userJson.getInt("user_level")
                    );

                    //storing the user in shared preferences
                    PrefManager.getInstance(getApplicationContext()).setUserLogin(user);

                    //starting the profile activity

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
//                      Toast.makeText(getApplicationContext(), "Invalid username or password!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "The Server seems to be offline", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("password", password);

            //returing the response
            return requestHandler.sendPostRequest(URLS.URL_LOGIN, params);
        }
    }

}
