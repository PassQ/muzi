package com.ocwvar.muzi.Fragments.Bulletin;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ocwvar.muzi.Activities.CircumActivity;
import com.ocwvar.muzi.Adapters.BulletinAdapters.BulletinCircumAdapter;
import com.ocwvar.muzi.Beans.BulletinBean;
import com.ocwvar.muzi.Network.Callbacks.OnCircumScrollAtBottomCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetCircumCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 覃毅
 * 获取周边列表操作
 */
public class BulletinCircumFragment extends Fragment implements OnCircumScrollAtBottomCallback,OnGetCircumCallback {

    private RecyclerView mRecyclerView;
    int circumIndex = 1,circumTotal = -1;
    //一次读取列表的数据量  默认:10
    private final int pageSize = 10;
    private BulletinCircumAdapter bulletinCircumAdapter=new BulletinCircumAdapter();



    public BulletinCircumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        circumIndex = 1;
        circumTotal = -1;
        //清空数据
        bulletinCircumAdapter.clearDatas();
        NetworkHelper.getInstance().getCircumList(BulletinCircumFragment.this,pageSize,circumIndex);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View circumLayout=inflater.inflate(R.layout.fragment_bulletin_circum,container,false);
        mRecyclerView= (RecyclerView) circumLayout.findViewById(R.id.bulletin_circum_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.setAdapter(bulletinCircumAdapter);
        bulletinCircumAdapter.setOnCircumScrollAtBottomCallback(BulletinCircumFragment.this);
        bulletinCircumAdapter.setOnClickLitener(new BulletinCircumAdapter.OnClickLitener() {
            @Override
            public void onItemClick(View view, int position,BulletinBean bulletinBean) {
                Intent intent=new Intent();
                Bundle bundle =new Bundle();
                bundle.putSerializable("BulletinBean",bulletinBean);
                intent.putExtras(bundle);

                intent.setClass(getActivity(), CircumActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        // Inflate the layout for this fragment
        return circumLayout;
    }


    //滑动到底部监听回调
    @Override
    public void onCircumScrollAtBottom() {
        if(shouldLoadNextPage()){
            circumIndex += 1;
            NetworkHelper.getInstance().getCircumList(this,pageSize,circumIndex);
        }

    }
    //获取列表成功回调
    @Override
    public void onGetCircumCompleted(int pageSize, int pageIndex, ArrayList<BulletinBean> list, int totalCount) {
        circumTotal=totalCount;
        bulletinCircumAdapter.addData(list);
    }
    //获取列表失败回调
    @Override
    public void onGetCircumFailed(boolean isException) {

    }
    //是否需要读取下一页列表
    private  boolean shouldLoadNextPage(){
        return circumTotal != -1 && circumTotal != bulletinCircumAdapter.getItemCount();
    }
}
