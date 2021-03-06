package com.bawei.huangminghuan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Huangminghuan on 2017/6/2.
 */

public class MyFragmentPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public MyFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyFragmentPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragments.get(arg0);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}

