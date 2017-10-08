package com.ocwvar.muzi.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import com.ocwvar.muzi.Activities.RepireScoringActivity;
import com.ocwvar.muzi.Activities.RepireStatusDetailActivity;
import com.ocwvar.muzi.Adapters.RepireStatusAdapter;
import com.ocwvar.muzi.Beans.RepireStatusBean;
import com.ocwvar.muzi.Network.Callbacks.OnGetRepireStatusListCallback;
import com.ocwvar.muzi.Network.Callbacks.OnRecycleViewClickCallbacks;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Fragments
 * Date: 2016/5/10  10:16
 * Project: Muzi
 * 报修信息查询页面--已处理页面
 */
public class RepireStatusFragment extends Fragment implements OnGetRepireStatusListCallback, DialogInterface.OnCancelListener, OnRecycleViewClickCallbacks.OnRepireStatusListCallback, SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    RepireStatusAdapter adapter;
    SwipeRefreshLayout swipeRefresh;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        adapter = new RepireStatusAdapter();

        View view = inflater.inflate(R.layout.fragment_repire_page2, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_repire2);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh_repire2);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("正在刷新数据...");
        progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
        progressDialog.setOnCancelListener(this);
        progressDialog.setCanceledOnTouchOutside(false);

        swipeRefresh.setColorSchemeColors(Color.argb(255, 99, 184, 255));
        swipeRefresh.setOnRefreshListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnRepireStatusListCallback(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshList();
    }

    private void refreshList() {
        progressDialog.show();
        NetworkHelper.getInstance().getRepireStatusList(this, AppOptions.USERINFO.user.getUserID());
    }

    @Override
    public void onGotRepireStatusListCompleted(ArrayList<RepireStatusBean> repireStatusBeen) {
        progressDialog.dismiss();
        swipeRefresh.setRefreshing(false);

        adapter.updateDatas(repireStatusBeen);
        Toast.makeText(getContext(), "数据已刷新", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGotRepireStatusListFailed(boolean isException) {
        progressDialog.dismiss();

        if (isException) {
            Toast.makeText(getContext(), "获取数据失败,请检查网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "获取数据失败,请重试", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        NetworkHelper.getInstance().cancelTask(NetworkHelper.TaskType.GetRepireStatus);
    }

    @Override
    public void onRepireStatusMenuClick(RepireStatusBean repireStatusBean) {
        if (repireStatusBean.getCurrentState().equals("4")) {
            Intent intent = new Intent(getActivity(), RepireScoringActivity.class);
            intent.putExtra("RepireStatusBean", repireStatusBean);
            startActivityForResult(intent, 1);
        } else {
            Intent intent = new Intent(getActivity(), RepireStatusDetailActivity.class);
            intent.putExtra("RepireStatusBean", repireStatusBean);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //如果是从评分界面返回 , 则应刷新列表
            refreshList();
        }
    }

    @Override
    public void onRefresh() {
        refreshList();
    }

}
