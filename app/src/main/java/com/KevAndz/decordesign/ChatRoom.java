package com.KevAndz.decordesign;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.KevAndz.decordesign.controller.ChatAdapter;
import com.KevAndz.decordesign.controller.ChatRoomAdapter;
import com.KevAndz.decordesign.controller.PrefManager;
import com.KevAndz.decordesign.controller.URLS;
import com.KevAndz.decordesign.model.ChatModel;
import com.KevAndz.decordesign.model.ChatRoomModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatRoom extends AppCompatActivity {
    User user;
    EditText messageBox;
    ChatRoomAdapter adapter;
    List<ChatRoomModel> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Bundle bundle = getIntent().getExtras();
        user = PrefManager.getInstance(this).getUser();
        final int targetUser_id =  bundle.getInt("targetUser_id");

        TextView name = findViewById(R.id.messageSenderName);
        name.setText(bundle.getString("targetUser_name"));

        messageBox = (EditText) findViewById(R.id.messageText);

        ChatRoom.showChatData show = new ChatRoom.showChatData(targetUser_id);
        show.execute();

        findViewById(R.id.sdBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (! messageBox.getText().toString().equals("")){
                    ChatRoomModel chat = new ChatRoomModel();
                    chat.setReceiverId(String.valueOf(user.getId()));
                    chat.setReceiverImageUrl(user.getProf_img_url());
                    chat.setReceiverName(user.getName());
                    chat.setMessage(messageBox.getText().toString());
                    listData.add(chat);
                    adapter.notifyDataSetChanged();
                    new sendMessage(targetUser_id, messageBox.getText().toString()).execute();
                    messageBox.setText("");
                }else{
                    messageBox.setError("Enter Message!");
                    messageBox.requestFocus();
                }
            }
        });

    }
    class sendMessage extends AsyncTask<Void, Void, String> {

        int user_id;
        String message;
        sendMessage(int user_id, String message) {
            this.user_id = user_id;
            this.message = message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("jSON-Output-sendmessage", s);
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();

            params.put("sender_id", String.valueOf(user.getId()));
            params.put("receiver_id", String.valueOf(user_id));
            params.put("message", String.valueOf(message));

            //returing the response
            return requestHandler.sendPostRequest(URLS.URL_SENDCHAT, params);
        }
    }

    class showChatData extends AsyncTask<Void, Void, String> {
        ProgressBar progressBar;
        int user_id;
        showChatData(int user_id) {
            this.user_id = user_id;
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
            Log.d("jSON-Output-message", s);

            try {
                JSONObject obj = new JSONObject(s);

                if (!obj.getBoolean("error")) {

                    JSONArray jArray = obj.getJSONArray("user");
                    ListView lvData = findViewById(R.id.lvChatData);

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject objs = jArray.getJSONObject(i);
                        ChatRoomModel chat = new ChatRoomModel();
                        if(! objs.getString("message").equals("")){
                            chat.setReceiverId(objs.getString("receiver"));
                            chat.setReceiverImageUrl(objs.getString("receiver_image"));
                            chat.setReceiverName(objs.getString("receiver_name"));
                            chat.setMessage(objs.getString("message"));
                            listData.add(chat);
                        }
                    }

                    adapter = new ChatRoomAdapter(getApplicationContext(), R.layout.data_message_list_view, listData);
                    adapter.notifyDataSetChanged();
                    lvData.setAdapter(adapter);
                } else {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
            params.put("targetUser_id", String.valueOf(user_id));
            params.put("currentUser_id", String.valueOf(user.getId()));

            //returing the response
            return requestHandler.sendPostRequest(URLS.URL_CHATDATA, params);
        }
    }

}