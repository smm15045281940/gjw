package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.ChooseAddressAdapter;
import utils.ToastUtils;

public class ChooseAddressActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private View rootView;
    private ImageView backIv;
    private TextView firstAddressTv,secondAddressTv,thirdAddressTv;
    private View firstAddressV,secondAddressV,thirdAddressV;
    private ListView mLv;
    private ChooseAddressAdapter mAdapter;
    private List<String> firstAddressList = new ArrayList<>();
    private List<String> secondAddressList = new ArrayList<>();
    private List<String> thirdAddressList = new ArrayList<>();
    private List<String> mDataList = new ArrayList<>();
    private final int CHOOSE_FIRST = 0;
    private final int CHOOSE_SECOND = 1;
    private final int CHOOSE_THIRD = 2;
    private int CHOOSE_STATE = CHOOSE_FIRST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this,R.layout.activity_choose_address,null);
        setContentView(rootView);
        initView(rootView);
        setData();
        loadData();
        setListener();
    }

    private void initView(View rootView) {
        backIv = (ImageView) rootView.findViewById(R.id.iv_chooseaddress_back);
        firstAddressTv = (TextView) rootView.findViewById(R.id.tv_chooseaddress_first);
        secondAddressTv = (TextView) rootView.findViewById(R.id.tv_chooseaddress_second);
        thirdAddressTv = (TextView) rootView.findViewById(R.id.tv_chooseaddress_third);
        firstAddressV = rootView.findViewById(R.id.v_chooseaddress_first);
        secondAddressV = rootView.findViewById(R.id.v_chooseaddress_second);
        thirdAddressV = rootView.findViewById(R.id.v_chooseaddress_third);
        mLv = (ListView) rootView.findViewById(R.id.lv_chooseaddress);
    }

    private void setData(){
        mAdapter = new ChooseAddressAdapter(this,mDataList);
        mLv.setAdapter(mAdapter);
    }

    private void loadData(){
        firstAddressList.add("北京");
        firstAddressList.add("天津");
        firstAddressList.add("河北");
        firstAddressList.add("山西");
        mDataList.addAll(firstAddressList);
        mAdapter.notifyDataSetChanged();
    }

    private void setListener(){
        backIv.setOnClickListener(this);
        firstAddressTv.setOnClickListener(this);
        secondAddressTv.setOnClickListener(this);
        mLv.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_chooseaddress_back:
                finish();
                break;
            case R.id.tv_chooseaddress_first:
                if(CHOOSE_STATE != CHOOSE_FIRST){
                    firstAddressTv.setText("一级地区");
                    secondAddressTv.setText("二级地区");
                    mDataList.clear();
                    mDataList.addAll(firstAddressList);
                    mAdapter.notifyDataSetChanged();
                    CHOOSE_STATE = CHOOSE_FIRST;
                    changeColor(CHOOSE_STATE);
                }
                break;
            case R.id.tv_chooseaddress_second:
                if(CHOOSE_STATE == CHOOSE_THIRD){
                    secondAddressTv.setText("二级地区");
                    mDataList.clear();
                    mDataList.addAll(secondAddressList);
                    mAdapter.notifyDataSetChanged();
                    CHOOSE_STATE = CHOOSE_SECOND;
                    changeColor(CHOOSE_STATE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (CHOOSE_STATE){
            case CHOOSE_FIRST:
                if(firstAddressList.get(position).equals("北京")){
                    firstAddressTv.setText(firstAddressList.get(position));
                    secondAddressList.clear();
                    secondAddressList.add("北京市");
                    mDataList.clear();
                    mDataList.addAll(secondAddressList);
                    mAdapter.notifyDataSetChanged();
                    CHOOSE_STATE = CHOOSE_SECOND;
                    changeColor(CHOOSE_STATE);
                }
                break;
            case CHOOSE_SECOND:
                if(secondAddressList.get(position).equals("北京市")){
                    secondAddressTv.setText(secondAddressList.get(position));
                    thirdAddressList.clear();
                    thirdAddressList.add("东城区");
                    thirdAddressList.add("西城区");
                    thirdAddressList.add("朝阳区");
                    mDataList.clear();
                    mDataList.addAll(thirdAddressList);
                    mAdapter.notifyDataSetChanged();
                    CHOOSE_STATE = CHOOSE_THIRD;
                    changeColor(CHOOSE_STATE);
                }
                break;
            case CHOOSE_THIRD:
                Intent intent = new Intent();
                String sendAddress = firstAddressTv.getText().toString()+" "+secondAddressTv.getText().toString()+" "+thirdAddressList.get(position);
                intent.putExtra("sendAddress",sendAddress);
                setResult(1,intent);
                finish();
                break;
            default:
                break;
        }
    }

    private void changeColor(int CHOOSE_STATE){
        switch (CHOOSE_STATE){
            case CHOOSE_FIRST:
                firstAddressTv.setTextColor(Color.RED);
                secondAddressTv.setTextColor(Color.BLACK);
                thirdAddressTv.setTextColor(Color.BLACK);
                firstAddressV.setVisibility(View.VISIBLE);
                secondAddressV.setVisibility(View.INVISIBLE);
                thirdAddressV.setVisibility(View.INVISIBLE);
                break;
            case CHOOSE_SECOND:
                firstAddressTv.setTextColor(Color.BLACK);
                secondAddressTv.setTextColor(Color.RED);
                thirdAddressTv.setTextColor(Color.BLACK);
                firstAddressV.setVisibility(View.INVISIBLE);
                secondAddressV.setVisibility(View.VISIBLE);
                thirdAddressV.setVisibility(View.INVISIBLE);
                break;
            case CHOOSE_THIRD:
                firstAddressTv.setTextColor(Color.BLACK);
                secondAddressTv.setTextColor(Color.BLACK);
                thirdAddressTv.setTextColor(Color.RED);
                firstAddressV.setVisibility(View.INVISIBLE);
                secondAddressV.setVisibility(View.INVISIBLE);
                thirdAddressV.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
