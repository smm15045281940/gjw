package com.gangjianwang.www.gangjianwang;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import config.PersonConfig;
import utils.CodeUtils;
import utils.RegularUtils;
import utils.ToastUtils;

public class PhoneproveActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mBackRl;
    private ImageView mImagecodeIv;
    private EditText mPhonenumberEt, mImagecodeEt, mMsgcodeEt;
    private Button mGetmsgBtn, mNexttipBtn;
    private GradientDrawable mGetmsgBtnGd, mNexttipBtnGd;
    private int mGetmsgproveLength;
    private int mImagecodeLength;
    private int mMsgcodeLength;

    private CodeUtils codeUtils;

    Handler mGetmsgproveHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == 666) {
                    mNexttipBtn.setEnabled(true);
                    mGetmsgBtnGd.setColor(PersonConfig.loginBtnChooseColor);
                } else if (msg.what == 777) {
                    mNexttipBtn.setEnabled(false);
                    mGetmsgBtnGd.setColor(PersonConfig.loginBtnDefaultColor);
                }
            }
        }
    };

    Handler mNexttipHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == 666) {
                    mNexttipBtn.setEnabled(true);
                    mNexttipBtnGd.setColor(PersonConfig.loginBtnChooseColor);
                } else if (msg.what == 777) {
                    mNexttipBtn.setEnabled(false);
                    mNexttipBtnGd.setColor(PersonConfig.loginBtnDefaultColor);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_phoneprove);
        initView();
        setListener();
    }

    private void initView() {
        mBackRl = (RelativeLayout) findViewById(R.id.rl_phoneprove_back);
        mImagecodeIv = (ImageView) findViewById(R.id.iv_phoneprove_imagecode);
        mPhonenumberEt = (EditText) findViewById(R.id.et_phoneprove_phonenumber);
        mImagecodeEt = (EditText) findViewById(R.id.et_phoneprove_imageprovecode);
        mMsgcodeEt = (EditText) findViewById(R.id.et_phoneprove_msgprovecode);
        mGetmsgBtn = (Button) findViewById(R.id.btn_phoneprove_getmsgprove);
        mGetmsgBtn.setEnabled(false);
        mGetmsgBtnGd = (GradientDrawable) mGetmsgBtn.getBackground();
        mGetmsgBtnGd.setColor(PersonConfig.loginBtnDefaultColor);
        mNexttipBtn = (Button) findViewById(R.id.btn_phoneprove_nexttip);
        mNexttipBtn.setEnabled(false);
        mNexttipBtnGd = (GradientDrawable) mNexttipBtn.getBackground();
        mNexttipBtnGd.setColor(PersonConfig.loginBtnDefaultColor);
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        mImagecodeIv.setImageBitmap(bitmap);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mImagecodeIv.setOnClickListener(this);
        mGetmsgBtn.setOnClickListener(this);
        mNexttipBtn.setOnClickListener(this);
        mPhonenumberEt.addTextChangedListener(mGetmsgCodeTextWatcher);
        mImagecodeEt.addTextChangedListener(mImagecodeTextWatcher);
        mMsgcodeEt.addTextChangedListener(mMsgcodeTextWatcher);
    }

    private void sendHandler() {
        if (mGetmsgproveLength == 11 && mImagecodeLength == 4 && mMsgcodeLength >= 1) {
            mNexttipHandler.sendEmptyMessage(666);
        } else {
            mNexttipHandler.sendEmptyMessage(777);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_phoneprove_back:
                finish();
                break;
            case R.id.btn_phoneprove_getmsgprove:
                ToastUtils.toast(PhoneproveActivity.this, "正在获取短信验证码...");
                break;
            case R.id.iv_phoneprove_imagecode:
                Bitmap bitmap = codeUtils.createBitmap();
                mImagecodeIv.setImageBitmap(bitmap);
                break;
            case R.id.btn_phoneprove_nexttip:
                if (mImagecodeEt.getText().toString().equalsIgnoreCase(codeUtils.getCode())) {
                    ToastUtils.toast(PhoneproveActivity.this, "下一步");
                } else {
                    ToastUtils.toast(PhoneproveActivity.this, "验证码不正确");
                }
                break;
            default:
                break;
        }
    }

    TextWatcher mGetmsgCodeTextWatcher = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void afterTextChanged(Editable s) {
            mGetmsgproveLength = temp.length();
            if (mGetmsgproveLength == 11) {
                if (RegularUtils.isPhonenumber(mPhonenumberEt.getText().toString())) {
                    mGetmsgproveHandler.sendEmptyMessage(666);
                } else {
                    mGetmsgproveHandler.sendEmptyMessage(777);
                }
            } else {
                mGetmsgproveHandler.sendEmptyMessage(777);
            }
            sendHandler();
        }
    };

    TextWatcher mImagecodeTextWatcher = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void afterTextChanged(Editable s) {
            mImagecodeLength = temp.length();
            sendHandler();
        }
    };

    TextWatcher mMsgcodeTextWatcher = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void afterTextChanged(Editable s) {
            mMsgcodeLength = temp.length();
            sendHandler();
        }
    };

}
