package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fragment.KindFirstFragment;
import fragment.KindSecondFragment;
import fragment.KindThirdFragment;

public class KindActivity extends AppCompatActivity implements View.OnClickListener {

    //视图
    private ImageView mBackIv;
    private TextView mFirstTv, mSecondTv, mThirdTv;
    private View mFirstV, mSecondV, mThirdV;
    //碎片
    private Fragment mKindFirstFragment, mKindSecondFragment, mKindThirdFragment;
    //碎片集合
    private List<Fragment> mFragmentList;
    //碎片管理器
    private FragmentManager mFragmentManager;
    //碎片索引
    private int curIndex = 0;
    private String mFirstStr, mSecondStr, mThirdStr;
    //按钮状态线程
    public Handler mbuttonStatehandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                Bundle bundle = msg.getData();
                //点了一级分类碎片
                if (msg.what == 0) {
                    mFirstStr = bundle.getString("firstStr");
                    mFirstTv.setText(mFirstStr);
                    changeFragment(1);
                }
                //点了二级分类碎片
                if (msg.what == 1) {
                    mSecondStr = bundle.getString("secondStr");
                    mSecondTv.setText(mSecondStr);
                    changeFragment(2);
                }
                //点了三级分类碎片
                if(msg.what == 2){
                    mThirdStr = bundle.getString("thirdStr");
                    Intent intent = new Intent();
                    intent.putExtra("kind",mFirstStr+" "+mSecondStr+" "+mThirdStr);
                    setResult(0,intent);
                    finish();
                }
            }
        }
    };

    //创建
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_kind);
        initView();
        initManager();
        setListener();
    }

    //初始化视图
    private void initView() {
        mBackIv = (ImageView) findViewById(R.id.iv_back);
        mFirstTv = (TextView) findViewById(R.id.tv_kind_first);
        mSecondTv = (TextView) findViewById(R.id.tv_kind_second);
        mThirdTv = (TextView) findViewById(R.id.tv_kind_third);
        mFirstV = findViewById(R.id.v_kind_first);
        mSecondV = findViewById(R.id.v_kind_second);
        mThirdV = findViewById(R.id.v_kind_third);
    }

    //初始化碎片管理器
    private void initManager() {
        mFragmentManager = getSupportFragmentManager();
        mKindFirstFragment = new KindFirstFragment();
        mKindSecondFragment = new KindSecondFragment();
        mKindThirdFragment = new KindThirdFragment();
        mFragmentList = new ArrayList<>();
        mFragmentList.add(mKindFirstFragment);
        mFragmentList.add(mKindSecondFragment);
        mFragmentList.add(mKindThirdFragment);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.ll_kind_sit, mFragmentList.get(0));
        transaction.commit();
    }

    //设置监听
    private void setListener() {
        mBackIv.setOnClickListener(this);
        mFirstTv.setOnClickListener(this);
        mSecondTv.setOnClickListener(this);
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_kind_first:
                if(curIndex != 0){
                    mFirstTv.setText("第一系列");
                    mSecondTv.setText("第二系列");
                }
                changeFragment(0);
                break;
            case R.id.tv_kind_second:
                if(curIndex == 2){
                    mSecondTv.setText("第二系列");
                }
                changeFragment(1);
                break;
            default:
                break;
        }
    }

    //切换碎片
    private void changeFragment(int tarIndex) {

        if (curIndex != tarIndex) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            Fragment curFragment = mFragmentList.get(curIndex);
            Fragment tarFragment = mFragmentList.get(tarIndex);
            transaction.hide(curFragment);
            if (tarFragment.isAdded()) {
                transaction.show(tarFragment);
            } else {
                transaction.add(R.id.ll_kind_sit, tarFragment);
            }
            transaction.commit();
            curIndex = tarIndex;
            switch (tarIndex) {
                case 0:
                    mFirstTv.setTextColor(Color.RED);
                    mSecondTv.setTextColor(Color.DKGRAY);
                    mThirdTv.setTextColor(Color.DKGRAY);
                    mFirstV.setVisibility(View.VISIBLE);
                    mSecondV.setVisibility(View.INVISIBLE);
                    mThirdV.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    mFirstTv.setTextColor(Color.DKGRAY);
                    mSecondTv.setTextColor(Color.RED);
                    mThirdTv.setTextColor(Color.DKGRAY);
                    mFirstV.setVisibility(View.INVISIBLE);
                    mSecondV.setVisibility(View.VISIBLE);
                    mThirdV.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    mFirstTv.setTextColor(Color.DKGRAY);
                    mSecondTv.setTextColor(Color.DKGRAY);
                    mThirdTv.setTextColor(Color.RED);
                    mFirstV.setVisibility(View.INVISIBLE);
                    mSecondV.setVisibility(View.INVISIBLE);
                    mThirdV.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    private void toast(String s) {
        Toast.makeText(KindActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
