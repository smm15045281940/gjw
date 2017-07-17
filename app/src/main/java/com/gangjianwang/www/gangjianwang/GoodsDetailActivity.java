package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fragment.GoodsDetailDetailFragment;
import fragment.GoodsDetailEvaluateFragment;
import fragment.GoodsDetailGoodsFragment;

public class GoodsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout backRl;
    private RelativeLayout goodsRl, detailRl, evaluateRl;
    private TextView goodsTv, detailTv, evaluateTv;
    private View goodsV, detailV, evaluateV;
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList;
    private Fragment goodsDetailGoodsFragment, goodsDetailDetailFragment, goodsDetailEvaluateFragment;
    private String goodsId = "";
    private String storeId = "";
    private int curPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(GoodsDetailActivity.this, R.layout.activity_goods_detail, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        initRoot();
    }

    private void initRoot() {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_goodsdetail_back);
        goodsRl = (RelativeLayout) rootView.findViewById(R.id.rl_goodsdetail_goods);
        detailRl = (RelativeLayout) rootView.findViewById(R.id.rl_goodsdetail_detail);
        evaluateRl = (RelativeLayout) rootView.findViewById(R.id.rl_goodsdetail_evaluate);
        goodsTv = (TextView) rootView.findViewById(R.id.tv_goodsdetail_goods);
        detailTv = (TextView) rootView.findViewById(R.id.tv_goodsdetail_detail);
        evaluateTv = (TextView) rootView.findViewById(R.id.tv_goodsdetail_evaluate);
        goodsV = rootView.findViewById(R.id.v_goodsdetail_goods);
        detailV = rootView.findViewById(R.id.v_goodsdetail_detail);
        evaluateV = rootView.findViewById(R.id.v_goodsdetail_evaluate);
        changeColor(0);
    }

    private void initData() {
        fragmentManager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        goodsDetailGoodsFragment = new GoodsDetailGoodsFragment();
        goodsDetailDetailFragment = new GoodsDetailDetailFragment();
        goodsDetailEvaluateFragment = new GoodsDetailEvaluateFragment();
        fragmentList.add(goodsDetailGoodsFragment);
        fragmentList.add(goodsDetailDetailFragment);
        fragmentList.add(goodsDetailEvaluateFragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.ll_goodsdetail_sit, fragmentList.get(curPosition));
        transaction.commit();
        Intent intent = getIntent();
        goodsId = intent.getStringExtra("goods_id");
        storeId = intent.getStringExtra("store_id");
        Bundle bundle = new Bundle();
        bundle.putString("goods_id", goodsId);
        bundle.putString("store_id", storeId);
        goodsDetailGoodsFragment.setArguments(bundle);
    }

    private void changeColor(int tarPosition) {
        switch (tarPosition) {
            case 0:
                goodsTv.setTextColor(Color.RED);
                detailTv.setTextColor(Color.BLACK);
                evaluateTv.setTextColor(Color.BLACK);
                goodsV.setVisibility(View.VISIBLE);
                detailV.setVisibility(View.INVISIBLE);
                evaluateV.setVisibility(View.INVISIBLE);
                break;
            case 1:
                goodsTv.setTextColor(Color.BLACK);
                detailTv.setTextColor(Color.RED);
                evaluateTv.setTextColor(Color.BLACK);
                goodsV.setVisibility(View.INVISIBLE);
                detailV.setVisibility(View.VISIBLE);
                evaluateV.setVisibility(View.INVISIBLE);
                break;
            case 2:
                goodsTv.setTextColor(Color.BLACK);
                detailTv.setTextColor(Color.BLACK);
                evaluateTv.setTextColor(Color.RED);
                goodsV.setVisibility(View.INVISIBLE);
                detailV.setVisibility(View.INVISIBLE);
                evaluateV.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void changeFrag(int tarPosition) {
        if (tarPosition != curPosition) {
            Fragment curFragment = fragmentList.get(curPosition);
            Fragment tarFragment = fragmentList.get(tarPosition);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(curFragment);
            if (tarFragment.isAdded()) {
                transaction.show(tarFragment);
            } else {
                transaction.add(R.id.ll_goodsdetail_sit, tarFragment);
            }
            transaction.commit();
            curPosition = tarPosition;
        }
    }

    private void setListener() {
        backRl.setOnClickListener(this);
        goodsRl.setOnClickListener(this);
        detailRl.setOnClickListener(this);
        evaluateRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_goodsdetail_back:
                finish();
                break;
            case R.id.rl_goodsdetail_goods:
                changeColor(0);
                changeFrag(0);
                break;
            case R.id.rl_goodsdetail_detail:
                changeColor(1);
                changeFrag(1);
                break;
            case R.id.rl_goodsdetail_evaluate:
                changeColor(2);
                changeFrag(2);
                break;
            default:
                break;
        }
    }
}
