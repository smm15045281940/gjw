package com.gangjianwang.www.gangjianwang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccountBalanceActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout mBackRl;
    private LinearLayout accountBalanceLl,rechargeDetailLl,accountCashLl;
    private TextView accountBalanceTv,rechargeDetailTv,accountCashTv;
    private View accountBalanceV,rechargeDetailV,accountCashV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_account_balance, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
    }

    private void initView() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_accountbalance_back);
        accountBalanceLl = (LinearLayout) rootView.findViewById(R.id.ll_account_balance);
    }

    private void initData() {
    }

    private void setData() {
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_accountbalance_back:
                finish();
                break;
            default:
                break;
        }
    }
}
