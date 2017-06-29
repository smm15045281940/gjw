package com.gangjianwang.www.gangjianwang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import config.NetConfig;

public class AgreeMentActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout mBackRl;
    private WebView mWv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_agree_ment, null);
        setContentView(rootView);
        initView();
        setData();
        setListener();
        loadData();
    }

    private void initView() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_agreement_back);
        mWv = (WebView) rootView.findViewById(R.id.wv_agreement);
    }

    private void setData() {
        mWv.getSettings().setJavaScriptEnabled(true);
        mWv.setWebViewClient(new AgreeMentWebViewClient());
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
    }

    private void loadData() {
        mWv.loadUrl(NetConfig.agreeMentUrl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_agreement_back:
                finish();
                break;
            default:
                break;
        }
    }

    private class AgreeMentWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
