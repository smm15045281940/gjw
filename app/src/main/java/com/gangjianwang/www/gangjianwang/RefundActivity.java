package com.gangjianwang.www.gangjianwang;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fragment.RefundFragment;
import fragment.ReturnFragment;

public class RefundActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout mBackRl, mRefundRl, mReturnRl;
    private GradientDrawable mRefundGd, mReturnGd;
    private TextView mRefundTv, mReturnTv;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_refund, null);
        setContentView(rootView);
        initView();
        initManager();
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

    private void initManager() {
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.ll_refund_sit, new RefundFragment());
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
                changeFragment(0);
                break;
            case R.id.rl_refund_return:
                changeFragment(1);
                break;
            default:
                break;
        }
    }

    private void changeFragment(int index) {
        switch (index) {
            case 0:
                mRefundGd.setColor(Color.RED);
                mReturnGd.setColor(Color.WHITE);
                mRefundTv.setTextColor(Color.WHITE);
                mReturnTv.setTextColor(Color.BLACK);
                FragmentTransaction refundTransaction = mFragmentManager.beginTransaction();
                refundTransaction.replace(R.id.ll_refund_sit, new RefundFragment());
                refundTransaction.commit();
                break;
            case 1:
                mRefundGd.setColor(Color.WHITE);
                mReturnGd.setColor(Color.RED);
                mRefundTv.setTextColor(Color.BLACK);
                mReturnTv.setTextColor(Color.WHITE);
                FragmentTransaction returnTransaction = mFragmentManager.beginTransaction();
                returnTransaction.replace(R.id.ll_refund_sit, new ReturnFragment());
                returnTransaction.commit();
                break;
            default:
                break;
        }
    }
}
