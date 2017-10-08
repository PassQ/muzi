package com.ocwvar.muzi.Adapters.HXSoundAdapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters
 * Date: 2016/7/7  12:56
 * Project: Muzi
 * 和谐之声ViewPager适配器
 */
public class HXPagerAdapter extends PagerAdapter {

    //服务投诉 , 服务求助 , 合理化建议  三个显示列表
    RecyclerView questionList, suggestList, serviceList;
    QuestionAdapter questionAdapter;
    ServiceAdapter serviceAdapter;
    SuggestAdapter suggestAdapter;

    @Override
    public int getCount() {
        return 3;
    }

    public void setServiceAdapter(ServiceAdapter serviceAdapter) {
        this.serviceAdapter = serviceAdapter;
    }

    public void setSuggestAdapter(SuggestAdapter suggestAdapter) {
        this.suggestAdapter = suggestAdapter;
    }

    public void setQuestionAdapter(QuestionAdapter questionAdapter) {
        this.questionAdapter = questionAdapter;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        switch (position) {
            case 0:
                if (questionList == null) {
                    initQuestionList(container.getContext());
                }
                if (questionList != null) {
                    container.addView(questionList);
                    return questionList;
                } else {
                    return null;
                }
            case 1:
                if (serviceList == null) {
                    initServiceListList(container.getContext());
                }
                if (serviceList != null) {
                    container.addView(serviceList);
                    return serviceList;
                } else {
                    return null;
                }
            case 2:
                if (suggestList == null) {
                    initSuggestList(container.getContext());
                }
                if (suggestList != null) {
                    container.addView(suggestList);
                    return suggestList;
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
                container.removeView(questionList);
                break;
            case 1:
                container.removeView(serviceList);
                break;
            case 2:
                container.removeView(suggestList);
                break;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "服务投诉";
            case 1:
                return "服务求助";
            case 2:
                return "合理化建议";
            default:
                return "未定义";
        }
    }

    private void initSuggestList(Context context) {
        if (suggestAdapter != null) {
            suggestList = new RecyclerView(context);
            suggestList.setAdapter(suggestAdapter);
            suggestList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            suggestList.setHasFixedSize(true);
        }
    }

    private void initQuestionList(Context context) {
        if (questionAdapter != null) {
            questionList = new RecyclerView(context);
            questionList.setAdapter(questionAdapter);
            questionList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            questionList.setHasFixedSize(true);
        }
    }

    private void initServiceListList(Context context) {
        if (serviceAdapter != null) {
            serviceList = new RecyclerView(context);
            serviceList.setAdapter(serviceAdapter);
            serviceList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            serviceList.setHasFixedSize(true);
        }
    }

}
