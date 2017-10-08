package com.ocwvar.muzi.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Date: 2016/5/12  16:12
 * Project: Muzi
 */
public class GuideDetailWebActivity extends AppCompatActivity {

    private final static String baseURL = NetworkHelper.BASE_URL;
    WebView webView;
    ArtclesBean artclesBean;
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_detail_web);

        if (getIntent().getExtras() != null) {
            artclesBean = getIntent().getExtras().getParcelable("item");
            if (artclesBean != null) {
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar12);
                setSupportActionBar(toolbar);
                setTitle("详情");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                webView = (WebView) findViewById(R.id.webView);
                title = (TextView) findViewById(R.id.textView_guideDetail_title);
                title.setText(artclesBean.getTitle());

                final String style = "<style type=\"text/css\">img{max-width:100%;max-height:auto;}</style>";
                final String htmlData = style + artclesBean.getContent();
                System.out.println(htmlData);
                webView.loadDataWithBaseURL(baseURL, htmlData, "text/html", "utf-8", null);

            } else {
                Toast.makeText(GuideDetailWebActivity.this, "数据损坏,请重试", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(GuideDetailWebActivity.this, "启动失败,请重试", Toast.LENGTH_SHORT).show();
        }

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

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

}
