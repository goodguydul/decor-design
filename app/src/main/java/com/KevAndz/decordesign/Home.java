package com.KevAndz.decordesign;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.KevAndz.decordesign.controller.FeedAdapter;
import com.KevAndz.decordesign.controller.DesignerAdapter;
import com.KevAndz.decordesign.controller.PrefManager;
import com.KevAndz.decordesign.controller.URLS;
import com.KevAndz.decordesign.model.FeedModel;
import com.KevAndz.decordesign.model.DesignerData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Home extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final User user = PrefManager.getInstance(getActivity().getApplicationContext()).getUser();
        ProgressBar progressBar = view.findViewById(R.id.progressBar) ;
        Home.showPost show = new Home.showPost(user.getId(), progressBar);
        show.execute();


        view.findViewById(R.id.refreshFeed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(Home.this).attach(Home.this).commit();
            }
        });

        view.findViewById(R.id.addPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PostImage.class);
                intent.putExtra("user_id", user.getId());

                startActivityForResult(intent, 10009);
            }
        });


        return view;
    }

    class showPost extends AsyncTask<Void, Void, String> {

        int user_id;
        ProgressBar progressBar;
        showPost(int user_id, ProgressBar progressBar ) {
            this.user_id = user_id;
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(s);
            Log.d("jSON-Output", s);

            try {
                JSONObject obj = new JSONObject(s);

                if (!obj.getBoolean("error")) {

                    JSONArray jArray = obj.getJSONArray("user");
                    ListView lvData = getActivity().findViewById(R.id.feedListView);
                    final User user = PrefManager.getInstance(getContext()).getUser();
                    final List<FeedModel> listData = new ArrayList<>();

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject objs = jArray.getJSONObject(i);

                        FeedModel feed = new FeedModel();
                        feed.setAuthorId(String.valueOf(objs.getInt("user_id")));
                        feed.setAuthorImgUrl(objs.getString("user_image"));
                        feed.setAuthorName(objs.getString("user_name"));
                        feed.setCaption(objs.getString("caption"));
                        feed.setPostImageUrl(objs.getString("img_url"));
                        feed.setLike(objs.getString("likes") + " Likes");
                        feed.setPostDate(objs.getString("postDate"));
                        listData.add(feed);
                    }

                    FeedAdapter adapter = new FeedAdapter(getContext(), R.layout.data_feed_list, listData);
                    adapter.notifyDataSetChanged();
                    lvData.setAdapter(adapter);

                    lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            FeedModel feed = listData.get(position);
                            Intent intent;
                            intent = new Intent(view.getContext(), PostDetails.class);
                            intent.putExtra("targetUser_id", feed.getAuthorId());

                            startActivity(intent);
                        }
                    });
                } else {
//                    Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
            return requestHandler.sendPostRequest(URLS.URL_FETCHFEED, params);
        }
    }

}