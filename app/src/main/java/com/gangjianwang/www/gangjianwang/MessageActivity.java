package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import config.ParaConfig;
import utils.ToastUtils;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView, emptyView;
    private RelativeLayout mBackRl;
    private ListView lv;
    private ProgressDialog progressDialog;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case ParaConfig.DEFEAT:
                        progressDialog.dismiss();
                        emptyView.setVisibility(View.VISIBLE);
                        ToastUtils.toast(MessageActivity.this, ParaConfig.NETWORK_ERROR);
                        break;
                    case ParaConfig.SUCCESS:
                        progressDialog.dismiss();
                        emptyView.setVisibility(View.VISIBLE);
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
        rootView = View.inflate(this, R.layout.activity_message, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(ParaConfig.DEFEAT);
        handler.removeMessages(ParaConfig.SUCCESS);
    }

    private void initView() {
        initRoot();
        initEmpty();
    }

    private void initRoot() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_message_back);
        lv = (ListView) rootView.findViewById(R.id.lv_message);
    }

    private void initEmpty() {
        emptyView = View.inflate(this, R.layout.empty, null);
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ImageView) emptyView.findViewById(R.id.iv_empty_icon)).setImageResource(ParaConfig.MESSAGE_ICON);
        ((TextView) emptyView.findViewById(R.id.tv_empty_hint)).setText(ParaConfig.MESSAGE_HINT);
        ((TextView) emptyView.findViewById(R.id.tv_empty_content)).setText(ParaConfig.MESSAGE_CONTENT);
        ((ViewGroup) lv.getParent()).addView(emptyView);
        lv.setEmptyView(emptyView);
    }

    private void initData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
    }

    private void loadData() {
        emptyView.setVisibility(View.INVISIBLE);
        progressDialog.show();
        handler.sendEmptyMessageDelayed(ParaConfig.SUCCESS, 500);
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
