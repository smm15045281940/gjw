package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.MyfootAdapter;
import bean.Myfoot;
import config.NetConfig;
import customview.MyRefreshListView;
import customview.OnRefreshListener;
import utils.ToastUtils;

public class FootActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener, OnRefreshListener {

    private View rootView, emptyView;
    private RelativeLayout mBackRl, mClearRl;
    private MyRefreshListView mMyfootLv;
    private ImageView emptyIconIv;
    private TextView emptyHintTv, emptyRecommendTv;
    private List<Myfoot> mDataList = new ArrayList<>();
    private MyfootAdapter mAdapter;
    private AlertDialog deleteAd, clearAd;
    private int deletePosition;
    private ProgressDialog mPd;
    private OkHttpClient okHttpClient;
    private Handler handler;
    private final int LOAD_FIRST = 1;
    private final int LOAD_REFRESH = 2;
    private final int LOAD_LOAD = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_myfoot, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        loadData(LOAD_FIRST);
        setListener();
    }

    private void initView() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_myfoot_back);
        mClearRl = (RelativeLayout) rootView.findViewById(R.id.rl_myfoot_clear);
        mMyfootLv = (MyRefreshListView) rootView.findViewById(R.id.lv_myfoot);
        AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(FootActivity.this);
        deleteBuilder.setMessage("是否删除?");
        deleteBuilder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAd.dismiss();
            }
        }).setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDataList.remove(deletePosition);
                mAdapter.notifyDataSetChanged();
                deleteAd.dismiss();
            }
        });
        deleteAd = deleteBuilder.create();
        AlertDialog.Builder clearBuilder = new AlertDialog.Builder(FootActivity.this);
        clearBuilder.setMessage("是否清空?");
        clearBuilder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearAd.dismiss();
            }
        }).setNegativeButton("清空", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDataList.clear();
                mAdapter.notifyDataSetChanged();
                clearAd.dismiss();
                ToastUtils.toast(FootActivity.this, "已清空");
            }
        });
        clearAd = clearBuilder.create();
        emptyView = View.inflate(FootActivity.this, R.layout.empty_type_myfoot, null);
        emptyIconIv = (ImageView) emptyView.findViewById(R.id.iv_empty_type_myfoot_icon);
        emptyHintTv = (TextView) emptyView.findViewById(R.id.tv_empty_type_myfoot_hint);
        emptyRecommendTv = (TextView) emptyView.findViewById(R.id.tv_empty_type_myfoot_recommend);
        emptyIconIv.setImageResource(R.mipmap.empty_myfoot);
        emptyHintTv.setText("暂无您的浏览记录");
        emptyRecommendTv.setText("可以去看看哪些想要买的");
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) mMyfootLv.getParent()).addView(emptyView);
        mMyfootLv.setEmptyView(emptyView);
    }

    private void initData() {
        mPd = new ProgressDialog(this);
        mPd.setMessage("加载中..");
        okHttpClient = new OkHttpClient();
        handler = new Handler();
        mAdapter = new MyfootAdapter(this, mDataList);
    }

    private void setData() {
        mMyfootLv.setAdapter(mAdapter);
    }

    private void loadData(int LOAD_STATE) {
        switch (LOAD_STATE) {
            case LOAD_FIRST:
                emptyView.setVisibility(View.GONE);
                mPd.show();
                Request requestFirst = new Request.Builder().url(NetConfig.cityUrl).get().build();
                okHttpClient.newCall(requestFirst).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        mPd.dismiss();
                        ToastUtils.toast(FootActivity.this, "无网络");
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    Myfoot mf1 = new Myfoot();
                                    mf1.goodsName = "精品瓷砖";
                                    mf1.goodsPrice = "¥35.00";
                                    mf1.goodsImg = null;
                                    Myfoot mf2 = new Myfoot();
                                    mf2.goodsName = "圆管";
                                    mf2.goodsPrice = "¥25.50";
                                    mf2.goodsImg = null;
                                    Myfoot mf3 = new Myfoot();
                                    mf3.goodsName = "把手";
                                    mf3.goodsPrice = "¥10.00";
                                    mf3.goodsImg = null;
                                    mDataList.add(mf1);
                                    mDataList.add(mf2);
                                    mDataList.add(mf3);
                                    mDataList.add(mf1);
                                    mDataList.add(mf2);
                                    mDataList.add(mf3);
                                    mDataList.add(mf1);
                                    mDataList.add(mf2);
                                    mDataList.add(mf3);
                                    mDataList.add(mf1);
                                    mDataList.add(mf2);
                                    mDataList.add(mf3);
                                    mDataList.add(mf1);
                                    mDataList.add(mf2);
                                    mDataList.add(mf3);
                                    handler.post(runnableFirst);
                                }
                            }.start();
                        } else {
                            mPd.dismiss();
                            ToastUtils.toast(FootActivity.this, "出小差了");
                        }
                    }
                });
                break;
            case LOAD_REFRESH:
                Request requestRefresh = new Request.Builder().url(NetConfig.cityUrl).get().build();
                okHttpClient.newCall(requestRefresh).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        mMyfootLv.hideHeadView();
                        ToastUtils.toast(FootActivity.this, "无网络");
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    List<Myfoot> tempList = new ArrayList<Myfoot>();
                                    tempList.addAll(mDataList);
                                    mDataList.clear();
                                    Myfoot mf1 = new Myfoot();
                                    mf1.goodsName = "精品瓷砖";
                                    mf1.goodsPrice = "¥35.00";
                                    mf1.goodsImg = null;
                                    Myfoot mf2 = new Myfoot();
                                    mf2.goodsName = "圆管";
                                    mf2.goodsPrice = "¥25.50";
                                    mf2.goodsImg = null;
                                    Myfoot mf3 = new Myfoot();
                                    mf3.goodsName = "把手";
                                    mf3.goodsPrice = "¥10.00";
                                    mf3.goodsImg = null;
                                    mDataList.add(mf1);
                                    mDataList.add(mf2);
                                    mDataList.add(mf3);
                                    mDataList.addAll(tempList);
                                    handler.post(runnableRefresh);
                                }
                            }.start();
                        } else {
                            mMyfootLv.hideHeadView();
                            ToastUtils.toast(FootActivity.this, "出小差了");
                        }
                    }
                });
                break;
            case LOAD_LOAD:
                Request requestLoad = new Request.Builder().url(NetConfig.cityUrl).get().build();
                okHttpClient.newCall(requestLoad).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        mMyfootLv.hideFootView();
                        ToastUtils.toast(FootActivity.this, "无网络");
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    Myfoot mf1 = new Myfoot();
                                    mf1.goodsName = "精品瓷砖";
                                    mf1.goodsPrice = "¥35.00";
                                    mf1.goodsImg = null;
                                    Myfoot mf2 = new Myfoot();
                                    mf2.goodsName = "圆管";
                                    mf2.goodsPrice = "¥25.50";
                                    mf2.goodsImg = null;
                                    Myfoot mf3 = new Myfoot();
                                    mf3.goodsName = "把手";
                                    mf3.goodsPrice = "¥10.00";
                                    mf3.goodsImg = null;
                                    mDataList.add(mf1);
                                    mDataList.add(mf2);
                                    mDataList.add(mf3);
                                    handler.post(runnableLoad);
                                }
                            }.start();
                        } else {
                            mMyfootLv.hideFootView();
                            ToastUtils.toast(FootActivity.this, "出小差了");
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mClearRl.setOnClickListener(this);
        mMyfootLv.setOnItemLongClickListener(this);
        mMyfootLv.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_myfoot_back:
                finish();
                break;
            case R.id.rl_myfoot_clear:
                if (!mDataList.isEmpty()) {
                    clearAd.show();
                } else {
                    ToastUtils.toast(FootActivity.this, "没有什么可清空的");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_myfoot:
                deletePosition = position - 1;
                ToastUtils.toast(FootActivity.this, deletePosition + "");
                deleteAd.show();
                break;
            default:
                break;
        }
        return false;
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
            emptyView.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
    };

    Runnable runnableRefresh = new Runnable() {
        @Override
        public void run() {
            mMyfootLv.hideHeadView();
            mAdapter.notifyDataSetChanged();
        }
    };

    Runnable runnableLoad = new Runnable() {
        @Override
        public void run() {
            mMyfootLv.hideFootView();
            mAdapter.notifyDataSetChanged();
        }
    };
}
