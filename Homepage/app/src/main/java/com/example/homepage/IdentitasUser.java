package com.example.homepage;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class IdentitasUser extends Fragment {

    TextView username, nama_lengkap, detail_blog, detail_company, detail_location, detail_following, detail_follower;
    ImageView photo1;

    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_user_current, container, false);
        setHasOptionsMenu(true);

        progressBar = view.findViewById(R.id.progressBarLoading);
        username = view.findViewById(R.id.detail_username);
        nama_lengkap = view.findViewById(R.id.detail_nama);
        detail_blog = view.findViewById(R.id.detail_blog);
        detail_company = view.findViewById(R.id.detail_company);
        detail_location = view.findViewById(R.id.detail_location);
        photo1 = view.findViewById(R.id.detail_image);
        detail_following = view.findViewById(R.id.detail_following);
        detail_follower = view.findViewById(R.id.detail_follower);
        String USER_KEY = "username";
        String name = this.getArguments().getString(USER_KEY);
        username.setText(name);
        progressBar.setVisibility(View.VISIBLE);

        final AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "54fe0f28d7f5727805421fe1fe50e8f71768bc4b");
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
                            Glide.with(getActivity()).load(photo).into(photo1);

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
                                errorMessage = statusCode + " : " + error.getMessage();
                                break;
                        }
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}