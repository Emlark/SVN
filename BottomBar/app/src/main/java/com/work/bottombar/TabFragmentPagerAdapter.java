package com.work.bottombar;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */

public class TabFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private FragmentManager myfragmentManager;
    private List<Fragment> mlist;
    private  String[] titles;
    public TabFragmentPagerAdapter(FragmentManager fm,List<Fragment> list,String[] titles) {
        super(fm);
        this.mlist = list;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mlist.get(position);
    }
    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }


}


