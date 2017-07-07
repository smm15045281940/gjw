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

import fragment.RefundFragment;
import fragment.ReturnFragment;

public class RefundActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout mBackRl, mRefundRl, mReturnRl;
    private GradientDrawable mRefundGd, mReturnGd;
    private TextView mRefundTv, mReturnTv;
    private FragmentManager mFragmentManager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private int curPosition, tarPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_refund, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_refund_back);
        mRefundRl = (RelativeLayout) rootView.findViewById(R.id.rl_refund_refund);
        mReturnRl = (RelativeLayout) rootView.findViewById(R.id.rl_refund_return);
        mRefundGd = (GradientDrawable) mRefundRl.getBackground();
        mReturnGd = (GradientDrawable) mReturnRl.getBackground();
        mRefundTv = (TextView) rootView.findViewById(R.id.tv_refund_refund);
        mReturnTv = (TextView) rootView.findViewById(R.id.tv_refund_return);
        mRefundGd.setColor(Color.RED);
        mReturnGd.setColor(Color.WHITE);
        mRefundTv.setTextColor(Color.WHITE);
        mReturnTv.setTextColor(Color.BLACK);
    }

    private void initData() {
        mFragmentManager = getSupportFragmentManager();
        RefundFragment refundFragment = new RefundFragment();
        ReturnFragment returnFragment = new ReturnFragment();
        fragmentList.add(refundFragment);
        fragmentList.add(returnFragment);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.ll_refund_sit, fragmentList.get(0));
        transaction.commit();
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mRefundRl.setOnClickListener(this);
        mReturnRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_refund_back:
                finish();
                break;
            case R.id.rl_refund_refund:
                tarPosition = 0;
                changeFragment();
                break;
            case R.id.rl_refund_return:
                tarPosition = 1;
                changeFragment();
                break;
            default:
                break;
        }
    }

    private void changeFragment() {
        if (tarPosition != curPosition) {
            switch (tarPosition) {
                case 0:
                    mRefundGd.setColor(Color.RED);
                    mReturnGd.setColor(Color.WHITE);
                    mRefundTv.setTextColor(Color.WHITE);
                    mReturnTv.setTextColor(Color.BLACK);
                    break;
                case 1:
                    mRefundGd.setColor(Color.WHITE);
                    mReturnGd.setColor(Color.RED);
                    mRefundTv.setTextColor(Color.BLACK);
                    mReturnTv.setTextColor(Color.WHITE);
                    break;
                default:
                    break;
            }
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.ll_refund_sit, fragmentList.get(tarPosition));
            transaction.commit();
            curPosition = tarPosition;
        }
    }
}
