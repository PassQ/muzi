package com.ocwvar.muzi.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters
 * Date: 2016/5/9  9:08
 * Project: Muzi
 * 保修界面VP页面的适配器
 */
public class RepireViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> pagers;
    ArrayList<String> titles;

    public RepireViewPagerAdapter(FragmentManager fm) {
        super(fm);
        pagers = new ArrayList<>();
        titles = new ArrayList<>();
    }

    /**
     * 添加Fragment页面
     *
     * @param fragment 要添加的Fragment
     * @param title    页面的标题
     */
    public void addFragment(Fragment fragment, String title) {
        if (fragment != null && !TextUtils.isEmpty(title)) {
            pagers.add(fragment);
            titles.add(title);
            notifyDataSetChanged();
        }
    }

    @Override
    public Fragment getItem(int position) {
        return pagers.get(position);
    }

    @Override
    public int getCount() {
        return pagers.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
