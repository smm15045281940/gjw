package com.gangjianwang.www.gangjianwang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class StoreOfferActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout backRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_store_offer, null);
        setContentView(rootView);
        initView();
        setListener();
    }

    private void initView() {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_store_offer_back);
    }

    private void setListener() {
        backRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_store_offer_back:
                finish();
                break;
            default:
                break;
        }
    }
}
