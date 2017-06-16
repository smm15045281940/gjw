package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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
import customview.MyRefreshListView;
import customview.OnRefreshListener;
import customview.SlideBar;
import utils.ToastUtils;

public class ChangeCityActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, OnRefreshListener {

    private View rootView, headView, footView;
    private RelativeLayout mBackRl;
    private MyRefreshListView mLv;
    private SlideBar mSb;
    private List<City> mDataList = new ArrayList<>();
    private ChangeCityAdapter mAdapter;
    private String[] lowerLetter;
    private ProgressDialog mPd;
    private Handler handler;
    private OkHttpClient okHttpClient;
    private final int LOAD_FIRST = 1;
    private final int LOAD_REFRESH = 2;

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
        loadData(LOAD_FIRST);
    }

    private void initView() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_changecity_back);
        mLv = (MyRefreshListView) rootView.findViewById(R.id.lv_changecity);
        mSb = (SlideBar) rootView.findViewById(R.id.sb_changecity);
        headView = View.inflate(ChangeCityActivity.this, R.layout.head_changecity, null);
        footView = View.inflate(ChangeCityActivity.this, R.layout.foot_changecity, null);
    }

    private void initData() {
        lowerLetter = getResources().getStringArray(R.array.lowerletter);
        mPd = new ProgressDialog(this);
        mPd.setMessage("加载中..");
        handler = new Handler();
        okHttpClient = new OkHttpClient();
    }

    private void setData() {
        mAdapter = new ChangeCityAdapter(this, mDataList);
        mLv.setAdapter(mAdapter);
        mLv.addHeaderView(headView);
        mLv.addFooterView(footView);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mLv.setOnItemClickListener(this);
        mLv.setOnRefreshListener(this);
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

    private void loadData(int LOAD_STATE) {
        switch (LOAD_STATE) {
            case LOAD_FIRST:
                headView.setVisibility(View.GONE);
                footView.setVisibility(View.GONE);
                mPd.show();
                Request requestFirst = new Request.Builder().url(NetConfig.cityUrl).get().build();
                okHttpClient.newCall(requestFirst).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                handler.post(runnableNoNet);
                            }
                        }.start();
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String json = response.body().string();
                            parseJson(json);
                        }
                    }
                });
                break;
            case LOAD_REFRESH:
                Request requestRefresh = new Request.Builder().url(NetConfig.cityUrl).get().build();
                okHttpClient.newCall(requestRefresh).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                handler.post(runnableLvnoNet);
                            }
                        }.start();
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            mDataList.clear();
                            String json = response.body().string();
                            parseJson(json);
                        } else {
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    handler.post(runnableCxc);
                                }
                            }.start();
                        }
                    }
                });
                break;
            default:
                break;
        }
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
        if (position == 1) {
            intent.putExtra("cityName", "全国");
        } else {
            intent.putExtra("cityName", mDataList.get(position - 2).getCityName());
        }
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
            new Thread() {
                @Override
                public void run() {
                    handler.post(runnableFirst);
                }
            }.start();
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtils.toast(ChangeCityActivity.this, "解析数据异常");
        }
    }

    Runnable runnableNoNet = new Runnable() {
        @Override
        public void run() {
            mPd.dismiss();
            ToastUtils.toast(ChangeCityActivity.this, "无网络");
        }
    };

    Runnable runnableLvnoNet = new Runnable() {
        @Override
        public void run() {
            mLv.hideHeadView();
            ToastUtils.toast(ChangeCityActivity.this, "无网络");
        }
    };

    Runnable runnableCxc = new Runnable() {
        @Override
        public void run() {
            mLv.hideHeadView();
            ToastUtils.toast(ChangeCityActivity.this, "出小差了");
        }
    };

    Runnable runnableFirst = new Runnable() {
        @Override
        public void run() {
            mPd.dismiss();
            headView.setVisibility(View.VISIBLE);
            footView.setVisibility(View.VISIBLE);
            mLv.hideHeadView();
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onDownPullRefresh() {
        loadData(LOAD_REFRESH);
    }

    @Override
    public void onLoadingMore() {
        mLv.hideFootView();
    }
}
