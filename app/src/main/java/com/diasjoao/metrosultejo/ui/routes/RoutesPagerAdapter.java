package com.diasjoao.metrosultejo.ui.routes;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class RoutesPagerAdapter extends FragmentPagerAdapter {

    private final String[] lineNames = {"LINHA 1", "LINHA 2", "LINHA 3"};

    public RoutesPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return RoutesFragment.newInstance(lineNames[position]);
    }

    @Override
    public int getCount() {
        return lineNames.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return lineNames[position];
    }
}
