package com.gangjianwang.www.gangjianwang;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import adapter.InquiryListFragmentPagerAdapter;
import fragment.InquiryListAllFragment;
import fragment.InquiryListModifiedFragment;
import fragment.InquiryListOrderedFragment;
import fragment.InquiryListUnmodifiedFragment;
import fragment.InquiryListUnorderedFragment;

public class InquiryListActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout mBackRl;
    private TabLayout mTitleTl;
    private ViewPager mVp;
    private String[] tlTitle;
    private List<Fragment> fragmentList;
    private InquiryListAllFragment inquiryListAllFragment;
    private InquiryListUnmodifiedFragment inquiryListUnmodifiedFragment;
    private InquiryListModifiedFragment inquiryListModifiedFragment;
    private InquiryListUnorderedFragment inquiryListUnorderedFragment;
    private InquiryListOrderedFragment inquiryListOrderedFragment;
    private InquiryListFragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_inquiry_list, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
    }

    private void initView() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_inquirylist_back);
        mTitleTl = (TabLayout) rootView.findViewById(R.id.tl_inquirylist);
        mVp = (ViewPager) rootView.findViewById(R.id.vp_inquirylist);
    }

    private void initData() {
        tlTitle = getResources().getStringArray(R.array.inquirylist_title);
        fragmentList = new ArrayList<>();
        inquiryListAllFragment = new InquiryListAllFragment();
        inquiryListUnmodifiedFragment = new InquiryListUnmodifiedFragment();
        inquiryListModifiedFragment = new InquiryListModifiedFragment();
        inquiryListUnorderedFragment = new InquiryListUnorderedFragment();
        inquiryListOrderedFragment = new InquiryListOrderedFragment();
        fragmentList.add(inquiryListAllFragment);
        fragmentList.add(inquiryListUnmodifiedFragment);
        fragmentList.add(inquiryListModifiedFragment);
        fragmentList.add(inquiryListUnorderedFragment);
        fragmentList.add(inquiryListOrderedFragment);
        fragmentPagerAdapter = new InquiryListFragmentPagerAdapter(getSupportFragmentManager(), tlTitle, fragmentList);
    }

    private void setData() {
        mVp.setAdapter(fragmentPagerAdapter);
        mTitleTl.setupWithViewPager(mVp);
        mTitleTl.setTabTextColors(Color.BLACK, Color.RED);
        mTitleTl.setSelectedTabIndicatorColor(Color.RED);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_inquirylist_back:
                finish();
                break;
            default:
                break;
        }
    }
}
