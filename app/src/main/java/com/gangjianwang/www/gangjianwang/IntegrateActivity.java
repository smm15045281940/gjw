package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ListView;
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

import adapter.IntegrateAdapter;
import bean.Integrate;
import config.NetConfig;
import utils.ToastUtils;
import utils.UserUtils;

public class IntegrateActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    private View rootView;
    private RelativeLayout mBackRl;
    private SwipeRefreshLayout srl;
    private TextView pointsTv;
    private ListView lv;
    private ProgressDialog progressDialog;
    private OkHttpClient okHttpClient;
    private List<Integrate> integrateList = new ArrayList<>();
    private IntegrateAdapter integrateAdapter;
    private String point;
    private boolean isLogined, isLast;
    private String key;
    private int curPage = 1;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        progressDialog.dismiss();
                        ToastUtils.toast(IntegrateActivity.this, "无网络");
                        break;
                    case 1:
                        pointsTv.setText(point);
                        break;
                    case 2:
                        progressDialog.dismiss();
                        integrateAdapter.notifyDataSetChanged();
                        break;
                    case 3:
                        srl.setRefreshing(false);
                        integrateAdapter.notifyDataSetChanged();
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
        rootView = View.inflate(this, R.layout.activity_integrate, null);
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
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_integrate_back);
        srl = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_integrate);
        pointsTv = (TextView) rootView.findViewById(R.id.tv_integrate_point);
        lv = (ListView) rootView.findViewById(R.id.lv_integrate);
    }

    private void initData() {
        srl.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        progressDialog = new ProgressDialog(this);
        okHttpClient = new OkHttpClient();
        integrateAdapter = new IntegrateAdapter(this, integrateList);
        isLogined = UserUtils.isLogined(this);
        if (isLogined) {
            key = UserUtils.readLogin(this, isLogined).getKey();
        }
    }

    private void setData() {
        lv.setAdapter(integrateAdapter);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        srl.setOnRefreshListener(this);
        lv.setOnScrollListener(this);
    }

    private void loadData() {
        if (isLogined) {
            progressDialog.show();
            Request requestNum = new Request.Builder().url(NetConfig.integrateNumHeadUrl + key + NetConfig.integrateNumFootUrl).get().build();
            okHttpClient.newCall(requestNum).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    handler.sendEmptyMessage(0);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        if (parseJsonNum(response.body().string())) {
                            handler.sendEmptyMessage(1);
                        } else {
                            handler.sendEmptyMessage(0);
                        }
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                }
            });
            Request request = new Request.Builder().url(NetConfig.integrateHeadUrl + key + NetConfig.integrateFootPageHeadUrl + curPage + NetConfig.integrateFootPageFootUrl).get().build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    handler.sendEmptyMessage(0);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
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

    private boolean parseJsonNum(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            JSONObject objDatas = objBean.optJSONObject("datas");
            point = objDatas.optString("point");
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            JSONObject objDatas = objBean.optJSONObject("datas");
            JSONArray arrLogList = objDatas.optJSONArray("log_list");
            for (int i = 0; i < arrLogList.length(); i++) {
                Integrate integrate = new Integrate();
                JSONObject o = arrLogList.optJSONObject(i);
                integrate.setStageText(o.optString("stagetext"));
                integrate.setPlDesc(o.optString("pl_desc"));
                integrate.setPlPoints(o.optString("pl_points"));
                integrate.setAddTimeText(o.optString("addtimetext"));
                integrateList.add(integrate);
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
            case R.id.rl_integrate_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        curPage = 1;
        Request requestNum = new Request.Builder().url(NetConfig.integrateNumHeadUrl + key + NetConfig.integrateNumFootUrl).get().build();
        okHttpClient.newCall(requestNum).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (parseJsonNum(response.body().string())) {
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
        Request request = new Request.Builder().url(NetConfig.integrateHeadUrl + key + NetConfig.integrateFootPageHeadUrl + curPage + NetConfig.integrateFootPageFootUrl).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    integrateList.clear();
                    if (parseJson(response.body().string())) {
                        handler.sendEmptyMessage(3);
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isLast && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            curPage++;
            if (curPage == 4) {
                ToastUtils.toast(IntegrateActivity.this, "到底了");
                curPage = 3;
            } else {
                load(curPage);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        isLast = (firstVisibleItem + visibleItemCount == totalItemCount);
    }

    private void load(int curPage) {
        progressDialog.show();
        Request request = new Request.Builder().url(NetConfig.integrateHeadUrl + key + NetConfig.integrateFootPageHeadUrl + curPage + NetConfig.integrateFootPageFootUrl).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
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
