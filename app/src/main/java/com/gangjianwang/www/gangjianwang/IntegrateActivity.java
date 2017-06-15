package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import config.NetConfig;
import customview.MyRefreshListView;
import customview.OnRefreshListener;
import utils.ToastUtils;

public class IntegrateActivity extends AppCompatActivity implements View.OnClickListener, OnRefreshListener {

    private RelativeLayout mBackRl;
    private MyRefreshListView mLv;
    private final int LOAD_FIRST = 1;
    private final int LOAD_REFRESH = 2;
    private final int LOAD_LOAD = 3;
    private ProgressDialog mPd;
    private Handler handler;
    private OkHttpClient okHttpClient;

    private List<String> testList = new ArrayList<>();
    private ArrayAdapter<String> testAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_integrate);
        initView();
        initData();
        setData();
        setListener();
        loadData(LOAD_FIRST);
    }

    private void initView() {
        mBackRl = (RelativeLayout) findViewById(R.id.rl_integrate_back);
        mLv = (MyRefreshListView) findViewById(R.id.lv_integrate);
    }

    private void initData() {
        mPd = new ProgressDialog(this);
        mPd.setMessage("加载中..");
        handler = new Handler();
        okHttpClient = new OkHttpClient();
        testAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testList);
    }

    private void setData() {
        mLv.setAdapter(testAdapter);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mLv.setOnRefreshListener(this);
    }

    private void loadData(int LOAD_STATE) {
        switch (LOAD_STATE) {
            case LOAD_FIRST:
                mPd.show();
                Request requestFirst = new Request.Builder().url(NetConfig.cityUrl).get().build();
                okHttpClient.newCall(requestFirst).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        mPd.dismiss();
                        ToastUtils.toast(IntegrateActivity.this, "无网络");
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    for (int i = 0; i < 20; i++) {
                                        testList.add("会员积分-首次加载-" + i);
                                    }
                                    handler.post(runnableFirst);
                                }
                            }.start();
                        } else {
                            mPd.dismiss();
                            ToastUtils.toast(IntegrateActivity.this, "出小差了");
                        }
                    }
                });
                break;
            case LOAD_REFRESH:
                Request requestRefresh = new Request.Builder().url(NetConfig.cityUrl).get().build();
                okHttpClient.newCall(requestRefresh).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        mLv.hideHeadView();
                        ToastUtils.toast(IntegrateActivity.this, "无网络");
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    List<String> tempList = new ArrayList<String>();
                                    tempList.addAll(testList);
                                    testList.clear();
                                    for (int i = 0; i < 1; i++) {
                                        testList.add("会员积分-刷新" + i);
                                    }
                                    testList.addAll(tempList);
                                    handler.post(runnableRefresh);
                                }
                            }.start();
                        } else {
                            mLv.hideHeadView();
                            ToastUtils.toast(IntegrateActivity.this, "出小差了");
                        }
                    }
                });
                break;
            case LOAD_LOAD:
                Request requestLoad = new Request.Builder().url(NetConfig.cityUrl).get().build();
                okHttpClient.newCall(requestLoad).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        mLv.hideFootView();
                        ToastUtils.toast(IntegrateActivity.this, "无网络");
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    for (int i = 0; i < 1; i++) {
                                        testList.add("会员积分-加载-" + i);
                                    }
                                    handler.post(runnableLoad);
                                }
                            }.start();
                        } else {
                            mLv.hideFootView();
                            ToastUtils.toast(IntegrateActivity.this, "出小差了");
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_integrate_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDownPullRefresh() {
        loadData(LOAD_REFRESH);
    }

    @Override
    public void onLoadingMore() {
        loadData(LOAD_LOAD);
    }

    Runnable runnableFirst = new Runnable() {
        @Override
        public void run() {
            mPd.dismiss();
            testAdapter.notifyDataSetChanged();
        }
    };

    Runnable runnableRefresh = new Runnable() {
        @Override
        public void run() {
            mLv.hideHeadView();
            testAdapter.notifyDataSetChanged();
        }
    };

    Runnable runnableLoad = new Runnable() {
        @Override
        public void run() {
            mLv.hideFootView();
            testAdapter.notifyDataSetChanged();
        }
    };
}
