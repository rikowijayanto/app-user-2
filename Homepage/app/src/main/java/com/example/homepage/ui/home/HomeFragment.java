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

import com.example.homepage.ListUserAdapter;
import com.example.homepage.R;
import com.example.homepage.SearchPage;
import com.example.homepage.User;

import java.util.ArrayList;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ProgressBar;


public class HomeFragment extends Fragment {
    private ProgressBar progressBar;
    private ArrayList <User> list = new ArrayList<>();
    private RecyclerView rvUser;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar = root.findViewById(R.id.progressBarLoading);
        rvUser = root.findViewById(R.id.rv_user);
        rvUser.setHasFixedSize(true);

        list.addAll(getListUsers());
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
        rvUser.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        ListUserAdapter listUserAdapter = new ListUserAdapter(list);
        rvUser.setAdapter(listUserAdapter);
        progressBar.setVisibility(View.INVISIBLE);
    }


    public ArrayList <User>  getListUsers () {
        progressBar.setVisibility(View.VISIBLE);
        String[] dataName = getResources().getStringArray(R.array.username);
        String[] dataPhoto = getResources().getStringArray(R.array.avatar);

        ArrayList <User> listUser = new ArrayList<>();

        for (int i = 0; i < dataName.length; i++) {
            User user = new User();
            user.setName(dataName[i]);
            user.setPhoto(dataPhoto[i]);
            listUser.add(user);

        }

        return listUser;

    }
}