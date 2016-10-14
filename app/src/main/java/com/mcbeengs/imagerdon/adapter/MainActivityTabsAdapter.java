package com.mcbeengs.imagerdon.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.mcbeengs.imagerdon.fragment.ArtistsFragment;
import com.mcbeengs.imagerdon.fragment.StatisticsFragment;
import com.mcbeengs.imagerdon.fragment.TasksFragment;

/**
 * Created by McBeengs on 02/10/2016.
 */

public class MainActivityTabsAdapter extends FragmentPagerAdapter {

    private Context context;

    public MainActivityTabsAdapter(Context context, android.support.v4.app.FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public String getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Tasks";
            case 1:
                return "Artists";
            case 2:
                return "Statistics";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TasksFragment();
            case 1:
                return  new ArtistsFragment();
            case 2:
                return new StatisticsFragment();
            default:
                return null;
        }
    }
}
