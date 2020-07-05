package com.example.homepage.ui.notifications;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.homepage.R;
import com.example.homepage.SearchPage;
import com.example.homepage.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class NotificationsFragment extends Fragment {

    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        Bundle bundle = new Bundle();
        bundle.putString("username", "rikowijayanto");

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getActivity(), getActivity().getSupportFragmentManager(), bundle);
        ViewPager viewPager = root.findViewById(R.id.view_pager1);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = root.findViewById(R.id.tabs_1);
        tabs.setupWithViewPager(viewPager);


        return root;
    }
}