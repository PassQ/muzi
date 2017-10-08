package com.ocwvar.muzi.Adapters.HelpingAdapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters
 * Date: 2016/6/13  12:56
 * Project: Muzi
 * 爱心互助ViewPager适配器
 */
public class HelpingAdapter extends PagerAdapter {

    //我的爱心 , 我的困难 两个显示列表
    RecyclerView myHelping, myAsking, contribution;
    MyHelpingAdapter helpingAdapter;
    MyAskingAdapter askingAdapter;
    ContributionAdapter contributionAdapter;

    @Override
    public int getCount() {
        return 3;
    }

    public void setAskingAdapter(MyAskingAdapter askingAdapter) {
        this.askingAdapter = askingAdapter;
    }

    public void setHelpingAdapter(MyHelpingAdapter helpingAdapter) {
        this.helpingAdapter = helpingAdapter;
    }

    public void setContribuAdapter(ContributionAdapter contribuAdapter) {
        this.contributionAdapter = contribuAdapter;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        switch (position) {
            case 0:
                if (myHelping == null) {
                    initHelpingList(container.getContext());
                }
                if (myHelping != null) {
                    container.addView(myHelping);
                    return myHelping;
                } else {
                    return null;
                }
            case 1:
                if (myAsking == null) {
                    initAskingList(container.getContext());
                }
                if (myAsking != null) {
                    container.addView(myAsking);
                    return myAsking;
                } else {
                    return null;
                }
            case 2:
                if (contribution == null) {
                    initContriList(container.getContext());
                }
                if (contribution != null) {
                    container.addView(contribution);
                    return contribution;
                } else {
                    return null;
                }
            default:
                return null;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        switch (position) {
            case 0:
                container.removeView(myHelping);
                break;
            case 1:
                container.removeView(myAsking);
                break;
            case 2:
                container.removeView(contribution);
                break;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "我献爱心";
            case 1:
                return "我的困难";
            case 2:
                return "公益展示";
            default:
                return "未定义";
        }
    }

    private void initHelpingList(Context context) {
        if (helpingAdapter != null) {
            myHelping = new RecyclerView(context);
            myHelping.setAdapter(helpingAdapter);
            myHelping.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            myHelping.setHasFixedSize(true);
        }
    }

    private void initAskingList(Context context) {
        if (askingAdapter != null) {
            myAsking = new RecyclerView(context);
            myAsking.setAdapter(askingAdapter);
            myAsking.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            myAsking.setHasFixedSize(true);
        }
    }

    private void initContriList(Context context) {
        if (contributionAdapter != null) {
            contribution = new RecyclerView(context);
            contribution.setAdapter(contributionAdapter);
            contribution.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            contribution.setHasFixedSize(true);
        }
    }

}
