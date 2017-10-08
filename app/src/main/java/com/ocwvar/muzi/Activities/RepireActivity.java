package com.ocwvar.muzi.Activities;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ocwvar.muzi.Adapters.RepireViewPagerAdapter;
import com.ocwvar.muzi.Fragments.RepireFragment;
import com.ocwvar.muzi.Fragments.RepireStatusFragment;
import com.ocwvar.muzi.R;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Date: 2016/5/9  9:04
 * Project: Muzi
 * 报修界面
 */
public class RepireActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    RepireViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repire_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar7);
        setSupportActionBar(toolbar);
        setTitle("维修申请");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //对象的实例化
        tabLayout = (TabLayout) findViewById(R.id.repire_tablayout);
        viewPager = (ViewPager) findViewById(R.id.repire_viewpager);
        adapter = new RepireViewPagerAdapter(getSupportFragmentManager());

        //对象的属性设置
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorHeight(10);

        adapter.addFragment(new RepireFragment(), "报修申请");
        adapter.addFragment(new RepireStatusFragment(), "报修查询");

        clearUpFiles();
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
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    /**
     * 清除残留的文件
     */
    private void clearUpFiles() {
        final String TEMP_PATH_1 = Environment.getExternalStorageDirectory().getPath() + "/ctmp1.jpg";
        final String TEMP_PATH_2 = Environment.getExternalStorageDirectory().getPath() + "/ctmp2.jpg";
        final String TEMP_PATH_3 = Environment.getExternalStorageDirectory().getPath() + "/ctmp3.jpg";
        final String TEMP_PATH_4 = Environment.getExternalStorageDirectory().getPath() + "/ctmp4.jpg";
        final String TEMP_PATH_5 = Environment.getExternalStorageDirectory().getPath() + "/ctmp5.jpg";
        new File(TEMP_PATH_1).delete();
        new File(TEMP_PATH_2).delete();
        new File(TEMP_PATH_3).delete();
        new File(TEMP_PATH_4).delete();
        new File(TEMP_PATH_5).delete();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearUpFiles();
    }
}
