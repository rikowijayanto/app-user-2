package com.example.homepage.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homepage.ListUserAdapter;
import com.example.homepage.R;
import com.example.homepage.User;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ArrayList <User> list = new ArrayList<>();

    private HomeViewModel homeViewModel;
    private RecyclerView rvUser;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        rvUser = root.findViewById(R.id.rv_user);
        rvUser.setHasFixedSize(true);

        list.addAll(getListUsers());
        showRecyclerList();



        return root;
    }

    private void showRecyclerList() {

        rvUser.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        ListUserAdapter listUserAdapter = new ListUserAdapter(list);
        rvUser.setAdapter(listUserAdapter);
    }


    public ArrayList <User>  getListUsers () {

        String[] dataName = getResources().getStringArray(R.array.data_name);
        String[] dataDescription = getResources().getStringArray(R.array.data_description);
        String[] dataPhoto = getResources().getStringArray(R.array.data_photo);

        ArrayList <User> listUser = new ArrayList<>();

        for (int i = 0; i < dataName.length; i++) {
            User user = new User();
            user.setName(dataName[i]);
            user.setDescription(dataDescription[i]);
            user.setPhoto(dataPhoto[i]);

            listUser.add(user);

        }

        return listUser;

    }
}