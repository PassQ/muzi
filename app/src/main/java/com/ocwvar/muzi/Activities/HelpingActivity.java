package com.ocwvar.muzi.Activities;

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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ocwvar.muzi.Adapters.HelpingAdapters.ContributionAdapter;
import com.ocwvar.muzi.Adapters.HelpingAdapters.HelpingAdapter;
import com.ocwvar.muzi.Adapters.HelpingAdapters.MyAskingAdapter;
import com.ocwvar.muzi.Adapters.HelpingAdapters.MyHelpingAdapter;
import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Beans.UserType;
import com.ocwvar.muzi.Network.Callbacks.OnAskingScrollAtBottomCallback;
import com.ocwvar.muzi.Network.Callbacks.OnContrScrollAtBottomCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetAskingListCallbacak;
import com.ocwvar.muzi.Network.Callbacks.OnGetContribuListCallbacak;
import com.ocwvar.muzi.Network.Callbacks.OnGetHelpingListCallback;
import com.ocwvar.muzi.Network.Callbacks.OnHelpingScrollAtBottomCallback;
import com.ocwvar.muzi.Network.Callbacks.OnRecycleViewClickCallbacks;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Date: 2016/6/13  12:51
 * Project: Muzi
 * 爱心互助页面
 */
public class HelpingActivity
        extends AppCompatActivity
        implements OnGetHelpingListCallback,
        OnHelpingScrollAtBottomCallback,
        DialogInterface.OnCancelListener,
        OnAskingScrollAtBottomCallback,
        OnGetAskingListCallbacak,
        OnRecycleViewClickCallbacks.OnAskingListClickCallback,
        OnRecycleViewClickCallbacks.OnHelpingListClickCallback,
        OnContrScrollAtBottomCallback,
        OnRecycleViewClickCallbacks.OnContributionListClickCallback,
        OnGetContribuListCallbacak {

    //一次读取列表的数据量  默认:10
    private final String pageSize = "10";

    ViewPager viewPager;
    HelpingAdapter helpingVPAdapter;
    MyHelpingAdapter helpingAdapter;
    MyAskingAdapter askingAdapter;
    ContributionAdapter contribuAdapter;
    ProgressDialog progressDialog;

    int helpIndex = 1, helpTotalCount = -1;
    int askIndex = 1, askTotalCount = -1;
    int contribuIndex = 1, contribuTotalCount = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helping);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar17);
        setSupportActionBar(toolbar);
        setTitle("爱心互助");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.helping_tablayout);
        viewPager = (ViewPager) findViewById(R.id.helping_ViewPager);

        progressDialog = new ProgressDialog(this);
        helpingAdapter = new MyHelpingAdapter();
        askingAdapter = new MyAskingAdapter();
        contribuAdapter = new ContributionAdapter();
        helpingVPAdapter = new HelpingAdapter();

        progressDialog.setMessage("操作中 , 请稍候.......");
        progressDialog.setOnCancelListener(this);
        progressDialog.setCanceledOnTouchOutside(false);
        askingAdapter.setScrollAtBottomCallback(HelpingActivity.this);
        askingAdapter.setOnAskingListClickCallback(HelpingActivity.this);
        helpingAdapter.setScrollAtBottomCallback(HelpingActivity.this);
        helpingAdapter.setOnHelpingListClickCallback(HelpingActivity.this);
        contribuAdapter.setScrollAtBottomCallback(HelpingActivity.this);
        contribuAdapter.setOnHelpingListClickCallback(HelpingActivity.this);
        helpingVPAdapter.setHelpingAdapter(helpingAdapter);
        helpingVPAdapter.setAskingAdapter(askingAdapter);
        helpingVPAdapter.setContribuAdapter(contribuAdapter);

        viewPager.setAdapter(helpingVPAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (AppOptions.USERINFO.user.getUserType() == UserType.GUEST) {
            new AlertDialog.Builder(HelpingActivity.this).setMessage("未认证用户只能浏览公益展示").setPositiveButton("确定", null).show();
        }

    }

    /**
     * 我的困难点击事件回调
     *
     * @param artclesBean 点击项的文章数据
     * @param position    点击的位置
     */
    @Override
    public void onAskingClick(ArtclesBean artclesBean, int position) {
        Intent intent = new Intent(HelpingActivity.this, ArtcleActivity.class);
        intent.putExtra("ArtclesBean", artclesBean);
        startActivity(intent);
    }

    /**
     * 我的爱心点击事件回调
     *
     * @param artclesBean 点击项的文章数据
     * @param position    点击的位置
     */
    @Override
    public void onHelpingClick(ArtclesBean artclesBean, int position) {
        Intent intent = new Intent(HelpingActivity.this, ArtcleActivity.class);
        intent.putExtra("ArtclesBean", artclesBean);
        startActivity(intent);
    }

    /**
     * 公益展示点击事件回调
     *
     * @param artclesBean 点击项的文章数据
     * @param position    点击的位置
     */
    @Override
    public void onContributionClick(ArtclesBean artclesBean, int position) {
        Intent intent = new Intent(HelpingActivity.this, ArtcleActivity.class);
        intent.putExtra("ArtclesBean", artclesBean);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (AppOptions.USERINFO.user.getUserType() != UserType.GUEST) {
            getMenuInflater().inflate(R.menu.menu_helpask, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_newTask:
                Intent intent = new Intent(HelpingActivity.this, RequestHelpActivity.class);
                intent.putExtra("ArtcleType", RequestHelpActivity.ARTCLE_ASKHELP);
                startActivity(intent);
                break;
        }
        return true;
    }

    /**
     * 清空数据 , 重新刷新数据
     */
    private void refresh() {
        //重置状态
        this.helpIndex = 1;
        this.helpTotalCount = -1;
        this.askIndex = 1;
        this.askTotalCount = -1;
        this.contribuIndex = 1;
        this.contribuTotalCount = -1;
        //清空数据
        askingAdapter.clearDatas();
        helpingAdapter.clearDatas();
        contribuAdapter.clearDatas();
        //开始刷新
        if (AppOptions.USERINFO.user.getUserType() != UserType.GUEST) {
            NetworkHelper.getInstance().getHelpingList(HelpingActivity.this, pageSize, Integer.toString(helpIndex), AppOptions.USERINFO.user.getUserID());
            NetworkHelper.getInstance().getAskingList(HelpingActivity.this, pageSize, Integer.toString(helpIndex), AppOptions.USERINFO.user.getUserID());
        }
        NetworkHelper.getInstance().getContribuList(HelpingActivity.this, pageSize, Integer.toString(contribuIndex));
        progressDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    //////////////////////////////////////////////我的爱心列表//////////////////////////////////////////////////

    /**
     * 获取 我的爱心 列表回调
     *
     * @param pageSize    获取页的单页数据
     * @param totalCount  后台一共的数据量
     * @param index       获取页的页码
     * @param artclesBeen 获取到的数据
     */
    @Override
    public void onGotHelpingList(String pageSize, int totalCount, String index, ArrayList<ArtclesBean> artclesBeen) {
        progressDialog.dismiss();

        helpTotalCount = totalCount;
        helpingAdapter.addHelpingBean(artclesBeen);
    }

    /**
     * 获取 我的爱心 列表回调失败
     *
     * @param isException 是否为异常
     */
    @Override
    public void onGotHelpingFailed(boolean isException) {
        progressDialog.dismiss();

        if (isException) {
            Toast.makeText(HelpingActivity.this, "加载中发生异常 , 请检查网络", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(HelpingActivity.this, "操作取消 或 无数据读取", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 我的爱心 列表滚动到底部监听
     */
    @Override
    public void onHelpingScrollAtBottom() {
        if (shouldLoadHelpNextPage()) {
            helpIndex += 1;
            NetworkHelper.getInstance().getHelpingList(HelpingActivity.this, pageSize, Integer.toString(helpIndex), AppOptions.USERINFO.user.getUserID());
        } else {
            Toast.makeText(HelpingActivity.this, "已读取完成列表", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 我的爱心 列表是否需要读取下一页
     *
     * @return 是否需要读取
     */
    private boolean shouldLoadHelpNextPage() {
        return helpTotalCount != -1 && helpTotalCount != helpingAdapter.getItemCount();
    }

    //////////////////////////////////////////////我的爱心列表//////////////////////////////////////////////////

    @Override
    public void onCancel(DialogInterface dialog) {
        NetworkHelper.getInstance().cancelTask(NetworkHelper.TaskType.GetHelpingList);
        NetworkHelper.getInstance().cancelTask(NetworkHelper.TaskType.GetAskingList);
        NetworkHelper.getInstance().cancelTask(NetworkHelper.TaskType.GetContribuList);
    }

    //////////////////////////////////////////////我的困难列表//////////////////////////////////////////////////

    /**
     * 我的困难 列表是否需要读取下一页
     *
     * @return 是否需要读取
     */
    private boolean shouldLoadAskNextPage() {
        return askTotalCount != -1 && askTotalCount != askingAdapter.getItemCount();
    }

    /**
     * 我的困难 列表滚动到底部监听
     */
    @Override
    public void onAskingScrollAtBottom() {
        if (shouldLoadAskNextPage()) {
            askIndex += 1;
            NetworkHelper.getInstance().getAskingList(HelpingActivity.this, pageSize, Integer.toString(helpIndex), AppOptions.USERINFO.user.getUserID());
        } else {
            Toast.makeText(HelpingActivity.this, "已读取完成列表", Toast.LENGTH_SHORT).show();
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
    public void onGotAskingList(String pageSize, int totalCount, String index, ArrayList<ArtclesBean> artclesBeen) {
        progressDialog.dismiss();

        askTotalCount = totalCount;
        askingAdapter.addAskingBean(artclesBeen);
    }

    /**
     * 获取 我的困难 列表回调失败
     *
     * @param isException 是否为异常
     */
    @Override
    public void onGotAskingFailed(boolean isException) {
        progressDialog.dismiss();

        if (isException) {
            Toast.makeText(HelpingActivity.this, "加载中发生异常 , 请检查网络", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(HelpingActivity.this, "操作取消 或 无数据读取", Toast.LENGTH_SHORT).show();
        }
    }

    //////////////////////////////////////////////我的困难列表//////////////////////////////////////////////////


    //////////////////////////////////////////////公益展示列表//////////////////////////////////////////////////

    /**
     * 公益展示 列表是否需要读取下一页
     *
     * @return 是否需要读取
     */
    private boolean shouldLoadContribuNextPage() {
        return contribuTotalCount != -1 && contribuTotalCount != contribuAdapter.getItemCount();
    }

    /**
     * 公益展示 列表滚动到底部监听
     */
    @Override
    public void onContrScrollAtBottom() {
        if (shouldLoadContribuNextPage()) {
            contribuIndex += 1;
            NetworkHelper.getInstance().getContribuList(HelpingActivity.this, pageSize, Integer.toString(contribuIndex));
        } else {
            Toast.makeText(HelpingActivity.this, "已读取完成列表", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取 公益展示 列表回调
     *
     * @param pageSize    获取页的单页数据
     * @param totalCount  后台一共的数据量
     * @param index       获取页的页码
     * @param artclesBeen 获取到的数据
     */
    @Override
    public void onGotContribuList(String pageSize, int totalCount, String index, ArrayList<ArtclesBean> artclesBeen) {
        progressDialog.dismiss();

        contribuTotalCount = totalCount;
        contribuAdapter.addContributionBean(artclesBeen);
    }

    /**
     * 获取 公益展示 列表回调失败
     *
     * @param isException 是否为异常
     */
    @Override
    public void onGotContribuFailed(boolean isException) {
        progressDialog.dismiss();

        if (isException) {
            Toast.makeText(HelpingActivity.this, "加载中发生异常 , 请检查网络", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(HelpingActivity.this, "操作取消 或 无数据读取", Toast.LENGTH_SHORT).show();
        }
    }

    //////////////////////////////////////////////公益展示列表//////////////////////////////////////////////////
}
