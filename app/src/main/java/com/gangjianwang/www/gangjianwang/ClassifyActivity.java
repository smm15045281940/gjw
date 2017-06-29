package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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

import adapter.ClassifyLeftAdapter;
import adapter.ClassifyRightAdapter;
import bean.ClassifyLeft;
import bean.ClassifyRightInner;
import bean.ClassifyRightOuter;
import config.NetConfig;
import utils.ToastUtils;

public class ClassifyActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, ListItemClickHelp {

    private View rootView;
    private RelativeLayout mBackRl;
    private ListView mLeftLv, mRightLv;
    private List<ClassifyLeft> mLeftDataList = new ArrayList<>();
    private List<ClassifyRightOuter> mRightDataList = new ArrayList<>();
    private ClassifyLeftAdapter mLeftAdapter;
    private ClassifyRightAdapter mRightAdapter;
    private OkHttpClient okHttpClient;
    private ProgressDialog mPd;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        mPd.dismiss();
                        ToastUtils.toast(ClassifyActivity.this, "无网络");
                        break;
                    case 1:
                        mLeftAdapter.notifyDataSetChanged();
                        loadRightData("1");
                        break;
                    case 2:
                        mPd.dismiss();
                        mRightAdapter.notifyDataSetChanged();
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
        rootView = View.inflate(this, R.layout.activity_classify, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
        loadLeftData();
    }

    private void initView() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_classify_back);
        mLeftLv = (ListView) rootView.findViewById(R.id.lv_classify_left);
        mRightLv = (ListView) rootView.findViewById(R.id.lv_classify_right);
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
        mPd = new ProgressDialog(this);
        mPd.setMessage("加载中..");
        mLeftAdapter = new ClassifyLeftAdapter(this, mLeftDataList);
        mRightAdapter = new ClassifyRightAdapter(this, mRightDataList, this);
    }

    private void setData() {
        mLeftLv.setAdapter(mLeftAdapter);
        mRightLv.setAdapter(mRightAdapter);
    }

    private void loadLeftData() {
        mPd.show();
        Request request = new Request.Builder().url(NetConfig.classifyLeftUrl).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    if (parseLeftJson(json))
                        handler.sendEmptyMessage(1);
                }
            }
        });
    }

    private void loadRightData(String leftId) {
        mPd.show();
        Request request = new Request.Builder().url(NetConfig.classifyRightUrl + leftId).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    mRightDataList.clear();
                    if (parseRightJson(json))
                        handler.sendEmptyMessage(2);
                }
            }
        });
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mLeftLv.setOnItemClickListener(this);
    }

    private boolean parseLeftJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            JSONObject objDatas = objBean.optJSONObject("datas");
            JSONArray arrClassList = objDatas.optJSONArray("class_list");
            for (int i = 0; i < arrClassList.length(); i++) {
                JSONObject o = arrClassList.optJSONObject(i);
                ClassifyLeft classifyLeft = new ClassifyLeft();
                classifyLeft.setId(o.optString("gc_id"));
                classifyLeft.setTitle(o.optString("gc_name"));
                classifyLeft.setImgUrl(o.optString("image"));
                mLeftDataList.add(classifyLeft);
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean parseRightJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            JSONObject objDatas = objBean.optJSONObject("datas");
            JSONArray arrClassList = objDatas.optJSONArray("class_list");
            for (int i = 0; i < arrClassList.length(); i++) {
                JSONObject o = arrClassList.optJSONObject(i);
                ClassifyRightOuter c = new ClassifyRightOuter();
                c.setId(o.optString("gc_id"));
                c.setName(o.optString("gc_name"));
                List<ClassifyRightInner> l = new ArrayList<>();
                JSONArray a = o.optJSONArray("child");
                for (int j = 0; j < a.length(); j++) {
                    JSONObject oo = a.optJSONObject(j);
                    ClassifyRightInner cc = new ClassifyRightInner();
                    cc.setId(oo.optString("gc_id"));
                    cc.setName(oo.optString("gc_name"));
                    l.add(cc);
                }
                c.setList(l);
                mRightDataList.add(c);
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
            case R.id.rl_classify_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        loadRightData(mLeftDataList.get(position).getId());
    }

    @Override
    public void onClick(View item, View widget, int position, int which, boolean isChecked) {
        switch (which) {
            case R.id.ll_item_classify_right_outer:
                Intent intent = new Intent(ClassifyActivity.this, ContractProjectActivity.class);
                intent.putExtra("gc_id", mRightDataList.get(position).getId());
                startActivity(intent);
                break;
        }
    }
}
