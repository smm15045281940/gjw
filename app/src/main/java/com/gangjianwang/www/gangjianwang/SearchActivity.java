package com.gangjianwang.www.gangjianwang;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import customview.FlowLayout;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mBackRl, mToSearchRl;
    private ImageView mSearchIv;
    private TextView mSearchDefaultTv;
    private EditText mSearchEt;
    private ImageView mContentcloseIv;
    private int mSearchLength;

    private FlowLayout mFl;
    private String[] flowStr = {"我IE电放费带你飞", "到我诶积分地方地方", "欧弟的风景地方", "ID佛山街of地", "哦为对方答复", "的佛的所肩负的", "都费大幅度", "a", "d", "d", "dfe", "dfdf", "dfdf"};

    Handler mSearchHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == 666) {
                    mSearchIv.setVisibility(View.INVISIBLE);
                    mSearchDefaultTv.setVisibility(View.INVISIBLE);
                    mContentcloseIv.setVisibility(View.VISIBLE);
                } else if (msg.what == 777) {
                    mSearchIv.setVisibility(View.VISIBLE);
                    mSearchDefaultTv.setVisibility(View.VISIBLE);
                    mContentcloseIv.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        initView();
        initFlow();
        setListener();
    }

    private void initView() {
        mBackRl = (RelativeLayout) findViewById(R.id.rl_search_back);
        mToSearchRl = (RelativeLayout) findViewById(R.id.rl_search_toSearch);
        mSearchIv = (ImageView) findViewById(R.id.iv_search_search);
        mSearchDefaultTv = (TextView) findViewById(R.id.tv_search_default);
        mContentcloseIv = (ImageView) findViewById(R.id.iv_search_contentclose);
        mContentcloseIv.setVisibility(View.INVISIBLE);
        mSearchEt = (EditText) findViewById(R.id.et_search_search);
        mFl = (FlowLayout) findViewById(R.id.fl);
    }

    private void initFlow() {
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < flowStr.length; i++) {
            final TextView tv = new TextView(SearchActivity.this);
            tv.setPadding(10, 10, 10, 10);
            tv.setText(flowStr[i]);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SearchActivity.this, tv.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
            mFl.addView(tv, lp);
        }
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mToSearchRl.setOnClickListener(this);
        mSearchEt.addTextChangedListener(mSearchEtTextWatcher);
        mSearchEt.setOnClickListener(this);
        mContentcloseIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_search_back:
                finish();
                break;
            case R.id.et_search_search:
                mSearchEt.setCursorVisible(true);
                break;
            case R.id.iv_search_contentclose:
                mSearchEt.setText("");
                mSearchEt.setCursorVisible(false);
                break;
            case R.id.rl_search_toSearch:
                Toast.makeText(SearchActivity.this, "search", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void sendHandler() {
        if (mSearchLength != 0) {
            mSearchHandler.sendEmptyMessage(666);
        } else {
            mSearchHandler.sendEmptyMessage(777);
        }
    }

    TextWatcher mSearchEtTextWatcher = new TextWatcher() {

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
            mSearchLength = temp.length();
            sendHandler();
        }
    };
}
