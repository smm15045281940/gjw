package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import adapter.MakeSureOrderAdapter;
import bean.AddAddress;
import bean.MakeSureOrder;
import utils.ToastUtils;

public class MakeSureOrderActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private View rootView;
    private RelativeLayout backRl, submitOrderRl;
    private ListView mLv;
    private MakeSureOrder mMakeSureOrder;
    private MakeSureOrderAdapter mMakeSureOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(MakeSureOrderActivity.this, R.layout.activity_make_sure_order, null);
        setContentView(rootView);
        initView(rootView);
        initData();
        setData();
        setListener();
    }

    private void initView(View rootView) {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_makesureorder_back);
        submitOrderRl = (RelativeLayout) rootView.findViewById(R.id.rl_makesureorder_submitorder);
        mLv = (ListView) rootView.findViewById(R.id.lv_makesureorder);
    }

    private void initData() {
        mMakeSureOrder = new MakeSureOrder();
        mMakeSureOrder.setName("smm");
        mMakeSureOrder.setNumber("15045281940");
        mMakeSureOrder.setAddress("哈尔滨道外区开源街3号");
        mMakeSureOrder.setPayway("");
        mMakeSureOrder.setBillinfo("");
    }

    private void setData() {
        mMakeSureOrderAdapter = new MakeSureOrderAdapter(MakeSureOrderActivity.this, mMakeSureOrder);
        mLv.setAdapter(mMakeSureOrderAdapter);
    }

    private void setListener() {
        backRl.setOnClickListener(MakeSureOrderActivity.this);
        submitOrderRl.setOnClickListener(MakeSureOrderActivity.this);
        mLv.setOnItemClickListener(MakeSureOrderActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            if (data != null) {
                Bundle bundle = data.getBundleExtra("addAddress");
                AddAddress addAddress = (AddAddress) bundle.getSerializable("addAddress");
                mMakeSureOrder.setName(addAddress.getName());
                mMakeSureOrder.setNumber(addAddress.getNumber());
                mMakeSureOrder.setAddress(addAddress.getRoughAddress() + addAddress.getDetailAddress());
                mMakeSureOrderAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == 2 && resultCode == 1) {
            if (data != null) {
                mMakeSureOrder.setPayway(data.getStringExtra("payWay"));
                mMakeSureOrderAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == 3 && resultCode == 1) {
            if (data != null) {
                mMakeSureOrder.setBillinfo(data.getStringExtra("billInfo"));
                mMakeSureOrderAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_makesureorder_back:
                finish();
                break;
            case R.id.rl_makesureorder_submitorder:
                ToastUtils.toast(MakeSureOrderActivity.this, "提交订单");
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_makesureorder:
                switch (position) {
                    case 0:
                        startActivityForResult(new Intent(MakeSureOrderActivity.this, ReceiveAddressManageActivity.class), 1);
                        break;
                    case 1:
                        startActivityForResult(new Intent(MakeSureOrderActivity.this, ChoosePayWayActivity.class), 2);
                        break;
                    case 2:
                        startActivityForResult(new Intent(MakeSureOrderActivity.this, ManageBillInfoActivity.class), 3);
                        break;
                    default:
                        ToastUtils.toast(MakeSureOrderActivity.this, position + "");
                        break;
                }
                break;
            default:
                break;
        }
    }
}
