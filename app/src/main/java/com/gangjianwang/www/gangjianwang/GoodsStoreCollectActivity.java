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
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fragment.GoodsCollectFragment;
import fragment.ShopCollectFragment;

public class GoodsStoreCollectActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mBackRl;
    private RelativeLayout mGoodscollectRl, mShopcollectRl;
    private GradientDrawable mGoodscollectGd, mShopcollectGd;
    private TextView mGoodscollectTv, mShopcollectTv;

    private FragmentManager mFragmentManager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private GoodsCollectFragment goodsCollectFragment;
    private ShopCollectFragment shopCollectFragment;
    private int curPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_goods_store_collect);
        initView();
        initData();
        setListener();
        receiveIntent();
    }

    private void initView() {
        mBackRl = (RelativeLayout) findViewById(R.id.rl_collect_back);
        mGoodscollectRl = (RelativeLayout) findViewById(R.id.rl_goodsstorecollect_goodscollect);
        mShopcollectRl = (RelativeLayout) findViewById(R.id.rl_goodsstorecollect_shopcollect);
        mGoodscollectTv = (TextView) findViewById(R.id.tv_goodsstorecollect_goodscollect);
        mShopcollectTv = (TextView) findViewById(R.id.tv_goodstorecollect_storecollect);
        mGoodscollectGd = (GradientDrawable) mGoodscollectRl.getBackground();
        mShopcollectGd = (GradientDrawable) mShopcollectRl.getBackground();
        mGoodscollectGd.setColor(Color.RED);
        mShopcollectGd.setColor(Color.WHITE);
        mGoodscollectTv.setTextColor(Color.WHITE);
        mShopcollectTv.setTextColor(Color.BLACK);
    }

    private void initData() {
        mFragmentManager = getSupportFragmentManager();
        goodsCollectFragment = new GoodsCollectFragment();
        shopCollectFragment = new ShopCollectFragment();
        fragmentList.add(goodsCollectFragment);
        fragmentList.add(shopCollectFragment);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.ll_goodsshopcollect, fragmentList.get(curPosition));
        transaction.commit();
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mGoodscollectRl.setOnClickListener(this);
        mShopcollectRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_collect_back:
                finish();
                break;
            case R.id.rl_goodsstorecollect_goodscollect:
                changeColor(0);
                changFrag(0);
                break;
            case R.id.rl_goodsstorecollect_shopcollect:
                changeColor(1);
                changFrag(1);
                break;
        }
    }

    private void changeColor(int tarPosition) {
        switch (tarPosition) {
            case 0:
                mGoodscollectGd.setColor(Color.RED);
                mShopcollectGd.setColor(Color.WHITE);
                mGoodscollectTv.setTextColor(Color.WHITE);
                mShopcollectTv.setTextColor(Color.BLACK);
                break;
            case 1:
                mGoodscollectGd.setColor(Color.WHITE);
                mShopcollectGd.setColor(Color.RED);
                mGoodscollectTv.setTextColor(Color.BLACK);
                mShopcollectTv.setTextColor(Color.WHITE);
                break;
            default:
                break;
        }
    }

    private void changFrag(int tarPosition) {
        if (tarPosition != curPosition) {
            Fragment curFragment = fragmentList.get(curPosition);
            Fragment tarFragment = fragmentList.get(tarPosition);
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.hide(curFragment);
            if (tarFragment.isAdded()) {
                transaction.show(tarFragment);
            } else {
                transaction.add(R.id.ll_goodsshopcollect, tarFragment);
            }
            transaction.commit();
            curPosition = tarPosition;
        }
    }

    private void receiveIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getIntExtra("collect", 0) == 1) {
                changeColor(1);
                changFrag(1);
            }
        }
    }
}
