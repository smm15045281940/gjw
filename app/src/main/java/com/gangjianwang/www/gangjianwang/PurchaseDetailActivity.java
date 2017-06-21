package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;

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

import adapter.PurchaseDetailAdapter;
import bean.PurchaseDetail;
import bean.PurchaseDetailPhotoDescription;
import config.NetConfig;
import utils.ToastUtils;

public class PurchaseDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout backRl;
    private ListView mLv;
    private String detailId;
    private OkHttpClient okHttpClient;
    private ProgressDialog mPd;
    private PurchaseDetailAdapter mAdapter;
    private List<PurchaseDetail> mDataList = new ArrayList<>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        mPd.dismiss();
                        ToastUtils.toast(PurchaseDetailActivity.this, "无网络");
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
        rootView = View.inflate(this, R.layout.activity_purchase_detail, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
        loadData(detailId);
    }

    private void initView() {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_purchase_detail_back);
        mLv = (ListView) rootView.findViewById(R.id.lv_purchase_detail);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null)
            detailId = intent.getStringExtra("purchaseId");
        okHttpClient = new OkHttpClient();
        mPd = new ProgressDialog(this);
        mPd.setMessage("加载中..");
        mAdapter = new PurchaseDetailAdapter(this, mDataList);
    }

    private void setData() {
        mLv.setAdapter(mAdapter);
    }

    private void setListener() {
        backRl.setOnClickListener(this);
    }

    private void loadData(String id) {
        mPd.show();
        Request request = new Request.Builder().url(NetConfig.purchaseDetailUrl + id).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
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
            JSONObject o = objDatas.optJSONObject("purchase_info");
            PurchaseDetail purchaseDetail = new PurchaseDetail();
            purchaseDetail.setPurchaseState(o.optString("purchase_state_desc"));
            purchaseDetail.setPurchaseNumber(o.optString("purchase_sn"));
            purchaseDetail.setPurchaseName(o.optString("purchase_name"));
            purchaseDetail.setCreateTime(o.optString("add_time"));
            purchaseDetail.setGoodsName(o.optString("goods_name"));
            purchaseDetail.setGoodsKind(o.optString("gc_name"));
            purchaseDetail.setPurchaseAmount(o.optString("purchase_num"));
            purchaseDetail.setGoodsDescription(o.optString("goods_intro"));
            List<PurchaseDetailPhotoDescription> list = new ArrayList<>();
            JSONArray arrExt = o.optJSONArray("ext");
            for (int i = 0; i < arrExt.length(); i++) {
                JSONObject ect = arrExt.optJSONObject(i);
                PurchaseDetailPhotoDescription p = new PurchaseDetailPhotoDescription();
                p.setImgUrl(ect.optString("photo"));
                p.setDescription(ect.optString("descrip"));
                list.add(p);
                Log.e("TAG", p.toString());
            }
            purchaseDetail.setPhotoDescriptionList(list);
            purchaseDetail.setBillRequire(o.optString("goods_vat_desc"));
            purchaseDetail.setTransportRequire(o.optString("shipping_fee_desc"));
            purchaseDetail.setReceiveArea(o.optString("area_info"));
            purchaseDetail.setDetailArea(o.optString("address_info"));
            purchaseDetail.setDeliverTime(o.optString("delivery_time"));
            purchaseDetail.setDescription(o.optString("purchase_description"));
            purchaseDetail.setBidendTime(o.optString("bid_end_time"));
            purchaseDetail.setResultTime(o.optString("result_time"));
            purchaseDetail.setMaxPrice(o.optString("max_price"));
            purchaseDetail.setMemberName(o.optString("member_name"));
            purchaseDetail.setMemberPhone(o.optString("member_phone"));
            purchaseDetail.setMemberMobile(o.optString("member_mobile"));
            mDataList.add(purchaseDetail);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_purchase_detail_back:
                finish();
                break;
            default:
                break;
        }
    }
}
