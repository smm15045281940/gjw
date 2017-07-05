package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import utils.RegularUtils;
import utils.ToastUtils;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout backRl, saveRl;
    private TextView titleTv;
    private EditText nameEt, phoneEt, addressEt;
    private TextView areaTv;
    private Switch defaultSh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_add_address, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        initRoot();
    }

    private void initRoot() {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_addaddress_back);
        saveRl = (RelativeLayout) rootView.findViewById(R.id.rl_addaddress_save);
        titleTv = (TextView) rootView.findViewById(R.id.tv_addaddress_title);
        nameEt = (EditText) rootView.findViewById(R.id.et_addaddress_name);
        phoneEt = (EditText) rootView.findViewById(R.id.et_addaddress_phone);
        addressEt = (EditText) rootView.findViewById(R.id.et_addaddress_address);
        areaTv = (TextView) rootView.findViewById(R.id.tv_addaddress_area);
        defaultSh = (Switch) rootView.findViewById(R.id.sh_addaddress_default);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            titleTv.setText(intent.getStringExtra("title"));
        }
    }

    private void setListener() {
        backRl.setOnClickListener(this);
        saveRl.setOnClickListener(this);
        areaTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_addaddress_back:
                finish();
                break;
            case R.id.rl_addaddress_save:
                toJudge();
                break;
            case R.id.tv_addaddress_area:
                startActivityForResult(new Intent(AddAddressActivity.this, ReceiveAreaActivity.class), 0);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1 && data != null) {
            areaTv.setText(data.getStringExtra("sendAddress"));
        }
    }

    private void toJudge() {
        String name = nameEt.getText().toString();
        String phone = phoneEt.getText().toString();
        String area = areaTv.getText().toString();
        String address = addressEt.getText().toString();
        boolean isDefault = defaultSh.isChecked();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.toast(AddAddressActivity.this, "姓名不能为空！");
        } else if (TextUtils.isEmpty(phone)) {
            ToastUtils.toast(AddAddressActivity.this, "手机不能为空！");
        } else if (!RegularUtils.isPhonenumber(phone)) {
            ToastUtils.toast(AddAddressActivity.this, "手机格式不正确！");
        } else if (TextUtils.isEmpty(area)) {
            ToastUtils.toast(AddAddressActivity.this, "地区不能为空！");
        } else if (TextUtils.isEmpty(address)) {
            ToastUtils.toast(AddAddressActivity.this, "详细地址不能为空！");
        } else {
            ToastUtils.toast(AddAddressActivity.this, "save");
        }
    }
}
