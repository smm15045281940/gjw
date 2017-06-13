package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import bean.Address;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mBackIv,mSaveIv;
    private TextView mAddressTitleTv;
    private EditText mNameEt,mPhoneEt,mAddressEt;
    private Switch mDefaultSh;
    private Address editAddress;
    private int ADDRESS_STATE = 0;
    private final int ADD_ADDRESS = 0;
    private final int EDIT_ADDRESS = 1;
    private int editPostion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_address);
        init();
        initView();
        setListener();
    }

    private void init() {
        Intent intent = getIntent();
        if(intent.getIntExtra("addressstate",0) == 0){
            ADDRESS_STATE = ADD_ADDRESS;
        }else if(intent.getIntExtra("addressstate",0) == 1){
            ADDRESS_STATE = EDIT_ADDRESS;
            editPostion = intent.getIntExtra("editposition",0);
            Bundle bundle = intent.getBundleExtra("addressobject");
            editAddress = (Address) bundle.getSerializable("addressobject");
        }
    }

    private void initView() {
        mBackIv = (ImageView) findViewById(R.id.iv_addaddress_back);
        mSaveIv = (ImageView) findViewById(R.id.iv_addaddress_save);
        mAddressTitleTv = (TextView) findViewById(R.id.tv_address_title);
        mNameEt = (EditText) findViewById(R.id.et_addaddress_name);
        mPhoneEt = (EditText) findViewById(R.id.et_addaddress_phone);
        mAddressEt = (EditText) findViewById(R.id.et_addaddress_address);
        mDefaultSh = (Switch) findViewById(R.id.sh_addaddress_default);
        switch (ADDRESS_STATE){
            case ADD_ADDRESS:
                mAddressTitleTv.setText("新增收货地址");
                break;
            case EDIT_ADDRESS:
                mAddressTitleTv.setText("编辑收货地址");
                mNameEt.setText(editAddress.getName());
                mPhoneEt.setText(editAddress.getPhone());
                mAddressEt.setText(editAddress.getAddress());
                mDefaultSh.setChecked(editAddress.getDefault());
                break;
            default:
                break;
        }
    }

    private void setListener() {
        mBackIv.setOnClickListener(this);
        mSaveIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_addaddress_back:
                finish();
                break;
            case R.id.iv_addaddress_save:
                switch (ADDRESS_STATE){
                    case ADD_ADDRESS:
                        Address address = new Address();
                        address.setName(mNameEt.getText().toString());
                        address.setPhone(mPhoneEt.getText().toString());
                        address.setAddress(mAddressEt.getText().toString());
                        address.setDefault(mDefaultSh.isChecked());
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("addaddress",address);
                        intent.putExtra("addaddress",bundle);
                        setResult(1,intent);
                        finish();
                        break;
                    case EDIT_ADDRESS:
                        Address address2 = new Address();
                        address2.setName(mNameEt.getText().toString());
                        address2.setPhone(mPhoneEt.getText().toString());
                        address2.setAddress(mAddressEt.getText().toString());
                        address2.setDefault(mDefaultSh.isChecked());
                        Intent intent2 = new Intent();
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("editposition",editPostion);
                        bundle2.putSerializable("addaddress",address2);
                        intent2.putExtra("addaddress",bundle2);
                        setResult(2,intent2);
                        finish();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

}
