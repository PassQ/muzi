package com.ocwvar.muzi.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ocwvar.muzi.Network.Callbacks.OnCheckUser;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.BaseActivity;

import cn.jpush.android.api.JPushInterface;

public class ZcActivity extends BaseActivity implements OnCheckUser, DialogInterface.OnCancelListener {

    Button requireCode;
    EditText getPhoneNumber;
    ProgressDialog progressDialog;

    @Override
    protected boolean onPreSetup() {
        return true;
    }

    @Override
    protected int setActivityView() {
        return R.layout.activity_zc;
    }

    @Override
    protected int onSetToolBar() {
        return 0;
    }

    @Override
    protected void onSetupViews() {
        setTitle(R.string.register_step1_title);
        requireCode = (Button) findViewById(R.id.button_zc_sendCode);
        getPhoneNumber = (EditText) findViewById(R.id.editText_inputPN);
        progressDialog = new ProgressDialog(ZcActivity.this);

        progressDialog.setMessage(getString(R.string.simple_checking));
        progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(this);
        progressDialog.setCanceledOnTouchOutside(false);
        requireCode.setOnClickListener(this);
    }

    @Override
    protected void onViewClick(View clickedView) {
        switch (clickedView.getId()) {
            case R.id.button_zc_sendCode:
                final String phoneNumber = getPhoneNumber.getText().toString();

                //先检查手机号码的正确性
                if (isPhoneNumber(phoneNumber)) {
                    progressDialog.show();
                    //查询是否已存在相同的用户
                    NetworkHelper.getInstance().checkUser(this, phoneNumber);
                } else {
                    Toast.makeText(ZcActivity.this, R.string.phoneNumber_error, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected boolean onViewLongClick(View holdedView) {
        return false;
    }

    /**
     * 检测是否为手机号
     *
     * @param string 要检测的字符串
     * @return 检测结果
     */
    private boolean isPhoneNumber(String string) {
        if (TextUtils.isEmpty(string)) {
            return false;
        } else {
            return string.matches("^[1][3,5,7,8]+\\d{9}");
        }
    }

    @Override
    public void onCompleted(String checkID, boolean isExist) {
        progressDialog.dismiss();

        if (isExist) {
            Toast.makeText(ZcActivity.this, R.string.phoneNumber_registered, Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(ZcActivity.this, Zc02Activity.class);
            intent.putExtra("phoneNumber", checkID);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onFailed() {
        progressDialog.dismiss();

        Toast.makeText(ZcActivity.this, R.string.error_network, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        NetworkHelper.getInstance().cancelTask(NetworkHelper.TaskType.CheckUserExist);
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
