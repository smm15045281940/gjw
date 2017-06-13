package com.gangjianwang.www.gangjianwang;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fragment.AccountBalanceFragment;
import fragment.AccountCashFragment;
import fragment.RechargeDetailFragment;

public class AccountBalanceActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mBackRl;
    private TextView mAccountBalanceTitleTv;
    private TextView mAccountBalanceTv, mRechargeDetailTv, mAccountCashTv;
    private View mAccountBalanceV, mRechargeDetailV, mAccountCashV;

    private FragmentManager mFragmentManager;
    private List<Fragment> mFragmentList;
    private int curIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_account_balance);
        initView();
        initManager();
        setListener();
    }

    private void initView() {
        mBackRl = (RelativeLayout) findViewById(R.id.rl_accountbalance_back);
        mAccountBalanceTitleTv = (TextView) findViewById(R.id.tv_accountbalance_title);
        mAccountBalanceTv = (TextView) findViewById(R.id.tv_accountbalance);
        mRechargeDetailTv = (TextView) findViewById(R.id.tv_rechargedetail);
        mAccountCashTv = (TextView) findViewById(R.id.tv_accountcash);
        mAccountBalanceV = findViewById(R.id.v_accountbalance);
        mRechargeDetailV = findViewById(R.id.v_rechargedetail);
        mAccountCashV = findViewById(R.id.v_accountcash);

        mAccountBalanceTv.setTextColor(Color.RED);
        mAccountBalanceV.setVisibility(View.VISIBLE);
    }

    private void initManager() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentList = new ArrayList<>();
        AccountBalanceFragment accountBalanceFragment = new AccountBalanceFragment();
        RechargeDetailFragment rechargeDetailFragment = new RechargeDetailFragment();
        AccountCashFragment accountCashFragment = new AccountCashFragment();
        mFragmentList.add(accountBalanceFragment);
        mFragmentList.add(rechargeDetailFragment);
        mFragmentList.add(accountCashFragment);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.ll_accountbalance_sit, mFragmentList.get(0));
        transaction.commit();
        curIndex = 0;
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mAccountBalanceTv.setOnClickListener(this);
        mRechargeDetailTv.setOnClickListener(this);
        mAccountCashTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_accountbalance_back:
                finish();
                break;
            case R.id.tv_accountbalance:
                selected(0);
                changeFragment(0);
                break;
            case R.id.tv_rechargedetail:
                selected(1);
                changeFragment(1);
                break;
            case R.id.tv_accountcash:
                selected(2);
                changeFragment(2);
                break;
        }
    }

    private void selected(int a) {
        switch (a) {
            case 0:
                mAccountBalanceTitleTv.setText("预存款账户");

                mAccountBalanceTv.setTextColor(Color.RED);
                mRechargeDetailTv.setTextColor(Color.BLACK);
                mAccountCashTv.setTextColor(Color.BLACK);

                mAccountBalanceV.setVisibility(View.VISIBLE);
                mRechargeDetailV.setVisibility(View.INVISIBLE);
                mAccountCashV.setVisibility(View.INVISIBLE);
                break;
            case 1:
                mAccountBalanceTitleTv.setText("预存款充值");

                mAccountBalanceTv.setTextColor(Color.BLACK);
                mRechargeDetailTv.setTextColor(Color.RED);
                mAccountCashTv.setTextColor(Color.BLACK);

                mAccountBalanceV.setVisibility(View.INVISIBLE);
                mRechargeDetailV.setVisibility(View.VISIBLE);
                mAccountCashV.setVisibility(View.INVISIBLE);
                break;
            case 2:
                mAccountBalanceTitleTv.setText("预存款提现");

                mAccountBalanceTv.setTextColor(Color.BLACK);
                mRechargeDetailTv.setTextColor(Color.BLACK);
                mAccountCashTv.setTextColor(Color.RED);

                mAccountBalanceV.setVisibility(View.INVISIBLE);
                mRechargeDetailV.setVisibility(View.INVISIBLE);
                mAccountCashV.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void changeFragment(int b) {
        if (curIndex != b) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            Fragment curFragment = mFragmentList.get(curIndex);
            Fragment tarFragment = mFragmentList.get(b);
            transaction.hide(curFragment);
            if (tarFragment.isAdded()) {
                transaction.show(tarFragment);
            } else {
                transaction.replace(R.id.ll_accountbalance_sit, tarFragment);
            }
            transaction.commit();
            curIndex = b;
        }
    }
}
