package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;

import bean.AddAddress;
import utils.RegularUtils;
import utils.ToastUtils;

public class AddReceiveAddressActivity extends AppCompatActivity implements View.OnClickListener{

    private View rootView;
    private RelativeLayout backRl,saveRl;
    private EditText nameEt,numberEt,roughAddressEt,detailAddressEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(AddReceiveAddressActivity.this,R.layout.activity_add_receive_address,null);
        setContentView(rootView);
        initView(rootView);
        setListener();
    }

    private void initView(View rootView) {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_addreceiveaddress_back);
        saveRl = (RelativeLayout) rootView.findViewById(R.id.rl_addreceiveaddress_save);
        nameEt = (EditText) rootView.findViewById(R.id.et_addreceiveaddress_receivername);
        numberEt = (EditText) rootView.findViewById(R.id.et_addreceiveaddress_contactphone);
        roughAddressEt = (EditText) rootView.findViewById(R.id.et_addreceiveaddress_addresschoose);
        detailAddressEt = (EditText) rootView.findViewById(R.id.et_addreceiveaddress_detailaddress);
    }

    private void setListener(){
        backRl.setOnClickListener(AddReceiveAddressActivity.this);
        saveRl.setOnClickListener(AddReceiveAddressActivity.this);
        roughAddressEt.setOnClickListener(AddReceiveAddressActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 1){
            if(data != null){
                roughAddressEt.setText(data.getStringExtra("sendAddress"));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_addreceiveaddress_back:
                finish();
                break;
            case R.id.et_addreceiveaddress_addresschoose:
                startActivityForResult(new Intent(AddReceiveAddressActivity.this,ChooseAddressActivity.class),1);
                break;
            case R.id.rl_addreceiveaddress_save:
                if(TextUtils.isEmpty(nameEt.getText().toString())){
                    ToastUtils.toast(AddReceiveAddressActivity.this,"姓名不能为空！");
                    return;
                }else if(TextUtils.isEmpty(numberEt.getText().toString())){
                    ToastUtils.toast(AddReceiveAddressActivity.this,"手机不能为空！");
                    return;
                }else if(!RegularUtils.isPhonenumber(numberEt.getText().toString())){
                    ToastUtils.toast(AddReceiveAddressActivity.this,"手机不正确！");
                    return;
                }else if(TextUtils.isEmpty(roughAddressEt.getText().toString())){
                    ToastUtils.toast(AddReceiveAddressActivity.this,"地区不能为空！");
                    return;
                }else if(TextUtils.isEmpty(detailAddressEt.getText().toString())){
                    ToastUtils.toast(AddReceiveAddressActivity.this,"详细地址不能为空！");
                    return;
                }
                AddAddress addAddress = new AddAddress();
                addAddress.setName(nameEt.getText().toString());
                addAddress.setNumber(numberEt.getText().toString());
                addAddress.setRoughAddress(roughAddressEt.getText().toString());
                addAddress.setDetailAddress(detailAddressEt.getText().toString());
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("addAddress",addAddress);
                intent.putExtra("addAddress",bundle);
                setResult(1,intent);
                finish();
                break;
            default:
                break;
        }
    }
}
