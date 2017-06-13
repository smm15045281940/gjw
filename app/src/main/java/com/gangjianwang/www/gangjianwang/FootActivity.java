package com.gangjianwang.www.gangjianwang;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.MyfootAdapter;
import bean.Myfoot;
import utils.ToastUtils;

public class FootActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    private RelativeLayout mBackRl, mClearRl;
    private ListView mMyfootLv;
    private List<Myfoot> mDataList;
    private MyfootAdapter mAdapter;
    private AlertDialog deleteAd, clearAd;
    private int deletePosition;
    private TextView mEmptyTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_myfoot);
        initView();
        initData();
        setData();
        loadData();
        setListener();
    }

    private void initView() {
        mBackRl = (RelativeLayout) findViewById(R.id.rl_myfoot_back);
        mClearRl = (RelativeLayout) findViewById(R.id.rl_myfoot_clear);
        mMyfootLv = (ListView) findViewById(R.id.lv_myfoot);
        mEmptyTv = (TextView) findViewById(R.id.tv_myfoot_empty);
        mMyfootLv.setEmptyView(mEmptyTv);
        AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(FootActivity.this);
        deleteBuilder.setMessage("是否删除?");
        deleteBuilder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAd.dismiss();
            }
        }).setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDataList.remove(deletePosition);
                mAdapter.notifyDataSetChanged();
                deleteAd.dismiss();
            }
        });
        deleteAd = deleteBuilder.create();
        AlertDialog.Builder clearBuilder = new AlertDialog.Builder(FootActivity.this);
        clearBuilder.setMessage("是否清空?");
        clearBuilder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearAd.dismiss();
            }
        }).setNegativeButton("清空", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDataList.clear();
                mAdapter.notifyDataSetChanged();
                clearAd.dismiss();
            }
        });
        clearAd = clearBuilder.create();
    }

    private void initData() {
        mDataList = new ArrayList<>();
    }

    private void setData() {
        mAdapter = new MyfootAdapter(this, mDataList);
        mMyfootLv.setAdapter(mAdapter);
    }

    private void loadData() {
        Myfoot mf1 = new Myfoot();
        mf1.goodsName = "精品瓷砖";
        mf1.goodsPrice = "¥35.00";
        mf1.goodsImg = null;
        Myfoot mf2 = new Myfoot();
        mf2.goodsName = "圆管";
        mf2.goodsPrice = "¥25.50";
        mf2.goodsImg = null;
        Myfoot mf3 = new Myfoot();
        mf3.goodsName = "把手";
        mf3.goodsPrice = "¥10.00";
        mf3.goodsImg = null;
        mDataList.add(mf1);
        mDataList.add(mf2);
        mDataList.add(mf3);
        mAdapter.notifyDataSetChanged();
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mClearRl.setOnClickListener(this);
        mMyfootLv.setOnItemLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_myfoot_back:
                finish();
                break;
            case R.id.rl_myfoot_clear:
                clearAd.show();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        deletePosition = position;
        deleteAd.show();
        return true;
    }
}
