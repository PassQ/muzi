package com.ocwvar.muzi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.ImageHelper.OCImageLoader;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Date: 2016/5/16  11:28
 * Project: Muzi
 * 设置页面
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    View about, clear;
    TextView cache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar14);
        setSupportActionBar(toolbar);
        setTitle("设置");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        about = findViewById(R.id.view_setting_about);
        clear = findViewById(R.id.view_setting_clear);
        cache = (TextView) findViewById(R.id.textView_setting_cache);
        about.setOnClickListener(this);
        clear.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        updateCacheSize();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_setting_about:
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                break;
            case R.id.view_setting_clear:
                OCImageLoader.loader().clearCacheDirectory();
                OCImageLoader.loader().releaseLruCaches();
                updateCacheSize();
                break;
        }
    }

    /**
     * 更新缓存文件夹大小
     */
    private void updateCacheSize() {
        long size = OCImageLoader.loader().getCacheDirectorySize();
        if (size == 0) {
            cache.setText("0 MB");
        } else {
            cache.setText(String.format("%.2f", (float) (size / (1024.0 * 1024.0))) + " MB");
        }
    }

}
