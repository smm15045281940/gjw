package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import adapter.ManageBillInfoAdapter;
import utils.HeightUtils;
import utils.ToastUtils;


public class ManageBillInfoActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ListItemClickHelp, AdapterView.OnItemClickListener {

    private View rootView;
    private RelativeLayout backRl;
    private RadioGroup isBillRg, billTypeRg;
    private ListView historyLv;
    private LinearLayout isBillLl, billContentLl;
    private EditText billTitleEt;
    private RelativeLayout addContentRl, billTitleRl, sureRl;
    private Spinner sp;
    private List<String> mDataList;
    private ArrayAdapter<String> mAdapter;
    private boolean isAddBillContentClicked = false;

    private List<String> historyList;
    private ManageBillInfoAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(ManageBillInfoActivity.this, R.layout.activity_manage_bill_info, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
    }

    private void initView() {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_managebillinfo_back);
        isBillRg = (RadioGroup) rootView.findViewById(R.id.rg_managebillinfo_isbill);
        billTypeRg = (RadioGroup) rootView.findViewById(R.id.rg_managebillinfo_billtype);
        historyLv = (ListView) rootView.findViewById(R.id.lv_manage_bill_history);
        isBillLl = (LinearLayout) rootView.findViewById(R.id.ll_manage_bill_isbill);
        isBillLl.setVisibility(View.GONE);
        billContentLl = (LinearLayout) rootView.findViewById(R.id.ll_manage_bill_billcontent);
        billContentLl.setVisibility(View.GONE);
        billTitleEt = (EditText) rootView.findViewById(R.id.et_managebillinfo_billtitle);
        addContentRl = (RelativeLayout) rootView.findViewById(R.id.rl_managebillinfo_addbillcontent);
        billTitleRl = (RelativeLayout) rootView.findViewById(R.id.rl_managebillinfo_billtitle);
        billTitleRl.setVisibility(View.GONE);
        sureRl = (RelativeLayout) rootView.findViewById(R.id.rl_managebillinfo_sure);
        sp = (Spinner) rootView.findViewById(R.id.sp_managebillinfo_billcontent);
    }

    private void initData() {
        mDataList = new ArrayList<>();
        mDataList.add("明细");
        mDataList.add("酒");
        mDataList.add("食品");
        mDataList.add("饮料");
        mDataList.add("玩具");
        mDataList.add("日用品");
        mDataList.add("装修材料");
        mDataList.add("化妆品");
        mDataList.add("办公用品");
        mDataList.add("学生用品");
        mDataList.add("家居用品");
        mDataList.add("饰品");
        mDataList.add("服装");
        mDataList.add("箱包");
        mDataList.add("精品");
        mDataList.add("家电");
        mDataList.add("劳防用品");
        mDataList.add("耗材");
        mDataList.add("电脑配件");
        historyList = new ArrayList<>();
        historyList.add("办公用品");
        historyList.add("学生用品");
    }

    private void setData() {
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDataList);
        sp.setAdapter(mAdapter);
        historyAdapter = new ManageBillInfoAdapter(this, historyList, this);
        historyLv.setAdapter(historyAdapter);
        HeightUtils.setListViewHeight(historyLv);
    }

    private void setListener() {
        backRl.setOnClickListener(this);
        isBillRg.setOnCheckedChangeListener(this);
        billTypeRg.setOnCheckedChangeListener(this);
        addContentRl.setOnClickListener(this);
        sureRl.setOnClickListener(this);
        historyLv.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_managebillinfo_back:
                finish();
                break;
            case R.id.rl_managebillinfo_addbillcontent:
                isAddBillContentClicked = true;
                billContentLl.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_managebillinfo_sure:
                String billInfo = getBillInfo();
                Intent intent = new Intent();
                intent.putExtra("billInfo", billInfo);
                setResult(1, intent);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.rg_managebillinfo_isbill:
                switch (checkedId) {
                    case R.id.rb_managebillinfo_isbill_no:
                        billTitleRl.setVisibility(View.GONE);
                        billContentLl.setVisibility(View.GONE);
                        isBillLl.setVisibility(View.GONE);
                        isAddBillContentClicked = false;
                        break;
                    case R.id.rb_managebillinfo_isbill_yes:
                        isBillLl.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.rg_managebillinfo_billtype:
                switch (checkedId) {
                    case R.id.rb_managebillinfo_billtype_person:
                        billTitleRl.setVisibility(View.GONE);
                        break;
                    case R.id.rb_managebillinfo_billtype_company:
                        billTitleRl.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    private String getBillInfo() {
        String str;
        if (((RadioButton) isBillRg.getChildAt(0)).isChecked()) {
            str = "不需要发票";
            return str;
        } else if (!isAddBillContentClicked) {
            str = "";
            return str;
        } else if (((RadioButton) billTypeRg.getChildAt(0)).isChecked()) {
            str = (String) sp.getSelectedItem();
            return str;
        } else {
            str = billTitleEt.getText().toString() + " " + sp.getSelectedItem();
            return str;
        }
    }

    @Override
    public void onClick(View item, View widget, int position, int which, boolean isChecked) {
        switch (which) {
            case R.id.iv_managebillinfo_billhistory_delete:
                historyList.remove(position);
                historyAdapter.notifyDataSetChanged();
                HeightUtils.setListViewHeight(historyLv);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String billInfo = historyList.get(position);
        Intent intent = new Intent();
        intent.putExtra("billInfo", billInfo);
        setResult(1, intent);
        finish();
    }
}
