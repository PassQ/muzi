package com.ocwvar.muzi.Adapters;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.ocwvar.muzi.Adapters.CommunityCultureRecycleAdapters.AnnCulturalParkAdapter;
import com.ocwvar.muzi.Adapters.CommunityCultureRecycleAdapters.CommunityCultureAdapter;
import com.ocwvar.muzi.Adapters.CommunityCultureRecycleAdapters.TraditionalCultureAdapter;


/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters
 * Date: 2016/5/11  20:40
 * Project: Muzi
 * 小区文化 PageAdapter
 */

public class CommunityCultureListAdapter extends PagerAdapter {

    //用于显示的三个ListView
    private RecyclerView[] recyclerViews;
    private Object[] adapters;

    public CommunityCultureListAdapter() {

        recyclerViews = new RecyclerView[]{
                null, null, null
        };

        adapters = new Object[]{
                null, null, null
        };

    }

    /**
     * 设置每个RecyclerView的Adapter
     *
     * @param position 要设置的位置
     * @param adapter  要设置的Adapter
     */
    public void setRecyclerViewAdapter(int position, RecyclerView.Adapter adapter) {
        adapters[position] = adapter;
    }

    @Override
    public int getCount() {
        return recyclerViews.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RecyclerView recyclerView = recyclerViews[position];
        if (recyclerView == null) {
            recyclerView = new RecyclerView(container.getContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setHasFixedSize(true);
            switch (position) {
                case 0:
                    recyclerView.setAdapter((CommunityCultureAdapter) adapters[position]);
                    break;
                case 1:
                    recyclerView.setAdapter((AnnCulturalParkAdapter) adapters[position]);
                    break;
                case 2:
                    recyclerView.setAdapter((TraditionalCultureAdapter) adapters[position]);
                    break;
            }
        }
        container.addView(recyclerView);
        return recyclerView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RecyclerView) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "社区文化";
            case 1:
                return "安和文化园";
            case 2:
                return "传统文化";
            default:
                return "未定义";
        }
    }

}
