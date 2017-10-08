package com.ocwvar.muzi.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.ocwvar.muzi.Fragments.Bulletin.BulletinBusinessFragment;
import com.ocwvar.muzi.Fragments.Bulletin.BulletinCircumFragment;
import com.ocwvar.muzi.R;

/**
 tabhost页面
 */


public class BulletinActivty extends AppCompatActivity implements View.OnClickListener {
    private LinearLayoutManager linearLayoutManager;
    private BulletinBusinessFragment bulletinBusinessFragment;
    private BulletinCircumFragment bulletinCircumFragment;
    private View businessLayout;
    private View circumLayout;
    private TextView businessText;
    private TextView circumText;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin);
        init();//初始化
        fragmentManager=getFragmentManager();
        setTabSelection(0);

    }

    private void init() {
        businessLayout=findViewById(R.id.business_Layout);
        circumLayout=findViewById(R.id.circum_Layout);
        businessText= (TextView) findViewById(R.id.business_text);
        circumText= (TextView) findViewById(R.id.circum_text);
        businessLayout.setOnClickListener(this);
        circumLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.business_Layout:
                setTabSelection(0);//点击了tab时的操作
                break;
            case R.id.circum_Layout:
                setTabSelection(1);
                break;
            default:
                break;
        }

    }

    private void setTabSelection(int i) {
        clearSelection();//清空选择状态操作
        FragmentTransaction transaction =fragmentManager.beginTransaction();//开启fragment事务
        hideFragment(transaction);//隐藏所有fragment
        switch (i){
            case 0:
                //当点击了选项卡时，将tab标题进行变色和修改大小操作
                businessText.setTextColor(Color.rgb(252,122,19));
                businessText.setTextSize(20);
                if(bulletinBusinessFragment==null){//进行页面初始化操作
                    bulletinBusinessFragment=new BulletinBusinessFragment();
                    transaction.add(R.id.content,bulletinBusinessFragment);
                }else {
                    transaction.show(bulletinBusinessFragment);
                }
                break;
            case 1:
                circumText.setTextColor(Color.rgb(252,122,19));
                circumText.setTextSize(20);
                if(bulletinCircumFragment==null){
                    bulletinCircumFragment=new BulletinCircumFragment();
                    transaction.add(R.id.content,bulletinCircumFragment);
                }else {
                    transaction.show(bulletinCircumFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if(bulletinBusinessFragment!=null){
            transaction.hide(bulletinBusinessFragment);
        }
        if(bulletinCircumFragment!=null){
            transaction.hide(bulletinCircumFragment);
        }

    }


    private void clearSelection() {
        businessText.setTextSize(16);
        businessText.setTextColor(Color.GRAY);
        circumText.setTextColor(Color.GRAY);
        circumText.setTextSize(16);
    }
}
