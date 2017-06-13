package com.gangjianwang.www.gangjianwang;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import adapter.MyIntegrateAdapter;
import adapter.MyfootAdapter;
import bean.MyIntegrate;
import bean.MyIntegrateSum;
import customview.MyRefreshListView;
import customview.OnRefreshListener;

public class IntegrateActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mAnimRl;
    private RelativeLayout mBackRl;
    private ListView mLv;
    private TextView mEmptyTv;
    private ImageView mAnimIv;
    private ObjectAnimator animator;//加载动画
    private List<MyIntegrateSum> mFirstList;
    private List<MyIntegrate> mDataList;//数据
    private MyIntegrateAdapter mAdapter;//适配器
    private int pageIndex = 1;

    Handler mCancelLoadingAnimHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == 666) {
                    cancelAnim();
                    setEmptyView();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_integrate);
        initView();
        initAnim();
        initData();
        bindData();
        setListener();
        loadData(pageIndex);
    }

    private void initView() {
        mBackRl = (RelativeLayout) findViewById(R.id.rl_integrate_back);
        mLv = (ListView) findViewById(R.id.lv_integrate);
        mEmptyTv = (TextView) findViewById(R.id.tv_empty);
        mAnimRl = (RelativeLayout) findViewById(R.id.rl_integrate_loading);
        mAnimIv = (ImageView) findViewById(R.id.iv_integrate_loading);
    }

    private void initAnim() {
        animator = ObjectAnimator.ofFloat(mAnimIv, "rotation", 0.0F, 359.0F);
        animator.setDuration(500);
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.RESTART);
    }

    private void initData() {
        mFirstList = new ArrayList<>();
        mDataList = new ArrayList<>();
    }

    private void bindData() {
        mAdapter = new MyIntegrateAdapter(this, mFirstList, mDataList);
        mLv.setAdapter(mAdapter);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
    }

    private void loadData(int pageIndex) {
        startAnim();
        MyIntegrateSum myIntegrateSum = new MyIntegrateSum();
        myIntegrateSum.integrateSumName = "我的积分";
        myIntegrateSum.integrateSumScore = "17450";
        mFirstList.add(myIntegrateSum);
        MyIntegrate myIntegrate1 = new MyIntegrate();
        myIntegrate1.integrateTitle = "登录";
        myIntegrate1.integrateContent = "会员登录";
        myIntegrate1.integrateScore = "+20";
        myIntegrate1.integrateTime = "2016-4-20";
        mDataList.add(myIntegrate1);
        MyIntegrate myIntegrate2 = new MyIntegrate();
        myIntegrate2.integrateTitle = "登录";
        myIntegrate2.integrateContent = "会员登录";
        myIntegrate2.integrateScore = "+5";
        myIntegrate2.integrateTime = "2017-4-5";
        mDataList.add(myIntegrate2);
        mAdapter.notifyDataSetChanged();
        mCancelLoadingAnimHandler.sendEmptyMessageDelayed(666, 1000);
    }

    private void startAnim() {
        mLv.setVisibility(View.INVISIBLE);
        mAnimRl.setVisibility(View.VISIBLE);
        animator.start();
    }

    private void cancelAnim() {
        mAnimRl.setVisibility(View.INVISIBLE);
        mLv.setVisibility(View.VISIBLE);
        animator.cancel();
    }

    private void setEmptyView() {
        mLv.setEmptyView(mEmptyTv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_integrate_back:
                finish();
                break;
            default:
                break;
        }
    }
}
