package com.gangjianwang.www.gangjianwang;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import config.NetConfig;
import config.ParaConfig;
import fragment.AllGoodsFragment;
import fragment.GoodsNewFragment;
import fragment.ShopActionFragment;
import fragment.ShopFirstpageFragment;
import utils.HeightUtils;
import utils.ToastUtils;
import utils.UserUtils;

public class StoreDetailActivity extends AppCompatActivity implements View.OnClickListener, ListItemClickHelp {

    private View rootView, popView;
    private RelativeLayout backRl, classifyRl;
    private EditText searchEt;
    private RelativeLayout shopIntroRl, freeTicketRl, contactServiceRl;
    private GridView mGv;
    private List<String> mGvList = new ArrayList<>();

    private LinearLayout shopFirstpageLl, allGoodsLl, goodsNewLl, shopActionLl;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private List<Fragment> fragmentList = new ArrayList<>();
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

    private PopupWindow voucherPop;
    private ImageView popCloseIv;
    private ListView voucherLv;
    private View voucherEmptyView;
    private ProgressDialog progressDialog;
    private OkHttpClient okHttpClient;
    private String key, store_id;

    private String imageUrl, name;
    private int collect;
    private boolean isFavorite;
    private ImageView imageIv;
    private TextView nameTv, favoriteTv, collectTv;
    private RelativeLayout favoriteRl;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case 0:
                        ToastUtils.toast(StoreDetailActivity.this, ParaConfig.NETWORK_ERROR);
                        break;
                    case 1:
                        setStoreInfo();
//                        initFragment();
                        break;
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
        handler.removeMessages(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(StoreDetailActivity.this, R.layout.activity_store_detail, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
    }

    private void initView() {
        initRoot();
        initProgress();
        initPop();
        initEmpty();
    }

    private void initRoot() {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_shopdetail_back);
        classifyRl = (RelativeLayout) rootView.findViewById(R.id.rl_shopdetail_classify);
        searchEt = (EditText) rootView.findViewById(R.id.et_shopdetail_search);
        shopIntroRl = (RelativeLayout) rootView.findViewById(R.id.rl_shopdetail_shopintro);
        freeTicketRl = (RelativeLayout) rootView.findViewById(R.id.rl_shopdetail_freeticket);
        contactServiceRl = (RelativeLayout) rootView.findViewById(R.id.rl_shopdetail_contactservice);
        mGv = (GridView) rootView.findViewById(R.id.gv_shopdetail);
        shopFirstpageLl = (LinearLayout) rootView.findViewById(R.id.ll_shopdetail_shopfirstpage);
        allGoodsLl = (LinearLayout) rootView.findViewById(R.id.ll_shopdetail_allgoods);
        goodsNewLl = (LinearLayout) rootView.findViewById(R.id.ll_shopdetail_goodsnew);
        shopActionLl = (LinearLayout) rootView.findViewById(R.id.ll_shopdetail_shopaction);

        imageIv = (ImageView) rootView.findViewById(R.id.iv_store_detail_image);
        nameTv = (TextView) rootView.findViewById(R.id.tv_store_detail_name);
        favoriteTv = (TextView) rootView.findViewById(R.id.tv_store_detail_favorite);
        collectTv = (TextView) rootView.findViewById(R.id.tv_store_detail_collect);
        favoriteRl = (RelativeLayout) rootView.findViewById(R.id.rl_store_detail_favorite);
    }

    private void initProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initPop() {
        popView = View.inflate(this, R.layout.pop_store_detail_voucher, null);
        popCloseIv = (ImageView) popView.findViewById(R.id.iv_pop_store_detail_voucher_close);
        voucherLv = (ListView) popView.findViewById(R.id.lv_pop_store_detail_voucher);
        voucherPop = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        voucherPop.setFocusable(true);
        voucherPop.setTouchable(true);
        voucherPop.setOutsideTouchable(false);
        voucherPop.setAnimationStyle(R.style.goodsdetailgoods_popwindow_anim);
        voucherPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }

