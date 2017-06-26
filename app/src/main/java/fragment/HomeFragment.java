package fragment;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.gangjianwang.www.gangjianwang.ChangeCityActivity;
import com.gangjianwang.www.gangjianwang.ClassifyActivity;
import com.gangjianwang.www.gangjianwang.ContractProjectActivity;
import com.gangjianwang.www.gangjianwang.DigouStoreActivity;
import com.gangjianwang.www.gangjianwang.GjSpecialSaleActivity;
import com.gangjianwang.www.gangjianwang.HomeActivity;
import com.gangjianwang.www.gangjianwang.MessageActivity;
import com.gangjianwang.www.gangjianwang.R;
import com.gangjianwang.www.gangjianwang.SearchActivity;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.FirstpageAdapter;
import adapter.MyPagerAdapter;
import bean.GoodsPost;
import bean.GoodsRecommend;
import bean.GoodsShow;
import config.NetConfig;
import customview.MyRefreshListView;
import customview.OnRefreshListener;
import utils.ToastUtils;

/**
 * Created by 孙明明 on 2017/4/10 0010.
 */

public class HomeFragment extends Fragment implements View.OnClickListener, OnRefreshListener {

    private View rootView, headView, footView, diglogView, optionPopWindowView;//根、头、尾、对话、弹窗
    private RelativeLayout mTopRl, mContactRl, mOptionPwcloseRl;//Bar、Tel、关弹窗
    private RelativeLayout mClassifyRl, mShopListRl, mContractRl, mDigouCityRl;//分类、店铺、承揽、底购
    private RelativeLayout changeCityRl, searchRl, optionRl, mPopFirstpageRl, mPopShopcarRl, mPopMymallRl, mPopMessageRl;//城市、搜索、选项、弹窗首页、弹窗购物车、弹窗商城、弹窗消息
    private TextView mCityTv, mCityNameTv, mDialogSureTv, mDialogCancelTv, mOnlineTv;//城市、对话城市、对话确定、对话取消、上线
    private ImageView mHeadLogoIv;//头Logo
    private PopupWindow mOptionPw;//弹窗
    private AlertDialog ad;//对话框

    //轮播图
    private ViewPager mVp;
    private LinearLayout mVpLl;
    private MyPagerAdapter mMyPagerAdapter;
    private List<String> imgurlList = new ArrayList<>();
    private List<ImageView> mImageViewList = new ArrayList<>();
    private List<ImageView> mImageViewDotList = new ArrayList<>();
    private int curPosition = 1;
    private int dotPosition = 0;
    private int prePosition = 0;
    private boolean isLoop = true;

