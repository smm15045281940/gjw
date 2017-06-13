package com.gangjianwang.www.gangjianwang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mBackRl;
    private TextView emptyTv;
    private ListView mLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_message);
        initView();
        setListener();
    }

    private void initView() {
        mBackRl = (RelativeLayout) findViewById(R.id.rl_message_back);
        emptyTv = (TextView) findViewById(R.id.tv_message_empty);
        mLv = (ListView) findViewById(R.id.lv_message);
        mLv.setEmptyView(emptyTv);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_message_back:
                finish();
                break;
            default:
                break;
        }
    }
}