    private void initEmpty() {
        voucherEmptyView = View.inflate(this, R.layout.empty, null);
        voucherEmptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ImageView) voucherEmptyView.findViewById(R.id.iv_empty_icon)).setImageResource(R.mipmap.mcc_08_b);
        ((TextView) voucherEmptyView.findViewById(R.id.tv_empty_hint)).setText("暂无代金券可以领取");
        ((TextView) voucherEmptyView.findViewById(R.id.tv_empty_content)).setText("店铺代金券可享受商品折扣");
        ((ViewGroup) voucherLv.getParent()).addView(voucherEmptyView);
        voucherLv.setEmptyView(voucherEmptyView);
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
        key = UserUtils.readLogin(this, true).getKey();
        store_id = "6";
    }

    private void initFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("store_id", store_id);
        ToastUtils.log(this, "store_id:" + store_id);
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
        shopFirstpageFragment.setArguments(bundle);
        SHOP_STATE = SHOP_FIRSTPAGE;
    }

    private void setData() {
    }

    private void loadData() {
        progressDialog.show();
        loadStoreInfoData();
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
    }

    private void loadStoreInfoData() {
        RequestBody body = new FormEncodingBuilder().add("key", key).add("store_id", store_id).build();
        Request request = new Request.Builder().url(NetConfig.storeDetailUrl).post(body).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful() && parseStoreInfoJson(response.body().string())) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    private boolean parseStoreInfoJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            if (objBean.optInt("code") == 200) {
                JSONObject objDatas = objBean.optJSONObject("datas");
                JSONObject objStoreInfo = objDatas.optJSONObject("store_info");
                name = objStoreInfo.optString("store_name");
                imageUrl = objStoreInfo.optString("store_avatar");
                isFavorite = objStoreInfo.optBoolean("is_favorate");
                collect = objStoreInfo.optInt("store_collect");
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setStoreInfo() {
        Picasso.with(this).load(imageUrl).placeholder(imageIv.getDrawable()).into(imageIv);
        nameTv.setText(name);
        collectTv.setText(collect + "");
        setFavorite();
    }

    private void setFavorite() {
        if (isFavorite) {
            favoriteRl.setBackgroundColor(Color.parseColor("#DB4453"));
            favoriteTv.setText("已收藏");
        } else {
            favoriteRl.setBackgroundColor(Color.parseColor("#333333"));
            favoriteTv.setText("收藏");
        }
    }

    private void setListener() {
        backRl.setOnClickListener(this);
        classifyRl.setOnClickListener(this);
        searchEt.setOnClickListener(this);
        shopIntroRl.setOnClickListener(this);
        freeTicketRl.setOnClickListener(this);
        contactServiceRl.setOnClickListener(this);
        shopFirstpageLl.setOnClickListener(this);
        allGoodsLl.setOnClickListener(this);
        goodsNewLl.setOnClickListener(this);
        shopActionLl.setOnClickListener(this);
        popCloseIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_shopdetail_back:
                finish();
                break;
            case R.id.rl_shopdetail_classify:
                ToastUtils.toast(StoreDetailActivity.this, "分类");
                break;
            case R.id.et_shopdetail_search:
                ToastUtils.toast(StoreDetailActivity.this, "搜索");
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
                Intent intent = new Intent(this, ShopIntroActivity.class);
                intent.putExtra("store_id", store_id);
                startActivity(intent);
                break;
            case R.id.rl_shopdetail_freeticket:
                WindowManager.LayoutParams lp1 = getWindow().getAttributes();
                lp1.alpha = 0.7f;
                getWindow().setAttributes(lp1);
                voucherPop.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.rl_shopdetail_contactservice:
                Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ParaConfig.SERVICE_PHONE));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intentPhone);
                break;
            case R.id.iv_pop_store_detail_voucher_close:
                voucherPop.dismiss();
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
