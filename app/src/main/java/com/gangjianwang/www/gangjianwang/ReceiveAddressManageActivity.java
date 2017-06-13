package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.ReceiveAddressManageAdapter;
import bean.AddAddress;
import utils.ToastUtils;

public class ReceiveAddressManageActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private View rootView;
    private RelativeLayout backRl, addReceiveAddressRl;
    private ListView mLv;
    private ReceiveAddressManageAdapter mReceiveAddressManageAdapter;
    private List<AddAddress> mAddAddressList = new ArrayList<>();
    private TextView emptyTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(ReceiveAddressManageActivity.this, R.layout.activity_receive_address_manage, null);
        setContentView(rootView);
        initView(rootView);
        initData();
        setData();
        setListener();
    }

    private void initView(View rootView) {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_receiveaddressmanage_back);
        addReceiveAddressRl = (RelativeLayout) rootView.findViewById(R.id.rl_receiveaddressmanage_addreceiveaddress);
        mLv = (ListView) rootView.findViewById(R.id.lv_receiveaddressmanage);
        emptyTv = (TextView) rootView.findViewById(R.id.tv_receiveaddressmanage_empty);
        mLv.setEmptyView(emptyTv);
    }

    private void initData(){
        AddAddress add = new AddAddress();
        add.setName("a");
        add.setNumber("15846113106");
        add.setRoughAddress("哈尔滨道外区");
        add.setDetailAddress("开源街3号");
        mAddAddressList.add(add);
    }

    private void setData() {
        mReceiveAddressManageAdapter = new ReceiveAddressManageAdapter(ReceiveAddressManageActivity.this,mAddAddressList);
        mLv.setAdapter(mReceiveAddressManageAdapter);
    }

    private void setListener() {
        backRl.setOnClickListener(ReceiveAddressManageActivity.this);
        mLv.setOnItemClickListener(ReceiveAddressManageActivity.this);
        addReceiveAddressRl.setOnClickListener(ReceiveAddressManageActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 1){
            if(data != null){
                Bundle bundle = data.getBundleExtra("addAddress");
                AddAddress addAddress = (AddAddress) bundle.getSerializable("addAddress");
                mAddAddressList.add(addAddress);
                mReceiveAddressManageAdapter.notifyDataSetChanged();
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
                startActivityForResult(new Intent(ReceiveAddressManageActivity.this,AddReceiveAddressActivity.class),1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.lv_receiveaddressmanage:
                Bundle bundle = new Bundle();
                bundle.putSerializable("addAddress",mAddAddressList.get(position));
                Intent intent = new Intent();
                intent.putExtra("addAddress",bundle);
                setResult(1,intent);
                finish();
                break;
            default:
                break;
        }
    }
}
