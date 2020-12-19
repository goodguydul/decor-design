package com.KevAndz.decordesign;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import com.KevAndz.decordesign.controller.DesignerAdapter;
import com.KevAndz.decordesign.controller.PrefManager;
import com.KevAndz.decordesign.controller.URLS;
import com.KevAndz.decordesign.model.DesignerData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Search extends Fragment {

    EditText searchQ;

    public Search(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchQ = view.findViewById(R.id.searchQuery);

        view.findViewById(R.id.btnSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity().getApplicationContext(), searchQ.getText().toString(), Toast.LENGTH_SHORT).show();
                searchAction();
                hideSoftKeyboard();
            }
        });
        return view;
    }

    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    private void searchAction() {
        //first getting the values
        final String searchQuery = searchQ.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(searchQuery)) {
            searchQ.setError("Search Query cannot null !");
            searchQ.requestFocus();
            return;
        }

        if (searchQuery.length() < 3) {
            searchQ.setError("Minimum 3 Character!");
            searchQ.requestFocus();
            return;
        }

        //if everything is fine
        Search.searchActionClass ul = new Search.searchActionClass(searchQuery);
        ul.execute();
    }
    class searchActionClass extends AsyncTask<Void, Void, String> {

        String searchQuery;
        searchActionClass(String searchQuery) {
            this.searchQuery = searchQuery;
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
//                //if no error in response
                if (!obj.getBoolean("error")) {

                    Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONArray jArray = (JSONArray) obj.getJSONArray("user");
                    ListView lvData = (ListView) getActivity().findViewById(R.id.lvDataUser);
                    final User user = PrefManager.getInstance(getContext()).getUser();
                    final List<DesignerData> listData = new ArrayList<>();

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject objs = jArray.getJSONObject(i);
                        Log.d("added - ", objs.getString("name") + "-" + objs.getInt("id") );
                        DesignerData designer = new DesignerData();
                        designer.setId(objs.getInt("id"));
                        designer.setNames(objs.getString("name"));
                        designer.setUsername(objs.getString("username"));
                        designer.setEmail(objs.getString("email"));
                        designer.setPhone(objs.getString("phonenumber"));
                        designer.setGender(objs.getString("gender"));
                        designer.setBirthdate(objs.getString("birthdate"));
                        designer.setPictureimg_url(objs.getString("profileimg_url"));
                        designer.setCV_url(objs.getString("cv_url"));
                        listData.add(designer);
                    }
//                    Log.d("jArray Null", jArray.getString(2));
                    DesignerAdapter adapter = new DesignerAdapter(getContext(), R.layout.data_list_view, listData);
                    adapter.notifyDataSetChanged();
                    lvData.setAdapter(adapter);

                    lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            DesignerData designer = listData.get(position);
                            Intent intent;
                            if (designer.getId() == user.getId()){
                                Fragment mFragment = new Profile();
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.page_container, mFragment).commit();

                            }else{
                                intent = new Intent(view.getContext(), SearchDetail.class);
                                intent.putExtra("id", designer.getId());
                                intent.putExtra("names", designer.getNames());
                                intent.putExtra("email", designer.getEmail());
                                intent.putExtra("username", designer.getUsername());
                                intent.putExtra("gender", designer.getGender());
                                intent.putExtra("phone", designer.getPhone());
                                intent.putExtra("birthdate", designer.getBirthDate());
                                intent.putExtra("profileimg_url", designer.getPictureimg_url());
                                intent.putExtra("cv_url", designer.getCV_url());
                                startActivity(intent);
                            }

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
            params.put("searchQuery", searchQuery);

            //returing the response
            return requestHandler.sendPostRequest(URLS.URL_SEARCH, params);
        }
    }


}
