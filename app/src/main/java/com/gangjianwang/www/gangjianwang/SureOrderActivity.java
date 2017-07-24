package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.SureOrderStoreAdapter;
import bean.AddAddress;
import bean.SureOrderGoods;
import bean.SureOrderStore;
import config.NetConfig;
import config.ParaConfig;
import utils.ToastUtils;

public class SureOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout backRl, submitOrderRl;
    private LinearLayout addressLl, payWayLl, billInfoLl;
    private TextView nameTv, areaTv, addressTv, paywayTv, billInfoTv, orderAmountTv;
    private ProgressDialog progressDialog;

    private ListView lv;
    private List<SureOrderStore> sureOrderStoreList = new ArrayList<>();
    private SureOrderStoreAdapter sureOrderStoreAdapter;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private String name, area, address, payway, billInfo, orderAmount;

    private String key, cart_id, quotation_id, ifcart, address_id, store_id;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case 0:
                        ToastUtils.toast(SureOrderActivity.this, ParaConfig.NETWORK_ERROR);
                        break;
                    case 1:
                        setView();
                        break;
                    default:
                        break;
                }
                sureOrderStoreAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
        handler.removeMessages(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(SureOrderActivity.this, R.layout.activity_sure_order, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
        loadData();
    }

    private void initView() {
        initRoot();
        initProgress();
    }

    private void initRoot() {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_sure_order_back);
        submitOrderRl = (RelativeLayout) rootView.findViewById(R.id.rl_sure_order_submit);
        addressLl = (LinearLayout) rootView.findViewById(R.id.ll_sure_order_address);
        payWayLl = (LinearLayout) rootView.findViewById(R.id.ll_sure_order_payway);
        billInfoLl = (LinearLayout) rootView.findViewById(R.id.ll_sure_order_billinfo);
        nameTv = (TextView) rootView.findViewById(R.id.tv_sure_order_name);
        areaTv = (TextView) rootView.findViewById(R.id.tv_sure_order_area);
        addressTv = (TextView) rootView.findViewById(R.id.tv_sure_order_address);
        paywayTv = (TextView) rootView.findViewById(R.id.tv_sure_order_payway);
        billInfoTv = (TextView) rootView.findViewById(R.id.tv_sure_order_billinfo);
        orderAmountTv = (TextView) rootView.findViewById(R.id.tv_sure_order_order_amount);
        lv = (ListView) rootView.findViewById(R.id.lv_sure_order_store);
    }

    private void initProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initData() {
        sureOrderStoreAdapter = new SureOrderStoreAdapter(this, sureOrderStoreList);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        cart_id = intent.getStringExtra("cart_id");
        quotation_id = intent.getStringExtra("quotation_id");
        ifcart = intent.getStringExtra("ifcart");
        address_id = intent.getStringExtra("address_id");
        store_id = intent.getStringExtra("store_id");
        name = "";
        area = "";
        address = "";
        payway = "货到付款";
        billInfo = "";
        orderAmount = "";
    }

    private void setData() {
        lv.setAdapter(sureOrderStoreAdapter);
    }

    private void setListener() {
        backRl.setOnClickListener(SureOrderActivity.this);
        submitOrderRl.setOnClickListener(SureOrderActivity.this);
        addressLl.setOnClickListener(this);
        payWayLl.setOnClickListener(this);
        billInfoLl.setOnClickListener(this);
    }

    private void loadData() {
        progressDialog.show();
        RequestBody body = new FormEncodingBuilder()
                .add("key", key)
                .add("cart_id", cart_id)
                .add("quotation_id", quotation_id)
                .add("ifcart", ifcart)
                .add("address_id", address_id)
                .build();
        Request request = new Request.Builder().url(NetConfig.sureOrderUrl).post(body).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful() && parseJson(response.body().string())) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    private boolean parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            if (objBean.optInt("code") == 200) {
                JSONObject objDatas = objBean.optJSONObject("datas");
                orderAmount = objDatas.optString("order_amount");
                JSONObject objAddressInfo = objDatas.optJSONObject("address_info");
                name = objAddressInfo.optString("true_name");
                area = objAddressInfo.optString("area_info");
                address = objAddressInfo.optString("address");
                JSONObject objInvInfo = objDatas.optJSONObject("inv_info");
                billInfo = objInvInfo.optString("content");
                JSONObject objStoreCartList = objDatas.optJSONObject("store_cart_list");
                JSONObject objStore = objStoreCartList.optJSONObject(store_id);
                SureOrderStore sureOrderStore = new SureOrderStore();
                sureOrderStore.setStoreName(objStore.optString("store_name"));
                sureOrderStore.setStoreGoodsTotal(objStore.optString("store_goods_total"));
                JSONArray arrGoodsList = objStore.optJSONArray("goods_list");
                List<SureOrderGoods> sureOrderGoodsList = new ArrayList<>();
                for (int j = 0; j < arrGoodsList.length(); j++) {
                    JSONObject objGoods = arrGoodsList.optJSONObject(j);
                    SureOrderGoods sureOrderGoods = new SureOrderGoods();
                    sureOrderGoods.setGoodsName(objGoods.optString("goods_name"));
                    sureOrderGoods.setGoodsSpec(objGoods.optString("goods_spec"));
                    sureOrderGoods.setGoodsPrice(objGoods.optString("goods_price"));
                    sureOrderGoods.setGoodsImageUrl(objGoods.optString("goods_image_url"));
                    sureOrderGoodsList.add(sureOrderGoods);
                }
                sureOrderStore.setSureOrderGoodsList(sureOrderGoodsList);
                sureOrderStoreList.add(sureOrderStore);
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setView() {
        nameTv.setText(name);
        areaTv.setText(area);
        addressTv.setText(address);
        paywayTv.setText(payway);
        billInfoTv.setText(billInfo);
        orderAmountTv.setText("¥" + orderAmount);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            if (data != null) {
                AddAddress addAddress = (AddAddress) data.getSerializableExtra("addAddress");
                nameTv.setText(addAddress.getName());
                areaTv.setText(addAddress.getRoughAddress());
                addressTv.setText(addAddress.getDetailAddress());
            }
        }
        if (requestCode == 2 && resultCode == 1) {
            if (data != null) {
                paywayTv.setText(data.getStringExtra("payWay"));
            }
        }
        if (requestCode == 3 && resultCode == 1) {
            if (data != null) {
                billInfoTv.setText(data.getStringExtra("billInfo"));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_sure_order_back:
                finish();
                break;
            case R.id.rl_sure_order_submit:
                ToastUtils.toast(SureOrderActivity.this, "提交订单");
                break;
            case R.id.ll_sure_order_address:
                startActivityForResult(new Intent(SureOrderActivity.this, ReceiveAddressManageActivity.class), 1);
                break;
            case R.id.ll_sure_order_payway:
                startActivityForResult(new Intent(SureOrderActivity.this, ChoosePayWayActivity.class), 2);
                break;
            case R.id.ll_sure_order_billinfo:
                startActivityForResult(new Intent(SureOrderActivity.this, ManageBillInfoActivity.class), 3);
                break;
            default:
                break;
        }
    }
}
