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

    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private int curPostion, tarPostion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_prepaid_phone_card);
        initView();
        initData();
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

    private void initData() {
        fragmentManager = getSupportFragmentManager();
        PrepaidCardAccountFragment prepaidCardAccountFragment = new PrepaidCardAccountFragment();
        PrepaidCardPayCashFragment prepaidCardPayCashFragment = new PrepaidCardPayCashFragment();
        fragmentList.add(prepaidCardAccountFragment);
        fragmentList.add(prepaidCardPayCashFragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.ll_prepaidcard_sit, fragmentList.get(curPostion));
        transaction.commit();
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
                tarPostion = 0;
                changeFragment();
                break;
            case R.id.rl_prepaidcard_paycash:
                tarPostion = 1;
                changeFragment();
                break;
        }
    }

    private void changeFragment() {
        if (curPostion != tarPostion) {
            switch (tarPostion) {
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
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment curFragment = fragmentList.get(curPostion);
            Fragment tarFragment = fragmentList.get(tarPostion);
            transaction.hide(curFragment);
            if (tarFragment.isAdded()) {
                transaction.show(tarFragment);
            } else {
                transaction.add(R.id.ll_prepaidcard_sit, tarFragment);
            }
            transaction.commit();
            curPostion = tarPostion;
        }
    }
}
