package com.ocwvar.muzi.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;
import com.ocwvar.muzi.Utils.BaseActivity;

import cn.jpush.android.api.JPushInterface;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class Zc02Activity extends BaseActivity implements View.OnClickListener {


    Button requireCode, checkCode;
    EditText getCodeString;

    boolean counting = false;
    String phoneNumber;

    ProgressDialog progressDialog;
    EventHandler eventHandler;
    Handler handler;

    CountDownThread countDownThread;

    @Override
    protected boolean onPreSetup() {
        if (getIntent().getExtras() != null) {
            phoneNumber = getIntent().getExtras().getString("phoneNumber");
            return !TextUtils.isEmpty(phoneNumber);
        } else {
            return false;
        }
    }

    @Override
    protected int setActivityView() {
        return R.layout.activity_zc_02;
    }

    @Override
    protected int onSetToolBar() {
        return 0;
    }

    @Override
    protected void onSetupViews() {
        setTitle(R.string.register_step2_title);

        //对象的实例化
        requireCode = (Button) findViewById(R.id.button_zc2_sendCode);
        checkCode = (Button) findViewById(R.id.button_zc2_checkCode);
        getCodeString = (EditText) findViewById(R.id.editText_zc2_getCode);
        progressDialog = new ProgressDialog(Zc02Activity.this);
        progressDialog.setMessage(getString(R.string.simple_working));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        //初始化SMSSDK
        initSMSSDK();

        requireCode.setOnClickListener(this);
        checkCode.setOnClickListener(this);
    }

    @Override
    protected void onViewClick(View clickedView) {

    }

    @Override
    protected boolean onViewLongClick(View holdedView) {
        return false;
    }

    /**
     * 初始化 SMS SDK 相关的操作
     */
    private void initSMSSDK() {

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
                        Toast.makeText(Zc02Activity.this, object.get("detail").getAsString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                }

                if (result == SMSSDK.RESULT_COMPLETE) {

                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        Toast.makeText(Zc02Activity.this, R.string.checkPass, Toast.LENGTH_SHORT).show();
                        getCodeString.getText().clear();
                        nextStep();
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //请求验证码成功
                        progressDialog.dismiss();
                    }

                }

            }
        };

        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int i, int i1, Object o) {
                //在SMS事件发生之后,给Handler发送状态消息
                Message msg = new Message();
                msg.arg1 = i;
                msg.arg2 = i1;
                msg.obj = o;
                handler.sendMessage(msg);
            }
        };

        SMSSDK.initSDK(Zc02Activity.this, AppOptions.SMS_KEY, AppOptions.SMS_SECRET);
        SMSSDK.registerEventHandler(eventHandler);

        //如果仍在之前倒数计时的范围内,则继续倒数计时
        if (AppOptions.WEAKCACHE.getZCSMSCounting() > 0) {
            countDownThread = new CountDownThread(System.currentTimeMillis(), AppOptions.WEAKCACHE.getZcSMSCounting_End());
            countDownThread.start();
        } else {
            //否则重置倒数计时数据
            AppOptions.WEAKCACHE.resetZCCounting();
        }


    }

    /**
     * 发送请求验证码
     */
    private void requestCode(String phoneNumber) {
        if (!counting) {
            //如果当前可以发送验证码
            progressDialog.show();

            //记录手机号
            this.phoneNumber = phoneNumber;

            Toast.makeText(Zc02Activity.this, "短信已发送,请稍候...", Toast.LENGTH_SHORT).show();

            //请求验证码
            SMSSDK.getVerificationCode("86", phoneNumber);

            //开始倒数线程
            countDownThread = new CountDownThread(System.currentTimeMillis(), System.currentTimeMillis() + 60000);
            countDownThread.start();
        }
    }

    /**
     * 下一步 , 进入下一个Activity
     */
    private void nextStep() {
        Intent intent = new Intent(Zc02Activity.this, Zc03Activity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销短信SDK的Handler
        SMSSDK.unregisterEventHandler(eventHandler);

        //如果仍在倒计时,则记录下计时的开始与结束时间,并结束倒数计时线程
        if (counting) {
            AppOptions.WEAKCACHE.setZcSMSCounting_Start(countDownThread.startAt);
            AppOptions.WEAKCACHE.setZcSMSCounting_End(countDownThread.endAt);
            countDownThread.cancel();
            countDownThread = null;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_zc2_checkCode:
                String code = getCodeString.getText().toString();
                if (!TextUtils.isEmpty(code)) {
                    progressDialog.show();
                    SMSSDK.submitVerificationCode("86", phoneNumber, code);
                } else {
                    Toast.makeText(Zc02Activity.this, R.string.SMS_Code_Failed, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.button_zc2_sendCode:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        requestCode(phoneNumber);
                    } else {
                        Toast.makeText(Zc02Activity.this, R.string.error_permission_sms, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    requestCode(phoneNumber);
                }
                break;
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
                    requireCode.setBackgroundColor(Color.argb(255, 39, 40, 34));
                    requireCode.setEnabled(false);
                }
            });

            while (!isCanceled && showTime >= 1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requireCode.setText(String.format("%s秒", String.valueOf(showTime)));
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
                    requireCode.setBackgroundColor(Color.argb(255, 253, 112, 67));
                    requireCode.setEnabled(true);
                    requireCode.setText(R.string.sendSMSCode);
                }
            });

        }

        public void cancel() {
            this.isCanceled = true;
        }

    }

}