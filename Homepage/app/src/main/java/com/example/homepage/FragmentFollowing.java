package com.example.homepage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class FragmentFollowing extends Fragment {

    private ArrayList<User> listUser = new ArrayList<>();
    private RecyclerView rvUser;
    ListUserAdapter listUserAdapter;
    ProgressBar progressBar;


    public FragmentFollowing() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.layout_home, container, false);
        rvUser = view.findViewById(R.id.rv_user);
        rvUser.setHasFixedSize(true);
        progressBar = view.findViewById(R.id.progressBarLoading);

        String query = this.getArguments().getString("username");

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "54fe0f28d7f5727805421fe1fe50e8f71768bc4b");
        client.addHeader("User-Agent", "request");
        String URL = "https://api.github.com/users/"+query.toLowerCase()+"/following";

        client.get(URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressBar.setVisibility(View.INVISIBLE);
                String result = new String(responseBody);
                try {

                    JSONArray jsonArray = new JSONArray(result);

                    for (int i=0; i < jsonArray.length(); i++) {
                        User user = new User ();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        user.setPhoto(jsonObject.getString("avatar_url"));
                        user.setName(jsonObject.getString("login"));
                        listUser.add(user);
                    }


                    rvUser.setLayoutManager(new LinearLayoutManager(getActivity()));
                    listUserAdapter = new ListUserAdapter(listUser);
                    rvUser.setAdapter(listUserAdapter);
                    progressBar.setVisibility(View.INVISIBLE);



                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.INVISIBLE);
                String errorMessage;
                switch (statusCode) {
                    case 401:
                        errorMessage = statusCode + " : Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + " : Forbidden";
                        break;
                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;
                    default:
                        errorMessage =  statusCode + " : " + error.getMessage();
                        break;
                }
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();

            }
        });


        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}