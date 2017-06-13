package com.gangjianwang.www.gangjianwang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class ClassifyActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private RelativeLayout mBackRl;
    private ListView mLeftLv, mRightLv;
    private List<String> mLeftDataList;
    private List<String> mRightDataList;
    private ArrayAdapter<String> mLeftAdapter, mRightAdapter;

    private String loadStr = "不锈钢系列";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_classify);
        initView();
        initData();
        bindData();
        loadData();
        bindListener();
    }

    private void initView() {
        mBackRl = (RelativeLayout) findViewById(R.id.rl_classify_back);
        mLeftLv = (ListView) findViewById(R.id.lv_classify_left);
        mRightLv = (ListView) findViewById(R.id.lv_classify_right);
    }

    private void initData() {
        mLeftDataList = new ArrayList<>();
        mRightDataList = new ArrayList<>();
    }

    private void bindData() {
        mLeftAdapter = new ArrayAdapter<String>(ClassifyActivity.this, android.R.layout.simple_list_item_1, mLeftDataList);
        mLeftLv.setAdapter(mLeftAdapter);
        mRightAdapter = new ArrayAdapter<String>(ClassifyActivity.this, android.R.layout.simple_list_item_1, mRightDataList);
        mRightLv.setAdapter(mRightAdapter);
    }

    private void loadData() {
        mLeftDataList.add("不锈钢系列");
        mLeftDataList.add("钢铁钢材系列");
        mLeftDataList.add("装修材料系列");
        mLeftDataList.add("五金工具系列");
        mLeftDataList.add("金属材料系列");
        mLeftDataList.add("建筑材料系列");
        mLeftDataList.add("机床系列");
        mLeftAdapter.notifyDataSetChanged();
        for (int i = 0; i < 20; i++) {
            mRightDataList.add("不锈钢系列" + i);
        }
        mRightAdapter.notifyDataSetChanged();
    }

    private void bindListener() {
        mBackRl.setOnClickListener(this);
        mLeftLv.setOnItemClickListener(this);
        mLeftLv.setSelector(R.color.red);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_classify_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        loadChangeStr(mLeftDataList.get(position));
    }

    private void loadChangeStr(String s) {
        if (!loadStr.equals(s)) {
            mRightDataList.clear();
            for (int i = 0; i < 20; i++) {
                mRightDataList.add(s + i);
            }
            mRightAdapter.notifyDataSetChanged();
            mRightLv.setSelection(0);
            loadStr = s;
        }
    }
}
