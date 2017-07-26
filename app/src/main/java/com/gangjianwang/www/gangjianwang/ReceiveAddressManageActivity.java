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

import adapter.ReceiveAddressManageAdapter;
import bean.AddAddress;
import config.NetConfig;
import config.ParaConfig;
import utils.ToastUtils;
import utils.UserUtils;

public class ReceiveAddressManageActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private View rootView;
    private RelativeLayout backRl, addReceiveAddressRl;
    private ProgressDialog progressDialog;
    private ListView mLv;
    private ReceiveAddressManageAdapter mReceiveAddressManageAdapter;
    private List<AddAddress> mAddAddressList = new ArrayList<>();
    private OkHttpClient okHttpClient = new OkHttpClient();
    private String key;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case 0:
                        ToastUtils.toast(ReceiveAddressManageActivity.this, ParaConfig.NETWORK_ERROR);
                        break;
                    case 1:
                        break;
                    default:
                        break;
                }
                mReceiveAddressManageAdapter.notifyDataSetChanged();
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
        rootView = View.inflate(ReceiveAddressManageActivity.this, R.layout.activity_receive_address_manage, null);
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
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_receiveaddressmanage_back);
        addReceiveAddressRl = (RelativeLayout) rootView.findViewById(R.id.rl_receiveaddressmanage_addreceiveaddress);
        mLv = (ListView) rootView.findViewById(R.id.lv_receiveaddressmanage);
    }

    private void initProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initData() {
        mReceiveAddressManageAdapter = new ReceiveAddressManageAdapter(ReceiveAddressManageActivity.this, mAddAddressList);
        key = UserUtils.readLogin(this, true).getKey();
    }

    private void setData() {
        mLv.setAdapter(mReceiveAddressManageAdapter);
    }

    private void setListener() {
        backRl.setOnClickListener(ReceiveAddressManageActivity.this);
        mLv.setOnItemClickListener(ReceiveAddressManageActivity.this);
        addReceiveAddressRl.setOnClickListener(ReceiveAddressManageActivity.this);
    }

    private void loadData() {
        progressDialog.show();
        RequestBody body = new FormEncodingBuilder().add("key", key).build();
        Request request = new Request.Builder().url(NetConfig.addressManageUrl).post(body).build();
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

    private boolean parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            if (objBean.optInt("code") == 200) {
                JSONObject objDatas = objBean.optJSONObject("datas");
                JSONArray arrAddressList = objDatas.optJSONArray("address_list");
                for (int i = 0; i < arrAddressList.length(); i++) {
                    JSONObject o = arrAddressList.optJSONObject(i);
                    AddAddress addAddress = new AddAddress();
                    addAddress.setName(o.optString("true_name"));
                    addAddress.setRoughAddress(o.optString("area_info"));
                    addAddress.setDetailAddress(o.optString("address"));
                    addAddress.setIsDefault(o.optString("is_default"));
                    mAddAddressList.add(addAddress);
                }
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            if (data != null) {
                if (data.getBooleanExtra("success", false)) {
                    mAddAddressList.clear();
                    loadData();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_receiveaddressmanage_back:
                finish();
                break;
            case R.id.rl_receiveaddressmanage_addreceiveaddress:
                startActivityForResult(new Intent(ReceiveAddressManageActivity.this, AddReceiveAddressActivity.class), 1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_receiveaddressmanage:
                Intent intent = new Intent();
                intent.putExtra("addAddress", mAddAddressList.get(position));
                setResult(1, intent);
                finish();
                break;
            default:
                break;
        }
    }
}
