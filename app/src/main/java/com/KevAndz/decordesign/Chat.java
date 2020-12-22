package com.KevAndz.decordesign;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.KevAndz.decordesign.controller.ChatAdapter;
import com.KevAndz.decordesign.controller.PrefManager;
import com.KevAndz.decordesign.controller.URLS;
import com.KevAndz.decordesign.model.ChatModel;
import com.KevAndz.decordesign.model.DesignerData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Chat extends Fragment {

    public Chat() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        User user = PrefManager.getInstance(getActivity().getApplicationContext()).getUser();

        Chat.showChat show = new Chat.showChat(user.getId());
        show.execute();

        return view;
    }

    class showChat extends AsyncTask<Void, Void, String> {

        int user_id;
        showChat(int user_id) {
            this.user_id = user_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("jSON-Output", s);

            try {
                JSONObject obj = new JSONObject(s);

                if (!obj.getBoolean("error")) {

//                    Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONArray jArray = obj.getJSONArray("user");
                    ListView lvData = getActivity().findViewById(R.id.lvDataChat);
                    final User user = PrefManager.getInstance(getContext()).getUser();
                    final List<ChatModel> listData = new ArrayList<>();

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject objs = jArray.getJSONObject(i);
                        Log.d("added - ", objs.getString("receiver_name") + "-" + objs.getInt("receiver") );
                        ChatModel chat = new ChatModel();
                        chat.setReceiverId(objs.getInt("receiver"));
                        chat.setReceiverImageUrl(objs.getString("receiver_image"));
                        chat.setReceiverName(objs.getString("receiver_name"));
                        chat.setMessage(objs.getString("message"));
                        listData.add(chat);
                    }

                    ChatAdapter adapter = new ChatAdapter(getContext(), R.layout.data_chat_list_view, listData);
                    adapter.notifyDataSetChanged();
                    lvData.setAdapter(adapter);

                    lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ChatModel chat = listData.get(position);
                            Intent intent;
                            intent = new Intent(view.getContext(), ChatRoom.class);
                            intent.putExtra("targetUser_id", chat.getReceiverId());
                            intent.putExtra("targetUser_name", chat.getReceiverName());

                            startActivity(intent);
                        }
                    });
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
            params.put("sender_id", String.valueOf(user_id));

            //returing the response
            return requestHandler.sendPostRequest(URLS.URL_CHATLIST, params);
        }
    }


}