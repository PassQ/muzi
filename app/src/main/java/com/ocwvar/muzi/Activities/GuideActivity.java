package com.ocwvar.muzi.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.ocwvar.muzi.Adapters.GuideAdapter;
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
 * Date: 2016/5/12  15:26
 * Project: Muzi
 * 办事指南
 */
public class GuideActivity extends AppCompatActivity implements OnRecycleViewClickCallbacks.OnGuideListClickCallback, OnArtcleLoadCallback, DialogInterface.OnCancelListener {

    RecyclerView recyclerView;
    GuideAdapter adapter;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar11);
        setSupportActionBar(toolbar);
        setTitle("办事指南");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        adapter = new GuideAdapter();
        progressDialog = new ProgressDialog(GuideActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.guide_list);
        adapter.setOnGuideListClick(this);

        progressDialog.setMessage("读取中,请稍候...");
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(this);
        progressDialog.setCanceledOnTouchOutside(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(GuideActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        NetworkHelper.getInstance().loadArtcles(this, false, new String[]{"100", "1", "5", null});
        NetworkHelper.getInstance().loadArtcles(this, false, new String[]{"100", "1", "6", null});
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
    public void onGuideListClick(int position, ArtclesBean artclesBean) {
        Intent intent = new Intent(GuideActivity.this, GuideDetailWebActivity.class);
        intent.putExtra("item", artclesBean);
        startActivity(intent);
    }

    @Override
    public void onArtcleLoadCompleted(int loadedArtclesCount, int totalArtclesCount, ArtcleType artcleType, int communityID, @Nullable ArrayList<ArtclesBean> arrayList) {
        progressDialog.dismiss();

        adapter.addArtcles(arrayList);
    }

    @Override
    public void onArtcleLoadFailed(String message, Exception e) {
        progressDialog.dismiss();

        if (e != null) {
            Toast.makeText(GuideActivity.this, "请求数据失败,请检查网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(GuideActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        NetworkHelper.getInstance().cancelTask(NetworkHelper.TaskType.GetArtcles_Task);
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
