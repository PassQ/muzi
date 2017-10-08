package com.ocwvar.muzi.Activities;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.ocwvar.muzi.Adapters.ServiceArtcleAdapters.PropertyAdapter;
import com.ocwvar.muzi.Adapters.ServiceArtcleAdapters.ServiceDynamicPagerAdapter;
import com.ocwvar.muzi.Adapters.ServiceArtcleAdapters.ServicePeopleAdapter;
import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Beans.UserType;
import com.ocwvar.muzi.Network.Callbacks.OnGetPropertyListCallbacak;
import com.ocwvar.muzi.Network.Callbacks.OnGetServicePropleListCallbacak;
import com.ocwvar.muzi.Network.Callbacks.OnPropertyScrollAtBottomCallback;
import com.ocwvar.muzi.Network.Callbacks.OnRecycleViewClickCallbacks;
import com.ocwvar.muzi.Network.Callbacks.OnServicePeopleScrollAtBottomCallback;
import com.ocwvar.muzi.Network.DataCacher;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Date: 2016/7/7  12:51
 * Project: Muzi
 * 服务动态 页面
 */
public class ServiceDynamicActivity
        extends AppCompatActivity
        implements
        DialogInterface.OnCancelListener,

        OnPropertyScrollAtBottomCallback,
        OnServicePeopleScrollAtBottomCallback,

        OnRecycleViewClickCallbacks.OnPropertyListClickCallback,
        OnGetPropertyListCallbacak,
        OnRecycleViewClickCallbacks.OnServicePeopleListClickCallback,
        OnGetServicePropleListCallbacak {

    //一次读取列表的数据量  默认:10
    private final String pageSize = "10";

    ViewPager viewPager;
    ServiceDynamicPagerAdapter pagerAdapter;
    PropertyAdapter propertyAdapter;
    ServicePeopleAdapter servicePeopleAdapter;
    ProgressDialog progressDialog;
    public FragmentManager fragmentManager;

    int propertyIndex = 1, propertyTotalCount = -1;
    int servicePeopleIndex = 1, servicePeopleTotalCount = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helping);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar17);
        setSupportActionBar(toolbar);
        setTitle("服务动态");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.helping_tablayout);
        viewPager = (ViewPager) findViewById(R.id.helping_ViewPager);

        progressDialog = new ProgressDialog(this);
        propertyAdapter = new PropertyAdapter();
        servicePeopleAdapter = new ServicePeopleAdapter();
        pagerAdapter = new ServiceDynamicPagerAdapter();

        progressDialog.setMessage("操作中 , 请稍候.......");
        progressDialog.setOnCancelListener(this);
        progressDialog.setCanceledOnTouchOutside(false);

        propertyAdapter.setScrollAtBottomCallback(ServiceDynamicActivity.this);
        propertyAdapter.setOnPropertyListClickCallback(ServiceDynamicActivity.this);

        servicePeopleAdapter.setScrollAtBottomCallback(ServiceDynamicActivity.this);
        servicePeopleAdapter.setOnServicePeopleListClickCallback(ServiceDynamicActivity.this);
        fragmentManager=getFragmentManager();
        pagerAdapter.setFragmentManager(fragmentManager);
        pagerAdapter.setPropertyAdapter(propertyAdapter);
        pagerAdapter.setServicePeopleAdapter(servicePeopleAdapter);
        pagerAdapter.setTag(getIntent().getIntExtra("TAG",0));

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (AppOptions.USERINFO.user.getUserType() == UserType.GUEST) {
            new AlertDialog.Builder(ServiceDynamicActivity.this).setMessage("未认证用户只能浏览便民服务").setPositiveButton("确定", null).show();
        }

    }

    /**
     * 物业服务 点击事件回调
     *
     * @param artclesBean 点击项的文章数据
     * @param position    点击的位置
     */
    @Override
    public void onPropertyListClick(ArtclesBean artclesBean, int position) {
        Intent intent = new Intent(ServiceDynamicActivity.this, ArtcleActivity.class);
        intent.putExtra("ArtclesBean", artclesBean);
        startActivity(intent);
    }

    /**
     * 便民信息 点击事件回调
     *
     * @param artclesBean 点击项的文章数据
     * @param position    点击的位置
     */
    @Override
    public void onServicePeopleListClick(ArtclesBean artclesBean, int position) {
        Intent intent = new Intent(ServiceDynamicActivity.this, ArtcleActivity.class);
        intent.putExtra("ArtclesBean", artclesBean);
        startActivity(intent);
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

    /**
     * 清空数据 , 重新刷新数据
     */
    private void refresh() {
        //重置状态
        this.propertyIndex = 1;
        this.propertyTotalCount = -1;
        this.servicePeopleIndex = 1;
        this.servicePeopleTotalCount = -1;

        //清空数据
        propertyAdapter.clearDatas();
        servicePeopleAdapter.clearDatas();

        //开始刷新
        if (AppOptions.USERINFO.user.getUserType() != UserType.GUEST) {
            NetworkHelper.getInstance().getPropertyList(ServiceDynamicActivity.this, pageSize, Integer.toString(propertyIndex), AppOptions.USERINFO.user.getCommunityID());
        }
        NetworkHelper.getInstance().getServicePeopleList(ServiceDynamicActivity.this, pageSize, Integer.toString(servicePeopleIndex));
        progressDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    //////////////////////////////////////////////物业服务 列表//////////////////////////////////////////////////

    /**
     * 获取 服务求助 列表回调
     *
     * @param pageSize    获取页的单页数据
     * @param totalCount  后台一共的数据量
     * @param index       获取页的页码
     * @param artclesBeen 获取到的数据
     *
     */
    @Override
    public void onGotPropertyList(String pageSize, int totalCount, String index, ArrayList<ArtclesBean> artclesBeen) {
        progressDialog.dismiss();

        propertyTotalCount = totalCount;
        propertyAdapter.addPropertyBean(artclesBeen);
    }

    /**
     * 获取 服务求助 列表回调失败
     *
     * @param isException 是否为异常
     */
    @Override
    public void onGotPropertyFailed(boolean isException) {
        progressDialog.dismiss();

        if (isException) {
            Toast.makeText(ServiceDynamicActivity.this, "加载中发生异常 , 请检查网络", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 服务求助 列表滚动到底部监听
     */
    @Override
    public void onPropertyScrollAtBottom() {
        if (shouldLoadHelpNextPage()) {
            propertyIndex += 1;
            NetworkHelper.getInstance().getPropertyList(ServiceDynamicActivity.this, pageSize, Integer.toString(propertyIndex), AppOptions.USERINFO.user.getCommunityID());
        }
    }

    /**
     * 服务求助 列表是否需要读取下一页
     *
     * @return 是否需要读取
     */
    private boolean shouldLoadHelpNextPage() {
        return propertyTotalCount != -1 && propertyTotalCount != propertyAdapter.getItemCount();
    }

    //////////////////////////////////////////////物业服务 列表//////////////////////////////////////////////////

    @Override
    public void onCancel(DialogInterface dialog) {
        NetworkHelper.getInstance().cancelTask(NetworkHelper.TaskType.GetPropertyList);
        NetworkHelper.getInstance().cancelTask(NetworkHelper.TaskType.GetServicePeopleList);
    }

    //////////////////////////////////////////////便民服务 列表//////////////////////////////////////////////////

    /**
     * 我的困难 列表是否需要读取下一页
     *
     * @return 是否需要读取
     */
    private boolean shouldLoadAskNextPage() {
        return servicePeopleTotalCount != -1 && servicePeopleTotalCount != servicePeopleAdapter.getItemCount();
    }

    /**
     * 我的困难 列表滚动到底部监听
     */
    @Override
    public void onServicePeopleScrollAtBottom() {
        if (shouldLoadAskNextPage()) {
            servicePeopleIndex += 1;
            NetworkHelper.getInstance().getServicePeopleList(ServiceDynamicActivity.this, pageSize, Integer.toString(servicePeopleIndex));
        }
    }

    /**
     * 获取 我的困难 列表回调
     *
     * @param pageSize    获取页的单页数据
     * @param totalCount  后台一共的数据量
     * @param index       获取页的页码
     * @param artclesBeen 获取到的数据
     */
    @Override
    public void onGotServicePropleList(String pageSize, int totalCount, String index, ArrayList<ArtclesBean> artclesBeen) {
        progressDialog.dismiss();

        servicePeopleTotalCount = totalCount;
        servicePeopleAdapter.addServicePeopleBean(artclesBeen);
    }

    /**
     * 获取 我的困难 列表回调失败
     *
     * @param isException 是否为异常
     */
    @Override
    public void onGotServicePropleFailed(boolean isException) {
        progressDialog.dismiss();

        if (isException) {
            Toast.makeText(ServiceDynamicActivity.this, "加载中发生异常 , 请检查网络", Toast.LENGTH_SHORT).show();
        }
    }

    //////////////////////////////////////////////我的困难 列表//////////////////////////////////////////////////

}
