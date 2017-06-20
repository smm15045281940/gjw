package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import adapter.GjSpecialAdapter;
import bean.GjSpecial;
import config.NetConfig;
import utils.ToastUtils;

public class GjSpecialSaleActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout mBackRl;
    private TextView mTitleTv;
    private ListView mLv;
    private OkHttpClient okHttpClient;
    private ProgressDialog mPd;
    private List<GjSpecial> mDataList = new ArrayList<>();
    private GjSpecialAdapter mAdapter;
    private String gjTitle;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        mPd.dismiss();
                        ToastUtils.toast(GjSpecialSaleActivity.this, "无网络");
                        break;
                    case 1:
                        mPd.dismiss();
                        mTitleTv.setText(gjTitle);
                        mAdapter.notifyDataSetChanged();
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
        rootView = View.inflate(this, R.layout.activity_gj_special_sale, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
        loadData();
    }

    private void initView() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_gj_special_sale_back);
        mTitleTv = (TextView) rootView.findViewById(R.id.tv_gj_special_sale_title);
        mLv = (ListView) rootView.findViewById(R.id.lv_gj_special_sale);
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
        mPd = new ProgressDialog(this);
        mPd.setMessage("加载中..");
        mAdapter = new GjSpecialAdapter(this, mDataList);
    }

    private void setData() {
        mLv.setAdapter(mAdapter);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
    }

    private void loadData() {
        mPd.show();
        Request request = new Request.Builder().url(NetConfig.gjSpecialSaleUrl).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    if (parseJson(json))
                        handler.sendEmptyMessage(1);
                }
            }
        });
    }

    private boolean parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            JSONObject objDatas = objBean.optJSONObject("datas");
            gjTitle = objDatas.optString("special_desc");
            JSONArray arrList = objDatas.optJSONArray("list");
            JSONObject objGoods = arrList.optJSONObject(0).optJSONObject("goods");
            JSONArray arrItem = objGoods.optJSONArray("item");
            for (int i = 0; i < arrItem.length(); i++) {
                JSONObject o = arrItem.optJSONObject(i);
                GjSpecial gjSpecial = new GjSpecial();
                gjSpecial.setId(o.optString("goods_id"));
                gjSpecial.setName(o.optString("goods_name"));
                gjSpecial.setPrice(o.optString("goods_promotion_price"));
                gjSpecial.setImgUrl(o.optString("goods_image"));
                mDataList.add(gjSpecial);
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
            case R.id.rl_gj_special_sale_back:
                finish();
                break;
            default:
                break;
        }
    }
}
