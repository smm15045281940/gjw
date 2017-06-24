package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class ReceiveAreaActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, OnRefreshListener {

    private View rootView;
    private RelativeLayout backRl;
    private TextView firstAddressTv, secondAddressTv, thirdAddressTv;
    private View firstAddressV, secondAddressV, thirdAddressV;
    private MyRefreshListView mLv;
    private OfKindAdapter mChooseAddressAdapter;
    private List<OfKind> mDataList = new ArrayList<>();
    private final int FIRST = 1;
    private final int SECOND = 2;
    private final int THIRD = 3;
    private int STATE = FIRST;
    private OkHttpClient okHttpClient;
    private ProgressDialog mPd;
    private String secondId;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        mPd.dismiss();
                        mChooseAddressAdapter.notifyDataSetChanged();
                        ToastUtils.toast(ReceiveAreaActivity.this, "无网络");
                        break;
                    case 1:
                        mPd.dismiss();
                        mChooseAddressAdapter.notifyDataSetChanged();
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
        rootView = View.inflate(this, R.layout.activity_choose_address, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
        loadData("0");
    }

    private void initView() {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_chooseaddress_back);
        firstAddressTv = (TextView) rootView.findViewById(R.id.tv_chooseaddress_first);
        secondAddressTv = (TextView) rootView.findViewById(R.id.tv_chooseaddress_second);
        thirdAddressTv = (TextView) rootView.findViewById(R.id.tv_chooseaddress_third);
        firstAddressV = rootView.findViewById(R.id.v_chooseaddress_first);
        secondAddressV = rootView.findViewById(R.id.v_chooseaddress_second);
        thirdAddressV = rootView.findViewById(R.id.v_chooseaddress_third);
        mLv = (MyRefreshListView) rootView.findViewById(R.id.lv_chooseaddress);
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
        mPd = new ProgressDialog(this);
        mPd.setMessage("加载中..");
        mChooseAddressAdapter = new OfKindAdapter(this, mDataList);
    }

    private void setData() {
        mLv.setAdapter(mChooseAddressAdapter);
    }

    private void setListener() {
        backRl.setOnClickListener(this);
        firstAddressTv.setOnClickListener(this);
        secondAddressTv.setOnClickListener(this);
        mLv.setOnItemClickListener(this);
        mLv.setOnRefreshListener(this);
    }

    private void loadData(String id) {
        mPd.show();
        Request requestFirst = new Request.Builder().url(NetConfig.receiveAreaUrl + id).get().build();
        okHttpClient.newCall(requestFirst).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    mDataList.clear();
                    String json = response.body().string();
                    if (parseJson(json))
                        handler.sendEmptyMessage(1);
                }
            }
        });
    }

    private boolean parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            JSONObject objDatas = objBean.optJSONObject("datas");
            JSONArray arrAreaList = objDatas.optJSONArray("area_list");
            for (int i = 0; i < arrAreaList.length(); i++) {
                JSONObject o = arrAreaList.optJSONObject(i);
                OfKind ofKind = new OfKind();
                ofKind.setId(o.optString("area_id"));
                ofKind.setName(o.optString("area_name"));
                mDataList.add(ofKind);
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_chooseaddress_back:
                finish();
                break;
            case R.id.tv_chooseaddress_first:
                if (STATE != FIRST) {
                    STATE = FIRST;
                    changeColor();
                    firstAddressTv.setText("一级地区");
                    secondAddressTv.setText("二级地区");
                    loadData("0");
                }
                break;
            case R.id.tv_chooseaddress_second:
                if (STATE == THIRD) {
                    STATE = SECOND;
                    changeColor();
                    secondAddressTv.setText("二级地区");
                    loadData(secondId);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (STATE) {
            case FIRST:
                STATE = SECOND;
                firstAddressTv.setText(mDataList.get(position - 1).getName());
                secondId = mDataList.get(position - 1).getId();
                loadData(mDataList.get(position - 1).getId());
                break;
            case SECOND:
                STATE = THIRD;
                secondAddressTv.setText(mDataList.get(position - 1).getName());
                loadData(mDataList.get(position - 1).getId());
                break;
            case THIRD:
                Intent intent = new Intent();
                String sendAddress = firstAddressTv.getText().toString() + " " + secondAddressTv.getText().toString() + " " + mDataList.get(position - 1).getName();
                intent.putExtra("sendAddress", sendAddress);
                setResult(1, intent);
                finish();
                break;
            default:
                break;
        }
        changeColor();
    }

    private void changeColor() {
        switch (STATE) {
            case FIRST:
                firstAddressTv.setTextColor(Color.RED);
                secondAddressTv.setTextColor(Color.BLACK);
                thirdAddressTv.setTextColor(Color.BLACK);
                firstAddressV.setVisibility(View.VISIBLE);
                secondAddressV.setVisibility(View.INVISIBLE);
                thirdAddressV.setVisibility(View.INVISIBLE);
                break;
            case SECOND:
                firstAddressTv.setTextColor(Color.BLACK);
                secondAddressTv.setTextColor(Color.RED);
                thirdAddressTv.setTextColor(Color.BLACK);
                firstAddressV.setVisibility(View.INVISIBLE);
                secondAddressV.setVisibility(View.VISIBLE);
                thirdAddressV.setVisibility(View.INVISIBLE);
                break;
            case THIRD:
                firstAddressTv.setTextColor(Color.BLACK);
                secondAddressTv.setTextColor(Color.BLACK);
                thirdAddressTv.setTextColor(Color.RED);
                firstAddressV.setVisibility(View.INVISIBLE);
                secondAddressV.setVisibility(View.INVISIBLE);
                thirdAddressV.setVisibility(View.VISIBLE);
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
}
