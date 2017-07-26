package com.gangjianwang.www.gangjianwang;

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

import fragment.GetVoucherFragment;
import fragment.MineVoucherFragment;

public class VoucherActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout mBackRl, mMineRl, mGetRl;
    private GradientDrawable mMineGd, mGetGd;
    private TextView mMineTv, mGetTv;
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private int curPosition, tarPostion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_voucher, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        initRoot();
    }

    private void initRoot() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_voucher_back);
        mMineRl = (RelativeLayout) rootView.findViewById(R.id.rl_voucher_mine);
        mGetRl = (RelativeLayout) rootView.findViewById(R.id.rl_voucher_get);
        mMineGd = (GradientDrawable) mMineRl.getBackground();
        mGetGd = (GradientDrawable) mGetRl.getBackground();
        mMineTv = (TextView) rootView.findViewById(R.id.tv_voucher_mine);
        mGetTv = (TextView) rootView.findViewById(R.id.tv_voucher_get);
        mMineGd.setColor(Color.RED);
        mMineTv.setTextColor(Color.WHITE);
    }

    private void initData() {
        fragmentManager = getSupportFragmentManager();
        MineVoucherFragment mineVoucherFragment = new MineVoucherFragment();
        GetVoucherFragment getVoucherFragment = new GetVoucherFragment();
        fragmentList.add(mineVoucherFragment);
        fragmentList.add(getVoucherFragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.ll_voucher_sit, fragmentList.get(curPosition));
        transaction.commit();
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mMineRl.setOnClickListener(this);
        mGetRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_voucher_back:
                finish();
                break;
            case R.id.rl_voucher_mine:
                tarPostion = 0;
                changeFragment();
                break;
            case R.id.rl_voucher_get:
                tarPostion = 1;
                changeFragment();
                break;
            default:
                break;
        }
    }

    private void changeFragment() {
        if (tarPostion != curPosition) {
            switch (tarPostion) {
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
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment curFragment = fragmentList.get(curPosition);
            Fragment tarFragment = fragmentList.get(tarPostion);
            transaction.hide(curFragment);
            if (tarFragment.isAdded()) {
                transaction.show(tarFragment);
            } else {
                transaction.add(R.id.ll_voucher_sit, tarFragment);
            }
            transaction.commit();
            curPosition = tarPostion;
        }
    }
}
