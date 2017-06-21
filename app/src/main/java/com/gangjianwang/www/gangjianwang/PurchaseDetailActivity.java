package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import utils.ToastUtils;

public class PurchaseDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout backRl;
    private String detailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_purchase_detail, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_purchase_detail_back);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null)
            detailId = intent.getStringExtra("purchaseId");
        ToastUtils.toast(this, detailId);
    }

    private void setListener() {
        backRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_purchase_detail_back:
                finish();
                break;
            default:
                break;
        }
    }
}
