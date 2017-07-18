package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
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

import adapter.GjBuyAdapter;
import bean.GjBuy;
import config.NetConfig;
import config.ParaConfig;
import utils.ToastUtils;

public class ClassifyDetailActivity extends AppCompatActivity implements View.OnClickListener, AbsListView.OnScrollListener {

    private View rootView;
    private RelativeLayout backRl;
    private ListView lv;
    private GjBuyAdapter adapter;
    private List<GjBuy> list;
    private ProgressDialog progressDialog;
    private OkHttpClient okHttpClient;
    private String gc_id, store_id;
    private int curPage = 1, totalPage;
    private boolean isLast;
    private int LOAD_STATE;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case 0:
                        ToastUtils.toast(ClassifyDetailActivity.this, ParaConfig.NETWORK_ERROR);
                        break;
                    case 1:
                        break;
                }
                adapter.notifyDataSetChanged();
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
        rootView = View.inflate(this, R.layout.activity_classify_detail, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
        loadData();
    }

    private void initView() {
        initRoot();
        initProgress();
    }

    private void initRoot() {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_classify_detail_back);
        lv = (ListView) rootView.findViewById(R.id.lv_classify_detail);
    }

    private void initProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initData() {
        LOAD_STATE = ParaConfig.FIRST;
        Intent intent = getIntent();
        if (intent != null) {
            gc_id = intent.getStringExtra("gc_id");
            store_id = intent.getStringExtra("store_id");
        }
        list = new ArrayList<>();
        okHttpClient = new OkHttpClient();
        adapter = new GjBuyAdapter(this, list);
    }

    private void setData() {
        lv.setAdapter(adapter);
    }

    private void setListener() {
        backRl.setOnClickListener(this);
    }

    private void loadData() {
        if (!TextUtils.isEmpty(gc_id) && !TextUtils.isEmpty(store_id)) {
            if (LOAD_STATE == ParaConfig.FIRST) {
                progressDialog.show();
            }
            Request request = new Request.Builder().url(NetConfig.classifyDetailUrl1 + gc_id + NetConfig.classifyDetailUrl2 + store_id + NetConfig.classifyDetailUrl3 + curPage + NetConfig.classifyDetailUrl4 + gc_id).build();
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
                totalPage = objBean.optInt("page_total");
                JSONObject objDatas = objBean.optJSONObject("datas");
                JSONArray arrGoodsList = objDatas.optJSONArray("goods_list");
                for (int i = 0; i < arrGoodsList.length(); i++) {
                    JSONObject o = arrGoodsList.optJSONObject(i);
                    GjBuy gjBuy = new GjBuy();
                    gjBuy.setGoodsId(o.optString("goods_id"));
                    gjBuy.setStoreId(o.optString("store_id"));
                    gjBuy.setGoodsPrice(o.optString("goods_price"));
                    gjBuy.setGoodsSalenum(o.optString("goods_salenum"));
                    gjBuy.setGoodsImageUrl(o.optString("goods_image_url"));
                    gjBuy.setGoodsJingle(o.optString("goods_jingle"));
                    gjBuy.setGoodsName(o.optString("goods_name"));
                    gjBuy.setStoreName(o.optString("store_name"));
                    list.add(gjBuy);
                }
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_classify_detail_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isLast && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (curPage < totalPage) {
                curPage++;
                LOAD_STATE = ParaConfig.LOAD;
                loadData();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        isLast = (firstVisibleItem + visibleItemCount) == totalItemCount;
    }
}
