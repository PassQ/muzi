package com.ocwvar.muzi.Activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Data: 2016/8/8 19:57
 * Project: Muzi
 * Web管理界面
 */
public class WebViewManagerPortal extends AppCompatActivity {

    final String test = NetworkHelper.BASE_URL + "Admin/Maintenance/Mobile/Managerlogin.aspx";
    final String title = "管理页面";
    WebView webView;
    Snackbar loadingMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(WebViewManagerPortal.this);
        setContentView(webView);
        setTitle(title);
        webView.setWebViewClient(new MyWebViewClient());
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        loadingMessage = Snackbar.make(webView, "页面读取中...", Snackbar.LENGTH_INDEFINITE);

        webView.loadUrl(test);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_web_exit:
                webView.freeMemory();
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            webView.goBack();
            return false;
        }
        return true;
    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            loadingMessage.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            loadingMessage.dismiss();
            Snackbar.make(view, "页面读取完毕", Snackbar.LENGTH_SHORT).show();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            loadingMessage.dismiss();
            Snackbar.make(view, "页面读取失败 , 页面或网络当前不可用. ", Snackbar.LENGTH_SHORT).show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webView.loadUrl(url);
            return true;
        }

    }

}
