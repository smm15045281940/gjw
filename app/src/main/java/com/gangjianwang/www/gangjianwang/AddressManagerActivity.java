package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapter.AddressAdapter;
import bean.Address;

public class AddressManagerActivity extends AppCompatActivity implements View.OnClickListener, ListItemClickHelp {

    private RelativeLayout mBackRl, mAddRl;
    private TextView mEmptyTv;
    private ListView mAddressManagerLv;
    private List<Address> mAddressList;
    private AddressAdapter mAddressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_address_manager);
        initView();
        initData();
        setData();
        setListener();
        loadData();
    }

    private void initView() {
        mBackRl = (RelativeLayout) findViewById(R.id.rl_addressmanage_back);
        mAddRl = (RelativeLayout) findViewById(R.id.rl_addressmanage_add);
        mEmptyTv = (TextView) findViewById(R.id.tv_address_manager_empty);
        mAddressManagerLv = (ListView) findViewById(R.id.lv_address_manager);
        mAddressManagerLv.setEmptyView(mEmptyTv);
    }

    private void initData() {
        mAddressList = new ArrayList<>();
    }

    private void setData() {
        mAddressAdapter = new AddressAdapter(AddressManagerActivity.this, mAddressList, this);
        mAddressManagerLv.setAdapter(mAddressAdapter);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mAddRl.setOnClickListener(this);
    }

    private void loadData() {
        Address address = new Address();
        address.setName("dfdfdfd");
        address.setPhone("129473974");
        address.setAddress("difdifjdkfdlfdjlfdjlfjd");
        address.setDefault(false);
        mAddressList.add(address);
        mAddressAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_addressmanage_back:
                finish();
                break;
            case R.id.rl_addressmanage_add:
                Intent intent = new Intent(AddressManagerActivity.this, AddAddressActivity.class);
                intent.putExtra("addressstate", 0);
                startActivityForResult(intent, 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1 && data != null) {
            Bundle bundle = data.getBundleExtra("addaddress");
            Address address = (Address) bundle.getSerializable("addaddress");
            if (address.getDefault()) {
                for (int i = 0; i < mAddressList.size(); i++) {
                    mAddressList.get(i).setDefault(false);
                }
            }
            mAddressList.add(address);
            mAddressAdapter.notifyDataSetChanged();
        }
        if (requestCode == 2 && resultCode == 2 && data != null) {
            Bundle bundle = data.getBundleExtra("addaddress");
            int editPosition = bundle.getInt("editposition");
            Address address = (Address) bundle.getSerializable("addaddress");
            List<Address> tempList1 = new ArrayList<>();
            List<Address> tempList2 = new ArrayList<>();
            for (int i = 0; i < editPosition; i++) {
                tempList1.add(mAddressList.get(editPosition));
            }
            for (int i = editPosition + 1; i < mAddressList.size(); i++) {
                tempList2.add(mAddressList.get(editPosition));
            }
            if (address.getDefault()) {
                for (int i = 0; i < mAddressList.size(); i++) {
                    mAddressList.get(i).setDefault(false);
                }
            }
            mAddressList.clear();
            mAddressList.addAll(tempList1);
            mAddressList.add(address);
            mAddressList.addAll(tempList2);
            mAddressAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View item, View widget, int position, int which, boolean isChecked) {
        switch (which) {
            case R.id.btn_item_address_manager_edit:
                Intent intent = new Intent(AddressManagerActivity.this, AddAddressActivity.class);
                intent.putExtra("addressstate", 1);
                intent.putExtra("editposition", position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("addressobject", mAddressList.get(position));
                intent.putExtra("addressobject", bundle);
                startActivityForResult(intent, 2);
                break;
            case R.id.btn_item_address_manager_delete:
                mAddressList.remove(position);
                mAddressAdapter.notifyDataSetChanged();
                break;
        }
    }
}
