package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fragment.RealorderFragment;
import fragment.VirtualorderFragment;

public class OrderActivity extends AppCompatActivity implements OnClickListener {

    private RelativeLayout mBackRl;
    private RelativeLayout mRealorderRl, mVirtualorderRl;
    private GradientDrawable mRealorderGd, mVirtualorderGd;
    private TextView mRealorderTv, mVirtualorderTv;
    private EditText mSearchEt;
    private ImageView mSearchIv;
    private Fragment mRealorderFragment, mVirtualorderFragment;
    private List<Fragment> mFragmentList;
    private FragmentManager mFragmentManager;
    private int curIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_myorder);
        initView();
        initManager();
        setListener();
        receiveIntent();
    }

    private void initView() {
        mBackRl = (RelativeLayout) findViewById(R.id.rl_myorder_back);
        mRealorderRl = (RelativeLayout) findViewById(R.id.rl_myorder_realorder);
        mVirtualorderRl = (RelativeLayout) findViewById(R.id.rl_myorder_virtualorder);
        mRealorderGd = (GradientDrawable) mRealorderRl.getBackground();
        mVirtualorderGd = (GradientDrawable) mVirtualorderRl.getBackground();
        mRealorderTv = (TextView) findViewById(R.id.tv_myorder_realorder);
        mVirtualorderTv = (TextView) findViewById(R.id.tv_myorder_virtualorder);
        mSearchEt = (EditText) findViewById(R.id.et_myorder_search);
        mSearchIv = (ImageView) findViewById(R.id.iv_myorder_search);
    }

    private void initManager() {
        mRealorderGd.setColor(Color.RED);
        mRealorderTv.setTextColor(Color.WHITE);
        mVirtualorderTv.setTextColor(Color.BLACK);
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mRealorderFragment = new RealorderFragment();
        mVirtualorderFragment = new VirtualorderFragment();
        mFragmentList = new ArrayList<>();
        mFragmentList.add(mRealorderFragment);
        mFragmentList.add(mVirtualorderFragment);
        transaction.add(R.id.ll_myorder_sit, mFragmentList.get(0));
        transaction.commit();
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mRealorderRl.setOnClickListener(this);
        mVirtualorderRl.setOnClickListener(this);
        mSearchIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_myorder_back:
                finish();
                break;
            case R.id.rl_myorder_realorder:
                changeFragment(0);
                break;
            case R.id.rl_myorder_virtualorder:
                changeFragment(1);
                break;
            case R.id.iv_myorder_search:
                if (!mSearchEt.getText().toString().isEmpty()) {
                    Toast.makeText(OrderActivity.this, mSearchEt.getText().toString(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void changeFragment(int tarIndex) {
        if (curIndex != tarIndex) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            Fragment curFragment = mFragmentList.get(curIndex);
            Fragment tarFragment = mFragmentList.get(tarIndex);
            transaction.hide(curFragment);
            if (tarFragment.isAdded()) {
                transaction.show(tarFragment);
            } else {
                transaction.add(R.id.ll_myorder_sit, tarFragment);
            }
            transaction.commit();
            curIndex = tarIndex;
            switch (tarIndex) {
                case 0:
                    mRealorderGd.setColor(Color.RED);
                    mVirtualorderGd.setColor(Color.WHITE);
                    mRealorderTv.setTextColor(Color.WHITE);
                    mVirtualorderTv.setTextColor(Color.BLACK);
                    break;
                case 1:
                    mRealorderGd.setColor(Color.WHITE);
                    mVirtualorderGd.setColor(Color.RED);
                    mRealorderTv.setTextColor(Color.BLACK);
                    mVirtualorderTv.setTextColor(Color.WHITE);
                    break;
                default:
                    break;
            }
        }
    }

    private void receiveIntent() {
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putInt("realorder", intent.getIntExtra("order", 0));
        mRealorderFragment.setArguments(bundle);
    }

}
