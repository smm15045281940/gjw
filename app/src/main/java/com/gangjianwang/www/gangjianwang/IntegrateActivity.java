package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
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
import config.ParaConfig;
import utils.ToastUtils;
import utils.UserUtils;

public class IntegrateActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout mBackRl;
    private TextView pointsTv;
    private ListView lv;
    private ProgressDialog progressDialog;
    private OkHttpClient okHttpClient;
    private List<Integrate> integrateList = new ArrayList<>();
    private IntegrateAdapter integrateAdapter;
    private String point;
    private boolean isLogined;
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
                        ToastUtils.toast(IntegrateActivity.this, ParaConfig.NETWORK_ERROR);
                        break;
                    case 1:
                        pointsTv.setText(point);
                        break;
                    case 2:
                        progressDialog.dismiss();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
        handler.removeMessages(1);
        handler.removeMessages(2);
    }

    private void initView() {
        initRoot();
    }

    private void initRoot() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_integrate_back);
        pointsTv = (TextView) rootView.findViewById(R.id.tv_integrate_point);
        lv = (ListView) rootView.findViewById(R.id.lv_integrate);
    }

    private void initData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
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
                        parseJsonNum(response.body().string());
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
                        parseJson(response.body().string());
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                }
            });
        }
    }

    private void parseJsonNum(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            JSONObject objDatas = objBean.optJSONObject("datas");
            point = objDatas.optString("point");
            handler.sendEmptyMessage(1);
            return;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(0);
    }

    private void parseJson(String json) {
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
            handler.sendEmptyMessage(2);
            return;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(0);
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
}
