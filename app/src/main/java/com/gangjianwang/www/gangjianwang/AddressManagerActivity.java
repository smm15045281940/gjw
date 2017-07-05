package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
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

import adapter.AddressAdapter;
import bean.Address;
import config.NetConfig;
import utils.ToastUtils;
import utils.UserUtils;

public class AddressManagerActivity extends AppCompatActivity implements View.OnClickListener, ListItemClickHelp {

    private View rootView, dialogView;
    private RelativeLayout mBackRl, mAddRl;
    private AlertDialog alertDialog;
    private RelativeLayout yesRl, noRl;
    private ListView lv;
    private List<Address> addressList = new ArrayList<>();
    private AddressAdapter addressAdapter;
    private ProgressDialog progressDialog;
    private OkHttpClient okHttpClient;
    private boolean isLogined;
    private String key;
    private int delPositon;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        progressDialog.dismiss();
                        ToastUtils.toast(AddressManagerActivity.this, "无网络");
                        break;
                    case 1:
                        progressDialog.dismiss();
                        addressAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        addressList.remove(delPositon);
                        addressAdapter.notifyDataSetChanged();
                        ToastUtils.toast(AddressManagerActivity.this, "删除成功");
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

    private void initView() {
        initRoot();
        initDialog();
    }

    private void initRoot() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_addressmanage_back);
        mAddRl = (RelativeLayout) rootView.findViewById(R.id.rl_addressmanage_add);
        lv = (ListView) rootView.findViewById(R.id.lv_address_manager);
    }

    private void initDialog() {
        dialogView = View.inflate(this, R.layout.dialog_delete, null);
        ((TextView) dialogView.findViewById(R.id.tv_dialog_delete_hint)).setText("确认删除吗?");
        ((TextView) dialogView.findViewById(R.id.tv_dialog_delete_yes)).setText("确认");
        ((TextView) dialogView.findViewById(R.id.tv_dialog_delete_no)).setText("取消");
        yesRl = (RelativeLayout) dialogView.findViewById(R.id.rl_dialog_delete_yes);
        noRl = (RelativeLayout) dialogView.findViewById(R.id.rl_dialog_delete_no);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        alertDialog = builder.create();
    }

    private void initData() {
        addressAdapter = new AddressAdapter(this, addressList, this);
        progressDialog = new ProgressDialog(this);
        okHttpClient = new OkHttpClient();
        isLogined = UserUtils.isLogined(this);
        if (isLogined) {
            key = UserUtils.readLogin(this, isLogined).getKey();
        }
    }

    private void setData() {
        lv.setAdapter(addressAdapter);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mAddRl.setOnClickListener(this);
        yesRl.setOnClickListener(this);
        noRl.setOnClickListener(this);
    }

    private void loadData() {
        progressDialog.show();
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
                startActivityForResult(intent, 1);
                break;
            case R.id.rl_dialog_delete_yes:
                alertDialog.dismiss();
                ToastUtils.toast(AddressManagerActivity.this, "删除" + delPositon);
                delete(delPositon);
                break;
            case R.id.rl_dialog_delete_no:
                alertDialog.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View item, View widget, int position, int which, boolean isChecked) {
        switch (which) {
            case R.id.tv_item_address_manager_edit:
                ToastUtils.log(AddressManagerActivity.this, "编辑" + position);
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
            String code = objBean.optString("code");
            if (code.equals("200")) {
                b = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return b;
    }
}
