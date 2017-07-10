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
import config.PersonConfig;
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
    private TextView mCityTv, mOnlineTv;//城市、对话城市、对话确定、对话取消、上线
    private ImageView mHeadLogoIv;//头Logo
    private PopupWindow mOptionPw;//弹窗

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

    public Handler cityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                Bundle bundle = msg.getData();
                mCityTv.setText(bundle.getString("city"));
            }
        }
    };

    public Handler handler = new Handler() {
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
                        loadVp();
                        int a;
                        for (int i = 0; i < imgurlList.size() + 2; i++) {
                            if (i == 0) {
                                a = imgurlList.size() - 1;
                            } else if (i == imgurlList.size() + 1) {
                                a = 0;
                            } else {
                                a = i - 1;
                            }
                            Picasso.with(getActivity()).load(imgurlList.get(a)).placeholder(mImageViewList.get(i).getDrawable()).into(mImageViewList.get(i));
                        }
                        Picasso.with(getActivity()).load(logoUrl).placeholder(mHeadLogoIv.getDrawable()).into(mHeadLogoIv);
                        break;
                    case REFRESHLOAD_DONE://刷新加载完成
                        mLv.hideHeadView();
                        myhomeAdapter.notifyDataSetChanged();
                        loadVp();
                        int b;
                        for (int i = 0; i < imgurlList.size() + 2; i++) {
                            if (i == 0) {
                                b = imgurlList.size() - 1;
                            } else if (i == imgurlList.size() + 1) {
                                b = 0;
                            } else {
                                b = i - 1;
                            }
                            Picasso.with(getActivity()).load(imgurlList.get(b)).placeholder(mImageViewList.get(i).getDrawable()).into(mImageViewList.get(i));
                        }
                        Picasso.with(getActivity()).load(logoUrl).placeholder(mHeadLogoIv.getDrawable()).into(mHeadLogoIv);
                        break;
                    case VIEWPAGER_SHOW://自动轮播
                        mVp.setCurrentItem(curPosition, true);
                        break;
                    case VIEWPAGER_RESHOW://重新轮播
                        isLoop = true;
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mOnlineOa.start();
            isLoop = true;
        } else {
            mOnlineOa.cancel();
            isLoop = false;
        }
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
        setData();
        setListener();
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

    private void initData() {
        myhomeAdapter = new FirstpageAdapter(getActivity(), goodsRecommendList, goodsPostList, goodsShowList);
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
        mImageViewList.clear();
        mImageViewDotList.clear();
        mVpLl.removeAllViews();
        curPosition = 1;
        dotPosition = 0;
        prePosition = 0;
        isLoop = true;
        ImageView imageView;
        ImageView imageViewDot;
        for (int i = 0; i < imgurlList.size() + 2; i++) {
            if (i == 0) {
                imageView = new ImageView(getActivity());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(R.mipmap.img_default);
                mImageViewList.add(imageView);
            } else if (i == imgurlList.size() + 1) {
                imageView = new ImageView(getActivity());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(R.mipmap.img_default);
                mImageViewList.add(imageView);
            } else {
                imageView = new ImageView(getActivity());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(R.mipmap.img_default);
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
                        handler.sendEmptyMessageDelayed(VIEWPAGER_RESHOW, PersonConfig.rotateCut_time);
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
                        SystemClock.sleep(PersonConfig.rotateCut_time);
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
                    goodsRecommend.setId1(o.optString("goods_id"));
                    goodsRecommend.setGoodsName1(o.optString("goods_name"));
                    goodsRecommend.setGoodsPrice1(o.optString("goods_promotion_price"));
                    goodsRecommend.setImgUrl1(o.optString("goods_image"));
                } else if (i == 1) {
                    goodsRecommend.setId2(o.optString("goods_id"));
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
                shoplistIntent.putExtra("stateId", 0);
                getActivity().startActivity(shoplistIntent);
                break;
            case R.id.rl_homehead_contract:
                Intent contractIntent = new Intent(getActivity(), ContractProjectActivity.class);
                contractIntent.putExtra("stateId", 1);
                getActivity().startActivity(contractIntent);
                break;
            case R.id.rl_homehead_digoucity:
                getActivity().startActivity(new Intent(getActivity(), DigouStoreActivity.class));
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

    @Override
    public void onDownPullRefresh() {
        loadData(LOAD_REFRESH);
    }

    @Override
    public void onLoadingMore() {
        mLv.hideFootView();
    }
}
