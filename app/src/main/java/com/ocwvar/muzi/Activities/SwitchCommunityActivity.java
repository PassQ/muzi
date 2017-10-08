package com.ocwvar.muzi.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ocwvar.muzi.Beans.PositionDataBeans.CommunityBean;
import com.ocwvar.muzi.Network.Callbacks.OnRequestDataListCallbacks;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;
import com.ocwvar.muzi.Utils.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Date: 2016/5/8  0:45
 * Project: Muzi
 * 切换小区页面
 */
public class SwitchCommunityActivity extends BaseActivity implements OnRequestDataListCallbacks, DialogInterface.OnCancelListener, AdapterView.OnItemSelectedListener {

    TextView communityName;
    Spinner communityList;
    Button done;
    View reset;

    ArrayAdapter<String> communityNamesAdapter;
    ProgressDialog progressDialog;

    int selectPosition = 0;

    List<CommunityBean> communityBeanList = null;

    @Override
    protected boolean onPreSetup() {
        return true;
    }

    @Override
    protected int setActivityView() {
        return R.layout.activity_switch_community;
    }

    @Override
    protected int onSetToolBar() {
        return R.id.toolbar;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onSetupViews() {
        setTitle(R.string.switch_community_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //对象的实例化
        communityNamesAdapter = new ArrayAdapter<>(SwitchCommunityActivity.this, android.R.layout.simple_spinner_dropdown_item);
        progressDialog = new ProgressDialog(SwitchCommunityActivity.this);
        communityName = (TextView) findViewById(R.id.textView_switch_name);
        communityList = (Spinner) findViewById(R.id.spinner_switch_list);
        done = (Button) findViewById(R.id.button_switch_done);
        reset = findViewById(R.id.view_reset);

        //对象的属性设置
        progressDialog.setMessage(getString(R.string.simple_working));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(this);
        progressDialog.setCancelable(true);

        reset.setOnClickListener(this);
        done.setOnClickListener(this);
        communityList.setOnItemSelectedListener(this);
        communityName.setText(AppOptions.USERINFO.getCommunityName());
        communityNamesAdapter.setDropDownViewResource(R.layout.item_switch_dropdown);
        communityList.setAdapter(communityNamesAdapter);

        //初始化下拉列表
        initList();
    }

    @Override
    protected void onViewClick(View clickedView) {
        switch (clickedView.getId()) {
            case R.id.button_switch_done:
                CommunityBean communityBean = communityBeanList.get(selectPosition);
                if (communityBean != null) {
                    AppOptions.USERINFO.tempCommunityName = communityBean.getName();
                    AppOptions.USERINFO.tempCommunityTAG = communityBean.getID();
                    communityName.setText(communityBean.getName());
                    Toast.makeText(SwitchCommunityActivity.this, getString(R.string.head_to_community_toast) + AppOptions.USERINFO.getCommunityName(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.view_reset:
                if (AppOptions.USERINFO.resetTempLocation()) {
                    communityName.setText(AppOptions.USERINFO.getCommunityName());
                    Toast.makeText(SwitchCommunityActivity.this, R.string.switch_back_default, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SwitchCommunityActivity.this, R.string.error_already_default, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected boolean onViewLongClick(View holdedView) {
        System.out.println("User:  " + AppOptions.USERINFO.user.getCommunityName());
        System.out.println("User:  " + AppOptions.USERINFO.user.getCommunityID());
        System.out.println("Temp:  " + AppOptions.USERINFO.tempCommunityName);
        System.out.println("Temp:  " + AppOptions.USERINFO.tempCommunityTAG);
        return true;
    }

    private void initList() {
        if (communityBeanList == null) {
            communityBeanList = new ArrayList<>();
            NetworkHelper.getInstance().requestDatasList(this, NetworkHelper.DataType.LIST_community, null);
            progressDialog.setCancelable(true);
            progressDialog.show();
        } else {
            for (int i = 0; i < communityBeanList.size(); i++) {
                //添加数据
                communityNamesAdapter.add(communityBeanList.get(i).getName());
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_switch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_switch_sync:
                progressDialog.show();
                NetworkHelper.getInstance().requestDatasList(this, NetworkHelper.DataType.LIST_community, null);
                break;
        }
        return true;
    }

    @Override
    public void onRequestCompleted(NetworkHelper.DataType dataType, ArrayList<Object> datasList) {
        //如果有请求到的数据
        if (datasList.size() > 0) {
            // 清空列表
            communityNamesAdapter.clear();

            for (int i = 0; i < datasList.size(); i++) {
                //添加数据
                communityNamesAdapter.add(((CommunityBean) datasList.get(i)).getName());
                communityBeanList.add((CommunityBean) datasList.get(i));
            }
            //更新列表
            communityNamesAdapter.notifyDataSetChanged();

        } else {
            Toast.makeText(SwitchCommunityActivity.this, R.string.error_no_community, Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
    }

    @Override
    public void onFailed(boolean isException) {
        progressDialog.dismiss();

        Toast.makeText(SwitchCommunityActivity.this, R.string.error_network, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        NetworkHelper.getInstance().cancelTask(NetworkHelper.TaskType.RequestDatasList_List);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectPosition = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
