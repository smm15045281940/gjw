package com.gangjianwang.www.gangjianwang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import config.NetConfig;

public class DigouStoreActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout backRl;
    private WebView digouWv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_digou_store, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
        loadData();
    }

    private void initView() {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_digou_store_back);
        digouWv = (WebView) rootView.findViewById(R.id.wv_digou_store);
    }

    private void initData() {
        digouWv.getSettings().setJavaScriptEnabled(true);
    }

    private void setData() {
        digouWv.setWebViewClient(new GjwWebViewClient());
    }

    private void loadData() {
        digouWv.loadUrl(NetConfig.digouStoreUrl);
    }

    private void setListener() {
        backRl.setOnClickListener(this);
    }

    private class GjwWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && digouWv.canGoBack()) {
            digouWv.goBack();
            return true;
        } else {
            finish();
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_digou_store_back:
                finish();
                break;
            default:
                break;
        }
    }
}
