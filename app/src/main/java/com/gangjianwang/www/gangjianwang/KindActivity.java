package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.OfKindAdapter;
import bean.OfKind;
import config.NetConfig;
import customview.MyRefreshListView;
import customview.OnRefreshListener;
import utils.ToastUtils;

public class KindActivity extends AppCompatActivity implements View.OnClickListener, OnRefreshListener, AdapterView.OnItemClickListener {

    private View rootView;
    private RelativeLayout backRl;
    private RelativeLayout mFirstRl, mSecondRl;
    private TextView mFirstTv, mSecondTv, mThirdTv;
    private View mFirstV, mSecondV, mThirdV;
    private MyRefreshListView mLv;
    private List<OfKind> mDataList = new ArrayList<>();
    private List<OfKind> mFirstList = new ArrayList<>();
    private List<OfKind> mSecondList = new ArrayList<>();
    private List<OfKind> mThirdList = new ArrayList<>();
    private OfKindAdapter mAdapter;
    private ProgressDialog mPd;
    private OkHttpClient okHttpClient;

    private final int LOAD_FIRST = 1;
    private final int LOAD_SECOND = 2;
    private final int LOAD_THIRD = 3;
    private int STATE = LOAD_FIRST;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        mPd.dismiss();
                        ToastUtils.toast(KindActivity.this, "无网络");
                        break;
                    case 1:
                        mPd.dismiss();
                        mAdapter.notifyDataSetChanged();
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
        rootView = View.inflate(this, R.layout.activity_kind, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
        loadData(LOAD_FIRST, 0);
    }

