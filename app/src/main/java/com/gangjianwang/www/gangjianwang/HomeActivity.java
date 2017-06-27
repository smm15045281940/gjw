package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import config.PersonConfig;
import fragment.CalculateFragment;
import fragment.HomeFragment;
import fragment.MineFragment;
import fragment.PurchaseFragment;
import fragment.ShopCarFragment;
import utils.UserUtils;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout mFirstpageRl, mPurchaseRl, mCalculateRl, mShopcarRl, mMeRl;
    private ImageView mFirstpageIv, mPurchaseIv, mShopcarIv, mMeIv;
    private TextView mFirstpageTv, mPurchaseTv, mShopcarTv, mMeTv;

    private Fragment mHomeFragment, mPurchaseFragment, mCalculateFragment, mShopcarFragment, mMineFragment;
    private List<Fragment> mFragmentList;
    private FragmentManager mFragmentManager;
    private int curPosition = 0;
    private long exitTime = 0;

    public Handler mChangeFragHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        changeFrag(0);
                        break;
                    case 1:
                        changeFrag(1);
                        break;
                    case 2:
                        changeFrag(2);
                        break;
                    case 3:
                        changeFrag(3);
                        break;
                    case 4:
                        changeFrag(4);
                        break;
                }
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            int a = intent.getIntExtra("what", 0);
            switch (a) {
                case 0:
                    changeFrag(0);
                    break;
                case 3:
                    changeFrag(3);
                    break;
                case 4:
                    changeFrag(4);
                    break;
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(HomeActivity.this, R.layout.activity_home, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_home_firstpage:
                changeFrag(0);
                break;
            case R.id.rl_home_purchase:
                if (UserUtils.isLogined(this)) {
                    changeFrag(1);
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
                break;
            case R.id.rl_home_calculate:
                if (UserUtils.isLogined(this)) {
                    changeFrag(2);
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
                break;
            case R.id.rl_home_shopcar:
                if (UserUtils.isLogined(this)) {
                    changeFrag(3);
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
                break;
            case R.id.rl_home_me:
                changeFrag(4);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        mFirstpageRl = (RelativeLayout) rootView.findViewById(R.id.rl_home_firstpage);
        mPurchaseRl = (RelativeLayout) rootView.findViewById(R.id.rl_home_purchase);
        mCalculateRl = (RelativeLayout) rootView.findViewById(R.id.rl_home_calculate);
        mShopcarRl = (RelativeLayout) rootView.findViewById(R.id.rl_home_shopcar);
        mMeRl = (RelativeLayout) rootView.findViewById(R.id.rl_home_me);
        mFirstpageIv = (ImageView) rootView.findViewById(R.id.iv_home_firstpage);
        mPurchaseIv = (ImageView) rootView.findViewById(R.id.iv_home_purchase);
        mShopcarIv = (ImageView) rootView.findViewById(R.id.iv_home_shopcar);
        mMeIv = (ImageView) rootView.findViewById(R.id.iv_home_me);
        mFirstpageTv = (TextView) rootView.findViewById(R.id.tv_home_firstpage);
        mPurchaseTv = (TextView) rootView.findViewById(R.id.tv_home_purchase);
        mShopcarTv = (TextView) rootView.findViewById(R.id.tv_home_shopcar);
        mMeTv = (TextView) rootView.findViewById(R.id.tv_home_me);
    }

    private void initData() {
        mFragmentManager = getSupportFragmentManager();
        mHomeFragment = new HomeFragment();
        mPurchaseFragment = new PurchaseFragment();
        mCalculateFragment = new CalculateFragment();
        mShopcarFragment = new ShopCarFragment();
        mMineFragment = new MineFragment();
        mFragmentList = new ArrayList<>();
        mFragmentList.add(mHomeFragment);
        mFragmentList.add(mPurchaseFragment);
        mFragmentList.add(mCalculateFragment);
        mFragmentList.add(mShopcarFragment);
        mFragmentList.add(mMineFragment);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.ll_home_sit, mFragmentList.get(0));
        transaction.commit();
        mFirstpageIv.setImageResource(R.mipmap.firstpage_choose);
        mFirstpageTv.setTextColor(PersonConfig.TV_HOME_CHOOSE);
    }

    private void setListener() {
        mFirstpageRl.setOnClickListener(this);
        mPurchaseRl.setOnClickListener(this);
        mCalculateRl.setOnClickListener(this);
        mShopcarRl.setOnClickListener(this);
        mMeRl.setOnClickListener(this);
    }

    private void changeFrag(int tarPosition) {
        if (curPosition != tarPosition) {
            switch (tarPosition) {
                case 0:
                    mFirstpageIv.setImageResource(R.mipmap.firstpage_choose);
                    mPurchaseIv.setImageResource(R.mipmap.purchase_default);
                    mShopcarIv.setImageResource(R.mipmap.shopcar_default);
                    mMeIv.setImageResource(R.mipmap.mine_default);
                    mFirstpageTv.setTextColor(PersonConfig.TV_HOME_CHOOSE);
                    mPurchaseTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mShopcarTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mMeTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    break;
                case 1:
                    mFirstpageIv.setImageResource(R.mipmap.firstpage_default);
                    mPurchaseIv.setImageResource(R.mipmap.purchase_choose);
                    mShopcarIv.setImageResource(R.mipmap.shopcar_default);
                    mMeIv.setImageResource(R.mipmap.mine_default);
                    mFirstpageTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mPurchaseTv.setTextColor(PersonConfig.TV_HOME_CHOOSE);
                    mShopcarTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mMeTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    break;
                case 2:
                    mFirstpageIv.setImageResource(R.mipmap.firstpage_default);
                    mPurchaseIv.setImageResource(R.mipmap.purchase_default);
                    mShopcarIv.setImageResource(R.mipmap.shopcar_default);
                    mMeIv.setImageResource(R.mipmap.mine_default);
                    mFirstpageTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mPurchaseTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mShopcarTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mMeTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    break;
                case 3:
                    mFirstpageIv.setImageResource(R.mipmap.firstpage_default);
                    mPurchaseIv.setImageResource(R.mipmap.purchase_default);
                    mShopcarIv.setImageResource(R.mipmap.shopcar_choose);
                    mMeIv.setImageResource(R.mipmap.mine_default);
                    mFirstpageTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mPurchaseTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mShopcarTv.setTextColor(PersonConfig.TV_HOME_CHOOSE);
                    mMeTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    break;
                case 4:
                    mFirstpageIv.setImageResource(R.mipmap.firstpage_default);
                    mPurchaseIv.setImageResource(R.mipmap.purchase_default);
                    mShopcarIv.setImageResource(R.mipmap.shopcar_default);
                    mMeIv.setImageResource(R.mipmap.mine_choose);
                    mFirstpageTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mPurchaseTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mShopcarTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mMeTv.setTextColor(PersonConfig.TV_HOME_CHOOSE);
                    break;
            }
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            Fragment curFragment = mFragmentList.get(curPosition);
            Fragment tarFragment = mFragmentList.get(tarPosition);
            transaction.hide(curFragment);
            if (tarFragment.isAdded()) {
                transaction.show(tarFragment);
            } else {
                transaction.add(R.id.ll_home_sit, tarFragment);
            }
            transaction.commit();
            curPosition = tarPosition;
        }
    }

    private void exit() {
        if (System.currentTimeMillis() - exitTime < 2000) {
            finish();
        } else {
            Toast.makeText(HomeActivity.this, PersonConfig.exitHint, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }
    }
}
