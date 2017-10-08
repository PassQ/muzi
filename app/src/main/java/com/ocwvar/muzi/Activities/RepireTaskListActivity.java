package com.ocwvar.muzi.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ocwvar.muzi.Adapters.RepireTaskAdapters.RepirePageAdapter;
import com.ocwvar.muzi.Fragments.RepireMan.RepiredFragment;
import com.ocwvar.muzi.Fragments.RepireMan.UnRepiredFragment;
import com.ocwvar.muzi.R;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Data: 2016/6/30 23:48
 * Project: Muzi
 * 维修人员列表界面
 */
public class RepireTaskListActivity extends AppCompatActivity {

    ViewPager viewPager;
    RepirePageAdapter repirePageAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repire_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar18);
        setSupportActionBar(toolbar);
        setTitle("维修列表");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.repireTask_tablayout);
        viewPager = (ViewPager) findViewById(R.id.repireTask_ViewPager);
        repirePageAdapter = new RepirePageAdapter(getSupportFragmentManager(), new RepiredFragment(), new UnRepiredFragment());
        viewPager.setAdapter(repirePageAdapter);
        tabLayout.setupWithViewPager(viewPager);

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
