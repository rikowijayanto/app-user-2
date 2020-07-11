package com.example.homepage.ui.dashboard;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.homepage.EmptyResult;
import com.example.homepage.GridUserAdapter;
import com.example.homepage.R;
import com.example.homepage.SearchPage;
import com.example.homepage.User;
import com.example.homepage.ui.home.HomeFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class DashboardFragment extends Fragment {

    private ArrayList <User> list = new ArrayList<>();
    private RecyclerView rvUser;
    private ArrayList <User> listUser = new ArrayList<>();
    private String query = "riko";
    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_feeds, container, false);
        progressBar = root.findViewById(R.id.progressBarLoading);
        rvUser = root.findViewById(R.id.rv_grid);
        showRecyclerGrid();
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);


        SearchView searchView = (SearchView) (menu.findItem(R.id.action_search)).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getActivity(), SearchPage.class);
                intent.putExtra(SearchPage.EXTRA_QUERY, query);
                startActivity(intent);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void showRecyclerGrid () {

        progressBar.setVisibility(View.VISIBLE);
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
                        listUser.add(user);
                    }

                    if (listUser.isEmpty()) {
                        Intent intent = new Intent(getActivity(), EmptyResult.class);
                        startActivity(intent);
                    } else {
                        rvUser.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        GridUserAdapter gridUserAdapter = new GridUserAdapter(listUser);
                        rvUser.setAdapter(gridUserAdapter);
                        progressBar.setVisibility(View.INVISIBLE);
                    }


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

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.keluar) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

        return true;
    }
}