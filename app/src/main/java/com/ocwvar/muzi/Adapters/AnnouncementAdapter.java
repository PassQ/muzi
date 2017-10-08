package com.ocwvar.muzi.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.ocwvar.muzi.Adapters.AnnouncementExpandableAdapters.CommunityExpAdapter;
import com.ocwvar.muzi.Adapters.AnnouncementExpandableAdapters.CompanyExpAdapter;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters
 * Date: 2016/5/18  16:54
 * Project: Muzi
 */
public class AnnouncementAdapter extends PagerAdapter {

    private ExpandableListView communityAnn;
    private ExpandableListView companyAnn;
    private CommunityExpAdapter communityExpAdapter;
    private CompanyExpAdapter companyExpAdapter;
    private ExpandableListView.OnChildClickListener onChildClickListener;

    @Override
    public int getCount() {
        return 2;
    }

    /**
     * 设置小区公告列表适配器
     *
     * @param communityExpAdapter
     */
    public void setCommunityExpAdapter(CommunityExpAdapter communityExpAdapter) {
        this.communityExpAdapter = communityExpAdapter;
    }

    /**
     * 设置公司公告列表适配器
     *
     * @param companyExpAdapter
     */
    public void setCompanyExpAdapter(CompanyExpAdapter companyExpAdapter) {
        this.companyExpAdapter = companyExpAdapter;
    }

    public void setOnChildClickListener(ExpandableListView.OnChildClickListener onChildClickListener) {
        this.onChildClickListener = onChildClickListener;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        switch (position) {
            case 0:
                if (communityAnn == null) {
                    initCommunityAnn(container.getContext());
                }
                container.addView(communityAnn);
                return communityAnn;
            case 1:
                if (companyAnn == null) {
                    initCompanyAnn(container.getContext());
                }
                container.addView(companyAnn);
                return companyAnn;
            default:
                return new View(container.getContext());
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ExpandableListView) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "本区公告";
            case 1:
                return "公司公告";
            default:
                return "未定义";
        }
    }

    /**
     * 初始化小区公告折叠列表
     *
     * @param context ViewPager的Context
     */
    private void initCommunityAnn(Context context) {
        communityAnn = new ExpandableListView(context);
        communityAnn.setAdapter(communityExpAdapter);
        communityAnn.setOnChildClickListener(onChildClickListener);
        communityAnn.setTag("communityAnn");
    }

    /**
     * 初始化公司公告折叠列表
     *
     * @param context ViewPager的Context
     */
    private void initCompanyAnn(Context context) {
        companyAnn = new ExpandableListView(context);
        companyAnn.setAdapter(companyExpAdapter);
        companyAnn.setOnChildClickListener(onChildClickListener);
        companyAnn.setTag("companyAnn");
    }


}
