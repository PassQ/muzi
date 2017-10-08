package com.ocwvar.muzi.Adapters.RepireTaskAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ocwvar.muzi.Fragments.RepireMan.RepiredFragment;
import com.ocwvar.muzi.Fragments.RepireMan.UnRepiredFragment;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters.RepireTaskAdapters
 * Data: 2016/7/5 19:43
 * Project: Muzi
 */
public class RepirePageAdapter extends FragmentPagerAdapter {

    RepiredFragment page1;
    UnRepiredFragment page2;

    public RepirePageAdapter(FragmentManager fm, RepiredFragment page1, UnRepiredFragment page2) {
        super(fm);
        this.page1 = page1;
        this.page2 = page2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return page1;
            case 1:
                return page2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "已受理";
            case 1:
                return "未受理";
            default:
                return "未定义";
        }
    }
}
