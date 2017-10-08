package com.ocwvar.muzi.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ocwvar.muzi.Adapters.RepireDerailPicturesAdapter;
import com.ocwvar.muzi.Beans.RepireStatusBean;
import com.ocwvar.muzi.R;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Data: 2016/7/11 15:32
 * Project: Muzi
 * 报修信息详情 (只在未维修完成前使用)
 */
public class RepireStatusDetailActivity extends AppCompatActivity {

    RepireStatusBean bean;
    TextView name, phone, message;
    RecyclerView recycleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            bean = getIntent().getExtras().getParcelable("RepireStatusBean");
        } else if (savedInstanceState != null) {
            bean = savedInstanceState.getParcelable("RepireStatusBean");
        } else {
            Toast.makeText(RepireStatusDetailActivity.this, "读取数据失败 , 请重试", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (bean == null) {
            Toast.makeText(RepireStatusDetailActivity.this, "读取数据失败 , 请重试", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            initViews();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(bean.getTitle());
            name.setText(bean.getServicemanName());
            phone.setText(bean.getServicemanTel());
            message.setText(bean.getContext());
        }

    }

    /**
     * 初始化View
     */
    private void initViews() {
        setContentView(R.layout.activity_repirestaus_detail);
        if (bean.getPictures() != null) {
            recycleView = (RecyclerView) findViewById(R.id.recycleView);
            recycleView.setAdapter(new RepireDerailPicturesAdapter(bean.getPicturesThu()));
            recycleView.setLayoutManager(new LinearLayoutManager(RepireStatusDetailActivity.this, LinearLayoutManager.VERTICAL, false));
            recycleView.setHasFixedSize(true);
        }
        name = (TextView) findViewById(R.id.textView_name);
        phone = (TextView) findViewById(R.id.textView_phone);
        message = (TextView) findViewById(R.id.textView_message);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
