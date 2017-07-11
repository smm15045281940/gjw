package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
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

import adapter.MyfootAdapter;
import bean.MyFoot;
import config.NetConfig;
import config.ParaConfig;
import customview.MyRefreshListView;
import customview.OnRefreshListener;
import utils.ToastUtils;
import utils.UserUtils;

public class FootActivity extends AppCompatActivity implements View.OnClickListener, OnRefreshListener {

    private View rootView, emptyView;
    private RelativeLayout mBackRl, mClearRl;
    private MyRefreshListView lv;
    private List<MyFoot> myFootList = new ArrayList<>();
    private MyfootAdapter mAdapter;
    private AlertDialog alertDialog;
    private OkHttpClient okHttpClient;
    private String key;
    private ProgressDialog progressDialog;
    private int STATE = ParaConfig.FIRST;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        progressDialog.dismiss();
                        mAdapter.notifyDataSetChanged();
                        if (STATE == ParaConfig.FIRST) {
                            ToastUtils.toast(FootActivity.this, ParaConfig.NETWORK_ERROR);
                        } else if (STATE == ParaConfig.REFRESH) {
                            ToastUtils.toast(FootActivity.this, ParaConfig.REFRESH_DEFEAT_ERROR);
                        }
                        break;
                    case 1:
                        if (STATE == ParaConfig.FIRST) {
                            progressDialog.dismiss();
                        } else if (STATE == ParaConfig.REFRESH) {
                            lv.hideHeadView();
                            STATE = ParaConfig.FIRST;
                            ToastUtils.toast(FootActivity.this, ParaConfig.REFRESH_SUCCESS);
                        }
                        mAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        progressDialog.dismiss();
                        myFootList.clear();
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
        rootView = View.inflate(this, R.layout.activity_myfoot, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        loadData();
        setListener();
    }

    private void initView() {
        initRoot();
        initEmpty();
        initDialog();
    }

    private void initRoot() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_myfoot_back);
        mClearRl = (RelativeLayout) rootView.findViewById(R.id.rl_myfoot_clear);
        lv = (MyRefreshListView) rootView.findViewById(R.id.lv_myfoot);
    }

    private void initEmpty() {
        emptyView = View.inflate(FootActivity.this, R.layout.empty_type_myfoot, null);
        ((ImageView) emptyView.findViewById(R.id.iv_empty_type_myfoot_icon)).setImageResource(R.mipmap.empty_myfoot);
        ((TextView) emptyView.findViewById(R.id.tv_empty_type_myfoot_hint)).setText("暂无您的浏览记录");
        ((TextView) emptyView.findViewById(R.id.tv_empty_type_myfoot_recommend)).setText("可以去看看哪些想要买的");
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) lv.getParent()).addView(emptyView);
        lv.setEmptyView(emptyView);
    }

    private void initDialog() {
        alertDialog = new AlertDialog.Builder(this).setMessage(ParaConfig.DELETE_CLEAR).setPositiveButton(ParaConfig.DELETE_NO, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        }).setNegativeButton(ParaConfig.DELETE_YES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                clear();
            }
        }).create();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
        progressDialog = new ProgressDialog(this);
        mAdapter = new MyfootAdapter(this, myFootList);
        key = UserUtils.readLogin(this, true).getKey();
    }

    private void setData() {
        lv.setAdapter(mAdapter);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mClearRl.setOnClickListener(this);
        lv.setOnRefreshListener(this);
    }

    private void loadData() {
        if (STATE == ParaConfig.FIRST) {
            emptyView.setVisibility(View.GONE);
            progressDialog.show();
        }
        Request request = new Request.Builder().url(NetConfig.footHeadUrl + key + NetConfig.footFootUrl).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (STATE == ParaConfig.REFRESH) {
                        myFootList.clear();
                    }
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
        try {
            JSONObject objBean = new JSONObject(json);
            JSONObject objDatas = objBean.optJSONObject("datas");
            JSONArray arrGoodsbrowseList = objDatas.optJSONArray("goodsbrowse_list");
            for (int i = 0; i < arrGoodsbrowseList.length(); i++) {
                MyFoot myFoot = new MyFoot();
                JSONObject o = arrGoodsbrowseList.optJSONObject(i);
                myFoot.setGoodsId(o.optString("goods_id"));
                myFoot.setGoodsName(o.optString("goods_name"));
                myFoot.setGoodsPrice(o.optString("goods_promotion_price"));
                myFoot.setGoodsImgUrl(o.optString("goods_image_url"));
                myFootList.add(myFoot);
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
            case R.id.rl_myfoot_back:
                finish();
                break;
            case R.id.rl_myfoot_clear:
                if (!myFootList.isEmpty()) {
                    alertDialog.show();
                } else {
                    ToastUtils.toast(FootActivity.this, "已经空了");
                }
                break;
            default:
                break;
        }
    }

    private void clear() {
        progressDialog.show();
        Request request = new Request.Builder().url(NetConfig.footDelHeadUrl + key + NetConfig.footDelFootUrl).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject objBean = new JSONObject(response.body().string());
                        if (objBean.optInt("code") == 200) {
                            handler.sendEmptyMessage(2);
                        } else {
                            handler.sendEmptyMessage(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
        handler.removeMessages(1);
        handler.removeMessages(2);
    }

    @Override
    public void onDownPullRefresh() {
        STATE = ParaConfig.REFRESH;
        loadData();
    }

    @Override
    public void onLoadingMore() {
        lv.hideFootView();
    }
}
