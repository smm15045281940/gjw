package com.gangjianwang.www.gangjianwang;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import adapter.MyFragmentPagerAdapter;
import fragment.AccountBalanceFragment;
import fragment.AccountCashFragment;
import fragment.RechargeDetailFragment;

public class AccountBalanceActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout mBackRl;
    private TabLayout mTl;
    private ViewPager mVp;
    private String[] mTitle;
    private List<Fragment> mFragmentList;
    private AccountBalanceFragment accountBalanceFragment;
    private RechargeDetailFragment rechargeDetailFragment;
    private AccountCashFragment accountCashFragment;
    private MyFragmentPagerAdapter mFragmentPagerAdapter;

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
        mTl = (TabLayout) rootView.findViewById(R.id.tl_accountbalance);
        mVp = (ViewPager) rootView.findViewById(R.id.vp_accountbalance);
    }

    private void initData() {
        mTitle = getResources().getStringArray(R.array.accountbalance_title);
        mFragmentList = new ArrayList<>();
        accountBalanceFragment = new AccountBalanceFragment();
        rechargeDetailFragment = new RechargeDetailFragment();
        accountCashFragment = new AccountCashFragment();
        mFragmentList.add(accountBalanceFragment);
        mFragmentList.add(rechargeDetailFragment);
        mFragmentList.add(accountCashFragment);
        mFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mTitle, mFragmentList);
    }

    private void setData() {
        mVp.setAdapter(mFragmentPagerAdapter);
        mTl.setupWithViewPager(mVp);
        mTl.setTabTextColors(Color.BLACK, Color.RED);
        mTl.setSelectedTabIndicatorColor(Color.RED);
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
