package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import adapter.AddressAdapter;
import bean.Address;
import config.NetConfig;
import config.ParaConfig;
import customview.MyRefreshListView;
import customview.OnRefreshListener;
import utils.ToastUtils;
import utils.UserUtils;

public class AddressManagerActivity extends AppCompatActivity implements View.OnClickListener, ListItemClickHelp, OnRefreshListener {

    private View rootView, emptyView;
    private RelativeLayout mBackRl, mAddRl;
    private AlertDialog alertDialog;
    private MyRefreshListView lv;
    private List<Address> addressList = new ArrayList<>();
    private AddressAdapter addressAdapter;
    private ProgressDialog progressDialog;
    private OkHttpClient okHttpClient;
    private boolean isLogined;
    private String key;
    private int delPositon;
    private int STATE = ParaConfig.FIRST;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        if (STATE == ParaConfig.FIRST) {
                            progressDialog.dismiss();
                            ToastUtils.toast(AddressManagerActivity.this, ParaConfig.NETWORK_ERROR);
                        } else if (STATE == ParaConfig.REFRESH) {
                            lv.hideHeadView();
                            ToastUtils.toast(AddressManagerActivity.this, ParaConfig.REFRESH_DEFEAT_ERROR);
                        }
                        addressAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        if (STATE == ParaConfig.FIRST) {
                            progressDialog.dismiss();
                        } else if (STATE == ParaConfig.REFRESH) {
                            lv.hideHeadView();
                            ToastUtils.toast(AddressManagerActivity.this, ParaConfig.REFRESH_SUCCESS);
                        }
                        addressAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        progressDialog.dismiss();
                        addressList.remove(delPositon);
                        addressAdapter.notifyDataSetChanged();
                        ToastUtils.toast(AddressManagerActivity.this, ParaConfig.DELETE_SUCCESS);
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
        rootView = View.inflate(this, R.layout.activity_address_manager, null);
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
        initEmpty();
        initDialog();
    }

    private void initRoot() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_addressmanage_back);
        mAddRl = (RelativeLayout) rootView.findViewById(R.id.rl_addressmanage_add);
        lv = (MyRefreshListView) rootView.findViewById(R.id.lv_address_manager);
    }

    private void initEmpty() {
        emptyView = View.inflate(this, R.layout.empty, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ImageView) emptyView.findViewById(R.id.iv_empty_icon)).setImageResource(ParaConfig.ADDRESS_ICON);
        ((TextView) emptyView.findViewById(R.id.tv_empty_hint)).setText(ParaConfig.ADDRESS_HINT);
        ((TextView) emptyView.findViewById(R.id.tv_empty_content)).setText(ParaConfig.ADDRESS_CONTENT);
        ((TextView) emptyView.findViewById(R.id.tv_empty_click)).setText(ParaConfig.ADDRESS_CLICK);
        (emptyView.findViewById(R.id.tv_empty_click)).setVisibility(View.VISIBLE);
        ((ViewGroup) lv.getParent()).addView(emptyView);
        emptyView.setVisibility(View.GONE);
        lv.setEmptyView(emptyView);
    }

    private void initDialog() {
        alertDialog = new AlertDialog.Builder(this).setMessage(ParaConfig.DELETE_MESSAGE).setNegativeButton(ParaConfig.DELETE_YES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                delete(delPositon);
            }
        }).setPositiveButton(ParaConfig.DELETE_NO, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        }).create();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private void initData() {
        addressAdapter = new AddressAdapter(this, addressList, this);
        progressDialog = new ProgressDialog(this);
        okHttpClient = new OkHttpClient();
        isLogined = UserUtils.isLogined(this);
        if (isLogined) {
            key = UserUtils.readLogin(this, true).getKey();
        }
    }

    private void setData() {
        lv.setAdapter(addressAdapter);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mAddRl.setOnClickListener(this);
        lv.setOnRefreshListener(this);
    }

    private void loadData() {
        if (STATE == ParaConfig.FIRST) {
            emptyView.setVisibility(View.INVISIBLE);
            progressDialog.show();
        }
        RequestBody body = new FormEncodingBuilder()
                .add("key", key)
                .build();
        Request request = new Request.Builder().url(NetConfig.addressManageUrl)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (STATE == ParaConfig.REFRESH) {
                        addressList.clear();
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
            JSONArray arrAddressList = objDatas.optJSONArray("address_list");
            for (int i = 0; i < arrAddressList.length(); i++) {
                Address address = new Address();
                JSONObject o = arrAddressList.optJSONObject(i);
                address.setAddressId(o.optString("address_id"));
                address.setTrueName(o.optString("true_name"));
                address.setMobPhone(o.optString("mob_phone"));
                address.setAreaInfo(o.optString("area_info"));
                address.setAddress(o.optString("address"));
                address.setIsDefault(o.optString("is_default"));
                addressList.add(address);
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
            case R.id.rl_addressmanage_back:
                finish();
                break;
            case R.id.rl_addressmanage_add:
                Intent intent = new Intent(AddressManagerActivity.this, AddAddressActivity.class);
                intent.putExtra("title", "新增收货地址");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View item, View widget, int position, int which, boolean isChecked) {
        switch (which) {
            case R.id.tv_item_address_manager_edit:
                Intent intent = new Intent(AddressManagerActivity.this, AddAddressActivity.class);
                intent.putExtra("title", "编辑收货地址");
                intent.putExtra("address", addressList.get(position));
                startActivity(intent);
                break;
            case R.id.tv_item_address_manager_delete:
                delPositon = position;
                alertDialog.show();
                break;
            default:
                break;
        }
    }

    private void delete(int position) {
        progressDialog.show();
        String addressId = addressList.get(position).getAddressId();
        RequestBody body = new FormEncodingBuilder()
                .add("address_id", addressId)
                .add("key", key)
                .build();
        Request request = new Request.Builder().url(NetConfig.addressManageDeleteUrl)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (parseDeleteson(response.body().string())) {
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

    private boolean parseDeleteson(String json) {
        boolean b = false;
        try {
            JSONObject objBean = new JSONObject(json);
            if (objBean.optInt("code") == 200) {
                b = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return b;
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