    private void initView() {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_kind_back);
        mFirstRl = (RelativeLayout) rootView.findViewById(R.id.rl_kind_first);
        mSecondRl = (RelativeLayout) rootView.findViewById(R.id.rl_kind_second);
        mFirstTv = (TextView) rootView.findViewById(R.id.tv_kind_first);
        mSecondTv = (TextView) rootView.findViewById(R.id.tv_kind_second);
        mThirdTv = (TextView) rootView.findViewById(R.id.tv_kind_third);
        mFirstV = rootView.findViewById(R.id.v_kind_first);
        mSecondV = rootView.findViewById(R.id.v_kind_second);
        mThirdV = rootView.findViewById(R.id.v_kind_third);
        mLv = (MyRefreshListView) rootView.findViewById(R.id.lv_calculate_ofkind);
        setTop();
    }

    private void initData() {
        mPd = new ProgressDialog(this);
        mPd.setMessage("加载中..");
        okHttpClient = new OkHttpClient();
        mAdapter = new OfKindAdapter(this, mDataList);
    }

    private void setData() {
        mLv.setAdapter(mAdapter);
    }

    private void setListener() {
        backRl.setOnClickListener(this);
        mFirstRl.setOnClickListener(this);
        mSecondRl.setOnClickListener(this);
        mLv.setOnRefreshListener(this);
        mLv.setOnItemClickListener(this);
    }

    private void loadData(int LOAD_STATE, int id) {
        switch (LOAD_STATE) {
            case LOAD_FIRST:
                mPd.show();
                Request requestFirst = new Request.Builder().url(NetConfig.ofKindHeadUrl + NetConfig.ofKindFootUrl).get().build();
                okHttpClient.newCall(requestFirst).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String result = response.body().string();
                            String json = cutJson(result);
                            mDataList.clear();
                            if (parseJson(json, LOAD_FIRST))
                                handler.sendEmptyMessage(1);

                        }
                    }
                });
                break;
            case LOAD_SECOND:
                mPd.show();
                Request requestSecond = new Request.Builder().url(NetConfig.ofKindHeadUrl + id + NetConfig.ofKindFootUrl).get().build();
                okHttpClient.newCall(requestSecond).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String result = response.body().string();
                            String json = cutJson(result);
                            mDataList.clear();
                            if (parseJson(json, LOAD_SECOND))
                                handler.sendEmptyMessage(1);
                        }
                    }
                });
                break;
            case LOAD_THIRD:
                mPd.show();
                Request requestThird = new Request.Builder().url(NetConfig.ofKindHeadUrl + id + NetConfig.ofKindFootUrl).get().build();
                okHttpClient.newCall(requestThird).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String result = response.body().string();
                            String json = cutJson(result);
                            mDataList.clear();
                            if (parseJson(json, LOAD_THIRD)) {
                                handler.sendEmptyMessage(1);
                            }
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    private String cutJson(String json) {
        int a = json.indexOf("(");
        int b = json.lastIndexOf(")");
        String s = json.substring(a + 1, b);
        return s;
    }

    private boolean parseJson(String json, int LOAD_STATE) {
        boolean b = false;
        try {
            JSONObject objBean = new JSONObject(json);
            JSONObject objDatas = objBean.optJSONObject("datas");
            JSONArray arrClassList = objDatas.optJSONArray("class_list");
            for (int i = 0; i < arrClassList.length(); i++) {
                JSONObject o = arrClassList.optJSONObject(i);
                OfKind ofKind = new OfKind();
                ofKind.setId(o.optString("gc_id"));
                ofKind.setName(o.optString("gc_name"));
                switch (LOAD_STATE) {
                    case LOAD_FIRST:
                        mFirstList.add(ofKind);
                        break;
                    case LOAD_SECOND:
                        mSecondList.add(ofKind);
                        break;
                    case LOAD_THIRD:
                        mThirdList.add(ofKind);
                        break;
                    default:
                        break;
                }
            }
            switch (LOAD_STATE) {
                case LOAD_FIRST:
                    mDataList.addAll(mFirstList);
                    break;
                case LOAD_SECOND:
                    mDataList.addAll(mSecondList);
                    break;
                case LOAD_THIRD:
                    mDataList.addAll(mThirdList);
                    break;
                default:
                    break;
            }
            b = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return b;
    }

    private void setTop() {
        switch (STATE) {
            case LOAD_FIRST:
                mFirstTv.setTextColor(Color.RED);
                mSecondTv.setTextColor(Color.BLACK);
                mThirdTv.setTextColor(Color.BLACK);
                mFirstV.setBackgroundColor(Color.RED);
                mSecondV.setBackgroundColor(Color.GRAY);
                mThirdV.setBackgroundColor(Color.GRAY);
                break;
            case LOAD_SECOND:
                mFirstTv.setTextColor(Color.BLACK);
                mSecondTv.setTextColor(Color.RED);
                mThirdTv.setTextColor(Color.BLACK);
                mFirstV.setBackgroundColor(Color.GRAY);
                mSecondV.setBackgroundColor(Color.RED);
                mThirdV.setBackgroundColor(Color.GRAY);
                break;
            case LOAD_THIRD:
                mFirstTv.setTextColor(Color.BLACK);
                mSecondTv.setTextColor(Color.BLACK);
                mThirdTv.setTextColor(Color.RED);
                mFirstV.setBackgroundColor(Color.GRAY);
                mSecondV.setBackgroundColor(Color.GRAY);
                mThirdV.setBackgroundColor(Color.RED);
                break;
            default:
                break;
        }
    }

    private void changeBottom() {
        mDataList.clear();
        switch (STATE) {
            case LOAD_FIRST:
                mDataList.addAll(mFirstList);
                break;
            case LOAD_SECOND:
                mDataList.addAll(mSecondList);
                break;
            default:
                break;
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_kind_back:
                finish();
                break;
            case R.id.rl_kind_first:
                if (STATE != LOAD_FIRST) {
                    mFirstTv.setText("一级分类");
                    mSecondTv.setText("二级分类");
                    mSecondList.clear();
                    mThirdList.clear();
                    STATE = LOAD_FIRST;
                    setTop();
                    changeBottom();
                }
                break;
            case R.id.rl_kind_second:
                if (STATE == LOAD_THIRD) {
                    mSecondTv.setText("二级分类");
                    mThirdList.clear();
                    STATE = LOAD_SECOND;
                    setTop();
                    changeBottom();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDownPullRefresh() {
        mLv.hideHeadView();
    }

    @Override
    public void onLoadingMore() {
        mLv.hideFootView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (STATE) {
            case LOAD_FIRST:
                STATE = LOAD_SECOND;
                setTop();
                mFirstTv.setText(mDataList.get(position - 1).getName());
                String str1 = mDataList.get(position - 1).getId();
                int gc_id1 = Integer.parseInt(str1);
                loadData(STATE, gc_id1);
                break;
            case LOAD_SECOND:
                STATE = LOAD_THIRD;
                setTop();
                mSecondTv.setText(mDataList.get(position - 1).getName());
                String str2 = mDataList.get(position - 1).getId();
                int gc_id2 = Integer.parseInt(str2);
                loadData(STATE, gc_id2);
                break;
            case LOAD_THIRD:
                ToastUtils.toast(KindActivity.this, mDataList.get(position - 1).getName());
                break;
            default:
                break;
        }

    }
}
