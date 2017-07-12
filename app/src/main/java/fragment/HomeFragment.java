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
import com.gangjianwang.www.gangjianwang.StoreActivity;
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

import adapter.HomeAdapter;
import adapter.MyPagerAdapter;
import bean.HomeBlockInner;
import bean.HomeBlockOuter;
import bean.HomeGoodsInner;
import bean.HomeGoodsOuter;
import bean.HomePost;
import config.NetConfig;
import config.ParaConfig;
import config.PersonConfig;
import customview.MyRefreshListView;
import customview.OnRefreshListener;
import utils.ToastUtils;

/**
 * Created by 孙明明 on 2017/4/10 0010.
 */

public class HomeFragment extends Fragment implements View.OnClickListener, OnRefreshListener {

    private View rootView, headView, footView, optionPopWindowView;
    private RelativeLayout mTopRl, mContactRl, mOptionPwcloseRl;
    private RelativeLayout mClassifyRl, mShopListRl, mContractRl, mDigouCityRl;
    private RelativeLayout changeCityRl, searchRl, optionRl, mPopFirstpageRl, mPopShopcarRl, mPopMymallRl, mPopMessageRl;//城市、搜索、选项、弹窗首页、弹窗购物车、弹窗商城、弹窗消息
    private TextView mCityTv, mOnlineTv;
    private ImageView mHeadLogoIv;
    private PopupWindow mOptionPw;

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
    private MyRefreshListView lv;
    private Handler mChangeFragHandler;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private ProgressDialog progressDialog;
    private int STATE = ParaConfig.FIRST;
    private List<HomeGoodsOuter> homeGoodsOuterList = new ArrayList<>();
    private List<HomeBlockOuter> homeBlockOuterList = new ArrayList<>();
    private List<HomePost> homePostList = new ArrayList<>();
    private HomeAdapter homeAdapter;

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
                    case 0:
                        switch (STATE) {
                            case ParaConfig.FIRST:
                                progressDialog.dismiss();
                                ToastUtils.toast(getActivity(), ParaConfig.NETWORK_ERROR);
                                break;
                            case ParaConfig.REFRESH:
                                lv.hideHeadView();
                                ToastUtils.toast(getActivity(), ParaConfig.REFRESH_DEFEAT_ERROR);
                                break;
                            default:
                                break;
                        }
                        homeAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        switch (STATE) {
                            case ParaConfig.FIRST:
                                progressDialog.dismiss();
                                createViewPager();
                                break;
                            case ParaConfig.REFRESH:
                                lv.hideHeadView();
                                loadViewPager();
                                STATE = ParaConfig.FIRST;
                                ToastUtils.toast(getActivity(), ParaConfig.REFRESH_SUCCESS);
                                break;
                            default:
                                break;
                        }
                        homeAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        mVp.setCurrentItem(curPosition, true);
                        break;
                    case 3:
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
        loadData();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
        handler.removeMessages(1);
        handler.removeMessages(2);
        handler.removeMessages(3);
    }

    private void initView() {
        initRoot();
        initHead();
        initFoot();
        initPop();
        initAnim();
        initProgress();
    }

    private void initRoot() {
        mTopRl = (RelativeLayout) rootView.findViewById(R.id.rl_homefrag_up);
        changeCityRl = (RelativeLayout) rootView.findViewById(R.id.rl_home_changecity);
        mCityTv = (TextView) rootView.findViewById(R.id.tv_home_city);
        searchRl = (RelativeLayout) rootView.findViewById(R.id.rl_home_search);
        optionRl = (RelativeLayout) rootView.findViewById(R.id.rl_home_option);
        lv = (MyRefreshListView) rootView.findViewById(R.id.mlv_home);
    }

    private void initHead() {
        headView = View.inflate(getActivity(), R.layout.head_home_cycle, null);
        mHeadLogoIv = (ImageView) headView.findViewById(R.id.iv_home_head_logo);
        mVp = (ViewPager) headView.findViewById(R.id.vp_home_cycle);
        mVpLl = (LinearLayout) headView.findViewById(R.id.ll_home_cycle_dot);
        mClassifyRl = (RelativeLayout) headView.findViewById(R.id.rl_homehead_classify);
        mShopListRl = (RelativeLayout) headView.findViewById(R.id.rl_homehead_shoplist);
        mContractRl = (RelativeLayout) headView.findViewById(R.id.rl_homehead_contract);
        mDigouCityRl = (RelativeLayout) headView.findViewById(R.id.rl_homehead_digoucity);
        mOnlineTv = (TextView) headView.findViewById(R.id.tv_home_head_online);
        lv.addHeaderView(headView);
    }

    private void initFoot() {
        footView = View.inflate(getActivity(), R.layout.foot_home_contact, null);
        mContactRl = (RelativeLayout) footView.findViewById(R.id.rl_foot_home_contact);
        lv.addFooterView(footView);
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
        homeAdapter = new HomeAdapter(getActivity(), homeGoodsOuterList, homeBlockOuterList, homePostList);
    }

    private void initAnim() {
        mOnlineOa = ObjectAnimator.ofFloat(mOnlineTv, "translationY", 0.0F, -100.0F);
        mOnlineOa.setDuration(8000);
        mOnlineOa.setRepeatCount(-1);
        mOnlineOa.start();
    }

    private void initProgress() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void setData() {
        lv.setAdapter(homeAdapter);
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
        lv.setOnRefreshListener(this);
    }

    private void loadData() {
        if (STATE == ParaConfig.FIRST) {
            progressDialog.show();
        }
        Request request = new Request.Builder().url(NetConfig.homeUrl).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (STATE == ParaConfig.REFRESH) {
                        imgurlList.clear();
                        homeGoodsOuterList.clear();
                        homeBlockOuterList.clear();
                        homePostList.clear();
                    }
                    String json = response.body().string();
                    if (parseJson(json))
                        handler.sendEmptyMessage(1);
                }
            }
        });
    }

    private void createViewPager() {
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
                        handler.sendEmptyMessageDelayed(3, PersonConfig.rotateCut_time);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        autoPlay();
        loadViewPager();
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
                        handler.sendEmptyMessage(2);
                    }
                }
            }
        }.start();
    }

    private void loadViewPager() {
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
    }

    private boolean parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            if (objBean.optInt("code") == 200) {
                JSONArray arrDatas = objBean.optJSONArray("datas");
                JSONObject objAdvList = arrDatas.optJSONObject(0).optJSONObject("adv_list");
                JSONArray arrItem0 = objAdvList.optJSONArray("item");
                for (int i = 0; i < arrItem0.length(); i++) {
                    JSONObject o0 = arrItem0.optJSONObject(i);
                    imgurlList.add(o0.optString("image"));
                }
                JSONObject objHome1 = arrDatas.optJSONObject(1).optJSONObject("home1");
                logoUrl = objHome1.optString("image");
                JSONObject objGoods = arrDatas.optJSONObject(2).optJSONObject("goods");
                HomeGoodsOuter homeGoodsOuter = new HomeGoodsOuter();
                homeGoodsOuter.setTitle(objGoods.optString("title"));
                List<HomeGoodsInner> homeGoodsInnerList = new ArrayList<>();
                JSONArray arrItem2 = objGoods.optJSONArray("item");
                for (int i = 0; i < arrItem2.length(); i++) {
                    JSONObject o2 = arrItem2.optJSONObject(i);
                    HomeGoodsInner homeGoodsInner = new HomeGoodsInner();
                    homeGoodsInner.setId(o2.optString("goods_id"));
                    homeGoodsInner.setName(o2.optString("goods_name"));
                    homeGoodsInner.setPrice(o2.optString("goods_promotion_price"));
                    homeGoodsInner.setImage(o2.optString("goods_image"));
                    homeGoodsInnerList.add(homeGoodsInner);
                }
                homeGoodsOuter.setHomeGoodsInnerList(homeGoodsInnerList);
                homeGoodsOuterList.add(homeGoodsOuter);
                JSONObject objBlock1 = arrDatas.optJSONObject(3).optJSONObject("home3");
                HomeBlockOuter homeBlockOuter1 = new HomeBlockOuter();
                homeBlockOuter1.setTitle(objBlock1.optString("title"));
                List<HomeBlockInner> homeBlockInnerList1 = new ArrayList<>();
                JSONArray arrBlock1 = objBlock1.optJSONArray("item");
                for (int i = 0; i < arrBlock1.length(); i++) {
                    JSONObject o3 = arrBlock1.optJSONObject(i);
                    HomeBlockInner homeBlockInner = new HomeBlockInner();
                    homeBlockInner.setImage(o3.optString("image"));
                    homeBlockInner.setType(o3.optString("type"));
                    homeBlockInner.setData(o3.optString("data"));
                    homeBlockInnerList1.add(homeBlockInner);
                }
                homeBlockOuter1.setHomeBlockInnerList(homeBlockInnerList1);
                JSONObject objBlock2 = arrDatas.optJSONObject(5).optJSONObject("home3");
                HomeBlockOuter homeBlockOuter2 = new HomeBlockOuter();
                homeBlockOuter2.setTitle(objBlock2.optString("title"));
                List<HomeBlockInner> homeBlockInnerList2 = new ArrayList<>();
                JSONArray arrBlock2 = objBlock2.optJSONArray("item");
                for (int i = 0; i < arrBlock2.length(); i++) {
                    JSONObject o5 = arrBlock2.optJSONObject(i);
                    HomeBlockInner homeBlockInner = new HomeBlockInner();
                    homeBlockInner.setImage(o5.optString("image"));
                    homeBlockInner.setType(o5.optString("type"));
                    homeBlockInner.setData(o5.optString("data"));
                    homeBlockInnerList2.add(homeBlockInner);
                }
                homeBlockOuter2.setHomeBlockInnerList(homeBlockInnerList2);
                JSONObject objBlock3 = arrDatas.optJSONObject(6).optJSONObject("home3");
                HomeBlockOuter homeBlockOuter3 = new HomeBlockOuter();
                homeBlockOuter3.setTitle(objBlock3.optString("title"));
                List<HomeBlockInner> homeBlockInnerList3 = new ArrayList<>();
                JSONArray arrBlock3 = objBlock3.optJSONArray("item");
                for (int i = 0; i < arrBlock3.length(); i++) {
                    JSONObject o6 = arrBlock3.optJSONObject(i);
                    HomeBlockInner homeBlockInner = new HomeBlockInner();
                    homeBlockInner.setImage(o6.optString("image"));
                    homeBlockInner.setType(o6.optString("type"));
                    homeBlockInner.setData(o6.optString("data"));
                    homeBlockInnerList3.add(homeBlockInner);
                }
                homeBlockOuter3.setHomeBlockInnerList(homeBlockInnerList3);
                homeBlockOuterList.add(homeBlockOuter1);
                homeBlockOuterList.add(homeBlockOuter2);
                homeBlockOuterList.add(homeBlockOuter3);
                JSONObject objPost = arrDatas.optJSONObject(4).optJSONObject("home1");
                HomePost homePost = new HomePost();
                homePost.setImage(objPost.optString("image"));
                homePost.setData(objPost.optString("data"));
                homePostList.add(homePost);
                return true;
            }
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
                Intent shoplistIntent = new Intent(getActivity(), StoreActivity.class);
                shoplistIntent.putExtra("stateId", 0);
                getActivity().startActivity(shoplistIntent);
                break;
            case R.id.rl_homehead_contract:
                Intent contractIntent = new Intent(getActivity(), StoreActivity.class);
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
        STATE = ParaConfig.REFRESH;
        loadData();
    }

    @Override
    public void onLoadingMore() {
        lv.hideFootView();
    }
}
