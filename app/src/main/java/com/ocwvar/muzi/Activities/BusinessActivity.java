package com.ocwvar.muzi.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ocwvar.muzi.Adapters.BusinessImageAdapter;
import com.ocwvar.muzi.Beans.BulletinBean;
import com.ocwvar.muzi.Beans.BusinessImageBean;
import com.ocwvar.muzi.Beans.ReplyTabBean;
import com.ocwvar.muzi.Network.Callbacks.OnGetBusinessImageCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetReplyTabCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.SimpleDownloader;
import com.ocwvar.muzi.Views.RatingBar;

import java.io.File;
import java.util.ArrayList;

public class BusinessActivity extends AppCompatActivity implements View.OnClickListener,OnGetBusinessImageCallback ,OnGetReplyTabCallback {
    private Toolbar toolbar;
    private Button buttonOrder;
    private LinearLayout reviewLayout;
    private RecyclerView mRecyclerView;
    private Intent sourceIntent;
    private int replyTabcount=0;
    private BulletinBean sourceBean;
    private ArrayList<ReplyTabBean> replyTabBeenList;
    private TextView text_name,text_score,text_address,text_tel,reply_1,reply_2,reply_3,reply_4,reply_5,text_openTime,text_offDay;
    private LinearLayout linearLayout1,linearLayout2,linearLayout3,linearLayout4,linearLayout5;
    private RatingBar ratingBar;
    private BusinessImageAdapter businessImageAdapter=new BusinessImageAdapter();
    private SimpleDownloader simpleDownloader =new SimpleDownloader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        sourceIntent=this.getIntent();
        sourceBean= (BulletinBean) sourceIntent.getSerializableExtra("bulletinBean");

        init();
        setView(sourceBean);

