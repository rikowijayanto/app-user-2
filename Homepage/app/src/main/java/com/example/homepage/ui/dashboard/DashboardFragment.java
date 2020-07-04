package com.example.homepage.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homepage.GridUserAdapter;
import com.example.homepage.R;
import com.example.homepage.User;

import java.util.ArrayList;
import java.util.Collection;

public class DashboardFragment extends Fragment {

    private ArrayList <User> list = new ArrayList<>();
    private RecyclerView rvUser;

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);


        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_feeds, container, false);
        

        rvUser = root.findViewById(R.id.rv_grid);

        list.addAll(getListUsers());
        showRecyclerGrid();
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public ArrayList <User>  getListUsers () {

        String[] dataPhoto = getResources().getStringArray(R.array.avatar);

        ArrayList <User> listUser = new ArrayList<>();

        for (int i = 0; i < dataPhoto.length; i++) {
            User user = new User();
            user.setPhoto(dataPhoto[i]);

            listUser.add(user);

        }

        return listUser;

    }

    private void showRecyclerGrid() {

        rvUser.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        GridUserAdapter gridUserAdapter = new GridUserAdapter(list);
        rvUser.setAdapter(gridUserAdapter);
    }
}