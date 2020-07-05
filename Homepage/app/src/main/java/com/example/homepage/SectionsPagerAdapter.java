package com.example.homepage;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    private Bundle bundle;


    public SectionsPagerAdapter(Context context, FragmentManager fm, Bundle bundle) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
        this.bundle = bundle;


    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.tab3,
            R.string.tab1,
            R.string.tab2
};


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {

            case 0:
                fragment = new IdentitasUser();
                fragment.setArguments(bundle);
                break;

            case 1:
                fragment = new FragmentFollowing();
                fragment.setArguments(bundle);
                break;


            case 2:
                fragment = new FragmentFollower();
                fragment.setArguments(bundle);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
}
