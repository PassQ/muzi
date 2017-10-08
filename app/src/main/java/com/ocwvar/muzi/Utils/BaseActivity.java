package com.ocwvar.muzi.Utils;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Project BlurBGActivityTest
 * Created by 区成伟
 * On 2016/9/21 13:13
 * File Location com.ocwvar.blurbgactivitytest
 * 基础Activity
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private Toolbar toolbar;
    private WeakReference<ProgressDialog> weakprogressDialog = new WeakReference<>(null);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (onPreSetup()) {
            setContentView(setActivityView());
            settingToolBar(onSetToolBar());
            onSetupViews();
        } else {
            finish();
        }
    }

    /**
     * 预先设置操作
     * </p>
     * 执行顺序: 0
     *
     * @return 是否进行接下来的操作 , False则会结束页面
     */
    protected abstract boolean onPreSetup();

    /**
     * 设置Activity使用的资源
     * </p>
     * 执行顺序: 1
     *
     * @return 布局资源
     */
    protected abstract int setActivityView();

    /**
     * 设置Activity使用的资源
     * </p>
     * 执行顺序: 2
     *
     * @return ToolBar 资源ID
     */
    protected abstract int onSetToolBar();

    /**
     * 初始化控件的操作
     * </p>
     * 执行顺序: 3
     */
    @SuppressWarnings("ConstantConditions")
    protected abstract void onSetupViews();

    /**
     * 设置默认ToolBar属性
     */
    private void settingToolBar(int resID) {
        Toolbar toolbar = (Toolbar) findViewById(resID);
        if (toolbar != null) {
            this.toolbar = toolbar;
            setSupportActionBar(toolbar);
        }
    }

    /**
     * 返回ToolBar对象
     *
     * @return ToolBar对象 , 如果没有在 onSetToolBar() 中返回资源ID , 则会返回NULL
     */
    protected
    @Nullable
    Toolbar getToolBar() {
        return this.toolbar;
    }

    /**
     * @return ToolBar是否已经加载成功
     */
    protected boolean isToolBarLoaded() {
        return toolbar != null;
    }

    /**
     * 控件的点击事件
     *
     * @param clickedView 被点击的控件
     */
    protected abstract void onViewClick(View clickedView);

    /**
     * 长按的点击事件
     *
     * @param holdedView 被点击的控件
     */
    protected abstract boolean onViewLongClick(View holdedView);

    /**
     * 显示一个等待对话框
     *
     * @param message          要显示的信息
     * @param canBeCancel      是否可以被取消
     * @param onCancelListener 取消时的回调接口
     */
    public void showProgressDialog(final String message, final boolean canBeCancel, final DialogInterface.OnCancelListener onCancelListener) {

        ProgressDialog progressDialog = weakprogressDialog.get();

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(BaseActivity.this);
        }
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(canBeCancel);
        progressDialog.setOnCancelListener(onCancelListener);

        weakprogressDialog = new WeakReference<>(progressDialog);

        weakprogressDialog.get().show();
    }

    /**
     * 使等待对话框消失
     */
    public void dismissProgressDialog() {

        final ProgressDialog progressDialog = weakprogressDialog.get();

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }

    @Override
    public void onClick(View v) {
        onViewClick(v);
    }

    @Override
    public boolean onLongClick(View v) {
        return onViewLongClick(v);
    }

}
