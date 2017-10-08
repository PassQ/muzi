package com.ocwvar.muzi.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Network.Callbacks.OnUpLikeCallback;
import com.ocwvar.muzi.Network.JsonDecoder;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Data: 2016/7/11 13:51
 * Project: Muzi
 * 文章界面  用webview来读取
 */
public class ArtcleActivity extends AppCompatActivity implements OnUpLikeCallback {

    private final static String baseURL = NetworkHelper.BASE_URL;
    private final static String TAG = "文章读取界面";

    ArtclesBean artclesBean;
    WebView webView;
    Toolbar toolbar;
    String artcleID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artclesBean = null;

        if (getIntent().getExtras() != null) {
            //如果有传递进来的数据 , 则先检查是否有  文章Bean , 再检查是否有文章ID

            artclesBean = getIntent().getExtras().getParcelable("ArtclesBean");
            if (artclesBean == null) {
                artcleID = getIntent().getExtras().getString("ArtclesID");
                if (!TextUtils.isEmpty(artcleID)) {
                    //如果只有文章ID , 则开始初始化View 和开始下载文章数据
                    Log.w(TAG, "传递数据---文章ID 已读取 " + artcleID);
                    initViews();
                    new DownloadArtcle(artcleID).execute();
                    return;
                }
            } else {
                Log.w(TAG, "传递数据---文章Bean Parcelable 对象已读取");
            }
        } else if (savedInstanceState != null) {
            //再检查保存的数据中是否有  文章Bean  ,  再检查是否有文章ID
            artclesBean = savedInstanceState.getParcelable("ArtclesBean");

            if (artclesBean == null) {
                artcleID = savedInstanceState.getString("ArtclesID");
                if (!TextUtils.isEmpty(artcleID)) {
                    //如果只有文章ID , 则开始初始化View 和开始下载文章数据
                    Log.w(TAG, "传递数据---文章ID 已读取 " + artcleID);
                    initViews();
                    new DownloadArtcle(artcleID).execute();
                    return;
                } else {
                    Log.w(TAG, "保存数据---文章Bean Parcelable 对象已读取");
                }
            }

        } else {
            //如果既没有传入数据 , 也没有先前保存的数据 ,则判定本次读取无效 , 结束页面
            Toast.makeText(ArtcleActivity.this, "读取文章数据失败 , 请重试", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (artclesBean == null && artcleID == null) {
            //如果读取的文章Bean为空 同时 文章ID也为空 , 则判定本次读取无效 , 结束页面
            Toast.makeText(ArtcleActivity.this, "读取文章数据失败 , 请重试", Toast.LENGTH_SHORT).show();
            finish();
        } else if (isArtcleBeanVaild(artclesBean)) {
            //如果读取的文章数据无效 , 则判定本次读取无效 , 结束页面 , 否则开始加载文章Html内容到Webview中
            if (initViews()) {
                //加载网页的风格
                initData(artclesBean);
            } else {
                Toast.makeText(ArtcleActivity.this, "加载页面数据失败 , 请重试", Toast.LENGTH_SHORT).show();
                finish();
            }

        } else if (artcleID == null) {
            //最后如果文章ID也为空 , 则判定本次读取无效 , 结束页面
            Toast.makeText(ArtcleActivity.this, "文章数据不完整 , 请重试", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    /**
     * 检查Artcle是否有效
     *
     * @param artclesBean 要检查的arrcleBean
     * @return 有效状态
     */
    private boolean isArtcleBeanVaild(ArtclesBean artclesBean) {
        if (artclesBean != null) {
            //如果文章有主体内容以及标题，则算作有效
            return !TextUtils.isEmpty(artclesBean.getContent()) && !TextUtils.isEmpty(artclesBean.getTitle());
        } else {
            return false;
        }
    }

    /**
     * 加载View的数据
     *
     * @return 是否加载成功
     */
    private boolean initViews() {
        setContentView(R.layout.activity_artcle_webview);
        this.webView = (WebView) findViewById(R.id.webView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (webView != null && toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            WebSettings setting = webView.getSettings();
            setting.setSupportZoom(false);
            setting.setLoadsImagesAutomatically(true);
            setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 开始加载数据
     *
     * @param artclesBean 文章数据源
     */
    private void initData(ArtclesBean artclesBean) {
        final String style = "<style type=\"text/css\">img{max-width:100%;max-height:auto;}</style>";
        final String htmlData = style + artclesBean.getContent();

        setTitle(artclesBean.getTitle());
        toolbar.setSubtitle("文章发布时间:" + artclesBean.getAddTime());
        webView.loadDataWithBaseURL(baseURL, htmlData, "text/html", "utf-8", null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item_like:
                NetworkHelper.getInstance().upLike(ArtcleActivity.this,AppOptions.USERINFO.user.getPhoneNumber(),artclesBean.getTitle());
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isArtcleBeanVaild(artclesBean)) {
            //如果有有效的文章数据 , 则优先保存
            outState.putParcelable("ArtclesBean", artclesBean);
        } else if (!TextUtils.isEmpty(artcleID)) {
            //如果有有效的文章ID , 同时没有有效的文章数据 , 则保存文章ID
            outState.putString("ArtclesID", artcleID);
        }
    }

    //获取到menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_like,menu);
        return true;
    }



    //点赞成功调用
    @Override
    public void onUpLikeCompleted(String userTel, String massage, String score) {
        Toast.makeText(ArtcleActivity.this,massage+"积分+"+score,Toast.LENGTH_LONG).show();
    }
    //点赞失败调用
    @Override
    public void onUpLikeFailed(boolean isException, String userTel) {


    }

    /**
     * 下载文章数据
     */
    class DownloadArtcle extends AsyncTask<Intent, Intent, ArtclesBean> implements DialogInterface.OnCancelListener {

        private final String URL_DownloadArtcleInterface = "http://119.29.195.12/AppAjax/GetSingleArticleHandle.ashx";
        private String artcleID;
        private ProgressDialog progressDialog;

        public DownloadArtcle(String artcleID) {
            this.artcleID = artcleID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ArtcleActivity.this);
            progressDialog.setMessage("正在下载文章，请稍候……");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setOnCancelListener(this);
            progressDialog.show();
        }

        @Override
        protected ArtclesBean doInBackground(Intent... params) {
            OkHttpClient httpClient = new OkHttpClient();
            RequestBody requestBody = new FormEncodingBuilder().add("ID", artcleID).build();
            Request request = new Request.Builder().post(requestBody).url(URL_DownloadArtcleInterface).build();
            Response response;
            try {
                response = httpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    ArrayList<ArtclesBean> arrayList = JsonDecoder.getArtclesJsonArray(response.body().string());
                    if (arrayList == null || arrayList.size() <= 0) {
                        return null;
                    } else {
                        return arrayList.get(0);
                    }
                }
            } catch (IOException e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArtclesBean artclesBean) {
            super.onPostExecute(artclesBean);
            progressDialog.dismiss();

            if (isArtcleBeanVaild(artclesBean)) {
                //如果下载完的文章信息无错 , 则开始加载
                initData(artclesBean);
            } else {
                Toast.makeText(ArtcleActivity.this, "文章数据损坏 , 无法解析", Toast.LENGTH_SHORT).show();
                finish();
            }

        }

        @Override
        public void onCancel(DialogInterface dialog) {
            cancel(true);
            finish();
        }

    }

}
