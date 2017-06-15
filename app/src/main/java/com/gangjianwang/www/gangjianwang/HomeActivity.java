package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import config.PersonConfig;
import fragment.CalculateFragment;
import fragment.HomeFragment;
import fragment.MineFragment;
import fragment.PurchaseFragment;
import fragment.ShopCarFragment;

public class HomeActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup homeRg;
    private RadioButton firstRb, purchaseRb, calculateRb, shopcarRb, meRb;
    private Fragment mHomeFragment, mPurchaseFragment, mCalculateFragment, mShopcarFragment, mMineFragment;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private FragmentManager mFragmentManager;
    private int curIndex, tarIndex;
    private long exitTime;

    public Handler mChangeFragHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        tarIndex = 0;
                        break;
                    case 1:
                        tarIndex = 1;
                        break;
                    case 2:
                        tarIndex = 2;
                        break;
                    case 3:
                        tarIndex = 3;
                        break;
                    case 4:
                        tarIndex = 4;
                        break;
                    default:
                        break;
                }
                ((RadioButton) homeRg.getChildAt(tarIndex)).setChecked(true);
                changeFragment();
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int a = intent.getIntExtra("what", 0);
        switch (a) {
            case 0:
                tarIndex = 0;
                break;
            case 3:
                tarIndex = 3;
                break;
        }
        ((RadioButton) homeRg.getChildAt(tarIndex)).setChecked(true);
        changeFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        homeRg = (RadioGroup) findViewById(R.id.rg_home);
        firstRb = (RadioButton) findViewById(R.id.rb_home_firstpage);
        purchaseRb = (RadioButton) findViewById(R.id.rb_home_purchase);
        calculateRb = (RadioButton) findViewById(R.id.rb_home_calculate);
        shopcarRb = (RadioButton) findViewById(R.id.rb_home_shopcar);
        meRb = (RadioButton) findViewById(R.id.rb_home_me);
        setRbSize(70, 70);
    }

    private void setRbSize(int height, int width) {
        Drawable firstDrawable = getResources().getDrawable(R.drawable.rb_first_selector);
        Drawable purchaseDrawable = getResources().getDrawable(R.drawable.rb_purchase_selector);
        Drawable calculateDrawable = getResources().getDrawable(R.drawable.rb_calculate_selector);
        Drawable shopcarDrawable = getResources().getDrawable(R.drawable.rb_shopcar_selector);
        Drawable meDrawable = getResources().getDrawable(R.drawable.rb_me_selector);
        firstDrawable.setBounds(0, 0, height, width);
        purchaseDrawable.setBounds(0, 0, height, width);
        calculateDrawable.setBounds(0, 0, height, width);
        shopcarDrawable.setBounds(0, 0, height, width);
        meDrawable.setBounds(0, 0, height, width);
        firstRb.setCompoundDrawables(null, firstDrawable, null, null);
        purchaseRb.setCompoundDrawables(null, purchaseDrawable, null, null);
        calculateRb.setCompoundDrawables(null, calculateDrawable, null, null);
        shopcarRb.setCompoundDrawables(null, shopcarDrawable, null, null);
        meRb.setCompoundDrawables(null, meDrawable, null, null);
    }

    private void initData() {
        mFragmentManager = getSupportFragmentManager();
        mHomeFragment = new HomeFragment();
        mPurchaseFragment = new PurchaseFragment();
        mCalculateFragment = new CalculateFragment();
        mShopcarFragment = new ShopCarFragment();
        mMineFragment = new MineFragment();
        mFragmentList.add(mHomeFragment);
        mFragmentList.add(mPurchaseFragment);
        mFragmentList.add(mCalculateFragment);
        mFragmentList.add(mShopcarFragment);
        mFragmentList.add(mMineFragment);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.ll_home_sit, mFragmentList.get(0));
        transaction.commit();
        curIndex = 0;
        ((RadioButton) homeRg.getChildAt(0)).setChecked(true);
    }

    private void setListener() {
        homeRg.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (System.currentTimeMillis() - exitTime < 2000) {
            finish();
        } else {
            Toast.makeText(HomeActivity.this, PersonConfig.exitHint, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.rg_home:
                switch (checkedId) {
                    case R.id.rb_home_firstpage:
                        tarIndex = 0;
                        break;
                    case R.id.rb_home_purchase:
                        tarIndex = 1;
                        break;
                    case R.id.rb_home_calculate:
                        tarIndex = 2;
                        break;
                    case R.id.rb_home_shopcar:
                        tarIndex = 3;
                        break;
                    case R.id.rb_home_me:
                        tarIndex = 4;
                        break;
                    default:
                        break;
                }
                changeFragment();
                break;
            default:
                break;
        }
    }

    private void changeFragment() {
        if (curIndex != tarIndex) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            Fragment curFragment = mFragmentList.get(curIndex);
            Fragment tarFragment = mFragmentList.get(tarIndex);
            transaction.hide(curFragment);
            if (tarFragment.isAdded()) {
                transaction.show(tarFragment);
            } else {
                transaction.add(R.id.ll_home_sit, tarFragment);
            }
            transaction.commit();
            curIndex = tarIndex;
        }
    }
}
