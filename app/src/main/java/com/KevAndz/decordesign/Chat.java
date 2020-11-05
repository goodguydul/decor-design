package com.KevAndz.decordesign;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.KevAndz.decordesign.controller.ChatAdapter;
import com.KevAndz.decordesign.model.ChatModel;
import com.KevAndz.decordesign.model.DesignerData;

import java.util.ArrayList;
import java.util.List;

public class Chat extends Fragment {

    public Chat() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        Chat.showChat show = new Chat.showChat();
        show.execute();

        return view;
    }

    class showChat extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            List<ChatModel> ListChat = new ArrayList<>();
            ChatModel cd1,cd2,cd3,cd4,cd5;

            ListView lvData = (ListView) getActivity().findViewById(R.id.lvDataChat);
            List<DesignerData> listData =new ArrayList<>();

            cd1 = new ChatModel();
            cd1.setSenderImage(R.drawable.user);
            cd1.setSenderName("[Designer] Kevin 1");
            cd1.setMessage("Hello, lorem ipsum.......");
            ListChat.add(cd1);

            cd2 = new ChatModel();
            cd2.setSenderImage(R.drawable.user);
            cd2.setSenderName("[Designer] Kevin 2");
            cd2.setMessage("Hello, lorem ipsum.......");
            ListChat.add(cd2);

            cd3 = new ChatModel();
            cd3.setSenderImage(R.drawable.user);
            cd3.setSenderName("[Designer] Kevin 3");
            cd3.setMessage("Hello, lorem ipsum.......");
            ListChat.add(cd3);

            cd4 = new ChatModel();
            cd4.setSenderImage(R.drawable.user);
            cd4.setSenderName("[Designer] Kevin 4");
            cd4.setMessage("Hello, lorem ipsum.......");
            ListChat.add(cd4);

            cd5 = new ChatModel();
            cd5.setSenderImage(R.drawable.user);
            cd5.setSenderName("[Designer] Kevin 5");
            cd5.setMessage("Hello, lorem ipsum.......");
            ListChat.add(cd5);

            ChatAdapter adapter = new ChatAdapter(getContext(), R.layout.data_chat_list_view, ListChat);
            adapter.notifyDataSetChanged();
            lvData.setAdapter(adapter);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }


}