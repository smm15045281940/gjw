package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.InquiryAdapter;
import bean.Goods;
import bean.Quotation;
import config.NetConfig;
import config.ParaConfig;
import utils.ToastUtils;
import utils.UserUtils;

public class InquiryActivity extends AppCompatActivity implements View.OnClickListener, AbsListView.OnScrollListener, ListItemClickHelp {

    private View rootView, emptyView;
    private RelativeLayout mBackRl;

    private LinearLayout allLl, unmodifiedLl, modifiedLl, unorderedLl, orderedLl;
    private TextView allTv, unmodifiedTv, modifiedTv, unorderedTv, orderedTv;
    private View allV, unmodifiedV, modifiedV, unorderedV, orderedV;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;
    private ListView lv;
    private InquiryAdapter adapter;
    private List<Quotation> list;
    private OkHttpClient okHttpClient;
    private String key, stateType = "";
    private int curPosition = 0, tarPosition = -1;
    private int curPage = 1, totalPage;
    private boolean isLast;
    private String cartId, quotationId, ifCart, addressId, storeId;
    private int delPosition;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case 0:
                        ToastUtils.toast(InquiryActivity.this, ParaConfig.NETWORK_ERROR);
                        break;
                    case 1:
                        break;
                    case 2:
                        list.remove(delPosition);
                        ToastUtils.toast(InquiryActivity.this, ParaConfig.DELETE_SUCCESS);
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
        handler.removeMessages(2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_inquiry, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
        loadData();
    }

    private void initView() {
        initRoot();
        initEmpty();
        initPro();
        initDia();
    }

