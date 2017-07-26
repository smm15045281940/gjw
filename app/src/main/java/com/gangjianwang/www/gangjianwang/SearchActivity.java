package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import config.NetConfig;
import config.ParaConfig;
import customview.FlowLayout;
import utils.ToastUtils;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout mBackRl;
    private EditText mSearchEt;
    private ImageView mContentcloseIv;
    private TextView hotTv, hisTv;
    private FlowLayout hotFl, hisFl;
    private Button clearHisBtn;

    private ViewGroup.MarginLayoutParams lp;
    private OkHttpClient okHttpClient;
    private ProgressDialog progressDialog;
    private String[] hotArr, hisArr;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        ToastUtils.toast(SearchActivity.this, ParaConfig.NETWORK_ERROR);
                        break;
                    case 1:
                        progressDialog.dismiss();
                        setView();
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
        rootView = View.inflate(this, R.layout.activity_search, null);
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
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_search_back);
        mContentcloseIv = (ImageView) rootView.findViewById(R.id.iv_search_contentclose);
        mSearchEt = (EditText) rootView.findViewById(R.id.et_search_search);
        hotTv = (TextView) rootView.findViewById(R.id.tv_search_hot);
        hisTv = (TextView) rootView.findViewById(R.id.tv_search_his);
        hotFl = (FlowLayout) rootView.findViewById(R.id.fl_search_hot);
        hisFl = (FlowLayout) rootView.findViewById(R.id.fl_search_history);
        clearHisBtn = (Button) rootView.findViewById(R.id.btn_search_clear_his);
    }

    private void initProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
        lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mSearchEt.addTextChangedListener(mSearchEtTextWatcher);
        mSearchEt.setOnClickListener(this);
        mContentcloseIv.setOnClickListener(this);
    }

    private void loadData() {
        progressDialog.show();
        Request request = new Request.Builder().url(NetConfig.searchLogUrl).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (parseJson(response.body().string())) {
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

    private boolean parseJson(String json) {
        boolean b = false;
        try {
            JSONObject objBean = new JSONObject(json);
            if (objBean.optInt("code") == 200) {
                JSONObject objDatas = objBean.optJSONObject("datas");
                JSONArray arrHot = objDatas.optJSONArray("list");
                if (arrHot != null && arrHot.length() != 0) {
                    hotArr = new String[arrHot.length()];
                    for (int i = 0; i < arrHot.length(); i++) {
                        hotArr[i] = arrHot.optString(i);
                    }
                }
                JSONArray arrHis = objDatas.optJSONArray("his_list");
                if (arrHis != null && arrHis.length() != 0) {
                    hisArr = new String[arrHis.length()];
                    for (int i = 0; i < arrHis.length(); i++) {
                        hisArr[i] = arrHis.optString(i);
                    }
                }
                b = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return b;
    }

    private void setView() {
        if (hotArr != null && hotArr.length != 0) {
            for (int i = 0; i < hotArr.length; i++) {
                TextView tv = new TextView(SearchActivity.this);
                tv.setBackgroundResource(R.drawable.shape_search_hot);
                tv.setPadding(20, 20, 20, 20);
                tv.setTextSize(15);
                tv.setTextColor(Color.BLACK);
                tv.setText(hotArr[i]);
                hotFl.addView(tv, lp);
            }
            hotTv.setVisibility(View.VISIBLE);
            hotFl.setVisibility(View.VISIBLE);
        } else {
            hotTv.setVisibility(View.GONE);
            hotFl.setVisibility(View.GONE);
        }
        if (hisArr != null && hisArr.length != 0) {
            for (int i = 0; i < hisArr.length; i++) {
                TextView tv = new TextView(SearchActivity.this);
                tv.setBackgroundResource(R.drawable.shape_search_his);
                tv.setPadding(20, 20, 20, 20);
                tv.setTextSize(15);
                tv.setTextColor(Color.BLACK);
                tv.setText(hisArr[i]);
                hisFl.addView(tv, lp);
            }
            hisTv.setVisibility(View.VISIBLE);
            hisFl.setVisibility(View.VISIBLE);
            clearHisBtn.setVisibility(View.VISIBLE);
        } else {
            hisTv.setVisibility(View.GONE);
            hisFl.setVisibility(View.GONE);
            clearHisBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_search_back:
                finish();
                break;
            case R.id.iv_search_contentclose:
                mSearchEt.setText("");
                break;
        }
    }

    TextWatcher mSearchEtTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                mContentcloseIv.setVisibility(View.INVISIBLE);
            } else {
                mContentcloseIv.setVisibility(View.VISIBLE);
            }
        }
    };
}
