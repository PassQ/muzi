package com.ocwvar.muzi.Fragments.RepireMan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ocwvar.muzi.Activities.RepireDetailActivity;
import com.ocwvar.muzi.Adapters.RepireTaskAdapters.RepiredAdapter;
import com.ocwvar.muzi.Beans.RepireBean;
import com.ocwvar.muzi.Network.Callbacks.OnGetRepireDataCallback;
import com.ocwvar.muzi.Network.Callbacks.OnRecycleViewClickCallbacks;
import com.ocwvar.muzi.Network.Callbacks.OnRepireListScrollAtBottomCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Fragments.RepireMan
 * Data: 2016/7/5 17:48
 * Project: Muzi
 * 已受理列表页面
 */
public class RepiredFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        OnRepireListScrollAtBottomCallback,
        OnGetRepireDataCallback,
        OnRecycleViewClickCallbacks.OnClickRepireListCallback {

    private final int ONCE_LOAD = 10;      //一次读取的数量
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    RepiredAdapter adapter;
    private int loadedCount = 0;            //读取的数量
    private int totalCount = 0;             //一共有的数量
    private int pageIndex = 1;             //一共有的数量
    private boolean loadingNext = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new RepiredAdapter(this, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null && inflater != null) {
            return inflater.inflate(R.layout.fragment_repired, container, false);
        } else {
            return null;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view != null) {
            refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
            refreshLayout.setOnRefreshListener(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NetworkHelper.getInstance().getRepireList(this, "1", AppOptions.USERINFO.user.getPhoneNumber(), ONCE_LOAD, pageIndex);
    }

    /**
     * 判断是否需要读取下一页
     *
     * @param totalCount  服务器数据数量
     * @param loadedCount 当前读取的数量
     * @return 是否要读取
     */
    private boolean shouldLoadNextPage(int totalCount, int loadedCount) {
        /**
         * 不触发的情况:
         * 1.当前正在读取下一页
         * 2.服务端的数据数量为0
         * 3.当前读取数量 等于 服务端的数据数量
         */
        return !(loadingNext || totalCount <= 0 || loadedCount == totalCount);
    }

    /**
     * 重置读取的数量状态
     */
    private void resetCounts() {
        loadedCount = 0;
        totalCount = 0;
        pageIndex = 1;
        adapter.clear();
        loadingNext = false;
    }

    /**
     * 触发刷新
     */
    @Override
    public void onRefresh() {
        if (!loadingNext) {
            resetCounts();
            NetworkHelper.getInstance().getRepireList(this, "1", AppOptions.USERINFO.user.getPhoneNumber(), ONCE_LOAD, pageIndex);
        } else {
            refreshLayout.setRefreshing(false);
        }
    }

    /**
     * 获取已受理数据
     *
     * @param repiredList 数据列表
     * @param totalCount  一共的数量
     */
    @Override
    public void onGotRepiredData(ArrayList<RepireBean> repiredList, int totalCount) {
        refreshLayout.setRefreshing(false);
        loadingNext = false;
        if (totalCount != 0 && repiredList != null) {
            this.totalCount = totalCount;
            this.loadedCount += repiredList.size();
            this.adapter.addData(repiredList);
        } else {
            Toast.makeText(getContext(), "无数据", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取未受理数据
     *
     * @param unRepiredList 数据列表
     * @param totalCount    一共的数量
     */
    @Override
    public void onGotUnRepiredData(ArrayList<RepireBean> unRepiredList, int totalCount) {

    }

    /**
     * 获取数据失败
     */
    @Override
    public void onFailed() {
        refreshLayout.setRefreshing(false);
        Toast.makeText(getContext(), "数据加载失败", Toast.LENGTH_SHORT).show();
    }

    /**
     * 列表滚动到底部时触发
     */
    @Override
    public void onScrollAtBottom() {
        if (shouldLoadNextPage(totalCount, loadedCount)) {
            Toast.makeText(getContext(), "正在读取下一页", Toast.LENGTH_SHORT).show();
            loadingNext = true;
            pageIndex += 1;
            NetworkHelper.getInstance().getRepireList(this, "1", AppOptions.USERINFO.user.getPhoneNumber(), ONCE_LOAD, pageIndex);
        } else {
            Toast.makeText(getContext(), "没有更多的数据了", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 列表点击事件
     *
     * @param repireBean 点击的目标数据
     * @param position   点击的位置
     */
    @Override
    public void onClickList(RepireBean repireBean, int position) {
        Intent intent = new Intent(getActivity(), RepireDetailActivity.class);
        intent.putExtra("RepireBean", repireBean);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode != RepireDetailActivity.ACTION_STAY) {
            //如果打开的页面内做了更改状态: 受理或完成 则重新刷新数据
            refreshLayout.setRefreshing(true);
            adapter.clear();
            resetCounts();
            NetworkHelper.getInstance().getRepireList(this, "1", AppOptions.USERINFO.user.getPhoneNumber(), ONCE_LOAD, pageIndex);
            Toast.makeText(getActivity(), "重新刷新数据中....", Toast.LENGTH_SHORT).show();
        }
    }

}
