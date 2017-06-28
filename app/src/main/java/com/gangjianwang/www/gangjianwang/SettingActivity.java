package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import utils.UserUtils;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView, dialogView;
    private RelativeLayout mBackRl, mYesRl, mNoRl;
    private RelativeLayout mLoginpasswordRl, mPhoneproveRl, mPaypwdRl, mUserfeedback, mQuitRl;
    private AlertDialog quitAd;

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
        dialogView = View.inflate(this, R.layout.dialog_quit, null);
        mYesRl = (RelativeLayout) dialogView.findViewById(R.id.rl_dialog_quit_yes);
        mNoRl = (RelativeLayout) dialogView.findViewById(R.id.rl_dialog_quit_no);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        quitAd = builder.create();
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mLoginpasswordRl.setOnClickListener(this);
        mPhoneproveRl.setOnClickListener(this);
        mPaypwdRl.setOnClickListener(this);
        mUserfeedback.setOnClickListener(this);
        mQuitRl.setOnClickListener(this);
        mYesRl.setOnClickListener(this);
        mNoRl.setOnClickListener(this);
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
                quitAd.show();
                break;
            case R.id.rl_dialog_quit_yes:
                quitAd.dismiss();
                UserUtils.clearLogin(this);
                Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
                intent.putExtra("what", 4);
                startActivity(intent);
                break;
            case R.id.rl_dialog_quit_no:
                quitAd.dismiss();
                break;
        }
    }
}
