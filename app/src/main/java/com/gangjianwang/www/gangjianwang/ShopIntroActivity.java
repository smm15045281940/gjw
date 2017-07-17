package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import bean.StoreIntro;
import config.NetConfig;
import config.ParaConfig;
import utils.ToastUtils;
import utils.UserUtils;

public class ShopIntroActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout backRl;
    private ProgressDialog progressDialog;

    private ImageView storeAvatarIv;
    private TextView storeNameTv, scNameTv, isFavoriteTv, storeCollectTv;
    private TextView storeDesccreditCreditTv, storeDesccreditPercentTextTv, storeDesccreditPercentTv;
    private TextView storeServicecreditCreditTv, storeServicecreditPercentTextTv, storeServicecreditPercentTv;
    private TextView storeDeliverycreditCreditTv, storeDeliverycreditPercentTextTv, storeDeliverycreditPercentTv;
    private TextView storeCompanyNameTv, areaInfoTv, storeTimeTextTv, storeZyTv, storePhoneTv;
    private RelativeLayout shopIntroIsFavorateRl;

    private String key, store_id;
    private OkHttpClient okHttpClient;
    private RequestBody body;
    private StoreIntro storeIntro;
    private boolean isFavorite;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case 0:
                        ToastUtils.toast(ShopIntroActivity.this, ParaConfig.NETWORK_ERROR);
                        break;
                    case 1:
                        setView();
                        break;
                    case 2:
                        loadData();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
        handler.removeMessages(1);
        handler.removeMessages(2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_shop_intro, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
        loadData();
    }

    private void initView() {
        initRoot();
        initProgress();
    }

    private void initRoot() {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_shopintro_back);
        storeAvatarIv = (ImageView) rootView.findViewById(R.id.iv_shop_intro_store_avatar);
        storeNameTv = (TextView) rootView.findViewById(R.id.tv_shop_intro_store_name);
        scNameTv = (TextView) rootView.findViewById(R.id.tv_shop_intro_sc_name);
        isFavoriteTv = (TextView) rootView.findViewById(R.id.tv_shop_intro_is_favorate);
        storeCollectTv = (TextView) rootView.findViewById(R.id.tv_shop_intro_store_collect);
        storeDesccreditCreditTv = (TextView) rootView.findViewById(R.id.tv_shop_intro_store_desccredit_credit);
        storeDesccreditPercentTextTv = (TextView) rootView.findViewById(R.id.tv_shop_intro_store_desccredit_percent_text);
        storeDesccreditPercentTv = (TextView) rootView.findViewById(R.id.tv_shop_intro_store_desccredit_percent);
        storeServicecreditCreditTv = (TextView) rootView.findViewById(R.id.tv_shop_intro_store_servicecredit_credit);
        storeServicecreditPercentTextTv = (TextView) rootView.findViewById(R.id.tv_shop_intro_store_servicecredit_percent_text);
        storeServicecreditPercentTv = (TextView) rootView.findViewById(R.id.tv_shop_intro_store_servicecredit_percent);
        storeDeliverycreditCreditTv = (TextView) rootView.findViewById(R.id.tv_shop_intro_store_deliverycredit_credit);
        storeDeliverycreditPercentTextTv = (TextView) rootView.findViewById(R.id.tv_shop_intro_store_deliverycredit_percent_text);
        storeDeliverycreditPercentTv = (TextView) rootView.findViewById(R.id.tv_shop_intro_store_deliverycredit_percent);
        storeCompanyNameTv = (TextView) rootView.findViewById(R.id.tv_shop_intro_store_company_name);
        areaInfoTv = (TextView) rootView.findViewById(R.id.tv_shop_intro_area_info);
        storeTimeTextTv = (TextView) rootView.findViewById(R.id.tv_shop_intro_store_time_text);
        storeZyTv = (TextView) rootView.findViewById(R.id.tv_shop_intro_store_zy);
        storePhoneTv = (TextView) rootView.findViewById(R.id.tv_shop_intro_store_phone);
        shopIntroIsFavorateRl = (RelativeLayout) rootView.findViewById(R.id.rl_shop_intro_is_favorate);
    }

    private void initProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initData() {
        key = UserUtils.readLogin(this, true).getKey();
        Intent intent = getIntent();
        if (intent != null) {
            store_id = intent.getStringExtra("store_id");
        }
        okHttpClient = new OkHttpClient();
        storeIntro = new StoreIntro();
    }

    private void setListener() {
        backRl.setOnClickListener(this);
        shopIntroIsFavorateRl.setOnClickListener(this);
    }

    private void loadData() {
        progressDialog.show();
        if (!TextUtils.isEmpty(store_id)) {
            body = new FormEncodingBuilder().add("key", key).add("store_id", store_id).build();
            Request request = new Request.Builder().url(NetConfig.storeIntroUrl).post(body).build();
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
    }

    private boolean parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            if (objBean.optInt("code") == 200) {
                JSONObject objDatas = objBean.optJSONObject("datas");
                JSONObject objStoreInfo = objDatas.optJSONObject("store_info");
                storeIntro.setStoreAvatar(objStoreInfo.optString("store_avatar"));
                storeIntro.setStoreName(objStoreInfo.optString("store_name"));
                storeIntro.setScName(objStoreInfo.optString("sc_name"));
                storeIntro.setFavorite(objStoreInfo.optBoolean("is_favorate"));
                storeIntro.setStoreCollect(objStoreInfo.optInt("store_collect"));
                JSONObject objStoreCredit = objStoreInfo.optJSONObject("store_credit");
                JSONObject objStoreDesccredit = objStoreCredit.optJSONObject("store_desccredit");
                storeIntro.setStoreDesccreditCredit(objStoreDesccredit.optString("credit"));
                storeIntro.setStoreDesccreditPercentText(objStoreDesccredit.optString("percent_text"));
                storeIntro.setStoreDesccreditPercent(objStoreDesccredit.optString("percent"));
                JSONObject objStoreServicecredit = objStoreCredit.optJSONObject("store_servicecredit");
                storeIntro.setStoreServicecreditCredit(objStoreServicecredit.optString("credit"));
                storeIntro.setStoreServicecreditPercentText(objStoreServicecredit.optString("percent_text"));
                storeIntro.setStoreServicecreditPercent(objStoreServicecredit.optString("percent"));
                JSONObject objStoreDeliverycredit = objStoreCredit.optJSONObject("store_deliverycredit");
                storeIntro.setStoreDeliverycreditCredit(objStoreDeliverycredit.optString("credit"));
                storeIntro.setStoreDeliverycreditPercentText(objStoreDeliverycredit.optString("percent_text"));
                storeIntro.setStoreDeliverycreditPercent(objStoreDeliverycredit.optString("percent"));
                storeIntro.setStoreCompanyName(objStoreInfo.optString("store_company_name"));
                storeIntro.setAreaInfo(objStoreInfo.optString("area_info"));
                storeIntro.setStoreTimeText(objStoreInfo.optString("store_time_text"));
                storeIntro.setStoreZy(objStoreInfo.optString("store_zy"));
                storeIntro.setStorePhone(objStoreInfo.optString("store_phone"));
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setView() {
        Picasso.with(this).load(storeIntro.getStoreAvatar()).placeholder(storeAvatarIv.getDrawable()).into(storeAvatarIv);
        storeNameTv.setText(storeIntro.getStoreName());
        scNameTv.setText(storeIntro.getScName());
        isFavorite = storeIntro.isFavorite();
        if (isFavorite) {
            isFavoriteTv.setText("已收藏");
        } else {
            isFavoriteTv.setText("收藏");
        }
        storeCollectTv.setText(storeIntro.getStoreCollect() + "");
        storeDesccreditCreditTv.setText(storeIntro.getStoreDesccreditCredit());
        storeDesccreditPercentTextTv.setText(storeIntro.getStoreDesccreditPercentText());
        storeDesccreditPercentTv.setText(storeIntro.getStoreDesccreditPercent());
        storeServicecreditCreditTv.setText(storeIntro.getStoreServicecreditCredit());
        storeServicecreditPercentTextTv.setText(storeIntro.getStoreServicecreditPercentText());
        storeServicecreditPercentTv.setText(storeIntro.getStoreServicecreditPercent());
        storeDeliverycreditCreditTv.setText(storeIntro.getStoreDeliverycreditCredit());
        storeDeliverycreditPercentTextTv.setText(storeIntro.getStoreDeliverycreditPercentText());
        storeDeliverycreditPercentTv.setText(storeIntro.getStoreDeliverycreditPercent());
        storeCompanyNameTv.setText(storeIntro.getStoreCompanyName());
        areaInfoTv.setText(storeIntro.getAreaInfo());
        storeTimeTextTv.setText(storeIntro.getStoreTimeText());
        storeZyTv.setText(storeIntro.getStoreZy());
        storePhoneTv.setText(storeIntro.getStorePhone());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_shopintro_back:
                finish();
                break;
            case R.id.rl_shop_intro_is_favorate:
                collectClick();
                break;
            default:
                break;
        }
    }

    private void collectClick() {
        if (isFavorite) {
            cancelCollect();
        } else {
            collect();
        }
    }

    private void collect() {
        Request request = new Request.Builder().url(NetConfig.storeIntroCollectUrl).post(body).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful() && parseCollectJson(response.body().string())) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    private void cancelCollect() {
        Request request = new Request.Builder().url(NetConfig.storeIntroCollectCancelUrl).post(body).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful() && parseCollectJson(response.body().string())) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    private boolean parseCollectJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            if (objBean.optInt("code") == 200) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
