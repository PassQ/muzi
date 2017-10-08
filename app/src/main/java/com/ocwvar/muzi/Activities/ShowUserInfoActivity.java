package com.ocwvar.muzi.Activities;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.UserBean;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;
import com.ocwvar.muzi.Utils.BaseActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Date: 2016/5/6  10:21
 * Project: Muzi
 * 显示用户信息界面
 */

public class ShowUserInfoActivity extends BaseActivity {

    TextView phone, apar, comm, unit, room;

    @Override
    protected boolean onPreSetup() {
        return true;
    }

    @Override
    protected int setActivityView() {
        return R.layout.activity_user_info;
    }

    @Override
    protected int onSetToolBar() {
        return R.id.toolbar3;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onSetupViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.userinfo_title);

        phone = (TextView) findViewById(R.id.textView_info_phone);
        apar = (TextView) findViewById(R.id.textView_info_apar);
        comm = (TextView) findViewById(R.id.textView_info_comm);
        unit = (TextView) findViewById(R.id.textView_info_unit);
        room = (TextView) findViewById(R.id.textView_info_room);
    }

    @Override
    protected void onViewClick(View clickedView) {

    }

    @Override
    protected boolean onViewLongClick(View holdedView) {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        UserBean userBean = AppOptions.USERINFO.user;
        if (userBean != null) {
            phone.setText(userBean.getPhoneNumber());
            comm.setText(userBean.getCommunityName());
            apar.setText(String.format("%s栋", userBean.getApartment()));
            unit.setText(String.format("%s单元", userBean.getUnitName()));
            room.setText(String.format("%s号", userBean.getRoomName()));
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

}
