package com.gangjianwang.www.gangjianwang;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fragment.ForgetpasswordFragment;
import fragment.LoginFragment;
import fragment.RegisterFragment;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTitleTv, mLogregTv;
    private RelativeLayout mBackRl, mLogregRl;
    private FragmentManager mFragmentManager;
    private Fragment mLoginFragment, mRegisterFragment, mForgetpwdFragment;
    private List<Fragment> mFragmentList;

    private final int LOGIN = 0;
    private final int REGESTER = 1;
    private final int FORGETPWD = 2;
    private int NOWSTATE = LOGIN;
    private int TODOSTATE;

    public Handler forgetpwdHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == 100) {
                    TODOSTATE = FORGETPWD;
                    changeFragment();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initView();
        initManager();
        setListener();
    }

    private void initView() {
        mTitleTv = (TextView) findViewById(R.id.tv_login_title);
        mLogregTv = (TextView) findViewById(R.id.tv_login_logreg);
        mBackRl = (RelativeLayout) findViewById(R.id.rl_login_back);
        mLogregRl = (RelativeLayout) findViewById(R.id.rl_login_logreg);
    }

    private void initManager() {
        mFragmentManager = getSupportFragmentManager();
        mLoginFragment = new LoginFragment();
        mRegisterFragment = new RegisterFragment();
        mForgetpwdFragment = new ForgetpasswordFragment();
        mFragmentList = new ArrayList<>();
        mFragmentList.add(mLoginFragment);
        mFragmentList.add(mRegisterFragment);
        mFragmentList.add(mForgetpwdFragment);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.ll_login_sit, mFragmentList.get(0));
        transaction.commit();
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mLogregRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_login_back:
                finish();
                break;
            case R.id.rl_login_logreg:
                if (NOWSTATE == LOGIN) {
                    TODOSTATE = REGESTER;
                    changeFragment();
                } else if (NOWSTATE == REGESTER) {
                    TODOSTATE = LOGIN;
                    changeFragment();
                } else if (NOWSTATE == FORGETPWD) {
                    TODOSTATE = LOGIN;
                    changeFragment();
                }
                break;
            default:
                break;
        }
    }

    private void changeFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        Fragment curFragment = mFragmentList.get(NOWSTATE);
        Fragment tarFragment = mFragmentList.get(TODOSTATE);
        transaction.hide(curFragment);
        if (tarFragment.isAdded()) {
            transaction.show(tarFragment);
        } else {
            transaction.add(R.id.ll_login_sit, tarFragment);
        }
        transaction.commit();
        NOWSTATE = TODOSTATE;
        switch (TODOSTATE) {
            case LOGIN:
                mTitleTv.setText("登录");
                mLogregTv.setText("注册");
                break;
            case REGESTER:
                mTitleTv.setText("会员注册");
                mLogregTv.setText("登录");
                break;
            case FORGETPWD:
                mTitleTv.setText("找回密码");
                mLogregTv.setText("登录");
                break;
        }
    }
}
