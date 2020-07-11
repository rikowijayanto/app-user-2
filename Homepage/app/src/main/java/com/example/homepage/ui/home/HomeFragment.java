package com.example.homepage.ui.home;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.homepage.EmptyResult;
import com.example.homepage.ListUserAdapter;
import com.example.homepage.R;
import com.example.homepage.SearchPage;
import com.example.homepage.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import java.util.ArrayList;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;
public class HomeFragment extends Fragment {
    private ProgressBar progressBar;
    private ArrayList <User> list = new ArrayList<>();
    private RecyclerView rvUser;

    private ArrayList <User> listUser = new ArrayList<>();
    ListUserAdapter listUserAdapter;

    private String query = "riko";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = root.findViewById(R.id.progressBarLoading);
        rvUser = root.findViewById(R.id.rv_user);
        rvUser.setHasFixedSize(true);
        showRecyclerList();
        return root;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater ) {
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

    private void showRecyclerList() {
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
                        user.setName(itemsArray.getJSONObject(i).getString("login"));
                        listUser.add(user);
                    }

                    if (listUser.isEmpty()) {
                        Intent intent = new Intent(getActivity(), EmptyResult.class);
                        startActivity(intent);
                    } else {
                        rvUser.setLayoutManager(new LinearLayoutManager(getActivity()));
                        listUserAdapter = new ListUserAdapter(listUser);
                        rvUser.setAdapter(listUserAdapter);
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
}