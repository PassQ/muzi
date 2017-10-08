package com.ocwvar.muzi.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ocwvar.muzi.Beans.UserBean;
import com.ocwvar.muzi.Network.Callbacks.OnGetUserInfoCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;
import com.ocwvar.muzi.Utils.BaseActivity;

import cn.jpush.android.api.JPushInterface;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Date: 2016/5/5  14:16
 * Project: Muzi
 * 找回密码界面
 */

public class ForgetPasswordActivity extends BaseActivity implements OnGetUserInfoCallback {

    EditText getPhonenumber, getCode;
    Button findButton, requestCode;
    EventHandler eventHandler;
    Handler handler;

    CountDownThread countDownThread;
    ProgressDialog progressDialog;
    String phoneNumber;
    boolean counting = false;

    @Override
    protected boolean onPreSetup() {
        return true;
    }

    @Override
    protected int setActivityView() {
        return R.layout.activity_forget;
    }

    @Override
    protected int onSetToolBar() {
        return R.id.toolbar;
    }

    @Override
    protected void onSetupViews() {
        setTitle("忘记密码");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(ForgetPasswordActivity.this);
        getPhonenumber = (EditText) findViewById(R.id.editText_forget_getPhone);
        getCode = (EditText) findViewById(R.id.editText_getcode);
        findButton = (Button) findViewById(R.id.button_forget_next);
        requestCode = (Button) findViewById(R.id.button_sendCode);

        requestCode.setOnClickListener(this);
        findButton.setOnClickListener(this);
        progressDialog.setMessage("操作进行中,请稍候...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        initHandler();

        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int i, int i1, Object o) {
                Message msg = new Message();
                msg.arg1 = i;
                msg.arg2 = i1;
                msg.obj = o;
                handler.sendMessage(msg);
            }
        };

        //初始化短信SDK
        SMSSDK.initSDK(ForgetPasswordActivity.this, AppOptions.SMS_KEY, AppOptions.SMS_SECRET);
        SMSSDK.registerEventHandler(eventHandler);

        //如果仍在之前倒数计时的范围内,则继续倒数计时
        if (AppOptions.WEAKCACHE.getForgetSMSCounting() > 0) {
            countDownThread = new CountDownThread(System.currentTimeMillis(), AppOptions.WEAKCACHE.getForgetSMSCounting_End());
            countDownThread.start();
        } else {
            //否则重置倒数计时数据
            AppOptions.WEAKCACHE.resetForgetCounting();
        }
    }

    @Override
    protected void onViewClick(View clickedView) {
        switch (clickedView.getId()) {
            case R.id.button_forget_next:
                //验证短信验证码
                final String code = getCode.getText().toString();
                if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(code)) {
                    progressDialog.show();
                    SMSSDK.submitVerificationCode("86", phoneNumber, code);
                }
                break;
            case R.id.button_sendCode:
                //发送请求验证码的短信
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        requestCode(getPhonenumber.getText().toString());
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, R.string.error_permission_sms, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    requestCode(getPhonenumber.getText().toString());
                }
                break;
        }
    }

    @Override
    protected boolean onViewLongClick(View holdedView) {
        return false;
    }

    /**
     * 创建Handler对象   用于处理短信验证等信息的处理
     */
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;

                final String returnText = data.toString();
                Log.w("MOB短信模块", "" + returnText);

                try {
                    Throwable throwable = (Throwable) data;
                    JsonObject object = new JsonParser().parse(throwable.getMessage()).getAsJsonObject();
                    if (object.has("detail")) {
                        Toast.makeText(ForgetPasswordActivity.this, object.get("detail").getAsString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                }

                if (result == SMSSDK.RESULT_COMPLETE) {

                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        Toast.makeText(ForgetPasswordActivity.this, R.string.SMS_Code_Pass, Toast.LENGTH_SHORT).show();
                        getPhonenumber.getText().clear();
                        getCode.getText().clear();
                        findUserPassword();
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        progressDialog.dismiss();
                    }

                }

            }
        };
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
    protected void onDestroy() {
        super.onDestroy();
        //注销短信SDK的Handler
        SMSSDK.unregisterEventHandler(eventHandler);

        //如果仍在倒计时,则记录下计时的开始与结束时间,并结束倒数计时线程
        if (counting) {
            AppOptions.WEAKCACHE.setForgetSMSCounting_Start(countDownThread.startAt);
            AppOptions.WEAKCACHE.setForgetSMSCounting_End(countDownThread.endAt);
            countDownThread.cancel();
            countDownThread = null;
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

    /**
     * 开始查找用户
     */
    private void findUserPassword() {
        NetworkHelper.getInstance().getUserInfo(this, phoneNumber);
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

    /**
     * 发送请求验证码
     */
    private void requestCode(String phoneNumber) {
        if (!counting) {
            //如果当前可以发送验证码

            if (isPhoneNumber(phoneNumber)) {
                //如果手机号合法

                progressDialog.show();

                //记录手机号
                this.phoneNumber = phoneNumber;

                Toast.makeText(ForgetPasswordActivity.this, R.string.SMS_Sended, Toast.LENGTH_SHORT).show();

                //请求验证码
                SMSSDK.getVerificationCode("86", getPhonenumber.getText().toString());

                //开始倒数线程
                countDownThread = new CountDownThread(System.currentTimeMillis(), System.currentTimeMillis() + 60000);
                countDownThread.start();
            } else {
                //如果手机号不合法

                Toast.makeText(ForgetPasswordActivity.this, R.string.phoneNumber_error, Toast.LENGTH_SHORT).show();
                getPhonenumber.getText().clear();
            }
        }
    }

    /**
     * 查找用户信息的回调
     *
     * @param userBean 查找到的信息
     */
    @Override
    public void onCompleted(UserBean userBean) {
        progressDialog.dismiss();
        phoneNumber = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
        builder.setTitle(R.string.forget_title);
        builder.setMessage(getString(R.string.head_result_password) + userBean.getPassword());
        builder.show();
    }

    /**
     * 查找失败的回调
     *
     * @param isException 是否为异常
     */
    @Override
    public void onFailed(boolean isException) {
        progressDialog.dismiss();
        phoneNumber = null;

        if (isException) {
            Toast.makeText(ForgetPasswordActivity.this, R.string.error_network, Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
            builder.setTitle(R.string.forget_title);
            builder.setMessage(getString(R.string.result_no_user));
            builder.show();
        }

    }

    /**
     * 倒数计时线程
     */
    private class CountDownThread extends Thread {

        long startAt, endAt;
        int showTime;
        private boolean isCanceled = false;

        CountDownThread(long startAt, long endAt) {
            this.startAt = startAt;
            this.endAt = endAt;
            showTime = (int) ((endAt - startAt) / 1000);
        }

        @Override
        public void run() {

            //倒数计时标记
            counting = true;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    requestCode.setBackgroundColor(Color.argb(255, 39, 40, 34));
                    requestCode.setEnabled(false);
                }
            });

            while (!isCanceled && showTime >= 1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requestCode.setText(String.format("%s秒", Integer.toString(showTime)));
                    }
                });
                showTime -= 1;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            counting = false;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    requestCode.setBackgroundColor(Color.argb(255, 253, 112, 67));
                    requestCode.setEnabled(true);
                    requestCode.setText(R.string.sendSMSCode);
                }
            });

        }

        public void cancel() {
            this.isCanceled = true;
        }

    }

}
