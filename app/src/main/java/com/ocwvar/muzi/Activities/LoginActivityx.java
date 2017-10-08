package com.ocwvar.muzi.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ocwvar.muzi.Beans.UserBean;
import com.ocwvar.muzi.Network.Callbacks.OnLoginCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.Network.OrderNoPendding;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;
import com.ocwvar.muzi.Utils.BaseActivity;
import com.ocwvar.muzi.Utils.PushReciver;

import cn.jpush.android.api.JPushInterface;

/**
 * Project muzi
 * Created by 区成伟
 * On 2016/11/2 10:15
 * File Location com.ocwvar.muzi.Activities
 */

public class LoginActivityx extends BaseActivity implements TextView.OnEditorActionListener, OnLoginCallback {

    EditText getUserName, getPassword;
    TextView register, forget, guest, manager;
    Button loginButton;

    ProgressDialog progressDialog;

    @Override
    protected boolean onPreSetup() {
        return true;
    }

    @Override
    protected int setActivityView() {
        return R.layout.activity_login;
    }

    @Override
    protected int onSetToolBar() {
        return 0;
    }

    @Override
    protected void onSetupViews() {
        //检查权限  针对Android 6.0+用户
        if (!checkPermission()) {
            requestPermission();
        }

        //实例化对象
        getUserName = (EditText) findViewById(R.id.editText_getUsername);
        getPassword = (EditText) findViewById(R.id.editText_getPassword);
        loginButton = (Button) findViewById(R.id.button_login);
        register = (TextView) findViewById(R.id.textView_register);
        forget = (TextView) findViewById(R.id.textView_forget);
        progressDialog = new ProgressDialog(LoginActivityx.this);
        guest = (TextView) findViewById(R.id.guest);
        manager = (TextView) findViewById(R.id.manager);

        //对象的相关属性设置
        progressDialog.setMessage(getString(R.string.login_loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        loginButton.setOnClickListener(this);
        forget.setOnClickListener(this);
        register.setOnClickListener(this);
        getPassword.setOnEditorActionListener(this);
        guest.setOnClickListener(this);
        manager.setOnClickListener(this);

    }

    @Override
    protected void onViewClick(View clickedView) {
        switch (clickedView.getId()) {
            case R.id.guest:
                //游客登录
                loginAsGuest();
                break;
            case R.id.manager:
                //管理登录
                startActivity(new Intent(LoginActivityx.this, WebViewManagerPortal.class));
                break;
            case R.id.button_login:
                //普通登录
                login();
                break;
            case R.id.textView_forget:
                //忘记密码
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.textView_register:
                //注册用户
                startActivity(new Intent(this, ZcActivity.class));
                break;
        }
    }

    /**
     * 游客登录
     */
    private void loginAsGuest() {
        //设置游客用户帐户信息
        UserBean userBean = new UserBean();
        userBean.setUserName("游客用户");
        userBean.setCommunityID("2");
        userBean.setCommunityName("银山小区");

        AppOptions.USERINFO.setUser(userBean);

        //小区TAG为 -1表示不接收推送
        PushReciver.getInstance().init(getApplicationContext(), "-1", "-1");

        Toast.makeText(LoginActivityx.this, R.string.login_successful_guest, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivityx.this, MainActivity.class));
    }

    /**
     * 开始登录
     */
    private void login() {
        final String username = getUserName.getText().toString();
        final String password = getPassword.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            //检查输入的数据是否合法
            Toast.makeText(LoginActivityx.this, R.string.error_input_empty, Toast.LENGTH_SHORT).show();
        } else {
            //显示登录读取对话框
            progressDialog.show();
            //开始登录功能
            NetworkHelper.getInstance().userLogin(this, username, password);
        }
    }

    @Override
    protected boolean onViewLongClick(View holdedView) {
        return false;
    }

    /**
     * 检查权限
     *
     * @return 是否已获取所有权限
     */
    @SuppressWarnings("SimplifiableIfStatement")
    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkPermission() {
        if (AppOptions.SDK_VERSION >= 23) {
            return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    /**
     * 请求权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission() {
        requestPermissions(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS}, 1);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == KeyEvent.KEYCODE_ENDCALL) {
            login();
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
        JPushInterface.stopPush(getApplicationContext());
    }

    /**
     * 登录成功回调
     *
     * @param username 登录的用户名
     * @param userBean 获取到的用户信息Bean
     */
    @Override
    public void onLoginCompleted(String username, UserBean userBean) {

        //初始化推送接收器
        JPushInterface.resumePush(getApplicationContext());
        PushReciver.getInstance().init(getApplicationContext(), userBean.getCommunityID(), userBean.getPhoneNumber());

        //缓存用户数据
        AppOptions.USERINFO.setUser(userBean);

        //同步未成功订单信息
        OrderNoPendding.getInstance().db2OrderNo(userBean.getPhoneNumber());

        //启动主界面
        progressDialog.cancel();
        Toast.makeText(LoginActivityx.this, R.string.login_successful_normal, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivityx.this, MainActivity.class));
        getPassword.getText().clear();
        getUserName.getText().clear();
    }

    /**
     * 登录失败回调
     *
     * @param message     失败时返回的信息
     * @param isException 是否为异常
     */
    @Override
    public void onLoginFailed(String message, boolean isException) {
        if(progressDialog.isShowing()){
            progressDialog.cancel();
        }

        if (isException) {
            //是异常导致的登录失败
            Toast.makeText(LoginActivityx.this, R.string.error_network, Toast.LENGTH_SHORT).show();
        } else {
            getPassword.getText().clear();
            getUserName.getText().clear();
            Toast.makeText(LoginActivityx.this, message, Toast.LENGTH_SHORT).show();
        }
    }

}
