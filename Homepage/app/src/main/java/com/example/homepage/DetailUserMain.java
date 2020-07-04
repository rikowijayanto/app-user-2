package com.example.homepage;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
    //private final String USER_IMAGE = "image";
    TextView username;
    ProgressBar progressBar;
    ImageView photo1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user_main);

        username = findViewById(R.id.detail_username);
        photo1 = findViewById(R.id.detail_image);


        String name = getIntent().getStringExtra(USER_KEY);
        username.setText(name);



        final AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "888cca33a72212b23a59c6453ebd573efa9eaf44");
        client.addHeader("User-Agent", "request");
        String URL = "https://api.github.com/users/"+name.toLowerCase();

        client.get(URL, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressBar.setVisibility(View.INVISIBLE);
                String result = new String(responseBody);
                try {

                    JSONObject responseObject = new JSONObject(result);

                    //photo loaded
                    String photo = responseObject.getString("avatar_url");
                    Glide.with(DetailUserMain.this).load(photo).into(photo1);

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
}