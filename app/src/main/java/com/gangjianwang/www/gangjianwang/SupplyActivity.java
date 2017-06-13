package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SupplyActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,View.OnClickListener{

    private ImageView mBackIv;
    private ListView mLv;
    private List<String> mDataList;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_supply);
        initView();
        initData();
        setData();
        setListener();
        loadData();
    }

    private void initView(){
        mBackIv = (ImageView) findViewById(R.id.iv_supply_back);
        mLv = (ListView) findViewById(R.id.lv_supply);
    }

    private void initData(){
        mDataList = new ArrayList<>();
    }

    private void setData(){
        mAdapter = new ArrayAdapter<String>(SupplyActivity.this,android.R.layout.simple_list_item_1,mDataList);
        mLv.setAdapter(mAdapter);
    }

    private void setListener(){
        mBackIv.setOnClickListener(this);
        mLv.setOnItemClickListener(this);
    }

    private void loadData(){
        for (int i = 0; i < 20; i++) {
            mDataList.add(i+"钢铁集团");
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_supply_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("supply",mDataList.get(position));
        setResult(1,intent);
        finish();
    }

}
