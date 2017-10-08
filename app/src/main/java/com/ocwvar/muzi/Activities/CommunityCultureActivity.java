package com.ocwvar.muzi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.ocwvar.muzi.Adapters.CommunityCultureListAdapter;
import com.ocwvar.muzi.Adapters.CommunityCultureRecycleAdapters.AnnCulturalParkAdapter;
import com.ocwvar.muzi.Adapters.CommunityCultureRecycleAdapters.CommunityCultureAdapter;
import com.ocwvar.muzi.Adapters.CommunityCultureRecycleAdapters.TraditionalCultureAdapter;
import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Network.ArtcleType;
import com.ocwvar.muzi.Network.Callbacks.OnArtcleLoadCallback;
import com.ocwvar.muzi.Network.Callbacks.OnRecycleViewClickCallbacks;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Date: 2016/5/11  20:37
 * Project: Muzi
 * 居家文化页面
 */
public class CommunityCultureActivity extends AppCompatActivity implements OnRecycleViewClickCallbacks.OnCommunityCultureListCallback, OnRecycleViewClickCallbacks.OnScrollAtBottomCallback, OnArtcleLoadCallback {

    //小区文化页面的ViewPager的Adapter
    CommunityCultureListAdapter communityCultureListAdapter;

    //社区文化列表 Adapter
    CommunityCultureAdapter communityCultureAdapter;

    //安和文化园列表 Adapter
    AnnCulturalParkAdapter annCulturalParkAdapter;

    //传统文化列表 Adapter
    TraditionalCultureAdapter traditionalCultureAdapter;

    ViewPager viewPager;
    TabLayout tabLayout;

    int communityCultureIndex = 1, communityCultureTotalCount = -1, communityCultureLoadedCount = 0;
    int annCulturalParkIndex = 1, annCulturalParkTotalCount = -1, annCulturalParkLoadedCount = 0;
    int traditionalCultureIndex = 1, traditionalCultureTotalCount = -1, traditionalCultureLoadedCount = 0;
    boolean isCommunityCultureLoading = false, isAnnCulturalParkLoading = false, isTraditionalCultureLoading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_culture);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar9);
        setSupportActionBar(toolbar);
        setTitle("居家文化");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        communityCultureListAdapter = new CommunityCultureListAdapter();
        communityCultureAdapter = new CommunityCultureAdapter();
        annCulturalParkAdapter = new AnnCulturalParkAdapter();
        traditionalCultureAdapter = new TraditionalCultureAdapter();
        viewPager = (ViewPager) findViewById(R.id.commCulture_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.commCulture_tablayout);

        communityCultureListAdapter.setRecyclerViewAdapter(0, communityCultureAdapter);
        communityCultureListAdapter.setRecyclerViewAdapter(1, annCulturalParkAdapter);
        communityCultureListAdapter.setRecyclerViewAdapter(2, traditionalCultureAdapter);

        annCulturalParkAdapter.setOnCommunityCultureListCallback(this);
        annCulturalParkAdapter.setOnScrollAtBottomCallback(this);
        communityCultureAdapter.setOnCommunityCultureListCallback(this);
        communityCultureAdapter.setOnScrollAtBottomCallback(this);
        traditionalCultureAdapter.setOnCommunityCultureListCallback(this);
        traditionalCultureAdapter.setOnScrollAtBottomCallback(this);
        viewPager.setAdapter(communityCultureListAdapter);
        tabLayout.setupWithViewPager(viewPager);

        NetworkHelper.getInstance().loadArtcles(this, false, new String[]{"5", "1", "8", null});
        NetworkHelper.getInstance().loadArtcles(this, false, new String[]{"5", "1", "9", null});
        NetworkHelper.getInstance().loadArtcles(this, false, new String[]{"5", "1", "10", null});

    }

    @Override
    public void onCommunityCultureListClick(String type, int position, ArtclesBean artclesBean) {
        Intent intent = new Intent(CommunityCultureActivity.this, ArtcleActivity.class);
        intent.putExtra("ArtclesBean", artclesBean);
        startActivity(intent);
    }

    @Override
    public void onScrollAtBottomCallback(String type) {
        switch (type) {
            case "社区文化":
                if (shouldLoadNextPage(type)) {
                    NetworkHelper.getInstance().loadArtcles(this, false, new String[]{"5", Integer.toString(communityCultureIndex), "8", null});
                    isCommunityCultureLoading = true;
                }
                break;
            case "安和文化园":
                if (shouldLoadNextPage(type)) {
                    NetworkHelper.getInstance().loadArtcles(this, false, new String[]{"5", Integer.toString(annCulturalParkIndex), "9", null});
                    isAnnCulturalParkLoading = true;
                }
                break;
            case "传统文化":
                if (shouldLoadNextPage(type)) {
                    NetworkHelper.getInstance().loadArtcles(this, false, new String[]{"5", Integer.toString(traditionalCultureIndex), "10", null});
                    isTraditionalCultureLoading = true;
                }
                break;
        }
    }

    /**
     * 检测当前数据  是否需要读取下一页
     *
     * @param type 要加载的列表标示
     * @return 是否读取
     */
    private boolean shouldLoadNextPage(String type) {
        switch (type) {
            case "社区文化":
                return !isCommunityCultureLoading && communityCultureLoadedCount <= communityCultureTotalCount;
            case "安和文化园":
                return !isAnnCulturalParkLoading && annCulturalParkLoadedCount <= annCulturalParkTotalCount;
            case "传统文化":
                return !isTraditionalCultureLoading && traditionalCultureLoadedCount <= traditionalCultureTotalCount;
            default:
                return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onArtcleLoadCompleted(int loadedArtclesCount, int totalArtclesCount, ArtcleType artcleType, int communityID, @Nullable ArrayList<ArtclesBean> arrayList) {
        switch (artcleType) {
            case 社区文化:
                if (communityCultureTotalCount == -1) {
                    //先设定总共的数量 , 以第一次读取的数值为准
                    communityCultureTotalCount = totalArtclesCount;
                }
                //增加当前页号 , 下次读取下一页
                communityCultureIndex += 1;
                //重置读取中的标记
                isCommunityCultureLoading = false;
                //累计添加文章的总数
                communityCultureLoadedCount += loadedArtclesCount;
                //将文章添加到Adapter中
                communityCultureAdapter.addArtcles(arrayList);
                break;
            case 安和文化园:
                if (annCulturalParkTotalCount == -1) {
                    annCulturalParkTotalCount = totalArtclesCount;
                }
                annCulturalParkIndex += 1;
                isAnnCulturalParkLoading = false;
                annCulturalParkLoadedCount += loadedArtclesCount;
                annCulturalParkAdapter.addArtcles(arrayList);
                break;
            case 传统文化:
                if (traditionalCultureTotalCount == -1) {
                    traditionalCultureTotalCount = totalArtclesCount;
                }
                traditionalCultureIndex += 1;
                isTraditionalCultureLoading = false;
                traditionalCultureLoadedCount += loadedArtclesCount;
                traditionalCultureAdapter.addArtcles(arrayList);
                break;
        }
    }

    @Override
    public void onArtcleLoadFailed(String message, Exception e) {
        if (e != null) {
            Toast.makeText(CommunityCultureActivity.this, "文章读取失败,请检查网络状态", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

}
