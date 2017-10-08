package com.ocwvar.muzi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.ocwvar.muzi.Adapters.AnnouncementAdapter;
import com.ocwvar.muzi.Adapters.AnnouncementExpandableAdapters.CommunityExpAdapter;
import com.ocwvar.muzi.Adapters.AnnouncementExpandableAdapters.CompanyExpAdapter;
import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Network.ArtcleType;
import com.ocwvar.muzi.Network.Callbacks.OnArtcleLoadCallback;
import com.ocwvar.muzi.Network.Callbacks.OnExpScrollAtBottomCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Date: 2016/5/18  16:51
 * Project: Muzi
 * 公告界面
 */
public class CommunityAnnouncementActivity extends AppCompatActivity implements OnExpScrollAtBottomCallback, OnArtcleLoadCallback, ExpandableListView.OnChildClickListener {

    AnnouncementAdapter viewPagerAdapter;
    CommunityExpAdapter communityExpAdapter;
    CompanyExpAdapter companyExpAdapter;
    ViewPager viewPager;

    boolean communityLoading = false;
    boolean companyLoading = false;
    int communityTotal = 0, companyTotal = 0;
    int communityLoaded = 0, companyLoaded = 0;
    int communityIndex = 1, companyIndex = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar16);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("公告");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.announcement_tablayout);
        viewPager = (ViewPager) findViewById(R.id.announcement_ViewPager);
        viewPagerAdapter = new AnnouncementAdapter();
        communityExpAdapter = new CommunityExpAdapter();
        companyExpAdapter = new CompanyExpAdapter();

        viewPagerAdapter.setCommunityExpAdapter(communityExpAdapter);
        viewPagerAdapter.setCompanyExpAdapter(companyExpAdapter);
        viewPagerAdapter.setOnChildClickListener(this);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        communityExpAdapter.setOnScrollAtBottomCallback(this);
        companyExpAdapter.setOnScrollAtBottomCallback(this);

        NetworkHelper.getInstance().loadArtcles(this, false, new String[]{"5", "1", "2", AppOptions.USERINFO.getCommunityTAG()});
        NetworkHelper.getInstance().loadArtcles(this, false, new String[]{"5", "1", "1", "1"});
    }

    @Override
    public void onScrollatBottom(String type, int groupIndex) {
        if (shouldNextPage(type)) {
            switch (type) {
                case "小区公告":
                    communityLoading = true;
                    NetworkHelper.getInstance().loadArtcles(this, false, new String[]{"5", Integer.toString(communityIndex), "2", AppOptions.USERINFO.getCommunityTAG()});
                    break;
                case "公司公告":
                    companyLoading = true;
                    NetworkHelper.getInstance().loadArtcles(this, false, new String[]{"5", Integer.toString(companyIndex), "1", "1"});
                    break;
            }
        }
    }

    @Override
    public void onArtcleLoadCompleted(int loadedArtclesCount, int totalArtclesCount, ArtcleType artcleType, int communityID, @Nullable ArrayList<ArtclesBean> arrayList) {
        switch (artcleType) {
            case 小区公告:
                communityLoading = false;
                communityLoaded += loadedArtclesCount;
                communityTotal = totalArtclesCount;
                communityIndex += 1;
                inputDatasIntoAdapter(arrayList, 0);
                break;
            case 公司公告:
                companyLoading = false;
                companyLoaded += loadedArtclesCount;
                companyTotal = totalArtclesCount;
                companyIndex += 1;
                inputDatasIntoAdapter(arrayList, 1);
                break;
        }
    }

    @Override
    public void onArtcleLoadFailed(String message, Exception e) {
        Toast.makeText(CommunityAnnouncementActivity.this, "文章读取失败", Toast.LENGTH_SHORT).show();
    }

    private void inputDatasIntoAdapter(ArrayList<ArtclesBean> arrayList, int page) {
        if (arrayList == null) return;

        switch (page) {
            case 0:
                //导入到小区公告
                for (ArtclesBean bean : arrayList) {
                    if (bean.getAddTime().substring(0, 4).equals("2016")) {
                        communityExpAdapter.addGroup("2016年");
                        communityExpAdapter.addItem(communityExpAdapter.indexPositionOfGroup("2016年"), bean);
                    } else if (bean.getAddTime().substring(0, 4).equals("2015")) {
                        communityExpAdapter.addGroup("2015年");
                        communityExpAdapter.addItem(communityExpAdapter.indexPositionOfGroup("2015年"), bean);
                    } else if (bean.getAddTime().substring(0, 4).equals("2017")) {
                        communityExpAdapter.addGroup("2017年");
                        communityExpAdapter.addItem(communityExpAdapter.indexPositionOfGroup("2017年"), bean);
                    }
                }
                break;
            case 1:
                //导入到公司公告
                for (ArtclesBean bean : arrayList) {
                    if (bean.getAddTime().substring(0, 4).equals("2016")) {
                        companyExpAdapter.addGroup("2016年");
                        companyExpAdapter.addItem(companyExpAdapter.indexPositionOfGroup("2016年"), bean);
                    } else if (bean.getAddTime().substring(0, 4).equals("2015")) {
                        companyExpAdapter.addGroup("2015年");
                        companyExpAdapter.addItem(companyExpAdapter.indexPositionOfGroup("2015年"), bean);
                    } else if (bean.getAddTime().substring(0, 4).equals("2017")) {
                        companyExpAdapter.addGroup("2017年");
                        companyExpAdapter.addItem(companyExpAdapter.indexPositionOfGroup("2017年"), bean);
                    }
                }
                break;
        }
    }

    private boolean shouldNextPage(String type) {
        switch (type) {
            case "小区公告":
                return communityLoaded < communityTotal && !communityLoading;
            case "公司公告":
                return companyLoaded < companyTotal && !companyLoading;
            default:
                return false;
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Intent intent = new Intent(CommunityAnnouncementActivity.this, ArtcleActivity.class);
        switch (parent.getTag().toString()) {
            case "companyAnn":
                intent.putExtra("ArtclesBean", companyExpAdapter.getItem(groupPosition, childPosition));
                break;
            case "communityAnn":
                intent.putExtra("ArtclesBean", communityExpAdapter.getItem(groupPosition, childPosition));
                break;
        }
        startActivity(intent);
        return true;
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
}
