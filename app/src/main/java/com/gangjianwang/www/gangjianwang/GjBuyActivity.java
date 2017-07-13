package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import config.ParaConfig;
import utils.ToastUtils;

public class GjBuyActivity extends AppCompatActivity implements View.OnClickListener, AbsListView.OnScrollListener {

    private View rootView;
    private LinearLayout backLl;
    private ProgressDialog progressDialog;
    private ListView lv;
    private WebView wv;
    private GjBuyAdapter gjBuyAdapter;
    private List<GjBuy> gjBuyList;
    private OkHttpClient okHttpClient;
    private int curPage = 1, totalPage;
    private boolean isNext;
    private String keyword;
    private final int LIST = 0;
    private final int WEB = 1;
    private int STATE = LIST;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case 0:
                        ToastUtils.toast(GjBuyActivity.this, ParaConfig.NETWORK_ERROR);
                        break;
                    case 1:
                        break;
                    default:
                        break;
                }
                gjBuyAdapter.notifyDataSetChanged();
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
        rootView = View.inflate(this, R.layout.activity_gj_buy, null);
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
        backLl = (LinearLayout) rootView.findViewById(R.id.ll_gj_buy_back);
        lv = (ListView) rootView.findViewById(R.id.lv_gj_buy);
        wv = (WebView) rootView.findViewById(R.id.wv_gj_buy);
    }

    private void initProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            keyword = intent.getStringExtra("keyword");
            if (keyword.startsWith("h")) {
                STATE = WEB;
            }
        }
        gjBuyList = new ArrayList<>();
        gjBuyAdapter = new GjBuyAdapter(this, gjBuyList);
        okHttpClient = new OkHttpClient();
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new GjBuyWebViewClient());
    }

    private void setData() {
        lv.setAdapter(gjBuyAdapter);
    }

    private void setListener() {
        backLl.setOnClickListener(this);
        lv.setOnScrollListener(this);
    }

    private void loadData() {
        switch (STATE) {
            case LIST:
                progressDialog.show();
                Request request = new Request.Builder().url("http://www.gangjianwang.com/mobile/index.php?act=goods&op=goods_list&keyword=" + keyword + "&page=10&curpage=" + curPage + "&keyword=" + keyword).get().build();
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
                break;
            case WEB:
                lv.setVisibility(View.INVISIBLE);
                wv.setVisibility(View.VISIBLE);
                wv.loadUrl(keyword);
                break;
            default:
                break;
        }
    }

    private boolean parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            if (objBean.optInt("code") == 200) {
                totalPage = objBean.optInt("page_total");
                JSONObject objDatas = objBean.optJSONObject("datas");
                JSONArray arrgoodsList = objDatas.optJSONArray("goods_list");
                for (int i = 0; i < arrgoodsList.length(); i++) {
                    JSONObject o = arrgoodsList.optJSONObject(i);
                    GjBuy gjBuy = new GjBuy();
                    gjBuy.setStoreName(o.optString("store_name"));
                    gjBuy.setGoodsImageUrl(o.optString("goods_image_url"));
                    gjBuy.setGoodsName(o.optString("goods_name"));
                    gjBuy.setGoodsJingle(o.optString("goods_jingle"));
                    gjBuy.setGoodsSalenum(o.optString("goods_salenum"));
                    gjBuy.setGoodsPrice(o.optString("goods_price"));
                    gjBuy.setStoreId(o.optString("store_id"));
                    gjBuy.setGoodsId(o.optString("goods_id"));
                    gjBuyList.add(gjBuy);
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
            case R.id.ll_gj_buy_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isNext && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (curPage < totalPage) {
                curPage++;
                loadData();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        isNext = (firstVisibleItem + visibleItemCount) == totalItemCount;
    }

    private class GjBuyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
