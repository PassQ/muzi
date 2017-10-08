package com.ocwvar.muzi.Fragments.Bulletin;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ocwvar.muzi.Activities.BusinessActivity;
import com.ocwvar.muzi.Activities.HistoryActivity;
import com.ocwvar.muzi.Adapters.BulletinAdapters.BulletinBusinessAdapter;
import com.ocwvar.muzi.Beans.BulletinBean;
import com.ocwvar.muzi.Network.Callbacks.OnBusinessScrollAtBottomCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetBusinessCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;

import java.util.ArrayList;


/**
 *商家列表页面
 *
 */
public class BulletinBusinessFragment extends Fragment
        implements OnGetBusinessCallback,OnBusinessScrollAtBottomCallback {
    private LinearLayout historyLayout;
    private RecyclerView mRecyclerView;
    private ArrayList<BulletinBean> bulletinBeenList;
    int businessIndex = 1, businessTotalCount = -1;
    //一次读取列表的数据量  默认:10
    private final int pageSize = 10;

    private BulletinBusinessAdapter bulletinBusinessAdapter=new BulletinBusinessAdapter();


    public BulletinBusinessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         View BusinessLayout=inflater.inflate(R.layout.fragment_bulletin_business, container, false);

        mRecyclerView= (RecyclerView) BusinessLayout.findViewById(R.id.bulletin_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(bulletinBusinessAdapter);

        historyLayout= (LinearLayout) BusinessLayout.findViewById(R.id.business_history);
        bulletinBusinessAdapter.setOnBusinessScrollAtBottomCallback(BulletinBusinessFragment.this);
        historyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getActivity(), HistoryActivity.class);
                startActivity(intent);
            }
        });

        bulletinBusinessAdapter.setOnClickLitener(new BulletinBusinessAdapter.OnClickLitener() {
            @Override
            public void onItemClick(View view, int position, BulletinBean bulletinBean) {
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putSerializable("bulletinBean",bulletinBean);
                intent.putExtras(bundle);
                intent.setClass(getActivity(), BusinessActivity.class);
                startActivity(intent);

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        updata();
        return BusinessLayout;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void updata() {
        businessIndex = 1;
        businessTotalCount = -1;
        //清空数据操作
        bulletinBusinessAdapter.clearDatas();
        NetworkHelper.getInstance().getBusinessList(this,pageSize,businessIndex);
    }

    //获取列表成功时回调
    @Override
    public void onGetBusinessCompleted(int pageSize, int pageIndex, ArrayList<BulletinBean> list, int totalCount) {
        businessTotalCount = totalCount;

        bulletinBusinessAdapter.addData(list);

    }

    //获取列表失败时回调
    @Override
    public void onGetBusinessFailed(boolean isException) {

    }

    //是否需要读取下一页列表
    private  boolean shouldLoadNextPage(){
        return businessTotalCount != -1 && businessTotalCount != bulletinBusinessAdapter.getItemCount();
    }

    //列表滚动到底部监听
    @Override
    public void onBusinessScrollAtBottom() {
        if(shouldLoadNextPage()){
            businessIndex += 1;
            NetworkHelper.getInstance().getBusinessList(this,pageSize,businessIndex);
        }
    }
}
