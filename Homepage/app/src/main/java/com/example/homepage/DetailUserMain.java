package com.example.homepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
public class DetailUserMain extends AppCompatActivity {

    private final String USER_KEY = "username";
    TextView username, nama_lengkap, detail_blog, detail_company, detail_location, detail_following, detail_follower;
    ProgressBar progressBar;
    ImageView photo1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user_main);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary)); //status bar or the time bar at the top
        }

        username = findViewById(R.id.detail_username);
        nama_lengkap = findViewById(R.id.detail_nama);
        detail_blog = findViewById(R.id.detail_blog);
        detail_company = findViewById(R.id.detail_company);
        detail_location = findViewById(R.id.detail_location);
        photo1 = findViewById(R.id.detail_image);
        detail_following = findViewById(R.id.detail_following);
        detail_follower = findViewById(R.id.detail_follower);


        String name = getIntent().getStringExtra(USER_KEY);
        username.setText(name);



        final AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "888cca33a72212b23a59c6453ebd573efa9eaf44");
        client.addHeader("User-Agent", "request");
        String URL = "https://api.github.com/users/"+name.toLowerCase();

        client.get(URL, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //progressBar.setVisibility(View.INVISIBLE);
                String result = new String(responseBody);
                try {

                    JSONObject responseObject = new JSONObject(result);

                    //photo loaded
                    String photo = responseObject.getString("avatar_url");
                    Glide.with(DetailUserMain.this).load(photo).into(photo1);

                    //nama loaded
                    String nama = responseObject.getString("name");
                    nama_lengkap.setText(nama);

                    //blog loade
                    String blog_nama = responseObject.getString("blog");

                    if (TextUtils.isEmpty(blog_nama) || blog_nama == null || blog_nama.length() == 0) {
                        detail_blog.setText(": -");
                    }
                    detail_blog.setText(": "+blog_nama);

                    //company laoded
                    String company_nama = responseObject.getString("company");

                    if (company_nama == "" || company_nama == "null") {
                        detail_company.setText(": -");
                    }
                    detail_company.setText(": "+company_nama);

                    //location loaded
                    String location_nama = responseObject.getString("location");

                    if (location_nama == "" || location_nama ==  null) {
                        detail_location.setText(": -");
                    }
                    detail_location.setText(": "+location_nama);

                    //follower loaded
                    String follower = responseObject.getString("followers");
                    detail_follower.setText(": "+follower);

                    //following laoded
                    String following = responseObject.getString("following");
                    detail_following.setText(": "+following);



                } catch (Exception e) {
                    Toast.makeText(DetailUserMain.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        errorMessage = statusCode + " : " + error.getMessage();
                        break;
                }
                Toast.makeText(DetailUserMain.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        }
        );
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
                Intent intent = new Intent(DetailUserMain.this, SearchPage.class);
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
}