package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import adapter.MyFragmentPagerAdapter;
import fragment.GoodsDetailDetailFragment;
import fragment.GoodsDetailEvaluateFragment;
import fragment.GoodsDetailGoodsFragment;

public class GoodsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout backRl;
    private TabLayout tl;
    private ViewPager vp;
    private String[] tlTitle;
    private List<Fragment> fragmentList;
    private Fragment goodsDetailGoodsFragment, goodsDetailDetailFragment, goodsDetailEvaluateFragment;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private String goodsId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(GoodsDetailActivity.this, R.layout.activity_goods_detail, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
    }

    private void initView() {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_goodsdetail_back);
        tl = (TabLayout) rootView.findViewById(R.id.tl_goodsdetail);
        vp = (ViewPager) rootView.findViewById(R.id.vp_goodsdetail);
    }

    private void initData() {
        tlTitle = getResources().getStringArray(R.array.goodsdetail_title);
        fragmentList = new ArrayList<>();
        goodsDetailGoodsFragment = new GoodsDetailGoodsFragment();
        goodsDetailDetailFragment = new GoodsDetailDetailFragment();
        goodsDetailEvaluateFragment = new GoodsDetailEvaluateFragment();
        fragmentList.add(goodsDetailGoodsFragment);
        fragmentList.add(goodsDetailDetailFragment);
        fragmentList.add(goodsDetailEvaluateFragment);
        Intent intent = getIntent();
        goodsId = intent.getStringExtra("goods_id");
        Bundle bundle = new Bundle();
        bundle.putString("goodsId", goodsId);
        goodsDetailGoodsFragment.setArguments(bundle);
    }

    private void setData() {
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), tlTitle, fragmentList);
        vp.setAdapter(myFragmentPagerAdapter);
        tl.setupWithViewPager(vp);
        tl.setTabTextColors(Color.BLACK, Color.RED);
        tl.setSelectedTabIndicatorColor(Color.RED);
        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setListener() {
        backRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_goodsdetail_back:
                finish();
                break;
            default:
                break;
        }
    }
}
