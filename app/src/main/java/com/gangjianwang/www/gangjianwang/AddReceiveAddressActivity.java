package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import config.NetConfig;
import config.ParaConfig;
import utils.RegularUtils;
import utils.ToastUtils;
import utils.UserUtils;

public class AddReceiveAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout backRl, saveRl;
    private EditText nameEt, numberEt, roughAddressEt, detailAddressEt;
    private String key, true_name, mob_phone, address, city_id, area_id, area_info, is_default;
    private ProgressDialog progressDialog;
    private OkHttpClient okHttpClient = new OkHttpClient();

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case 0:
                        ToastUtils.toast(AddReceiveAddressActivity.this, ParaConfig.NETWORK_ERROR);
                        break;
                    case 1:
                        Intent intent = new Intent();
                        intent.putExtra("success", true);
                        setResult(1, intent);
                        finish();
                        break;
                    default:
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
        rootView = View.inflate(AddReceiveAddressActivity.this, R.layout.activity_add_receive_address, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        initRoot();
        initProgress();
    }

    private void initRoot() {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_addreceiveaddress_back);
        saveRl = (RelativeLayout) rootView.findViewById(R.id.rl_addreceiveaddress_save);
        nameEt = (EditText) rootView.findViewById(R.id.et_addreceiveaddress_receivername);
        numberEt = (EditText) rootView.findViewById(R.id.et_addreceiveaddress_contactphone);
        roughAddressEt = (EditText) rootView.findViewById(R.id.et_addreceiveaddress_addresschoose);
        detailAddressEt = (EditText) rootView.findViewById(R.id.et_addreceiveaddress_detailaddress);
    }

    private void initProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initData() {
        key = UserUtils.readLogin(this, true).getKey();
    }

    private void setListener() {
        backRl.setOnClickListener(AddReceiveAddressActivity.this);
        saveRl.setOnClickListener(AddReceiveAddressActivity.this);
        roughAddressEt.setOnClickListener(AddReceiveAddressActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            if (data != null) {
                roughAddressEt.setText(data.getStringExtra("sendAddress"));
                city_id = data.getStringExtra("cityId");
                area_id = data.getStringExtra("areaId");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_addreceiveaddress_back:
                finish();
                break;
            case R.id.et_addreceiveaddress_addresschoose:
                startActivityForResult(new Intent(AddReceiveAddressActivity.this, ReceiveAreaActivity.class), 1);
                break;
            case R.id.rl_addreceiveaddress_save:
                if (TextUtils.isEmpty(nameEt.getText().toString())) {
                    ToastUtils.toast(AddReceiveAddressActivity.this, "姓名不能为空！");
                    return;
                } else if (TextUtils.isEmpty(numberEt.getText().toString())) {
                    ToastUtils.toast(AddReceiveAddressActivity.this, "手机不能为空！");
                    return;
                } else if (!RegularUtils.isPhonenumber(numberEt.getText().toString())) {
                    ToastUtils.toast(AddReceiveAddressActivity.this, "手机不正确！");
                    return;
                } else if (TextUtils.isEmpty(roughAddressEt.getText().toString())) {
                    ToastUtils.toast(AddReceiveAddressActivity.this, "地区不能为空！");
                    return;
                } else if (TextUtils.isEmpty(detailAddressEt.getText().toString())) {
                    ToastUtils.toast(AddReceiveAddressActivity.this, "详细地址不能为空！");
                    return;
                }
                add();
                break;
            default:
                break;
        }
    }

    private void add() {
        true_name = nameEt.getText().toString();
        mob_phone = numberEt.getText().toString();
        address = detailAddressEt.getText().toString();
        area_info = roughAddressEt.getText().toString();
        is_default = "0";
        RequestBody body = new FormEncodingBuilder()
                .add("key", key)
                .add("true_name", true_name)
                .add("mob_phone", mob_phone)
                .add("address", address)
                .add("city_id", city_id)
                .add("area_id", area_id)
                .add("area_info", area_info)
                .add("is_default", is_default)
                .build();
        Request request = new Request.Builder().url(NetConfig.addressAddUrl).post(body).build();
        progressDialog.show();
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
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
