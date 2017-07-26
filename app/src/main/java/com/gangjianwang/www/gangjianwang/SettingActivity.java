package com.gangjianwang.www.gangjianwang;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import config.ParaConfig;
import utils.UserUtils;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout mBackRl;
    private RelativeLayout mLoginpasswordRl, mPhoneproveRl, mPaypwdRl, mUserfeedback, mQuitRl;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_setting, null);
        setContentView(rootView);
        initView();
        setListener();
    }

    private void initView() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_setting_back);
        mLoginpasswordRl = (RelativeLayout) rootView.findViewById(R.id.rl_setting_loginpassword);
        mPhoneproveRl = (RelativeLayout) rootView.findViewById(R.id.rl_setting_phoneprove);
        mPaypwdRl = (RelativeLayout) rootView.findViewById(R.id.rl_setting_paypassword);
        mUserfeedback = (RelativeLayout) rootView.findViewById(R.id.rl_setting_userfeedback);
        mQuitRl = (RelativeLayout) rootView.findViewById(R.id.rl_setting_safequit);
        initDialog();
    }

    private void initDialog() {
        alertDialog = new AlertDialog.Builder(this).setMessage(ParaConfig.QUIT_MESSAGE).setNegativeButton(ParaConfig.DELETE_YES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                quit();
            }
        }).setPositiveButton(ParaConfig.DELETE_NO, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        }).create();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mLoginpasswordRl.setOnClickListener(this);
        mPhoneproveRl.setOnClickListener(this);
        mPaypwdRl.setOnClickListener(this);
        mUserfeedback.setOnClickListener(this);
        mQuitRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_setting_back:
                finish();
                break;
            case R.id.rl_setting_loginpassword:
                startActivity(new Intent(SettingActivity.this, PhoneproveActivity.class));
                break;
            case R.id.rl_setting_phoneprove:
                startActivity(new Intent(SettingActivity.this, PhoneproveActivity.class));
                break;
            case R.id.rl_setting_paypassword:
                startActivity(new Intent(SettingActivity.this, PhoneproveActivity.class));
                break;
            case R.id.rl_setting_userfeedback:
                startActivity(new Intent(SettingActivity.this, FeedbackActivity.class));
                break;
            case R.id.rl_setting_safequit:
                alertDialog.show();
                break;
        }
    }

    private void quit() {
        UserUtils.clearLogin(this);
        Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
        intent.putExtra("what", 4);
        startActivity(intent);
    }
}
