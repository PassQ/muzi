package com.ocwvar.muzi.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ocwvar.muzi.Adapters.ReviewAdapter;
import com.ocwvar.muzi.Beans.BulletinBean;
import com.ocwvar.muzi.Beans.BusinessImageBean;
import com.ocwvar.muzi.Beans.ReplyTabBean;
import com.ocwvar.muzi.Network.Callbacks.OnGetReplyImageCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetReplyListCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetReplyTabCallback;
import com.ocwvar.muzi.Network.Callbacks.OnReplyListAtBottomCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.SimpleDownloader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/*
* 评论列表页面
* */
public class ReviewActivity extends AppCompatActivity implements OnGetReplyTabCallback,OnGetReplyListCallback,OnReplyListAtBottomCallback,OnGetReplyImageCallback {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private ArrayList<ReplyTabBean> replyTabBeenList;
    private ArrayList<BulletinBean> reviewBeanList ;
    private TextView tv1,tv2,tv3,tv4;
    private LinearLayout linearLayout1,linearLayout2,linearLayout3,linearLayout4;
    private int tabTotal=0;
    private Intent sourceIntent;
    private BulletinBean sourceBean;
    int ReviewIndex = 1, ReviewTotalCount = -1;
    //一次读取列表的数据量  默认:10
    private final int pageSize = 10;
    private ReviewAdapter reviewAdapter;
    private SimpleDownloader simpleDownloader = new SimpleDownloader();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        reviewAdapter = new ReviewAdapter(this);

        sourceIntent=this.getIntent();
        sourceBean= (BulletinBean) sourceIntent.getSerializableExtra("bulletinBean");

        init();
        NetworkHelper.getInstance().getReplyTab(ReviewActivity.this,sourceBean.getBusinessID(), 4);
        NetworkHelper.getInstance().getReplyList(this,pageSize,ReviewIndex,sourceBean.getBusinessID());

        mRecyclerView= (RecyclerView) findViewById(R.id.review_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(reviewAdapter);
        reviewAdapter.setOnReplyListAtBottomCallback(ReviewActivity.this);
        reviewAdapter.setOnClickLitener(new ReviewAdapter.OnClickLitener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onImageClick(String path) {
                simpleDownloader.downloadFile(path, Environment.getExternalStorageDirectory().getPath() + "/anhe/", "ReviewImage.jpg", new SimpleDownloader.OnDownloadCallback() {
                    @Override
                    public void onDownloadSuccess(File downloadedFile) {
                        final Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(downloadedFile), "image/*");
                        startActivity(intent);
                    }

                    @Override
                    public void onDownloadFailed() {
                        Toast.makeText(ReviewActivity.this,"网络出错，请重试",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void init() {
        toolbar= (Toolbar) findViewById(R.id.review_toolbar);
        toolbar.setTitle("详细评论");
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        linearLayout1 = (LinearLayout) findViewById(R.id.review_linear1);
        linearLayout2 = (LinearLayout) findViewById(R.id.review_linear2);
        linearLayout3 = (LinearLayout) findViewById(R.id.review_linear3);
        linearLayout4 = (LinearLayout) findViewById(R.id.review_linear4);
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        ReviewIndex = 1;
//        ReviewTotalCount = -1;
//        //清空数据操作
//        reviewAdapter.clearDatas();
//        NetworkHelper.getInstance().getReplyList(this,pageSize,ReviewIndex,sourceBean.getBusinessID());
//    }

    //获取商家预设评论列表成功回调
    @Override
    public void onGetReplyTabCompleted(ArrayList<ReplyTabBean> list, int total) {
        this.replyTabBeenList = list;
        this.tabTotal = total;
        setReplyTabView();
        
    }

    //设置预设评论控件
    private void setReplyTabView() {
        if(replyTabBeenList!= null){
            if(replyTabBeenList.size()>0 ){
                switch (tabTotal){
                    case -1:
                    case  0:
                        break;
                    case  1:
                        tv1.setText(replyTabBeenList.get(0).getContent()+"("+replyTabBeenList.get(0).getCount()+")");
                        linearLayout1.setVisibility(View.VISIBLE);
                        break;
                    case  2:
                        tv1.setText(replyTabBeenList.get(0).getContent()+"("+replyTabBeenList.get(0).getCount()+")");
                        linearLayout1.setVisibility(View.VISIBLE);
                        tv2.setText(replyTabBeenList.get(1).getContent()+"("+replyTabBeenList.get(1).getCount()+")");
                        linearLayout2.setVisibility(View.VISIBLE);
                        break;
                    case  3:
                        tv1.setText(replyTabBeenList.get(0).getContent()+"("+replyTabBeenList.get(0).getCount()+")");
                        linearLayout1.setVisibility(View.VISIBLE);
                        tv2.setText(replyTabBeenList.get(1).getContent()+"("+replyTabBeenList.get(1).getCount()+")");
                        linearLayout2.setVisibility(View.VISIBLE);
                        tv3.setText(replyTabBeenList.get(2).getContent()+"("+replyTabBeenList.get(2).getCount()+")");
                        linearLayout3.setVisibility(View.VISIBLE);
                        break;
                    case  4:
                        tv1.setText(replyTabBeenList.get(0).getContent()+"("+replyTabBeenList.get(0).getCount()+")");
                        linearLayout1.setVisibility(View.VISIBLE);
                        tv2.setText(replyTabBeenList.get(1).getContent()+"("+replyTabBeenList.get(1).getCount()+")");
                        linearLayout2.setVisibility(View.VISIBLE);
                        tv3.setText(replyTabBeenList.get(2).getContent()+"("+replyTabBeenList.get(2).getCount()+")");
                        linearLayout3.setVisibility(View.VISIBLE);
                        tv4.setText(replyTabBeenList.get(3).getContent()+"("+replyTabBeenList.get(3).getCount()+")");
                        linearLayout4.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;

                }
            }
        }else {
            linearLayout1.setVisibility(View.INVISIBLE);
            linearLayout2.setVisibility(View.INVISIBLE);
            linearLayout3.setVisibility(View.INVISIBLE);
            linearLayout4.setVisibility(View.INVISIBLE);
        }
    }


    //获取商家预设评论列表失败回调
    @Override
    public void onGetReplyTabFailed(boolean isException) {
        setReplyTabView();
    }
    //获取商家详细评论列表成功回调
    @Override
    public void onGetReplyListCompleted(ArrayList<BulletinBean> list, int total) {
        ReviewTotalCount = total;
        reviewBeanList = list;
        reviewAdapter.addData(list);

    }
    //获取商家详细评论列表失败回调
    @Override
    public void onGetReplyListFailed(boolean isException) {

    }

    //底部监听回调
    @Override
    public void onReplyListAtBottom() {
        if(shouldLoadNextPage()){
            ReviewIndex += 1;
            NetworkHelper.getInstance().getReplyList(ReviewActivity.this,pageSize, ReviewIndex,sourceBean.getBusinessID());

        }

    }

    //是否需要读取下一页列表
    private  boolean shouldLoadNextPage(){
        return ReviewTotalCount != -1 && ReviewTotalCount != reviewAdapter.getItemCount();
    }



    //获取图片集合成功回调
    @Override
    public void onGetReplyImageCompleted(ArrayList<BusinessImageBean> list, int count) {
        if(reviewBeanList!=null){
            if(reviewBeanList.size()>0){

                    reviewBeanList.get(count).setImageList(list);


            }
        }
    }

    @Override
    public void onGetReplyImageFailed(boolean isException) {

    }
}
