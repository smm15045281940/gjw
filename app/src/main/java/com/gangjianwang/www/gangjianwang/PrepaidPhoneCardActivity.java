package com.gangjianwang.www.gangjianwang;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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

import fragment.PrepaidCardAccountFragment;
import fragment.PrepaidCardPayCashFragment;

public class PrepaidPhoneCardActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mBackRl;
    private RelativeLayout mAccountRl, mPayCashRl;
    private GradientDrawable mAccountGd, mPayCashGd;
    private TextView mAccountTv, mPayCashTv;

    private FragmentManager mFragmentManager;
    private List<Fragment> mFragmentList;
    private int curIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_prepaid_phone_card);
        initView();
        initManager();
        setLisenter();
    }

    private void initView() {
        mBackRl = (RelativeLayout) findViewById(R.id.rl_prepaidcard_back);
        mAccountRl = (RelativeLayout) findViewById(R.id.rl_prepaidcard_account);
        mPayCashRl = (RelativeLayout) findViewById(R.id.rl_prepaidcard_paycash);
        mAccountGd = (GradientDrawable) mAccountRl.getBackground();
        mPayCashGd = (GradientDrawable) mPayCashRl.getBackground();
        mAccountTv = (TextView) findViewById(R.id.tv_prepaidcard_account);
        mPayCashTv = (TextView) findViewById(R.id.tv_prepaidcard_paycash);

        mAccountGd.setColor(Color.RED);
        mAccountTv.setTextColor(Color.WHITE);
    }

    private void initManager() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentList = new ArrayList<>();
        PrepaidCardAccountFragment prepaidCardAccountFragment = new PrepaidCardAccountFragment();
        PrepaidCardPayCashFragment prepaidCardPayCashFragment = new PrepaidCardPayCashFragment();
        mFragmentList.add(prepaidCardAccountFragment);
        mFragmentList.add(prepaidCardPayCashFragment);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.ll_prepaidcard_sit, mFragmentList.get(0));
        transaction.commit();
        curIndex = 0;
    }

    private void setLisenter() {
        mBackRl.setOnClickListener(this);
        mAccountRl.setOnClickListener(this);
        mPayCashRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_prepaidcard_back:
                finish();
                break;
            case R.id.rl_prepaidcard_account:
                select(0);
                changeFragment(0);
                break;
            case R.id.rl_prepaidcard_paycash:
                select(1);
                changeFragment(1);
                break;
        }
    }

    private void select(int a) {
        switch (a) {
            case 0:
                mAccountGd.setColor(Color.RED);
                mPayCashGd.setColor(Color.WHITE);
                mAccountTv.setTextColor(Color.WHITE);
                mPayCashTv.setTextColor(Color.BLACK);
                break;
            case 1:
                mAccountGd.setColor(Color.WHITE);
                mPayCashGd.setColor(Color.RED);
                mAccountTv.setTextColor(Color.BLACK);
                mPayCashTv.setTextColor(Color.WHITE);
                break;
            default:
                break;
        }
    }

    private void changeFragment(int b) {
        if (curIndex != b) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            Fragment curFragment = mFragmentList.get(curIndex);
            Fragment tarFragment = mFragmentList.get(b);
            transaction.hide(curFragment);
            if(tarFragment.isAdded()){
                transaction.show(tarFragment);
            }else{
                transaction.replace(R.id.ll_prepaidcard_sit,tarFragment);
            }
            transaction.commit();
            curIndex = b;
        }
    }
}
