package com.ocwvar.muzi.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ocwvar.muzi.Network.Callbacks.OnUpdateRequestHelpCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Date: 2016/5/7  15:24
 * Project: Muzi
 * 上传文章页面
 */
public class RequestHelpActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, OnUpdateRequestHelpCallback {

    public static final int ARTCLE_ASKHELP = 100;
    public static final int ARTCLE_HXSOUND = 101;
    public static final int RESULT_UPLOADED = 200;
    public static final int RESULT_STAY = 201;

    View typeList;
    EditText getTitle, getQuestion;
    TextView typeShower;
    Button send;

    PopupMenu popupMenu;

    String tag = null;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        setTitle("提交新文章");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int artcleType = -1;

        if (savedInstanceState != null) {
            artcleType = savedInstanceState.getInt("ArtcleType");
        } else if (getIntent().getExtras() != null) {
            artcleType = getIntent().getExtras().getInt("ArtcleType");
        } else {
            Toast.makeText(RequestHelpActivity.this, "无请求数据 , 请重新进入", Toast.LENGTH_SHORT).show();
            finish();
        }

        //实例化对象
        typeList = findViewById(R.id.view_request_typelist);
        typeShower = (TextView) findViewById(R.id.textView_request_type);
        getTitle = (EditText) findViewById(R.id.editText_request_title);
        getQuestion = (EditText) findViewById(R.id.editText_request_question);
        send = (Button) findViewById(R.id.button_request_send);
        progressDialog = new ProgressDialog(RequestHelpActivity.this);
        popupMenu = new PopupMenu(RequestHelpActivity.this, typeList);

        //对象的属性设置
        send.setOnClickListener(this);
        typeList.setOnClickListener(this);
        popupMenu.setOnMenuItemClickListener(this);

        progressDialog.setMessage("正在提交数据,请稍候......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        initPopupMenu(artcleType);
        setResult(RESULT_STAY);
    }

    /**
     * 根据不同的参数加载不同的菜单
     */
    private void initPopupMenu(int artcleType) {
        switch (artcleType) {
            case ARTCLE_ASKHELP:
                popupMenu.getMenuInflater().inflate(R.menu.menu_request_type, popupMenu.getMenu());
                break;
            case ARTCLE_HXSOUND:
                popupMenu.getMenuInflater().inflate(R.menu.menu_newartcle_hx, popupMenu.getMenu());
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_request_typelist:
                popupMenu.show();
                break;
            case R.id.button_request_send:
                updateDatas();
                break;
        }
    }

    private void updateDatas() {
        String content = getQuestion.getText().toString();
        String title = getTitle.getText().toString();

        if (TextUtils.isEmpty(content) || tag == null) {
            Toast.makeText(RequestHelpActivity.this, "请输入有效内容和选择对应的文章类型", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();
        if (TextUtils.isEmpty(title)) {
            title = "未定义标题";
        }
        //提交的数据
        String[] datas = new String[]{
                title,      //标题
                tag,        //文章所属
                AppOptions.USERINFO.user.getCommunityID(),        //提交的小区ID
                AppOptions.USERINFO.user.getLoginID(),        //用户ID
                content         //文章内容
        };
        NetworkHelper.getInstance().updateRequestHelp(this, datas);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        //设置TAG分类显示文字
        typeShower.setText(item.getTitle());

        switch (item.getItemId()) {
            case R.id.menu_request_myHelp:
                //我献爱心
                tag = "11";
                break;
            case R.id.menu_request_myAsk:
                //我的困难
                tag = "12";
                break;
            case R.id.menu_request_question:
                //服务投诉
                tag = "13";
                break;
            case R.id.menu_request_askService:
                //服务求助
                tag = "14";
                break;
            case R.id.menu_request_suggest:
                //合理化建议
                tag = "15";
                break;
        }

        return true;
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
    public void onCompleted() {
        progressDialog.cancel();
        Toast.makeText(RequestHelpActivity.this, "提交成功!", Toast.LENGTH_SHORT).show();
        getQuestion.getText().clear();
        getTitle.getText().clear();
        setResult(RESULT_UPLOADED);
    }

    @Override
    public void onFailed(boolean isException) {
        progressDialog.cancel();
        if (isException) {
            Toast.makeText(RequestHelpActivity.this, "提交时发生了错误 , 请检查网络环境", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RequestHelpActivity.this, "数据提交失败 , 请重试", Toast.LENGTH_SHORT).show();
        }
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
