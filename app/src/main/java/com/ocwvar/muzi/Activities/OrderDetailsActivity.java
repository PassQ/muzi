package com.ocwvar.muzi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.BulletinBean;
import com.ocwvar.muzi.R;



public class OrderDetailsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    Button button;
    Intent sourceIntent;
    BulletinBean sourceBean;
    TextView text_Name,text_Introduce,text_Status,text_Tel,text_Massage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        toolbar= (Toolbar) findViewById(R.id.order_details_toolbar);
        toolbar.setTitle("预约详情");
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sourceIntent = getIntent();
        sourceBean = (BulletinBean) sourceIntent.getSerializableExtra("bulletinBean");
        init();
    }

    private void init() {
        text_Name = (TextView) findViewById(R.id.order_details_name);
        text_Introduce = (TextView) findViewById(R.id.order_details_introduce);
        text_Massage = (TextView) findViewById(R.id.order_details_massage);
        text_Status = (TextView) findViewById(R.id.order_status);
        text_Tel = (TextView) findViewById(R.id.order_details_tel);
        text_Name.setText(sourceBean.getTitle());
        text_Introduce.setText(sourceBean.getIntroduce());
        text_Tel.setText(sourceBean.getPhoneNum());
        button = (Button) findViewById(R.id.order_apply);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(sourceBean.getOrderStatus()!=-1){
            if(sourceBean.getOrderStatus()==0){
                text_Status.setText("正在受理");
            }else if(sourceBean.getOrderStatus()==1){
                text_Status.setText("未评价");
            }else if(sourceBean.getOrderStatus()==2){
                text_Status.setText("已评价");
            }else if(sourceBean.getOrderStatus()==3){
                text_Status.setText("已被商家忽略预约");
            }else if(sourceBean.getOrderStatus()==4){
                text_Status.setText("商家未响应");
            }else {
                text_Status.setText("获取失败");
            }
        }
        text_Massage.setText(sourceBean.getMassage());



    }
}
