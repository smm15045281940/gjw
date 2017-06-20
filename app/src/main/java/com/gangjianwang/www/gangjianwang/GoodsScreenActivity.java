package com.gangjianwang.www.gangjianwang;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

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

import adapter.AddressCityAdapter;
import adapter.AddressSpinnerAdapter;
import bean.Province;
import config.NetConfig;
import utils.ToastUtils;

public class GoodsScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout mBackRl, mResetRl, mScreenRl;
    private EditText mMinPriceEt, mMaxPriceEt;
    private Spinner mProvinceSp, mCitySp;
    private CheckBox cbDonation, cbGroupBuy, cbLimitDiscount, cbVirtual;
    private CheckBox cbPlatformSelfSupport;
    private CheckBox cbSevenDayReturn, cbQualityPromise, cbDamageReplace, cbQuickFlow;
    private List<Province> mProvinceList = new ArrayList<>();
    private List<String> mCityList = new ArrayList<>();
    private AddressSpinnerAdapter mProvinceAdapter;
    private AddressCityAdapter mCityAdapter;
    private OkHttpClient okHttpClient;
    private String priceSection;
    private String goodsAddress;
    private String goodsType;
    private String storeType;
    private String storeService;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        ToastUtils.toast(GoodsScreenActivity.this, "无网络");
                        break;
                    case 1:
                        mProvinceAdapter.notifyDataSetChanged();
                        mCityAdapter.notifyDataSetChanged();
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
        rootView = View.inflate(this, R.layout.activity_goods_screen, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
        loadData();
    }

    private void initView() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_goodsScreen_back);
        mResetRl = (RelativeLayout) rootView.findViewById(R.id.rl_goodsScreen_reset);
        mScreenRl = (RelativeLayout) rootView.findViewById(R.id.rl_goodsScreen_screen);
        mMinPriceEt = (EditText) rootView.findViewById(R.id.et_goodsScreen_minPrice);
        mMaxPriceEt = (EditText) rootView.findViewById(R.id.et_goodsScreen_maxPrice);
        mProvinceSp = (Spinner) rootView.findViewById(R.id.sp_goodsScreen_province);
        mCitySp = (Spinner) rootView.findViewById(R.id.sp_goodsScreen_city);
        cbDonation = (CheckBox) rootView.findViewById(R.id.cb_goods_screen_donation);
        cbGroupBuy = (CheckBox) rootView.findViewById(R.id.cb_goods_screen_groupbuy);
        cbLimitDiscount = (CheckBox) rootView.findViewById(R.id.cb_goods_screen_limitdiscount);
        cbVirtual = (CheckBox) rootView.findViewById(R.id.cb_goods_screen_virtual);
        cbPlatformSelfSupport = (CheckBox) rootView.findViewById(R.id.cb_goods_screen_platformselfsupport);
        cbSevenDayReturn = (CheckBox) rootView.findViewById(R.id.cb_goods_screen_sevendayreturn);
        cbQualityPromise = (CheckBox) rootView.findViewById(R.id.cb_goods_screen_qualitypromise);
        cbDamageReplace = (CheckBox) rootView.findViewById(R.id.cb_goods_screen_damagereplace);
        cbQuickFlow = (CheckBox) rootView.findViewById(R.id.cb_goods_screen_quickflow);
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
        mProvinceAdapter = new AddressSpinnerAdapter(this, mProvinceList);
        mCityAdapter = new AddressCityAdapter(this, mCityList);
    }

    private void setData() {
        mProvinceSp.setAdapter(mProvinceAdapter);
        mCitySp.setAdapter(mCityAdapter);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mResetRl.setOnClickListener(this);
        mScreenRl.setOnClickListener(this);
    }

    private void loadData() {
        Province province = new Province();
        province.setId("0");
        province.setName("不限");
        mProvinceList.add(province);
        mCityList.add("不限");
        Request request = new Request.Builder().url(NetConfig.provinceListUrl).get().build();
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
            JSONArray arrAreaList = objDatas.optJSONArray("area_list");
            for (int i = 0; i < arrAreaList.length(); i++) {
                JSONObject o = arrAreaList.optJSONObject(i);
                Province province = new Province();
                province.setId(o.optString("area_id"));
                province.setName(o.optString("area_name"));
                mProvinceList.add(province);
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void screen() {
        priceSection = "价格区间：" + mMinPriceEt.getText().toString() + "-" + mMaxPriceEt.getText().toString();
        goodsAddress = "商品所在地：" + ((Province) mProvinceSp.getSelectedItem()).getName() + " " + mCitySp.getSelectedItem().toString();
        StringBuilder sbGoodsType = new StringBuilder();
        if (cbDonation.isChecked()) {
            sbGoodsType.append(" 赠品");
        }
        if (cbGroupBuy.isChecked()) {
            sbGoodsType.append(" 团购");
        }
        if (cbLimitDiscount.isChecked()) {
            sbGoodsType.append(" 限时折扣");
        }
        if (cbVirtual.isChecked()) {
            sbGoodsType.append(" 虚拟");
        }
        goodsType = "商品类型：" + sbGoodsType.toString();
        StringBuilder sbStoreType = new StringBuilder();
        if (cbPlatformSelfSupport.isChecked()) {
            sbStoreType.append(" 平台自营");
        }
        storeType = "店铺类型：" + sbStoreType.toString();
        StringBuilder sbStoreService = new StringBuilder();
        if (cbSevenDayReturn.isChecked()) {
            sbStoreService.append(" 7天退货");
        }
        if (cbQualityPromise.isChecked()) {
            sbStoreService.append(" 品质承诺");
        }
        if (cbDamageReplace.isChecked()) {
            sbStoreService.append(" 破损补寄");
        }
        if (cbQuickFlow.isChecked()) {
            sbStoreService.append(" 急速物流");
        }
        storeService = "店铺服务：" + sbStoreService.toString();
        ToastUtils.toast(this, priceSection + "\n" + goodsAddress + "\n" + goodsType + "\n" + storeType + "\n" + storeService);
    }

    private void reset() {
        mMinPriceEt.setText("");
        mMaxPriceEt.setText("");
        mProvinceSp.setSelection(0);
        mCitySp.setSelection(0);
        cbDonation.setChecked(false);
        cbGroupBuy.setChecked(false);
        cbLimitDiscount.setChecked(false);
        cbVirtual.setChecked(false);
        cbPlatformSelfSupport.setChecked(false);
        cbSevenDayReturn.setChecked(false);
        cbQualityPromise.setChecked(false);
        cbDamageReplace.setChecked(false);
        cbQuickFlow.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_goodsScreen_back:
                finish();
                break;
            case R.id.rl_goodsScreen_reset:
                reset();
                break;
            case R.id.rl_goodsScreen_screen:
                screen();
                break;
            default:
                break;
        }
    }
}
