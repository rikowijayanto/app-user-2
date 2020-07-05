package com.example.homepage;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


public class UserDetail extends AppCompatActivity{
    private final String USER_KEY = "username";
    String nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        nama = getIntent().getStringExtra(USER_KEY);
        Bundle bundle = new Bundle();
        bundle.putString(USER_KEY, nama);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        IdentitasUser identitasUser = new IdentitasUser();
        identitasUser.setArguments(bundle);


        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);




    }

}