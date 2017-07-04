package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.Property;
import config.NetConfig;
import utils.ToastUtils;
import utils.UserUtils;

public class PropertyActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View rootView;
    private RelativeLayout mBackRl;
    private SwipeRefreshLayout srl;
    private RelativeLayout mAccountBalanceRl, mPrepaidCardRl, mVoucherRl, mRedbagRl, mVipIntegrateRl;
    private TextView availableRcBalanceTv, predepoitTv, voucherTv, redpacketTv, pointTv;
    private OkHttpClient okHttpClient;
    private ProgressDialog progressDialog;
    private List<Property> propertyList = new ArrayList<>();
    private boolean isLogined;
    private String key;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        progressDialog.dismiss();
                        ToastUtils.toast(PropertyActivity.this, "无网络");
                        break;
                    case 1:
                        progressDialog.dismiss();
                        setView();
                        break;
                    case 2:
                        srl.setRefreshing(false);
                        setView();
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
        rootView = View.inflate(this, R.layout.activity_myproperty, null);
        setContentView(rootView);
        initView();
        initData();
        loadData();
        setListener();
    }

    private void initView() {
        initRoot();
    }

    private void initRoot() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_myproperty_back);
        srl = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_property);
        mAccountBalanceRl = (RelativeLayout) rootView.findViewById(R.id.rl_myproperty_accountbalance);
        mPrepaidCardRl = (RelativeLayout) rootView.findViewById(R.id.rl_myproperty_prepaidphonecardaccountbalance);
        mVoucherRl = (RelativeLayout) rootView.findViewById(R.id.rl_myproperty_voucher);
        mRedbagRl = (RelativeLayout) rootView.findViewById(R.id.rl_myproperty_platredbag);
        mVipIntegrateRl = (RelativeLayout) rootView.findViewById(R.id.rl_myproperty_vipintegrate);
        availableRcBalanceTv = (TextView) rootView.findViewById(R.id.tv_myproperty_available_rc_balance);
        predepoitTv = (TextView) rootView.findViewById(R.id.tv_myproperty_predepoit);
        voucherTv = (TextView) rootView.findViewById(R.id.tv_myproperty_voucher);
        redpacketTv = (TextView) rootView.findViewById(R.id.tv_myproperty_redpacket);
        pointTv = (TextView) rootView.findViewById(R.id.tv_myproperty_point);
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
        progressDialog = new ProgressDialog(this);
        srl.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        isLogined = UserUtils.isLogined(this);
        if (isLogined) {
            key = (UserUtils.readLogin(this, isLogined)).getKey();
        }
    }

    private void loadData() {
        if (isLogined) {
            progressDialog.show();
            Request request = new Request.Builder().url(NetConfig.propertyHeadUrl + key).get().build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    handler.sendEmptyMessage(0);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        if (parseJson(response.body().string())) {
                            handler.sendEmptyMessage(1);
                        } else {
                            handler.sendEmptyMessage(0);
                        }
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                }
            });
        }
    }

    private boolean parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            JSONObject objDatas = objBean.optJSONObject("datas");
            Property property = new Property();
            property.setPoint(objDatas.optString("point"));
            property.setPredepoit(objDatas.optString("predepoit"));
            property.setAvailable_rc_balance(objDatas.optString("available_rc_balance"));
            property.setRedpacket(objDatas.optString("redpacket"));
            property.setVoucher(objDatas.optString("voucher"));
            propertyList.add(property);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        srl.setOnRefreshListener(this);
        mAccountBalanceRl.setOnClickListener(this);
        mPrepaidCardRl.setOnClickListener(this);
        mVoucherRl.setOnClickListener(this);
        mRedbagRl.setOnClickListener(this);
        mVipIntegrateRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_myproperty_back:
                finish();
                break;
            case R.id.rl_myproperty_accountbalance:
                startActivity(new Intent(PropertyActivity.this, AccountBalanceActivity.class));
                break;
            case R.id.rl_myproperty_prepaidphonecardaccountbalance:
                startActivity(new Intent(PropertyActivity.this, PrepaidPhoneCardActivity.class));
                break;
            case R.id.rl_myproperty_voucher:
                startActivity(new Intent(PropertyActivity.this, VoucherActivity.class));
                break;
            case R.id.rl_myproperty_platredbag:
                startActivity(new Intent(PropertyActivity.this, RedBagActivity.class));
                break;
            case R.id.rl_myproperty_vipintegrate:
                startActivity(new Intent(PropertyActivity.this, IntegrateActivity.class));
                break;
            default:
                break;
        }
    }

    private void setView() {
        availableRcBalanceTv.setText(propertyList.get(0).getAvailable_rc_balance() + "元");
        predepoitTv.setText(propertyList.get(0).getPredepoit() + "元");
        voucherTv.setText(propertyList.get(0).getVoucher() + "张");
        redpacketTv.setText(propertyList.get(0).getRedpacket() + "个");
        pointTv.setText(propertyList.get(0).getPoint() + "分");
    }

    @Override
    public void onRefresh() {
        Request request = new Request.Builder().url(NetConfig.propertyHeadUrl + key).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    propertyList.clear();
                    if (parseJson(response.body().string())) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }
}