    private void initRoot() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_inquirylist_back);
        allLl = (LinearLayout) rootView.findViewById(R.id.ll_inquiry_all);
        unmodifiedLl = (LinearLayout) rootView.findViewById(R.id.ll_inquiry_unmodified);
        modifiedLl = (LinearLayout) rootView.findViewById(R.id.ll_inquiry_modified);
        unorderedLl = (LinearLayout) rootView.findViewById(R.id.ll_inquiry_unordered);
        orderedLl = (LinearLayout) rootView.findViewById(R.id.ll_inquiry_ordered);
        allTv = (TextView) rootView.findViewById(R.id.tv_inquiry_all);
        unmodifiedTv = (TextView) rootView.findViewById(R.id.tv_inquiry_unmodified);
        modifiedTv = (TextView) rootView.findViewById(R.id.tv_inquiry_modified);
        unorderedTv = (TextView) rootView.findViewById(R.id.tv_inquiry_unordered);
        orderedTv = (TextView) rootView.findViewById(R.id.tv_inquiry_ordered);
        allV = rootView.findViewById(R.id.v_inquiry_all);
        unmodifiedV = rootView.findViewById(R.id.v_inquiry_unmodified);
        modifiedV = rootView.findViewById(R.id.v_inquiry_modified);
        unorderedV = rootView.findViewById(R.id.v_inquiry_unordered);
        orderedV = rootView.findViewById(R.id.v_inquiry_ordered);
        lv = (ListView) rootView.findViewById(R.id.lv_inquiry);
        allTv.setTextColor(Color.RED);
    }

    private void initEmpty() {
        emptyView = View.inflate(this, R.layout.empty, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ImageView) emptyView.findViewById(R.id.iv_empty_icon)).setImageResource(R.mipmap.empty_store_offer);
        ((TextView) emptyView.findViewById(R.id.tv_empty_hint)).setText("您还没有相关的询价单");
        ((TextView) emptyView.findViewById(R.id.tv_empty_content)).setText("可以去看看哪些想要买的");
        ((TextView) emptyView.findViewById(R.id.tv_empty_click)).setText("随便逛逛");
        (emptyView.findViewById(R.id.tv_empty_click)).setVisibility(View.VISIBLE);
        ((ViewGroup) lv.getParent()).addView(emptyView);
        lv.setEmptyView(emptyView);
    }

    private void initPro() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initDia() {
        alertDialog = new AlertDialog.Builder(this).setMessage("确定删除？").setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                delete();
            }
        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        }).create();
    }

    private void initData() {
        list = new ArrayList<>();
        adapter = new InquiryAdapter(this, list, this);
        okHttpClient = new OkHttpClient();
        key = UserUtils.readLogin(this, UserUtils.isLogined(this)).getKey();
    }

    private void setData() {
        lv.setAdapter(adapter);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        allLl.setOnClickListener(this);
        unmodifiedLl.setOnClickListener(this);
        modifiedLl.setOnClickListener(this);
        unorderedLl.setOnClickListener(this);
        orderedLl.setOnClickListener(this);
        lv.setOnScrollListener(this);
    }

    private void loadData() {
        if (!TextUtils.isEmpty(key)) {
            emptyView.setVisibility(View.INVISIBLE);
            progressDialog.show();
            RequestBody body = new FormEncodingBuilder().add("key", key).build();
            Request request = new Request.Builder().url(NetConfig.inquiryHeadUrl + curPage + NetConfig.inquiryFootHeadUrl + stateType + NetConfig.inquiryFootFootUrl).post(body).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    handler.sendEmptyMessage(0);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        if (parseJson(json)) {
                            handler.sendEmptyMessage(1);
                        }
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
                JSONArray arrQuotationList = objDatas.optJSONArray("quotation_list");
                for (int i = 0; i < arrQuotationList.length(); i++) {
                    JSONObject objQuotation = arrQuotationList.optJSONObject(i);
                    Quotation quotation = new Quotation();
                    quotation.setStoreId(objQuotation.optString("store_id"));
                    quotation.setQuotationId(objQuotation.optString("quotation_id"));
                    quotation.setStoreName(objQuotation.optString("store_name"));
                    quotation.setQuotationMod(objQuotation.optString("quotation_mod"));
                    quotation.setQuotationUsed(objQuotation.optString("quotation_used"));
                    quotation.setQuotationSn(objQuotation.optString("quotation_sn"));
                    quotation.setAddTime(objQuotation.optString("add_time"));
                    quotation.setOrderAmount(objQuotation.optString("order_amount"));
                    List<Goods> goodsList = new ArrayList<>();
                    JSONArray arrGoodsList = objQuotation.optJSONArray("goods_list");
                    for (int j = 0; j < arrGoodsList.length(); j++) {
                        JSONObject objGoods = arrGoodsList.optJSONObject(j);
                        Goods goods = new Goods();
                        goods.setGoodsName(objGoods.optString("goods_name"));
                        goods.setGoodsPayPrice(objGoods.optString("goods_pay_price"));
                        goods.setGoodsNum(objGoods.optString("goods_num"));
                        goods.setGoodsImage(objGoods.optString("goods_image"));
                        goodsList.add(goods);
                    }
                    quotation.setGoodsList(goodsList);
                    list.add(quotation);
                }
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void delete() {
        if (!TextUtils.isEmpty(quotationId)) {
            progressDialog.show();
            RequestBody body = new FormEncodingBuilder().add("quotation_id", quotationId).add("key", key).build();
            Request request = new Request.Builder().url(NetConfig.inquiryDelUrl).post(body).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    handler.sendEmptyMessage(0);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        if (!TextUtils.isEmpty(json)) {
                            if (parseDelJson(json)) {
                                handler.sendEmptyMessage(2);
                            }
                        }
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                }
            });
        }
    }

    private boolean parseDelJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            if (objBean.optInt("code") == 200) {
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
            case R.id.rl_inquirylist_back:
                finish();
                break;
            case R.id.ll_inquiry_all:
                tarPosition = 0;
                stateType = "";
                changeClick();
                break;
            case R.id.ll_inquiry_unmodified:
                tarPosition = 1;
                stateType = "unmod";
                changeClick();
                break;
            case R.id.ll_inquiry_modified:
                tarPosition = 2;
                stateType = "mod";
                changeClick();
                break;
            case R.id.ll_inquiry_unordered:
                tarPosition = 3;
                stateType = "unused";
                changeClick();
                break;
            case R.id.ll_inquiry_ordered:
                tarPosition = 4;
                stateType = "used";
                changeClick();
                break;
        }
    }

    private void changeClick() {
        if (tarPosition != curPosition) {
            curPage = 1;
            switch (tarPosition) {
                case 0:
                    allTv.setTextColor(Color.RED);
                    unmodifiedTv.setTextColor(Color.BLACK);
                    modifiedTv.setTextColor(Color.BLACK);
                    unorderedTv.setTextColor(Color.BLACK);
                    orderedTv.setTextColor(Color.BLACK);
                    allV.setVisibility(View.VISIBLE);
                    unmodifiedV.setVisibility(View.INVISIBLE);
                    modifiedV.setVisibility(View.INVISIBLE);
                    unorderedV.setVisibility(View.INVISIBLE);
                    orderedV.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    allTv.setTextColor(Color.BLACK);
                    unmodifiedTv.setTextColor(Color.RED);
                    modifiedTv.setTextColor(Color.BLACK);
                    unorderedTv.setTextColor(Color.BLACK);
                    orderedTv.setTextColor(Color.BLACK);
                    allV.setVisibility(View.INVISIBLE);
                    unmodifiedV.setVisibility(View.VISIBLE);
                    modifiedV.setVisibility(View.INVISIBLE);
                    unorderedV.setVisibility(View.INVISIBLE);
                    orderedV.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    allTv.setTextColor(Color.BLACK);
                    unmodifiedTv.setTextColor(Color.BLACK);
                    modifiedTv.setTextColor(Color.RED);
                    unorderedTv.setTextColor(Color.BLACK);
                    orderedTv.setTextColor(Color.BLACK);
                    allV.setVisibility(View.INVISIBLE);
                    unmodifiedV.setVisibility(View.INVISIBLE);
                    modifiedV.setVisibility(View.VISIBLE);
                    unorderedV.setVisibility(View.INVISIBLE);
                    orderedV.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    allTv.setTextColor(Color.BLACK);
                    unmodifiedTv.setTextColor(Color.BLACK);
                    modifiedTv.setTextColor(Color.BLACK);
                    unorderedTv.setTextColor(Color.RED);
                    orderedTv.setTextColor(Color.BLACK);
                    allV.setVisibility(View.INVISIBLE);
                    unmodifiedV.setVisibility(View.INVISIBLE);
                    modifiedV.setVisibility(View.INVISIBLE);
                    unorderedV.setVisibility(View.VISIBLE);
                    orderedV.setVisibility(View.INVISIBLE);
                    break;
                case 4:
                    allTv.setTextColor(Color.BLACK);
                    unmodifiedTv.setTextColor(Color.BLACK);
                    modifiedTv.setTextColor(Color.BLACK);
                    unorderedTv.setTextColor(Color.BLACK);
                    orderedTv.setTextColor(Color.RED);
                    allV.setVisibility(View.INVISIBLE);
                    unmodifiedV.setVisibility(View.INVISIBLE);
                    modifiedV.setVisibility(View.INVISIBLE);
                    unorderedV.setVisibility(View.INVISIBLE);
                    orderedV.setVisibility(View.VISIBLE);
                    break;
            }
            curPosition = tarPosition;
            list.clear();
            loadData();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isLast && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (curPage < totalPage) {
                curPage++;
                loadData();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        isLast = (firstVisibleItem + visibleItemCount) == totalItemCount;
    }

    @Override
    public void onClick(View item, View widget, int position, int which, boolean isChecked) {
        switch (which) {
            case R.id.rl_item_inquiry_del:
                quotationId = list.get(position).getQuotationId();
                delPosition = position;
                alertDialog.show();
                break;
            case R.id.rl_item_inquiry_gen:
                cartId = "|";
                quotationId = list.get(position).getQuotationId();
                ifCart = "";
                addressId = "";
                storeId = list.get(position).getStoreId();
                gen();
                break;
        }
    }

    private void gen() {
        Intent intent = new Intent(InquiryActivity.this, SureOrderActivity.class);
        intent.putExtra("key", key);
        intent.putExtra("cart_id", cartId);
        intent.putExtra("quotation_id", quotationId);
        intent.putExtra("ifcart", ifCart);
        intent.putExtra("address_id", addressId);
        intent.putExtra("store_id", storeId);
        startActivity(intent);
    }
}
