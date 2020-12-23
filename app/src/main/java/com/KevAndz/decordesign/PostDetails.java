package com.KevAndz.decordesign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.KevAndz.decordesign.controller.PrefManager;
import com.KevAndz.decordesign.controller.URLS;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PostDetails extends AppCompatActivity {
    ProgressBar progressBar;
    TextView userName, postCaption, likes, postDate;
    ImageView imgPost, imgUser;
    User user;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        progressBar = findViewById(R.id.progressBar) ;
        final Bundle bundle = getIntent().getExtras();
        user = PrefManager.getInstance(this).getUser();

        userName        = findViewById(R.id.txtName);
        postCaption     = findViewById(R.id.txtMessage);
        likes           = findViewById(R.id.likeCounter);
        postDate        = findViewById(R.id.postDate);
        imgPost         = findViewById(R.id.imagePost);
        imgUser         = findViewById(R.id.imgAuthorUser);

        userName.setText(bundle.getString("authorName"));
        postDate.setText(bundle.getString("postDate"));
        postCaption.setText(bundle.getString("captionText"));
        likes.setText(bundle.getString("like") + " Likes");

        if(bundle.getString("authorImgUrl") != null && ! bundle.getString("authorImgUrl").equals("null")){
            Picasso.get().load(bundle.getString("authorImgUrl") ).into(imgUser);
        }else{
            imgUser.setImageResource(R.drawable.user);
        }
        Picasso.get().load(bundle.getString("imgPost")).into(imgPost);

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new searchActionClass(bundle.getString("targetUser_id")).execute();
            }
        });
    }

    class searchActionClass extends AsyncTask<Void, Void, String> {
        String user_id;
        searchActionClass(String user_id) {
            this.user_id = user_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("jSON-Output", s);

            try {
                JSONObject obj = new JSONObject(s);

                if (!obj.getBoolean("error")) {

                    JSONArray jArray = (JSONArray) obj.getJSONArray("user");
                    Intent intent;
                        JSONObject objs = jArray.getJSONObject(0);

                        if (objs.getInt("id") == user.getId()) {
                            Fragment fragment = new Profile();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.postDetails, fragment).commit();

                        } else {
                            intent = new Intent(getApplicationContext(), SearchDetail.class);
                            intent.putExtra("id", objs.getInt("id"));
                            intent.putExtra("names", objs.getString("name"));
                            intent.putExtra("email", objs.getString("email"));
                            intent.putExtra("username", objs.getString("username"));
                            intent.putExtra("gender", objs.getString("gender"));
                            intent.putExtra("phone", objs.getString("phonenumber"));
                            intent.putExtra("birthdate", objs.getString("birthdate"));
                            intent.putExtra("profileimg_url", objs.getString("profileimg_url"));
                            intent.putExtra("cv_url", objs.getString("cv_url"));
                            finish();
                            startActivity(intent);
                        }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", user_id);

            //returing the response
            return requestHandler.sendPostRequest(URLS.URL_GETUSERBYID, params);
        }
    }
}