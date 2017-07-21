package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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

import adapter.ChangeCityAdapter;
import bean.City;
import config.NetConfig;
import config.ParaConfig;
import config.PersonConfig;
import customview.LruJsonCache;
import customview.SlideBar;
import utils.ToastUtils;

public class ChangeCityActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private View rootView;
    private RelativeLayout mBackRl;
    private ListView mLv;
    private SlideBar mSb;
    private List<City> mDataList = new ArrayList<>();
    private ChangeCityAdapter mAdapter;
    private String[] lowerLetter;
    private ProgressDialog progressDialog;
    private OkHttpClient okHttpClient;
    private LruJsonCache lruJsonCache;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        ToastUtils.toast(ChangeCityActivity.this, ParaConfig.NETWORK_ERROR);
                        break;
                    case 1:
                        progressDialog.dismiss();
                        mAdapter.notifyDataSetChanged();
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_changecity, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
        loadData();
    }

    private void initView() {
        initRoot();
    }

    private void initRoot() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_changecity_back);
        mLv = (ListView) rootView.findViewById(R.id.lv_changecity);
        mSb = (SlideBar) rootView.findViewById(R.id.sb_changecity);
    }

    private void initData() {
        lowerLetter = getResources().getStringArray(R.array.lowerletter);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        okHttpClient = new OkHttpClient();
        lruJsonCache = LruJsonCache.get(this);
        mAdapter = new ChangeCityAdapter(this, mDataList);
    }

    private void setData() {
        mLv.setAdapter(mAdapter);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mLv.setOnItemClickListener(this);
        mSb.setOnTouchLetterChangeListenner(new SlideBar.OnTouchLetterChangeListenner() {
            @Override
            public void onTouchLetterChange(MotionEvent event, String s) {
                ToastUtils.log(ChangeCityActivity.this, s);
                for (int i = 0; i < mDataList.size(); i++) {
                    if (s.equals(mDataList.get(i).getCityName())) {
                        mLv.setSelection(i);
                    }
                }
            }
        });
    }

    private void loadData() {
        String cacheData = lruJsonCache.getAsString("city");
        ToastUtils.log(this, "cacheData:" + cacheData);
        if (cacheData != null && parseJson(cacheData)) {
            handler.sendEmptyMessage(1);
        } else {
            progressDialog.show();
            Request requestFirst = new Request.Builder().url(NetConfig.cityUrl).get().build();
            okHttpClient.newCall(requestFirst).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    handler.sendEmptyMessage(0);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String cityJson = response.body().string();
                        lruJsonCache.put("city", cityJson, PersonConfig.CITY_CACHE_TIME);
                        if (parseJson(cityJson)) {
                            handler.sendEmptyMessage(1);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_changecity_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("cityName", mDataList.get(position).getCityName());
        setResult(1, intent);
        finish();
    }

    private boolean parseJson(String str) {
        try {
            JSONObject objBean = new JSONObject(str);
            JSONObject objDatas = objBean.optJSONObject("datas");
            City headCity = new City();
            headCity.setCityId("-1");
            headCity.setCityName("全国");
            mDataList.add(headCity);
            for (int i = 0; i < lowerLetter.length; i++) {
                JSONObject obj = objDatas.optJSONObject(lowerLetter[i]);
                if (obj != null) {
                    String upperLetter = obj.optString("short_name");
                    City city = new City();
                    city.setCityId("0");
                    city.setCityName(upperLetter);
                    mDataList.add(city);
                    JSONArray arr = obj.optJSONArray("cities");
                    for (int j = 0; j < arr.length(); j++) {
                        JSONObject o = arr.optJSONObject(j);
                        String cityId = o.optString("area_id");
                        String cityName = o.optString("area_name");
                        City arrCity = new City();
                        arrCity.setCityId(cityId);
                        arrCity.setCityName(cityName);
                        mDataList.add(arrCity);
                    }
                }
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
