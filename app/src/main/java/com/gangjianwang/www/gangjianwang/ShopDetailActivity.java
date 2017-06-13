package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.GoodsDetailGoodsHeadGvAdapter;
import fragment.AllGoodsFragment;
import fragment.GoodsNewFragment;
import fragment.ShopActionFragment;
import fragment.ShopFirstpageFragment;
import utils.HeightUtils;
import utils.ToastUtils;

public class ShopDetailActivity extends AppCompatActivity implements View.OnClickListener, ListItemClickHelp {

    private View rootView;
    private RelativeLayout backRl, classifyRl, optionRl;
    private EditText searchEt;
    private RelativeLayout shopIntroRl, freeTicketRl, contactServiceRl;
    private RelativeLayout collectRl;
    private TextView collectTv, fanCountTv;
    private GridView mGv;
    private GoodsDetailGoodsHeadGvAdapter mGvAdapter;
    private List<String> mGvList = new ArrayList<>();
    private final int YES_COLLECT_STATE = 1;
    private final int NO_COLLECT_STATE = 2;
    private int COLLECT_STATE = NO_COLLECT_STATE;
    private int fanCount = 2;

    private LinearLayout shopFirstpageLl, allGoodsLl, goodsNewLl, shopActionLl;
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList;
    private ShopFirstpageFragment shopFirstpageFragment;
    private AllGoodsFragment allGoodsFragment;
    private GoodsNewFragment goodsNewFragment;
    private ShopActionFragment shopActionFragment;
    private final int SHOP_FIRSTPAGE = 1;
    private final int ALL_GOODS = 2;
    private final int GOODS_NEW = 3;
    private final int SHOP_ACTION = 4;
    private int SHOP_STATE;
    private int TARGET_STATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(ShopDetailActivity.this, R.layout.activity_shop_detail, null);
        setContentView(rootView);
        initView();
        setData();
        loadData();
        setListener();
    }

    private void initView() {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_shopdetail_back);
        classifyRl = (RelativeLayout) rootView.findViewById(R.id.rl_shopdetail_classify);
        optionRl = (RelativeLayout) rootView.findViewById(R.id.rl_shopdetail_option);
        searchEt = (EditText) rootView.findViewById(R.id.et_shopdetail_search);
        shopIntroRl = (RelativeLayout) rootView.findViewById(R.id.rl_shopdetail_shopintro);
        freeTicketRl = (RelativeLayout) rootView.findViewById(R.id.rl_shopdetail_freeticket);
        contactServiceRl = (RelativeLayout) rootView.findViewById(R.id.rl_shopdetail_contactservice);
        collectRl = (RelativeLayout) rootView.findViewById(R.id.rl_shopdetail_collect);
        collectTv = (TextView) rootView.findViewById(R.id.tv_shopdetail_collect);
        fanCountTv = (TextView) rootView.findViewById(R.id.tv_shopdetail_fancount);
        mGv = (GridView) rootView.findViewById(R.id.gv_shopdetail);

        shopFirstpageLl = (LinearLayout) rootView.findViewById(R.id.ll_shopdetail_shopfirstpage);
        allGoodsLl = (LinearLayout) rootView.findViewById(R.id.ll_shopdetail_allgoods);
        goodsNewLl = (LinearLayout) rootView.findViewById(R.id.ll_shopdetail_goodsnew);
        shopActionLl = (LinearLayout) rootView.findViewById(R.id.ll_shopdetail_shopaction);
        fragmentManager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        shopFirstpageFragment = new ShopFirstpageFragment();
        allGoodsFragment = new AllGoodsFragment();
        goodsNewFragment = new GoodsNewFragment();
        shopActionFragment = new ShopActionFragment();
        fragmentList.add(shopFirstpageFragment);
        fragmentList.add(allGoodsFragment);
        fragmentList.add(goodsNewFragment);
        fragmentList.add(shopActionFragment);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ll_shopdetail_sit, fragmentList.get(0));
        fragmentTransaction.commit();
        SHOP_STATE = SHOP_FIRSTPAGE;
    }

    private void setData() {
        mGvAdapter = new GoodsDetailGoodsHeadGvAdapter(this, mGvList, this);
        mGv.setAdapter(mGvAdapter);
    }

    private void loadData() {
        mGvList.add("管材(材质201)");
        mGvList.add("板材(材质201)");
        mGvList.add("阀门系列");
        mGvList.add("楼梯系列");
        mGvList.add("门系列");
        mGvList.add("门控系列");
        mGvList.add("装饰配件");
        mGvList.add("抛光材料");
        mGvList.add("焊接配件");
        mGvList.add("电动工具");
        HeightUtils.setGridViewHeight(mGv);
        mGvAdapter.notifyDataSetChanged();
    }

    private void setListener() {
        backRl.setOnClickListener(this);
        classifyRl.setOnClickListener(this);
        optionRl.setOnClickListener(this);
        searchEt.setOnClickListener(this);
        shopIntroRl.setOnClickListener(this);
        freeTicketRl.setOnClickListener(this);
        contactServiceRl.setOnClickListener(this);
        collectRl.setOnClickListener(this);
        shopFirstpageLl.setOnClickListener(this);
        allGoodsLl.setOnClickListener(this);
        goodsNewLl.setOnClickListener(this);
        shopActionLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_shopdetail_back:
                finish();
                break;
            case R.id.rl_shopdetail_classify:
                ToastUtils.toast(ShopDetailActivity.this, "分类");
                break;
            case R.id.rl_shopdetail_option:
                ToastUtils.toast(ShopDetailActivity.this, "选项");
                break;
            case R.id.et_shopdetail_search:
                ToastUtils.toast(ShopDetailActivity.this, "搜索");
                break;
            case R.id.rl_shopdetail_collect:
                switch (COLLECT_STATE) {
                    case YES_COLLECT_STATE:
                        collectTv.setText("收藏");
                        collectRl.setBackgroundColor(Color.parseColor("#333333"));
                        fanCount--;
                        fanCountTv.setText(fanCount + "");
                        COLLECT_STATE = NO_COLLECT_STATE;
                        break;
                    case NO_COLLECT_STATE:
                        collectTv.setText("已收藏");
                        collectRl.setBackgroundColor(Color.parseColor("#DB4453"));
                        fanCount++;
                        fanCountTv.setText(fanCount + "");
                        COLLECT_STATE = YES_COLLECT_STATE;
                        break;
                    default:
                        break;
                }
                break;
            case R.id.ll_shopdetail_shopfirstpage:
                TARGET_STATE = SHOP_FIRSTPAGE;
                changFrag(TARGET_STATE);
                break;
            case R.id.ll_shopdetail_allgoods:
                TARGET_STATE = ALL_GOODS;
                changFrag(TARGET_STATE);
                break;
            case R.id.ll_shopdetail_goodsnew:
                TARGET_STATE = GOODS_NEW;
                changFrag(TARGET_STATE);
                break;
            case R.id.ll_shopdetail_shopaction:
                TARGET_STATE = SHOP_ACTION;
                changFrag(TARGET_STATE);
                break;
            case R.id.rl_shopdetail_shopintro:
                startActivity(new Intent(this, ShopIntroActivity.class));
                break;
            case R.id.rl_shopdetail_freeticket:
                ToastUtils.toast(ShopDetailActivity.this, "免费领券");
                break;
            case R.id.rl_shopdetail_contactservice:
                ToastUtils.toast(ShopDetailActivity.this, "联系客服");
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View item, View widget, int position, int which, boolean isChecked) {

    }

    private void changFrag(int TARGET_STATE) {
        if (SHOP_STATE != TARGET_STATE) {
            Fragment curFragment = fragmentList.get(SHOP_STATE - 1);
            Fragment tarFragment = fragmentList.get(TARGET_STATE - 1);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(curFragment);
            if (tarFragment.isAdded()) {
                fragmentTransaction.show(tarFragment);
            } else {
                fragmentTransaction.add(R.id.ll_shopdetail_sit, tarFragment);
            }
            fragmentTransaction.commit();
            SHOP_STATE = TARGET_STATE;
        }
    }
}
