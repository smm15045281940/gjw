package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fragment.GoodsCollectFragment;
import fragment.ShopCollectFragment;

public class GoodsStoreCollectActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mBackRl;
    private RelativeLayout mGoodscollectRl, mShopcollectRl;
    private GradientDrawable mGoodscollectGd, mShopcollectGd;
    private TextView mGoodscollectTv, mShopcollectTv;

    private FragmentManager mFragmentManager;
    private GoodsCollectFragment mGoodsCollectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_goods_store_collect);
        initView();
        initManager();
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

    private void initManager() {
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mGoodsCollectFragment = new GoodsCollectFragment();
        transaction.add(R.id.ll_goodsshopcollect, mGoodsCollectFragment);
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
                mGoodscollectGd.setColor(Color.RED);
                mShopcollectGd.setColor(Color.WHITE);
                mGoodscollectTv.setTextColor(Color.WHITE);
                mShopcollectTv.setTextColor(Color.BLACK);

                FragmentTransaction goodsTransaction = mFragmentManager.beginTransaction();
                goodsTransaction.replace(R.id.ll_goodsshopcollect, new GoodsCollectFragment());
                goodsTransaction.commit();
                break;
            case R.id.rl_goodsstorecollect_shopcollect:
                mGoodscollectGd.setColor(Color.WHITE);
                mShopcollectGd.setColor(Color.RED);
                mGoodscollectTv.setTextColor(Color.BLACK);
                mShopcollectTv.setTextColor(Color.WHITE);

                FragmentTransaction shopTransaction = mFragmentManager.beginTransaction();
                shopTransaction.replace(R.id.ll_goodsshopcollect, new ShopCollectFragment());
                shopTransaction.commit();
                break;
        }
    }

    private void receiveIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getIntExtra("collect", 0) == 1) {
                mGoodscollectGd.setColor(Color.WHITE);
                mShopcollectGd.setColor(Color.RED);
                mGoodscollectTv.setTextColor(Color.BLACK);
                mShopcollectTv.setTextColor(Color.WHITE);

                FragmentTransaction shopTransaction = mFragmentManager.beginTransaction();
                shopTransaction.replace(R.id.ll_goodsshopcollect, new ShopCollectFragment());
                shopTransaction.commit();
            }
        }
    }
}
