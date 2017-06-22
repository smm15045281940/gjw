package com.gangjianwang.www.gangjianwang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import config.PersonConfig;


public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mWelcomeIv, mAdvertisementIv;
    private Button mJumpBtn;
    private int mWelcomeTime;
    private int mCountTime;
    private int WELCOME_STATE = 0;
    private final int FIRST_IN = 1;
    private final int NONET = 2;
    private final int NET_ADVERTISEMENT = 3;
    private final int NET_NOADVERTISEMENT = 4;
    private final int HANDLER_ONESEC = 1;
    private final int HANDLER_THREESEC = 2;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case HANDLER_ONESEC:
                        jumpToHomeActivity();
                        break;
                    case HANDLER_THREESEC:
                        mWelcomeIv.setVisibility(View.INVISIBLE);
                        mAdvertisementIv.setVisibility(View.VISIBLE);
                        mJumpBtn.setVisibility(View.VISIBLE);
                        if (mCountTime == 0) {
                            jumpToHomeActivity();
                        } else {
                            mJumpBtn.setText(mCountTime + "秒跳转");
                            mCountTime--;
                            this.sendEmptyMessageDelayed(HANDLER_THREESEC, 1000);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        initView();
        initData();
        jumpByWelcomeState();
        setListener();
    }

    private void initView() {
        mWelcomeIv = (ImageView) findViewById(R.id.iv_welcome);
        mAdvertisementIv = (ImageView) findViewById(R.id.iv_advertisement);
        mJumpBtn = (Button) findViewById(R.id.btn_jump);
        mJumpBtn.setText(mCountTime + "秒跳转");
    }

    private void initData() {
        mWelcomeTime = PersonConfig.welcome_time * 1000;
        mCountTime = PersonConfig.poster_time;
    }

    private void jumpByWelcomeState() {
        if (isFirstIn()) {
            WELCOME_STATE = FIRST_IN;
        } else if (!haveNet()) {
            WELCOME_STATE = NONET;
        } else if (haveAdvertisement()) {
            WELCOME_STATE = NET_ADVERTISEMENT;
        } else {
            WELCOME_STATE = NET_NOADVERTISEMENT;
        }
        switch (WELCOME_STATE) {
            case FIRST_IN:
                jumpToGuideActivity();
                break;
            case NONET:
                jumpToHomeActivityDelayOneSec();
                break;
            case NET_ADVERTISEMENT:
                if (advertisementLoaded()) {
                    jumpToHomeActivityDelayThreeSec();
                }
                break;
            case NET_NOADVERTISEMENT:
                jumpToHomeActivityDelayOneSec();
                break;
            default:
                break;
        }
    }

    private void setListener() {
        mAdvertisementIv.setOnClickListener(this);
        mJumpBtn.setOnClickListener(this);
    }

    private void jumpToHomeActivityDelayOneSec() {
        handler.sendEmptyMessageDelayed(HANDLER_ONESEC, mWelcomeTime);
    }

    private void jumpToHomeActivityDelayThreeSec() {
        handler.sendEmptyMessageDelayed(HANDLER_THREESEC, mWelcomeTime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_advertisement:
                jumpToAdvertisementActivity();
                break;
            case R.id.btn_jump:
                jumpToHomeActivity();
                break;
            default:
                break;
        }
    }

    private void jumpToGuideActivity() {
        startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
        finish();
    }

    private void jumpToAdvertisementActivity() {
        startActivity(new Intent(WelcomeActivity.this, AdvertisementActivity.class));
        finish();
    }

    private void jumpToHomeActivity() {
        startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
        finish();
    }

    private boolean haveNet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean haveAdvertisement() {
        return true;
    }

    private boolean advertisementLoaded() {
        return true;
    }

    private boolean isFirstIn() {
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        String s = sp.getString("isFirstIn", "default");
        if (s.equals("no")) {
            return false;
        } else {
            SharedPreferences.Editor et = sp.edit();
            et.putString("isFirstIn", "no");
            et.commit();
            return true;
        }
    }
}
