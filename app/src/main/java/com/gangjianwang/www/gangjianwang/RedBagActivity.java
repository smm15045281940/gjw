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

import fragment.GetRedbagFragment;
import fragment.MineRedbagFragment;

public class RedBagActivity extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout mBackRl;
    private RelativeLayout mMineRl,mGetRl;
    private GradientDrawable mMineGd,mGetGd;
    private TextView mMineTv,mGetTv;

    private FragmentManager mFragmentManager;
    private List<Fragment> mFragmentList;
    private int curIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_red_bag);
        initView();
        initManager();
        setListener();
    }

    private void initView(){
        mBackRl = (RelativeLayout) findViewById(R.id.rl_redbag_back);
        mMineRl = (RelativeLayout) findViewById(R.id.rl_redbag_mine);
        mGetRl = (RelativeLayout) findViewById(R.id.rl_redbag_get);
        mMineGd = (GradientDrawable) mMineRl.getBackground();
        mGetGd = (GradientDrawable) mGetRl.getBackground();
        mMineTv = (TextView) findViewById(R.id.tv_redbag_mine);
        mGetTv = (TextView) findViewById(R.id.tv_redbag_get);
        mMineGd.setColor(Color.RED);
        mMineTv.setTextColor(Color.WHITE);
    }

    private void initManager(){
        mFragmentManager = getSupportFragmentManager();
        mFragmentList = new ArrayList<>();
        MineRedbagFragment mineRedbagFragment = new MineRedbagFragment();
        GetRedbagFragment getRedbagFragment = new GetRedbagFragment();
        mFragmentList.add(mineRedbagFragment);
        mFragmentList.add(getRedbagFragment);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.ll_redbag_sit,mFragmentList.get(0));
        transaction.commit();
        curIndex = 0;
    }

    private void setListener(){
        mBackRl.setOnClickListener(this);
        mMineRl.setOnClickListener(this);
        mGetRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_redbag_back:
                finish();
                break;
            case R.id.rl_redbag_mine:
                select(0);
                changeFragment(0);
                break;
            case R.id.rl_redbag_get:
                select(1);
                changeFragment(1);
                break;
            default:
                break;
        }
    }

    private void select(int a){
        switch (a){
            case 0:
                mMineGd.setColor(Color.RED);
                mGetGd.setColor(Color.WHITE);
                mMineTv.setTextColor(Color.WHITE);
                mGetTv.setTextColor(Color.BLACK);
                break;
            case 1:
                mMineGd.setColor(Color.WHITE);
                mGetGd.setColor(Color.RED);
                mMineTv.setTextColor(Color.BLACK);
                mGetTv.setTextColor(Color.WHITE);
                break;
            default:
                break;
        }
    }

    private void changeFragment(int b){
        if(curIndex!=b){
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            Fragment curFragment = mFragmentList.get(curIndex);
            Fragment tarFragment = mFragmentList.get(b);
            transaction.hide(curFragment);
            if(tarFragment.isAdded()){
                transaction.show(tarFragment);
            }else{
                transaction.replace(R.id.ll_redbag_sit,tarFragment);
            }
            transaction.commit();
            curIndex = b;
        }
    }
}
