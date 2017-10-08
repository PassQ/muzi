package com.ocwvar.muzi.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ocwvar.muzi.Adapters.HXSoundAdapters.HXPagerAdapter;
import com.ocwvar.muzi.Adapters.HXSoundAdapters.QuestionAdapter;
import com.ocwvar.muzi.Adapters.HXSoundAdapters.ServiceAdapter;
import com.ocwvar.muzi.Adapters.HXSoundAdapters.SuggestAdapter;
import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Network.Callbacks.OnGetQuestionListCallbacak;
import com.ocwvar.muzi.Network.Callbacks.OnGetServiceListCallbacak;
import com.ocwvar.muzi.Network.Callbacks.OnGetSuggestListCallbacak;
import com.ocwvar.muzi.Network.Callbacks.OnQuestionScrollAtBottomCallback;
import com.ocwvar.muzi.Network.Callbacks.OnRecycleViewClickCallbacks;
import com.ocwvar.muzi.Network.Callbacks.OnServiceScrollAtBottomCallback;
import com.ocwvar.muzi.Network.Callbacks.OnSuggestScrollAtBottomCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Date: 2016/7/7  12:51
 * Project: Muzi
 * 和谐之声 页面
 */
public class HXSoundActivity
        extends AppCompatActivity
        implements
        DialogInterface.OnCancelListener,

        OnQuestionScrollAtBottomCallback,
        OnServiceScrollAtBottomCallback,
        OnSuggestScrollAtBottomCallback,

        OnRecycleViewClickCallbacks.OnQuestionListClickCallback,
        OnGetQuestionListCallbacak,
        OnRecycleViewClickCallbacks.OnServiceListClickCallback,
        OnGetServiceListCallbacak,
        OnRecycleViewClickCallbacks.OnSuggestListClickCallback,
        OnGetSuggestListCallbacak {

    //一次读取列表的数据量  默认:10
    private final String pageSize = "10";

    ViewPager viewPager;
    HXPagerAdapter hxPagerAdapter;
    ServiceAdapter serviceAdapter;
    SuggestAdapter suggestAdapter;
    QuestionAdapter questionAdapter;
    ProgressDialog progressDialog;

    int serviceIndex = 1, serviceTotalCount = -1;
    int suggestIndex = 1, suggestTotalCount = -1;
    int questionIndex = 1, questionTotalCount = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helping);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar17);
        setSupportActionBar(toolbar);
        setTitle("和谐之声");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.helping_tablayout);
        viewPager = (ViewPager) findViewById(R.id.helping_ViewPager);

        progressDialog = new ProgressDialog(this);
        serviceAdapter = new ServiceAdapter();
        suggestAdapter = new SuggestAdapter();
        questionAdapter = new QuestionAdapter();
        hxPagerAdapter = new HXPagerAdapter();

        progressDialog.setMessage("操作中 , 请稍候.......");
        progressDialog.setOnCancelListener(this);
        progressDialog.setCanceledOnTouchOutside(false);

        serviceAdapter.setScrollAtBottomCallback(HXSoundActivity.this);
        serviceAdapter.setOnServiceListClickCallback(HXSoundActivity.this);

        suggestAdapter.setScrollAtBottomCallback(HXSoundActivity.this);
        suggestAdapter.setOnSuggestListClickCallback(HXSoundActivity.this);

        questionAdapter.setScrollAtBottomCallback(HXSoundActivity.this);
        questionAdapter.setOnQuestionListClickCallback(HXSoundActivity.this);

        hxPagerAdapter.setServiceAdapter(serviceAdapter);
        hxPagerAdapter.setSuggestAdapter(suggestAdapter);
        hxPagerAdapter.setQuestionAdapter(questionAdapter);

        viewPager.setAdapter(hxPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    /**
     * 服务求助 点击事件回调
     *
     * @param artclesBean 点击项的文章数据
     * @param position    点击的位置
     */
    @Override
    public void onServiceListClick(ArtclesBean artclesBean, int position) {
        Intent intent = new Intent(HXSoundActivity.this, ArtcleActivity.class);
        intent.putExtra("ArtclesBean", artclesBean);
        startActivity(intent);
    }

    /**
     * 合理化建议 点击事件回调
     *
     * @param artclesBean 点击项的文章数据
     * @param position    点击的位置
     */
    @Override
    public void onSuggestListClick(ArtclesBean artclesBean, int position) {
        Intent intent = new Intent(HXSoundActivity.this, ArtcleActivity.class);
        intent.putExtra("ArtclesBean", artclesBean);
        startActivity(intent);
    }

    /**
     * 物业服务 点击事件回调
     *
     * @param artclesBean 点击项的文章数据
     * @param position    点击的位置
     */
    @Override
    public void onQuestionListClick(ArtclesBean artclesBean, int position) {
        Intent intent = new Intent(HXSoundActivity.this, ArtcleActivity.class);
        intent.putExtra("ArtclesBean", artclesBean);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_helpask, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_newTask:
                Intent intent = new Intent(HXSoundActivity.this, RequestHelpActivity.class);
                intent.putExtra("ArtcleType", RequestHelpActivity.ARTCLE_HXSOUND);
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
        this.serviceIndex = 1;
        this.serviceTotalCount = -1;
        this.suggestIndex = 1;
        this.suggestTotalCount = -1;
        this.questionIndex = 1;
        this.questionTotalCount = -1;
        //清空数据
        serviceAdapter.clearDatas();
        suggestAdapter.clearDatas();
        questionAdapter.clearDatas();
        //开始刷新
        NetworkHelper.getInstance().getQuestionList(HXSoundActivity.this, pageSize, Integer.toString(questionIndex), AppOptions.USERINFO.getCommunityTAG());
        NetworkHelper.getInstance().getServiceList(HXSoundActivity.this, pageSize, Integer.toString(serviceIndex), AppOptions.USERINFO.getCommunityTAG());
        NetworkHelper.getInstance().getSuggestList(HXSoundActivity.this, pageSize, Integer.toString(suggestIndex), AppOptions.USERINFO.getCommunityTAG());
        progressDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    //////////////////////////////////////////////服务求助 列表//////////////////////////////////////////////////

    /**
     * 获取 服务求助 列表回调
     *
     * @param pageSize    获取页的单页数据
     * @param totalCount  后台一共的数据量
     * @param index       获取页的页码
     * @param artclesBeen 获取到的数据
     */
    @Override
    public void onGotServiceList(String pageSize, int totalCount, String index, ArrayList<ArtclesBean> artclesBeen) {
        progressDialog.dismiss();

        serviceTotalCount = totalCount;
        serviceAdapter.addServiceBean(artclesBeen);
    }

    /**
     * 获取 服务求助 列表回调失败
     *
     * @param isException 是否为异常
     */
    @Override
    public void onGotServiceFailed(boolean isException) {
        progressDialog.dismiss();

        if (isException) {
            Toast.makeText(HXSoundActivity.this, "加载中发生异常 , 请检查网络", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(HXSoundActivity.this, "操作取消 或 无数据读取", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 服务求助 列表滚动到底部监听
     */
    @Override
    public void onServiceScrollAtBottom() {
        if (shouldLoadHelpNextPage()) {
            serviceIndex += 1;
            NetworkHelper.getInstance().getServiceList(HXSoundActivity.this, pageSize, Integer.toString(serviceIndex), AppOptions.USERINFO.getCommunityTAG());
        } else {
            Toast.makeText(HXSoundActivity.this, "已读取完成列表", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 服务求助 列表是否需要读取下一页
     *
     * @return 是否需要读取
     */
    private boolean shouldLoadHelpNextPage() {
        return serviceTotalCount != -1 && serviceTotalCount != serviceAdapter.getItemCount();
    }

    //////////////////////////////////////////////服务求助 列表//////////////////////////////////////////////////

    @Override
    public void onCancel(DialogInterface dialog) {
        NetworkHelper.getInstance().cancelTask(NetworkHelper.TaskType.GetServiceList);
        NetworkHelper.getInstance().cancelTask(NetworkHelper.TaskType.GetSuggestList);
        NetworkHelper.getInstance().cancelTask(NetworkHelper.TaskType.GetQuestionList);
    }

    //////////////////////////////////////////////合理化建议 列表//////////////////////////////////////////////////

    /**
     * 我的困难 列表是否需要读取下一页
     *
     * @return 是否需要读取
     */
    private boolean shouldLoadAskNextPage() {
        return suggestTotalCount != -1 && suggestTotalCount != suggestAdapter.getItemCount();
    }

    /**
     * 我的困难 列表滚动到底部监听
     */
    @Override
    public void onSuggestScrollAtBottom() {
        if (shouldLoadAskNextPage()) {
            suggestIndex += 1;
            NetworkHelper.getInstance().getSuggestList(HXSoundActivity.this, pageSize, Integer.toString(suggestIndex), AppOptions.USERINFO.user.getCommunityID());
        } else {
            Toast.makeText(HXSoundActivity.this, "已读取完成列表", Toast.LENGTH_SHORT).show();
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
    public void onGotSuggestList(String pageSize, int totalCount, String index, ArrayList<ArtclesBean> artclesBeen) {
        progressDialog.dismiss();

        suggestTotalCount = totalCount;
        suggestAdapter.addSuggestBean(artclesBeen);
    }

    /**
     * 获取 我的困难 列表回调失败
     *
     * @param isException 是否为异常
     */
    @Override
    public void onGotSuggestFailed(boolean isException) {
        progressDialog.dismiss();

        if (isException) {
            Toast.makeText(HXSoundActivity.this, "加载中发生异常 , 请检查网络", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(HXSoundActivity.this, "操作取消 或 无数据读取", Toast.LENGTH_SHORT).show();
        }
    }

    //////////////////////////////////////////////我的困难 列表//////////////////////////////////////////////////


    //////////////////////////////////////////////物业服务 列表//////////////////////////////////////////////////

    /**
     * 公益展示 列表是否需要读取下一页
     *
     * @return 是否需要读取
     */
    private boolean shouldLoadContribuNextPage() {
        return questionTotalCount != -1 && questionTotalCount != questionAdapter.getItemCount();
    }

    /**
     * 公益展示 列表滚动到底部监听
     */
    @Override
    public void onQuestionScrollAtBottom() {
        if (shouldLoadContribuNextPage()) {
            questionIndex += 1;
            NetworkHelper.getInstance().getQuestionList(HXSoundActivity.this, pageSize, Integer.toString(questionIndex), AppOptions.USERINFO.getCommunityTAG());
        } else {
            Toast.makeText(HXSoundActivity.this, "已读取完成列表", Toast.LENGTH_SHORT).show();
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
    public void onGotQuestionList(String pageSize, int totalCount, String index, ArrayList<ArtclesBean> artclesBeen) {
        progressDialog.dismiss();

        questionTotalCount = totalCount;
        questionAdapter.addQuestionBean(artclesBeen);
    }

    /**
     * 获取 公益展示 列表回调失败
     *
     * @param isException 是否为异常
     */
    @Override
    public void onGotQuestionFailed(boolean isException) {
        progressDialog.dismiss();

        if (isException) {
            Toast.makeText(HXSoundActivity.this, "加载中发生异常 , 请检查网络", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(HXSoundActivity.this, "操作取消 或 无数据读取", Toast.LENGTH_SHORT).show();
        }
    }

    //////////////////////////////////////////////物业服务 列表//////////////////////////////////////////////////
}
