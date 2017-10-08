package com.ocwvar.muzi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ocwvar.muzi.Adapters.HistoryAdapter;
import com.ocwvar.muzi.Beans.BulletinBean;
import com.ocwvar.muzi.Network.Callbacks.OnCancalOrderCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetHistoryScrollAtBottomCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetOrderCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;

import java.util.ArrayList;

/*
* 预约历史页面
*
* */
public class HistoryActivity extends AppCompatActivity  implements OnGetOrderCallback,OnGetHistoryScrollAtBottomCallback, OnCancalOrderCallback{
    private RecyclerView mRecyclerView;
    private HistoryAdapter historyAdapter=new HistoryAdapter();
    private Toolbar toolbar;
    int historyTotal,historyIndex;
    //一次读取列表的数据量  默认:10
    private final int pageSize = 10;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);



        toolbar= (Toolbar) findViewById(R.id.toolbar_history);
        toolbar.setTitle("我的预约");
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        historyAdapter.setOnGetHistoryScrollAtBottomCallback(HistoryActivity.this);
        mRecyclerView= (RecyclerView) findViewById(R.id.history_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(historyAdapter);
        historyAdapter.setOnClickLitener(new HistoryAdapter.OnClickLitener() {
            @Override
            public void onItemClick(View view, int position, BulletinBean bulletinBean) {
                if(bulletinBean.getOrderStatus()==1) {
                    Bundle bundle =new Bundle();
                    bundle.putSerializable("bulletinBean",bulletinBean);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(HistoryActivity.this, ReviewDetailsActivity.class);
                    startActivity(intent);
                }else if(bulletinBean.getOrderStatus() == 0){
                    NetworkHelper.getInstance().cancalOrder(HistoryActivity.this,bulletinBean.getOrderID());

                }else if(bulletinBean.getOrderStatus() == 2||bulletinBean.getOrderStatus()==3||bulletinBean.getOrderStatus()==4){
                    Bundle bundle =new Bundle();
                    bundle.putSerializable("bulletinBean",bulletinBean);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(HistoryActivity.this, OrderDetailsActivity.class);
                    startActivity(intent);
                    Toast.makeText(HistoryActivity.this,"查看详细",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        historyIndex = 1;
        historyTotal = -1;
        //清空数据操作
        historyAdapter.clearDatas();
        NetworkHelper.getInstance().getOrderList(HistoryActivity.this,pageSize,historyIndex, AppOptions.USERINFO.user.getPhoneNumber());
    }

    //获取预约列表成功
    @Override
    public void onGetOrderCompleted(int pageSize, int pageIndex, ArrayList<BulletinBean> list, int totalCount) {
        historyTotal = totalCount;
        historyAdapter.addData(list);

    }
    //获取预约列表失败
    @Override
    public void onGetOrderFailed(boolean isException) {

    }
    //滚动到底部回调
    @Override
    public void onHistoryScrollAtBottom() {
        if(shouldLoadNextPage()){
            historyIndex += 1;
            NetworkHelper.getInstance().getOrderList(this,pageSize,historyIndex, AppOptions.USERINFO.user.getPhoneNumber());
        }

    }
    //是否需要读取下一页列表
    private  boolean shouldLoadNextPage(){
        return historyTotal != -1 && historyTotal != historyAdapter.getItemCount();
    }


    //取消预约成功回调
    @Override
    public void onCancalOrderCompleted(int commentID, boolean isSuccessed) {
        Toast.makeText(HistoryActivity.this,"取消成功",Toast.LENGTH_LONG).show();
    }

    //取消预约失败回调
    @Override
    public void onCancalOrderFailed(boolean isException) {
        Toast.makeText(HistoryActivity.this,"取消失败",Toast.LENGTH_LONG).show();
    }
}
