package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class PropertyActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mBackRl;
    private RelativeLayout mAccountBalanceRl, mPrepaidCardRl, mVoucherRl, mRedbagRl, mVipIntegrateRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_myproperty);
        initView();
        setListener();
    }

    private void initView() {
        mBackRl = (RelativeLayout) findViewById(R.id.rl_myproperty_back);
        mAccountBalanceRl = (RelativeLayout) findViewById(R.id.rl_myproperty_accountbalance);
        mPrepaidCardRl = (RelativeLayout) findViewById(R.id.rl_myproperty_prepaidphonecardaccountbalance);
        mVoucherRl = (RelativeLayout) findViewById(R.id.rl_myproperty_voucher);
        mRedbagRl = (RelativeLayout) findViewById(R.id.rl_myproperty_platredbag);
        mVipIntegrateRl = (RelativeLayout) findViewById(R.id.rl_myproperty_vipintegrate);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mAccountBalanceRl.setOnClickListener(this);
        mPrepaidCardRl.setOnClickListener(this);
        mVoucherRl.setOnClickListener(this);
        mRedbagRl.setOnClickListener(this);
        mVipIntegrateRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_myproperty_back:
                finish();
                break;
            case R.id.rl_myproperty_accountbalance:
                startActivity(new Intent(PropertyActivity.this, AccountBalanceActivity.class));
                break;
            case R.id.rl_myproperty_prepaidphonecardaccountbalance:
                startActivity(new Intent(PropertyActivity.this, PrepaidPhoneCardActivity.class));
                break;
            case R.id.rl_myproperty_voucher:
                startActivity(new Intent(PropertyActivity.this, VoucherActivity.class));
                break;
            case R.id.rl_myproperty_platredbag:
                startActivity(new Intent(PropertyActivity.this, RedBagActivity.class));
                break;
            case R.id.rl_myproperty_vipintegrate:
                startActivity(new Intent(PropertyActivity.this, IntegrateActivity.class));
                break;
            default:
                break;
        }
    }
}