        NetworkHelper.getInstance().getBusinessImage(BusinessActivity.this,sourceBean.getBusinessID());
        NetworkHelper.getInstance().getReplyTab(BusinessActivity.this, sourceBean.getBusinessID(), 5);
        businessImageAdapter.setOnClickLitener(new BusinessImageAdapter.OnClickLitener() {
            @Override
            public void onItemClick(View view, int position, String path) {
                simpleDownloader.downloadFile(path, Environment.getExternalStorageDirectory().getPath() + "/anhe/", "businessImage.jpg", new SimpleDownloader.OnDownloadCallback() {
                    @Override
                    public void onDownloadSuccess(File downloadedFile) {
                        final Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(downloadedFile), "image/*");
                        startActivity(intent);
                    }

                    @Override
                    public void onDownloadFailed() {
                        Toast.makeText(BusinessActivity.this,"网络出错，请重试",Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    private void setView(BulletinBean sourceBean) {
        if(sourceBean!=null) {
            text_name.setText(sourceBean.getTitle());
            text_address.setText("详细地址：" + sourceBean.getAddress());
            if(!TextUtils.isEmpty(sourceBean.getOpenTime())&&!TextUtils.isEmpty(sourceBean.getEndTime())){
                if("00:00".equals(sourceBean.getOpenTime())&&"00:00".equals(sourceBean.getEndTime())){
                    text_openTime.setText("开店时间：全天营业");
                }else {
                    text_openTime.setText("开店时间："+ sourceBean.getOpenTime()+"~"+sourceBean.getEndTime());
                }

            }else {
                text_openTime.setText("");
                text_openTime.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(sourceBean.getOffdays())){
              text_offDay.setText(sourceBean.getOffdays());

            }else {
                text_offDay.setText("");
                text_offDay.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(sourceBean.getScore())) {
                text_score.setText("评分  " + sourceBean.getScore());
                ratingBar.setStar(Float.parseFloat(sourceBean.getScore()));
            }else {
                text_score.setText("");
                ratingBar.setStar(0);
            }
            text_tel.setText(sourceBean.getPhoneNum());
        }


    }


    //设置预设评论控件
    private void setReplyTabView() {
        if(replyTabBeenList!= null){
            if(replyTabBeenList.size()>0 ){
                switch (replyTabcount){
                    case -1:
                    case  0:
                        break;
                    case  1:
                        reply_1.setText(replyTabBeenList.get(0).getContent());
                        linearLayout1.setVisibility(View.VISIBLE);
                        break;
                    case  2:
                        reply_1.setText(replyTabBeenList.get(0).getContent());
                        linearLayout1.setVisibility(View.VISIBLE);
                        reply_2.setText(replyTabBeenList.get(1).getContent());
                        linearLayout2.setVisibility(View.VISIBLE);
                        break;
                    case  3:
                        reply_1.setText(replyTabBeenList.get(0).getContent());
                        linearLayout1.setVisibility(View.VISIBLE);
                        reply_2.setText(replyTabBeenList.get(1).getContent());
                        linearLayout2.setVisibility(View.VISIBLE);
                        reply_3.setText(replyTabBeenList.get(2).getContent());
                        linearLayout3.setVisibility(View.VISIBLE);
                        reply_3.setTextColor(Color.GRAY);
                        linearLayout3.setBackgroundResource(R.drawable.shape_cart_gray);
                        break;
                    case  4:
                        reply_1.setText(replyTabBeenList.get(0).getContent());
                        linearLayout1.setVisibility(View.VISIBLE);
                        reply_2.setText(replyTabBeenList.get(1).getContent());
                        linearLayout2.setVisibility(View.VISIBLE);
                        reply_3.setText(replyTabBeenList.get(2).getContent());
                        linearLayout3.setVisibility(View.VISIBLE);
                        reply_3.setTextColor(Color.GRAY);
                        linearLayout3.setBackgroundResource(R.drawable.shape_cart_gray);
                        reply_4.setText(replyTabBeenList.get(3).getContent());
                        linearLayout4.setVisibility(View.VISIBLE);
                        reply_4.setTextColor(Color.GRAY);
                        linearLayout4.setBackgroundResource(R.drawable.shape_cart_gray);
                        break;
                    case  5:
                        reply_1.setText(replyTabBeenList.get(0).getContent());
                        linearLayout1.setVisibility(View.VISIBLE);
                        reply_2.setText(replyTabBeenList.get(1).getContent());
                        linearLayout2.setVisibility(View.VISIBLE);
                        reply_3.setText(replyTabBeenList.get(2).getContent());
                        linearLayout3.setVisibility(View.VISIBLE);
                        reply_3.setTextColor(Color.GRAY);
                        linearLayout3.setBackgroundResource(R.drawable.shape_cart_gray);
                        reply_4.setText(replyTabBeenList.get(3).getContent());
                        linearLayout4.setVisibility(View.VISIBLE);
                        reply_4.setTextColor(Color.GRAY);
                        linearLayout4.setBackgroundResource(R.drawable.shape_cart_gray);
                        reply_5.setText(replyTabBeenList.get(4).getContent());
                        linearLayout5.setVisibility(View.VISIBLE);
                        reply_5.setTextColor(Color.GRAY);
                        linearLayout5.setBackgroundResource(R.drawable.shape_cart_gray);


                        break;
                    default:
                        break;

                }
            }
        }else {
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.GONE);
            linearLayout3.setVisibility(View.GONE);
            linearLayout4.setVisibility(View.GONE);
            linearLayout5.setVisibility(View.GONE);
        }
    }


    //控件初始化操作
    private void init() {
        toolbar= (Toolbar) findViewById(R.id.business_toolbar);
        toolbar.setTitle("商家详情");
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ratingBar= (RatingBar) findViewById(R.id.RatingBar);
        text_offDay = (TextView) findViewById(R.id.business_offdays);
        text_name= (TextView) findViewById(R.id.business_name);
        text_address= (TextView) findViewById(R.id.business_address);
        text_tel= (TextView) findViewById(R.id.business_tel);
        text_score= (TextView) findViewById(R.id.business_score);
        text_openTime = (TextView) findViewById(R.id.business_openTime);
        reply_1 = (TextView) findViewById(R.id.tv1);
        reply_2 = (TextView) findViewById(R.id.tv2);
        reply_3 = (TextView) findViewById(R.id.tv3);
        reply_4 = (TextView) findViewById(R.id.tv4);
        reply_5 = (TextView) findViewById(R.id.tv5);
        linearLayout1= (LinearLayout) findViewById(R.id.business_linear1);
        linearLayout2= (LinearLayout) findViewById(R.id.business_linear2);
        linearLayout3= (LinearLayout) findViewById(R.id.business_linear3);
        linearLayout4= (LinearLayout) findViewById(R.id.business_linear4);
        linearLayout5= (LinearLayout) findViewById(R.id.business_linear5);

        buttonOrder= (Button) findViewById(R.id.business_btn_order);
        if(sourceBean.getStatus()!=1){
            buttonOrder.setEnabled(false);
        }
        buttonOrder.setOnClickListener(this);
        reviewLayout= (LinearLayout) findViewById(R.id.business_linear6);
        reviewLayout.setOnClickListener(this);
        mRecyclerView= (RecyclerView) findViewById(R.id.recycle_image);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        mRecyclerView.setAdapter(businessImageAdapter);
    }


    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("bulletinBean", sourceBean);
        switch (view.getId()){
            case R.id.business_btn_order:
                Intent intent=new Intent();
                intent.putExtras(bundle);
                intent.setClass(BusinessActivity.this,OrderActivity.class);
                startActivity(intent);
                break;
            case R.id.business_linear6:
                Intent intent1=new Intent();
                intent1.putExtras(bundle);
                intent1.setClass(BusinessActivity.this,ReviewActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }


    //图片列表获取成功回调
    @Override
    public void onGetBusinessImageCompleted(ArrayList<BusinessImageBean> imageList, int total) {
        businessImageAdapter.setTotal(total);
        businessImageAdapter.addData(imageList);

    }

    //图片列表获取失败回调
    @Override
    public void onGetBusinessImageFailed(boolean isException) {

    }


    //预设评论列表获取成功回调
    @Override
    public void onGetReplyTabCompleted(ArrayList<ReplyTabBean> list, int total) {
        replyTabBeenList = list;
        this.replyTabcount = total;
        setReplyTabView();


    }

    //预设评论列表获取失败回调
    @Override
    public void onGetReplyTabFailed(boolean isException) {
        setReplyTabView();

    }
}
