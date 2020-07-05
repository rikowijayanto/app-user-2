package com.example.homepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchPage extends AppCompatActivity {

    public static String EXTRA_QUERY = " ";
    private ProgressBar progressBar;

    private ArrayList <User> listUser = new ArrayList<>();
    private RecyclerView rvUser;
    ListUserAdapter listUserAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary)); //status bar or the time bar at the top
        }

        progressBar = findViewById(R.id.progressBarLoading);

        progressBar.setVisibility(View.INVISIBLE);
        rvUser = findViewById(R.id.rv_user);
        rvUser.setHasFixedSize(true);
        showRecyclerList();

    }




    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);


        SearchView searchView = (SearchView) (menu.findItem(R.id.action_search)).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(SearchPage.this, SearchPage.class);
                intent.putExtra(SearchPage.EXTRA_QUERY, query);
                startActivity(intent);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }


    public void showRecyclerList () {


        progressBar.setVisibility(View.VISIBLE);
        String query = getIntent().getStringExtra(SearchPage.EXTRA_QUERY);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "888cca33a72212b23a59c6453ebd573efa9eaf44");
        client.addHeader("User-Agent", "request");
        String URL = "https://api.github.com/search/users?q="+query.toLowerCase();

        client.get(URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressBar.setVisibility(View.INVISIBLE);
                String result = new String(responseBody);
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray itemsArray = jsonObject.getJSONArray("items");

                    for (int i=0; i < itemsArray.length(); i++) {
                        User user = new User ();
                        user.setPhoto(itemsArray.getJSONObject(i).getString("avatar_url"));
                        user.setName(itemsArray.getJSONObject(i).getString("login"));
                        listUser.add(user);
                    }

                    if (listUser.isEmpty()) {
                        Intent intent = new Intent(SearchPage.this, EmptyResult.class);
                        startActivity(intent);
                    } else {
                        rvUser.setLayoutManager(new LinearLayoutManager(SearchPage.this));
                        listUserAdapter = new ListUserAdapter(listUser);
                        rvUser.setAdapter(listUserAdapter);
                        progressBar.setVisibility(View.INVISIBLE);
                    }


                } catch (Exception e) {
                    Toast.makeText(SearchPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SearchPage.this, errorMessage, Toast.LENGTH_SHORT).show();

            }
        });
   }


}