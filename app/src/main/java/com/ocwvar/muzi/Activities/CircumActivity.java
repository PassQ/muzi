package com.ocwvar.muzi.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.BulletinBean;
import com.ocwvar.muzi.R;
import com.squareup.picasso.Picasso;

public class CircumActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView text_Name,text_Introduce,text_Tel;
    ImageView imageView_Detailed;
    Intent sourceIntent;
    BulletinBean sourceBean;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circum);
        toolbar= (Toolbar) findViewById(R.id.circum_toolbar);
        toolbar.setTitle("周边信息");
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sourceIntent = getIntent();
        sourceBean = (BulletinBean) sourceIntent.getSerializableExtra("BulletinBean");
        init();
    }

    private void init() {
        text_Name = (TextView) findViewById(R.id.circum_detailed_name);
        text_Introduce = (TextView) findViewById(R.id.circum_detailed_introduce);
        text_Tel = (TextView) findViewById(R.id.circum_detailed_tel);
        imageView_Detailed = (ImageView) findViewById(R.id.circum_detailed_imageview);
        text_Name.setText(sourceBean.getTitle());
        text_Introduce.setText(sourceBean.getIntroduce());
        text_Tel.setText(sourceBean.getPhoneNum());
        if(!TextUtils.isEmpty(sourceBean.getImagePath())) {
            Picasso
                    .with(imageView_Detailed.getContext())
                    .load(sourceBean.getImagePath())
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.ic_failed)
                    .placeholder(R.drawable.ic_loading)
                    .into(imageView_Detailed);
        }else {
            Picasso
                    .with(imageView_Detailed.getContext())
                    .load(R.drawable.lmg_periphery_preview)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.ic_failed)
                    .placeholder(R.drawable.ic_loading)
                    .into(imageView_Detailed);
        }
        imageView_Detailed.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }
}
