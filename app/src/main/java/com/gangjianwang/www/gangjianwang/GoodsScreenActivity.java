package com.gangjianwang.www.gangjianwang;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
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
    private RelativeLayout mBackRl, mResetRl;
    private Spinner mProvinceSp, mCitySp;
    private List<Province> mProvinceList = new ArrayList<>();
    private List<String> mCityList = new ArrayList<>();
    private AddressSpinnerAdapter mProvinceAdapter;
    private AddressCityAdapter mCityAdapter;
    private OkHttpClient okHttpClient;

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
        mProvinceSp = (Spinner) rootView.findViewById(R.id.sp_goodsScreen_province);
        mCitySp = (Spinner) rootView.findViewById(R.id.sp_goodsScreen_city);
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
        mProvinceAdapter = new AddressSpinnerAdapter(this, mProvinceList);
//        mCityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mCityList);
        //TODO
    }

    private void setData() {
        mProvinceSp.setAdapter(mProvinceAdapter);
        mCitySp.setAdapter(mCityAdapter);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mResetRl.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_goodsScreen_back:
                finish();
                break;
            case R.id.rl_goodsScreen_reset:
                ToastUtils.toast(GoodsScreenActivity.this, "重置");
                break;
            default:
                break;
        }
    }
}