    private String logoUrl;
    private ObjectAnimator mOnlineOa;
    private MyRefreshListView mLv;
    private List<GoodsRecommend> goodsRecommendList = new ArrayList<>();
    private List<GoodsPost> goodsPostList = new ArrayList<>();
    private List<GoodsShow> goodsShowList = new ArrayList<>();
    private FirstpageAdapter myhomeAdapter;
    private Handler mChangeFragHandler;
    public LocationClient mLocationClient;
    public BDLocationListener myListener = new MyLocationListener();
    private String cityName;
    private boolean isHidden = true;
    private final int LOAD_FIRST = 0;
    private final int LOAD_REFRESH = 1;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private ProgressDialog mPd;
    private final int FIRSTLOAD_NONET = 0;
    private final int REFRESHLOAD_NONET = 1;
    private final int FIRSTLOAD_DONE = 2;
    private final int REFRESHLOAD_DONE = 3;
    private final int VIEWPAGER_SHOW = 4;
    private final int VIEWPAGER_RESHOW = 5;
    private final int CHANGE_CITY = 6;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case FIRSTLOAD_NONET://首次加载无网络
                        mPd.dismiss();
                        ToastUtils.toast(getActivity(), "首次加载无网络");
                        break;
                    case REFRESHLOAD_NONET://刷新加载无网络
                        mLv.hideHeadView();
                        ToastUtils.toast(getActivity(), "刷新加载无网络");
                        break;
                    case FIRSTLOAD_DONE://首次加载完成
                        mPd.dismiss();
                        myhomeAdapter.notifyDataSetChanged();
                        Picasso.with(getActivity()).load(logoUrl).placeholder(mHeadLogoIv.getDrawable()).into(mHeadLogoIv);
                        loadVp();
                        break;
                    case REFRESHLOAD_DONE://刷新加载完成
                        mLv.hideHeadView();
                        myhomeAdapter.notifyDataSetChanged();
                        break;
                    case VIEWPAGER_SHOW://自动轮播
                        mVp.setCurrentItem(curPosition, true);
                        break;
                    case VIEWPAGER_RESHOW://重新轮播
                        isLoop = true;
                        break;
                    case CHANGE_CITY:
                        mCityNameTv.setText(cityName);
                        ad.dismiss();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    /**
     * 钢建网上线动画开始or停止
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isHidden) {
            mOnlineOa.cancel();
        } else {
            mOnlineOa.start();
        }
        isHidden = !isHidden;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeActivity homeActivity = (HomeActivity) getActivity();
        mChangeFragHandler = homeActivity.mChangeFragHandler;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, null);
        initView();
        initData();
        initLoc();
        setData();
        setListener();
        mLocationClient.start();
        loadData(LOAD_FIRST);
        return rootView;
    }

    private void initView() {
        mTopRl = (RelativeLayout) rootView.findViewById(R.id.rl_homefrag_up);
        changeCityRl = (RelativeLayout) rootView.findViewById(R.id.rl_home_changecity);
        mCityTv = (TextView) rootView.findViewById(R.id.tv_home_city);
        searchRl = (RelativeLayout) rootView.findViewById(R.id.rl_home_search);
        optionRl = (RelativeLayout) rootView.findViewById(R.id.rl_home_option);
        mLv = (MyRefreshListView) rootView.findViewById(R.id.mlv_home);
        initHead();
        initPop();
        initDia();
        initAnim();
        mLv.addHeaderView(headView);
        mLv.addFooterView(footView);
    }

    private void initHead() {
        headView = View.inflate(getActivity(), R.layout.head_home_cycle, null);
        mHeadLogoIv = (ImageView) headView.findViewById(R.id.iv_home_head_logo);
        mVp = (ViewPager) headView.findViewById(R.id.vp_home_cycle);
        mVpLl = (LinearLayout) headView.findViewById(R.id.ll_home_cycle_dot);
        footView = View.inflate(getActivity(), R.layout.foot_home_contact, null);
        mContactRl = (RelativeLayout) footView.findViewById(R.id.rl_foot_home_contact);
        mClassifyRl = (RelativeLayout) headView.findViewById(R.id.rl_homehead_classify);
        mShopListRl = (RelativeLayout) headView.findViewById(R.id.rl_homehead_shoplist);
        mContractRl = (RelativeLayout) headView.findViewById(R.id.rl_homehead_contract);
        mDigouCityRl = (RelativeLayout) headView.findViewById(R.id.rl_homehead_digoucity);
        mOnlineTv = (TextView) headView.findViewById(R.id.tv_home_head_online);
    }

    private void initPop() {
        optionPopWindowView = getActivity().getLayoutInflater().inflate(R.layout.popupwindow_home_option, null);
        mOptionPw = new PopupWindow(optionPopWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mOptionPw.setFocusable(true);
        mOptionPw.setTouchable(true);
        mOptionPw.setOutsideTouchable(true);
        mOptionPw.setBackgroundDrawable(new BitmapDrawable());
        mOptionPw.setAnimationStyle(R.style.homeoption_popwindow_anim);
        mOptionPwcloseRl = (RelativeLayout) optionPopWindowView.findViewById(R.id.rl_home_pop_close);
        mPopFirstpageRl = (RelativeLayout) optionPopWindowView.findViewById(R.id.rl_home_pop_firstpage);
        mPopShopcarRl = (RelativeLayout) optionPopWindowView.findViewById(R.id.rl_home_pop_shopcar);
        mPopMymallRl = (RelativeLayout) optionPopWindowView.findViewById(R.id.rl_home_pop_mymall);
        mPopMessageRl = (RelativeLayout) optionPopWindowView.findViewById(R.id.rl_home_pop_message);
    }

    private void initDia() {
        diglogView = View.inflate(getContext(), R.layout.dialog_city, null);
        mCityNameTv = (TextView) diglogView.findViewById(R.id.tv_dialog_city_cityname);
        mDialogSureTv = (TextView) diglogView.findViewById(R.id.tv_dialog_sure);
        mDialogCancelTv = (TextView) diglogView.findViewById(R.id.tv_dialog_cancel);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(diglogView);
        ad = builder.create();
    }

    private void initData() {
        myhomeAdapter = new FirstpageAdapter(getActivity(), goodsRecommendList, goodsPostList, goodsShowList);
    }

    private void initLoc() {
        mLocationClient = new LocationClient(getContext());
        mLocationClient.registerLocationListener(myListener);
        initLocation();
    }

    private void initAnim() {
        mPd = new ProgressDialog(getActivity());
        mOnlineOa = ObjectAnimator.ofFloat(mOnlineTv, "translationY", 0.0F, -100.0F);
        mOnlineOa.setDuration(8000);
        mOnlineOa.setRepeatCount(-1);
        mOnlineOa.start();
    }

    private void setData() {
        mLv.setAdapter(myhomeAdapter);
    }

    private void setListener() {
        changeCityRl.setOnClickListener(this);
        searchRl.setOnClickListener(this);
        optionRl.setOnClickListener(this);
        mHeadLogoIv.setOnClickListener(this);
        mContactRl.setOnClickListener(this);
        mOptionPwcloseRl.setOnClickListener(this);
        mPopFirstpageRl.setOnClickListener(this);
        mPopShopcarRl.setOnClickListener(this);
        mPopMymallRl.setOnClickListener(this);
        mPopMessageRl.setOnClickListener(this);
        mClassifyRl.setOnClickListener(this);
        mShopListRl.setOnClickListener(this);
        mContractRl.setOnClickListener(this);
        mDigouCityRl.setOnClickListener(this);
        mDialogSureTv.setOnClickListener(this);
        mDialogCancelTv.setOnClickListener(this);
        mLv.setOnRefreshListener(this);
    }

    private void loadData(int LOAD_STATE) {
        switch (LOAD_STATE) {
            case LOAD_FIRST:
                mPd.show();
                Request requestFirst = new Request.Builder().url(NetConfig.homeUrl).get().build();
                okHttpClient.newCall(requestFirst).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        handler.sendEmptyMessage(FIRSTLOAD_NONET);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String json = response.body().string();
                            if (parseJson(json))
                                handler.sendEmptyMessage(FIRSTLOAD_DONE);
                        }
                    }
                });
                break;
            case LOAD_REFRESH:
                Request requestRefresh = new Request.Builder().url(NetConfig.homeUrl).get().build();
                okHttpClient.newCall(requestRefresh).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        handler.sendEmptyMessage(REFRESHLOAD_NONET);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            imgurlList.clear();
                            goodsRecommendList.clear();
                            goodsPostList.clear();
                            goodsShowList.clear();
                            String json = response.body().string();
                            if (parseJson(json))
                                handler.sendEmptyMessage(REFRESHLOAD_DONE);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    private void loadVp() {
        ImageView imageView;
        ImageView imageViewDot;
        for (int i = 0; i < imgurlList.size() + 2; i++) {
            if (i == 0) {
                imageView = new ImageView(getActivity());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(R.mipmap.img_default);
                Picasso.with(getActivity()).load(imgurlList.get(imgurlList.size() - 1)).placeholder(imageView.getDrawable()).into(imageView);
                mImageViewList.add(imageView);
            } else if (i == imgurlList.size() + 1) {
                imageView = new ImageView(getActivity());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(R.mipmap.img_default);
                Picasso.with(getActivity()).load(imgurlList.get(0)).placeholder(imageView.getDrawable()).into(imageView);
                mImageViewList.add(imageView);
            } else {
                imageView = new ImageView(getActivity());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(R.mipmap.img_default);
                Picasso.with(getActivity()).load(imgurlList.get(i - 1)).placeholder(imageView.getDrawable()).into(imageView);
                mImageViewList.add(imageView);
            }
        }
        for (int i = 0; i < imgurlList.size(); i++) {
            imageViewDot = new ImageView(getActivity());
            imageViewDot.setImageResource(R.drawable.points_off);
            imageViewDot.setPadding(5, 0, 5, 0);
            mVpLl.addView(imageViewDot);
            mImageViewDotList.add(imageViewDot);
        }
        mImageViewDotList.get(dotPosition).setImageResource(R.drawable.points_on);
        mMyPagerAdapter = new MyPagerAdapter(mImageViewList);
        mVp.setAdapter(mMyPagerAdapter);
        mVp.setCurrentItem(curPosition);
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    curPosition = imgurlList.size();
                    dotPosition = imgurlList.size() - 1;
                } else if (position == imgurlList.size() + 1) {
                    curPosition = 1;
                    dotPosition = 0;
                } else {
                    curPosition = position;
                    dotPosition = position - 1;
                }
                mImageViewDotList.get(prePosition).setImageResource(R.drawable.points_off);
                mImageViewDotList.get(dotPosition).setImageResource(R.drawable.points_on);
                prePosition = dotPosition;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mVp.setCurrentItem(curPosition, false);
                }
            }
        });
        mVp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isLoop = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isLoop = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.sendEmptyMessageDelayed(VIEWPAGER_RESHOW, 5000);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        autoPlay();
    }

    private void autoPlay() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    if (isLoop) {
                        SystemClock.sleep(3000);
                        curPosition++;
                        handler.sendEmptyMessage(VIEWPAGER_SHOW);
                    }
                }
            }
        }.start();
    }

    private boolean parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            JSONArray arrDatas = objBean.optJSONArray("datas");
            JSONObject objAdvList = arrDatas.optJSONObject(0).optJSONObject("adv_list");
            JSONArray arrItem = objAdvList.optJSONArray("item");
            for (int i = 0; i < arrItem.length(); i++) {
                JSONObject o = arrItem.optJSONObject(i);
                String image = o.optString("image");
                imgurlList.add(image);
            }
            JSONObject objHome1 = arrDatas.optJSONObject(1).optJSONObject("home1");
            String image = objHome1.optString("image");
            logoUrl = image;
            GoodsRecommend goodsRecommend = new GoodsRecommend();
            JSONObject objGoods = arrDatas.optJSONObject(2).optJSONObject("goods");
            goodsRecommend.setTitle(objGoods.optString("title"));
            JSONArray arrItems = objGoods.optJSONArray("item");
            for (int i = 0; i < arrItems.length(); i++) {
                JSONObject o = arrItems.optJSONObject(i);
                if (i == 0) {
                    goodsRecommend.setGoodsName1(o.optString("goods_name"));
                    goodsRecommend.setGoodsPrice1(o.optString("goods_promotion_price"));
                    goodsRecommend.setImgUrl1(o.optString("goods_image"));
                } else if (i == 1) {
                    goodsRecommend.setGoodsName2(o.optString("goods_name"));
                    goodsRecommend.setGoodsPrice2(o.optString("goods_promotion_price"));
                    goodsRecommend.setImgUrl2(o.optString("goods_image"));
                }
            }
            goodsRecommendList.add(goodsRecommend);
            GoodsPost goodsPost = new GoodsPost();
            JSONObject objGoodsPost = arrDatas.optJSONObject(4).optJSONObject("home1");
            goodsPost.setImgUrl(objGoodsPost.optString("image"));
            goodsPostList.add(goodsPost);
            GoodsShow goodsShow1 = new GoodsShow();
            JSONObject objGoodsShow1 = arrDatas.optJSONObject(3).optJSONObject("home3");
            goodsShow1.setTitle(objGoodsShow1.optString("title"));
            JSONArray arrGoodsShow = objGoodsShow1.optJSONArray("item");
            for (int i = 0; i < arrGoodsShow.length(); i++) {
                JSONObject o = arrGoodsShow.optJSONObject(i);
                switch (i) {
                    case 0:
                        goodsShow1.setImgUrl1(o.optString("image"));
                        break;
                    case 1:
                        goodsShow1.setImgUrl2(o.optString("image"));
                        break;
                    case 2:
                        goodsShow1.setImgUrl3(o.optString("image"));
                        break;
                    case 3:
                        goodsShow1.setImgUrl4(o.optString("image"));
                        break;
                    case 4:
                        goodsShow1.setImgUrl5(o.optString("image"));
                        break;
                    case 5:
                        goodsShow1.setImgUrl6(o.optString("image"));
                        break;
                    default:
                        break;
                }
            }
            GoodsShow goodsShow2 = new GoodsShow();
            JSONObject objGoodsShow2 = arrDatas.optJSONObject(5).optJSONObject("home3");
            goodsShow2.setTitle(objGoodsShow2.optString("title"));
            JSONArray arrGoodsShow2 = objGoodsShow2.optJSONArray("item");
            for (int i = 0; i < arrGoodsShow2.length(); i++) {
                JSONObject o = arrGoodsShow2.optJSONObject(i);
                switch (i) {
                    case 0:
                        goodsShow2.setImgUrl1(o.optString("image"));
                        break;
                    case 1:
                        goodsShow2.setImgUrl2(o.optString("image"));
                        break;
                    case 2:
                        goodsShow2.setImgUrl3(o.optString("image"));
                        break;
                    case 3:
                        goodsShow2.setImgUrl4(o.optString("image"));
                        break;
                    case 4:
                        goodsShow2.setImgUrl5(o.optString("image"));
                        break;
                    case 5:
                        goodsShow2.setImgUrl6(o.optString("image"));
                        break;
                    default:
                        break;
                }
            }
            GoodsShow goodsShow3 = new GoodsShow();
            JSONObject objGoodsShow3 = arrDatas.optJSONObject(6).optJSONObject("home3");
            goodsShow3.setTitle(objGoodsShow3.optString("title"));
            JSONArray arrGoodsShow3 = objGoodsShow3.optJSONArray("item");
            for (int i = 0; i < arrGoodsShow3.length(); i++) {
                JSONObject o = arrGoodsShow3.optJSONObject(i);
                switch (i) {
                    case 0:
                        goodsShow3.setImgUrl1(o.optString("image"));
                        goodsShow3.setData1(o.optString("data"));
                        break;
                    case 1:
                        goodsShow3.setImgUrl2(o.optString("image"));
                        goodsShow3.setData2(o.optString("data"));
                        break;
                    case 2:
                        goodsShow3.setImgUrl3(o.optString("image"));
                        goodsShow3.setData3(o.optString("data"));
                        break;
                    case 3:
                        goodsShow3.setImgUrl4(o.optString("image"));
                        goodsShow3.setData4(o.optString("data"));
                        break;
                    case 4:
                        goodsShow3.setImgUrl5(o.optString("image"));
                        goodsShow3.setData5(o.optString("data"));
                        break;
                    case 5:
                        goodsShow3.setImgUrl6(o.optString("image"));
                        goodsShow3.setData6(o.optString("data"));
                        break;
                    default:
                        break;
                }
            }
            goodsShowList.add(goodsShow1);
            goodsShowList.add(goodsShow2);
            goodsShowList.add(goodsShow3);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_home_changecity:
                startActivityForResult(new Intent(getActivity(), ChangeCityActivity.class), 1);
                break;
            case R.id.rl_home_search:
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.rl_home_option:
                if (mOptionPw.isShowing()) {
                    mOptionPw.dismiss();
                } else {
                    mOptionPw.showAtLocation(mTopRl, Gravity.TOP, 0, 0);
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 0.7f;
                    getActivity().getWindow().setAttributes(lp);
                    mOptionPw.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                            lp.alpha = 1f;
                            getActivity().getWindow().setAttributes(lp);
                        }
                    });
                }
                break;
            case R.id.iv_home_head_logo:
                getActivity().startActivity(new Intent(getActivity(), GjSpecialSaleActivity.class));
                break;
            case R.id.rl_foot_home_contact:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:400-0788-889"));
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                getActivity().startActivity(intent);
                break;
            case R.id.rl_home_pop_close:
                mOptionPw.dismiss();
                break;
            case R.id.rl_home_pop_firstpage:
                mOptionPw.dismiss();
                mChangeFragHandler.sendEmptyMessage(0);
                break;
            case R.id.rl_home_pop_shopcar:
                mOptionPw.dismiss();
                mChangeFragHandler.sendEmptyMessage(3);
                break;
            case R.id.rl_home_pop_mymall:
                mOptionPw.dismiss();
                mChangeFragHandler.sendEmptyMessage(4);
                break;
            case R.id.rl_home_pop_message:
                mOptionPw.dismiss();
                getActivity().startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.rl_homehead_classify:
                getActivity().startActivity(new Intent(getActivity(), ClassifyActivity.class));
                break;
            case R.id.rl_homehead_shoplist:
                Intent shoplistIntent = new Intent(getActivity(), ContractProjectActivity.class);
                shoplistIntent.putExtra("stateId", 1);
                getActivity().startActivity(shoplistIntent);
                break;
            case R.id.rl_homehead_contract:
                Intent contractIntent = new Intent(getActivity(), ContractProjectActivity.class);
                contractIntent.putExtra("stateId", 2);
                getActivity().startActivity(contractIntent);
                break;
            case R.id.rl_homehead_digoucity:
                getActivity().startActivity(new Intent(getActivity(), DigouStoreActivity.class));
                break;
            case R.id.tv_dialog_sure:
                if (cityName != null) {
                    ad.dismiss();
                    mCityTv.setText(cityName);
                }
                break;
            case R.id.tv_dialog_cancel:
                ad.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            mCityTv.setText(data.getStringExtra("cityName"));
        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onDownPullRefresh() {
        loadData(LOAD_REFRESH);
    }

    @Override
    public void onLoadingMore() {
        mLv.hideFootView();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取定位结果
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间
            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息
            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数
                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息
            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            String str = sb.toString();
            int provinceIndex = str.indexOf("省");
            int cityIndex = str.indexOf("市");
            String s = str.substring(provinceIndex + 1, cityIndex);
            if (!s.equals(mCityTv.getText().toString())) {
                cityName = s;
                handler.sendEmptyMessage(CHANGE_CITY);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}
