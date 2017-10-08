package com.ocwvar.muzi.Activities;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ocwvar.muzi.Beans.UserBean;
import com.ocwvar.muzi.Network.Callbacks.OnUpdateInfoCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;
import com.ocwvar.muzi.Utils.BaseActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Date: 2016/5/6  11:03
 * Project: Muzi
 * 更改密码界面
 */

public class UpdatePasswordActivity extends BaseActivity implements View.OnClickListener, OnUpdateInfoCallback {

    boolean isReset = false;

    ProgressDialog progressDialog;

    EditText getOldPassword, getNewPassword, getSecondPassword;
    Button done;
    View view;

    @Override
    protected boolean onPreSetup() {
        if (getIntent().getExtras() != null) {
            isReset = getIntent().getExtras().getBoolean("isReset");
            return true;
        }
        return false;
    }

    @Override
    protected int setActivityView() {
        return R.layout.activity_update_password;
    }

    @Override
    protected int onSetToolBar() {
        return R.id.toolbar4;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onSetupViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view = findViewById(R.id.view_request_old_password);
        getOldPassword = (EditText) findViewById(R.id.editText_old_password);
        getNewPassword = (EditText) findViewById(R.id.editText_new_passwoed);
        getSecondPassword = (EditText) findViewById(R.id.editText_second_password);
        done = (Button) findViewById(R.id.button_done);

        //如果是重设密码模式，则不需要输入旧密码
        if (isReset) {
            setTitle(R.string.password_title_reset);
            view.setVisibility(View.GONE);
        } else {
            setTitle(R.string.changePassword_title);
        }

        done.setOnClickListener(this);

        progressDialog = new ProgressDialog(UpdatePasswordActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.simple_working));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onViewClick(View clickedView) {
        final String oldPassword = getOldPassword.getText().toString();
        final String newPassword = getNewPassword.getText().toString();
        final String secPassword = getSecondPassword.getText().toString();

        if (isReset) {
            if (!TextUtils.isEmpty(newPassword) && !TextUtils.isEmpty(secPassword) && newPassword.equals(secPassword)) {
                updatePassword(newPassword);
            } else {
                getNewPassword.getText().clear();
                getSecondPassword.getText().clear();
                Toast.makeText(UpdatePasswordActivity.this, R.string.password_incorrect_again, Toast.LENGTH_SHORT).show();
            }
        } else {
            if (oldPassword.equals(AppOptions.USERINFO.user.getPassword())) {
                //如果旧密码正确
                if (!TextUtils.isEmpty(newPassword) && !TextUtils.isEmpty(secPassword) && newPassword.equals(secPassword)) {
                    updatePassword(newPassword);
                } else {
                    getNewPassword.getText().clear();
                    getSecondPassword.getText().clear();
                    Toast.makeText(UpdatePasswordActivity.this, R.string.password_incorrect_again, Toast.LENGTH_SHORT).show();
                }
            } else {
                getOldPassword.getText().clear();
                Toast.makeText(UpdatePasswordActivity.this, R.string.password_incorrect, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected boolean onViewLongClick(View holdedView) {
        return false;
    }

    /**
     * 开始更新密码
     *
     * @param newPassword 新的密码
     */
    private void updatePassword(String newPassword) {
        progressDialog.show();

        UserBean userBean = AppOptions.USERINFO.user;
        if (userBean != null) {
            userBean.setPassword(newPassword);
            NetworkHelper.getInstance().updateUser(this, userBean);
        } else {
            Toast.makeText(UpdatePasswordActivity.this, R.string.error_network, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void completed(UserBean userBean) {
        progressDialog.cancel();

        getOldPassword.getText().clear();
        getNewPassword.getText().clear();
        getSecondPassword.getText().clear();
        Toast.makeText(UpdatePasswordActivity.this, R.string.change_password_successful, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failed(boolean syncFailed) {
        progressDialog.cancel();

        if (syncFailed) {
            Toast.makeText(UpdatePasswordActivity.this, R.string.change_password_success_half, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(UpdatePasswordActivity.this, R.string.error_network, Toast.LENGTH_SHORT).show();
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
