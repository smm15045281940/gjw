package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.MyIntegrateAdapter;
import bean.MyIntegrate;
import bean.MyIntegrateSum;
import config.NetConfig;
import customview.MyRefreshListView;
import customview.OnRefreshListener;
import utils.ToastUtils;

public class IntegrateActivity extends AppCompatActivity implements View.OnClickListener, OnRefreshListener {

    private RelativeLayout mBackRl;
    private MyRefreshListView mLv;
    private List<MyIntegrateSum> mFirstList = new ArrayList<>();
    private List<MyIntegrate> mDataList = new ArrayList<>();
    private MyIntegrateAdapter mAdapter;
    private final int LOAD_FIRST = 1;
    private final int LOAD_REFRESH = 2;
    private final int LOAD_LOAD = 3;
    private ProgressDialog mPd;
    private Handler handler;
    private OkHttpClient okHttpClient;

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
        mAdapter = new MyIntegrateAdapter(this, mFirstList, mDataList);
    }

    private void setData() {
        mLv.setAdapter(mAdapter);
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
                                    MyIntegrateSum myIntegrateSum = new MyIntegrateSum();
                                    myIntegrateSum.integrateSumName = "我的积分";
                                    myIntegrateSum.integrateSumScore = "10745";
                                    mFirstList.add(myIntegrateSum);
                                    MyIntegrate myIntegrate1 = new MyIntegrate();
                                    myIntegrate1.integrateTitle = "登录";
                                    myIntegrate1.integrateContent = "会员登录";
                                    myIntegrate1.integrateScore = "+20";
                                    myIntegrate1.integrateTime = "2016-4-20";
                                    mDataList.add(myIntegrate1);
                                    MyIntegrate myIntegrate2 = new MyIntegrate();
                                    myIntegrate2.integrateTitle = "登录";
                                    myIntegrate2.integrateContent = "会员登录";
                                    myIntegrate2.integrateScore = "+5";
                                    myIntegrate2.integrateTime = "2017-4-5";
                                    mDataList.add(myIntegrate2);
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
                                    List<MyIntegrate> tempList = new ArrayList<MyIntegrate>();
                                    tempList.addAll(mDataList);
                                    mDataList.clear();
                                    MyIntegrate myIntegrate1 = new MyIntegrate();
                                    myIntegrate1.integrateTitle = "登录";
                                    myIntegrate1.integrateContent = "会员登录";
                                    myIntegrate1.integrateScore = "+20";
                                    myIntegrate1.integrateTime = "2016-4-20";
                                    mDataList.add(myIntegrate1);
                                    MyIntegrate myIntegrate2 = new MyIntegrate();
                                    myIntegrate2.integrateTitle = "登录";
                                    myIntegrate2.integrateContent = "会员登录";
                                    myIntegrate2.integrateScore = "+5";
                                    myIntegrate2.integrateTime = "2017-4-5";
                                    mDataList.add(myIntegrate2);
                                    mDataList.addAll(tempList);
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
                                    MyIntegrate myIntegrate1 = new MyIntegrate();
                                    myIntegrate1.integrateTitle = "登录";
                                    myIntegrate1.integrateContent = "会员登录";
                                    myIntegrate1.integrateScore = "+20";
                                    myIntegrate1.integrateTime = "2016-4-20";
                                    mDataList.add(myIntegrate1);
                                    MyIntegrate myIntegrate2 = new MyIntegrate();
                                    myIntegrate2.integrateTitle = "登录";
                                    myIntegrate2.integrateContent = "会员登录";
                                    myIntegrate2.integrateScore = "+5";
                                    myIntegrate2.integrateTime = "2017-4-5";
                                    mDataList.add(myIntegrate2);
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
            mAdapter.notifyDataSetChanged();
        }
    };

    Runnable runnableRefresh = new Runnable() {
        @Override
        public void run() {
            mLv.hideHeadView();
            mAdapter.notifyDataSetChanged();
        }
    };

    Runnable runnableLoad = new Runnable() {
        @Override
        public void run() {
            mLv.hideFootView();
            mAdapter.notifyDataSetChanged();
        }
    };
}
