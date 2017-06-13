package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import adapter.ChangeCityAdapter;
import bean.City;
import config.NetConfig;
import customview.SlideBar;
import utils.ToastUtils;

public class ChangecityActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private View rootView;
    private RelativeLayout mBackRl;
    private ListView mLv;
    private SlideBar mSb;
    private List<City> mDataList;
    private ChangeCityAdapter mAdapter;
    private String[] lowerLetter;
    private ProgressDialog mPd;

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
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_changecity_back);
        mLv = (ListView) rootView.findViewById(R.id.lv_changecity);
        mSb = (SlideBar) rootView.findViewById(R.id.sb_changecity);
    }

    private void initData() {
        mDataList = new ArrayList<>();
        lowerLetter = getResources().getStringArray(R.array.lowerletter);
        mPd = new ProgressDialog(this);
        mPd.setMessage("加载中..");
    }

    private void setData() {
        mAdapter = new ChangeCityAdapter(this, mDataList);
        mLv.setAdapter(mAdapter);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mLv.setOnItemClickListener(this);
        mSb.setOnTouchLetterChangeListenner(new SlideBar.OnTouchLetterChangeListenner() {
            @Override
            public void onTouchLetterChange(MotionEvent event, String s) {
                for (int i = 0; i < mDataList.size(); i++) {
                    if (s.equals(mDataList.get(i).getCityName())) {
                        mLv.setSelection(i);
                    }
                }
            }
        });
    }

    private void loadData() {
        mPd.show();
        x.http().get(new RequestParams(NetConfig.cityUrl), new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                mPd.dismiss();
                if (result == null) {
                    ToastUtils.toast(ChangecityActivity.this, "无数据");
                } else {
                    parseJson(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mPd.dismiss();
                ToastUtils.toast(ChangecityActivity.this, "无网络");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                mPd.dismiss();
            }

            @Override
            public void onFinished() {
                mPd.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_changecity_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("cityName", mDataList.get(position).getCityName());
        ToastUtils.toast(ChangecityActivity.this, mDataList.get(position).getCityId() + "\n" + mDataList.get(position).getCityName());
        setResult(1, intent);
        finish();
    }

    private void parseJson(String str) {
        try {
            JSONObject objBean = new JSONObject(str);
            JSONObject objDatas = objBean.optJSONObject("datas");
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
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtils.toast(ChangecityActivity.this, "解析数据异常");
        }
    }

}
