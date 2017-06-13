package fragment;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.ChooseAddressActivity;
import com.gangjianwang.www.gangjianwang.HomeActivity;
import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.MakeSureOrderActivity;
import com.gangjianwang.www.gangjianwang.R;
import com.gangjianwang.www.gangjianwang.ShopDetailActivity;

import java.util.ArrayList;
import java.util.List;

import adapter.GoodsDetailGoodsHeadGvAdapter;
import adapter.GoodsDetailGoodsLvAdapter;
import adapter.MyPagerAdapter;
import bean.GoodsDetailGoods;
import customview.FlowLayout;
import customview.LazyFragment;
import customview.SizeThickTextView;
import utils.HeightUtils;
import utils.ToastUtils;

import static com.gangjianwang.www.gangjianwang.R.drawable.size_thick_shape;

/**
 * Created by Administrator on 2017/5/23.
 */

public class GoodsDetailGoodsFragment extends LazyFragment implements View.OnClickListener, ListItemClickHelp {

    private View rootView, headView, footView, popView;
    private PopupWindow popWindow;

    private LinearLayout leftTopLl;

    private ImageView popcloseIv, popAnimIv;
    private FlowLayout popSizeFl, popThickFl;
    private RelativeLayout popCustomServiceRl, popShopCarRl, popBuyNowRl, popAddShopCarRl;
    private TextView popMinusTv, popAddTv, popAmountTv;
    private int popAmount = 1;
    private ObjectAnimator translationXOa, translationYOa, scaleXOa, scaleYOa, rotationOa;

    private LinearLayout bottomLl;
    private RelativeLayout loadMoreRl;
    private RelativeLayout dataRl;
    private ListView leftLv, rightLv;
    private GridView headGv;
    private ViewPager headVp;
    private LinearLayout pointsLl;
    private TextView loadTv;
    private RelativeLayout customServiceRl, shopcarServiceRl, buyNowRl, addShopcarRl;
    private List<String> leftList = new ArrayList<>();
    private List<String> rightList = new ArrayList<>();
    private List<String> gridList = new ArrayList<>();
    private List<ImageView> imageList = new ArrayList<>();
    private ArrayAdapter<String> leftAdapter;
    private GoodsDetailGoodsHeadGvAdapter gridAdapter;
    private MyPagerAdapter myPagerAdapter;

    private List<GoodsDetailGoods> mMainList = new ArrayList<>();
    private List<String> mOtherList = new ArrayList<>();
    private GoodsDetailGoodsLvAdapter goodsDetailGoodsLvAdapter;
    private GoodsDetailGoods goodsDetailGoods;

    private List<String> sizeList = new ArrayList<>();
    private List<String> thickList = new ArrayList<>();

    Handler firstloadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == 1) {
                    leftAdapter.notifyDataSetChanged();
                    gridAdapter.notifyDataSetChanged();
                    goodsDetailGoodsLvAdapter.notifyDataSetChanged();
                    loadTv.setVisibility(View.INVISIBLE);
                    dataRl.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goodsdetail_goods, null);
        headView = inflater.inflate(R.layout.head_goodsdetailgoods, null);
        footView = inflater.inflate(R.layout.foot_goodsdetailgoods, null);
        popView = inflater.inflate(R.layout.pop_goodsdetailgoods, null);
        initView(rootView);
        initAnim();
        initData();
        setData();
        createVp(5);
        setListener();
        return rootView;
    }

    private void initView(View rootView) {
        popWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popWindow.setFocusable(true);
        popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setAnimationStyle(R.style.goodsdetailgoods_popwindow_anim);
        popcloseIv = (ImageView) popView.findViewById(R.id.iv_pop_goodsdetailgoods_close);
        popMinusTv = (TextView) popView.findViewById(R.id.tv_pop_goodsdetailgoods_buyamount_minus);
        popAddTv = (TextView) popView.findViewById(R.id.tv_pop_goodsdetailgoods_buyamount_add);
        popAmountTv = (TextView) popView.findViewById(R.id.tv_pop_goodsdetailgoods_buyamount_amount);
        popSizeFl = (FlowLayout) popView.findViewById(R.id.fl_pop_goodsdetailgoods_size);
        popThickFl = (FlowLayout) popView.findViewById(R.id.fl_pop_goodsdetailgoods_thick);
        popCustomServiceRl = (RelativeLayout) popView.findViewById(R.id.rl_pop_goodsdetailgoods_customservice);
        popShopCarRl = (RelativeLayout) popView.findViewById(R.id.rl_pop_goodsdetailgoods_shopcar);
        popBuyNowRl = (RelativeLayout) popView.findViewById(R.id.rl_pop_goodsdetailgoods_buynow);
        popAddShopCarRl = (RelativeLayout) popView.findViewById(R.id.rl_pop_goodsdetailgoods_addshopcar);
        popSizeFl = (FlowLayout) popView.findViewById(R.id.fl_pop_goodsdetailgoods_size);
        popThickFl = (FlowLayout) popView.findViewById(R.id.fl_pop_goodsdetailgoods_thick);
        popAnimIv = (ImageView) popView.findViewById(R.id.iv_pop_goodsdetailgoods_icon_before);

        leftTopLl = (LinearLayout) rootView.findViewById(R.id.ll_goodsdetailgoods_lefttop);
        bottomLl = (LinearLayout) rootView.findViewById(R.id.ll_goodsdetailgoods_bottom);
        dataRl = (RelativeLayout) rootView.findViewById(R.id.rl_goodsdetailgoods_data);
        loadTv = (TextView) rootView.findViewById(R.id.tv_goodsdetailgoods_firstload);
        leftLv = (ListView) rootView.findViewById(R.id.lv_goodsdetailgoods_left);
        rightLv = (ListView) rootView.findViewById(R.id.lv_goodsdetailgoods_right);
        customServiceRl = (RelativeLayout) rootView.findViewById(R.id.rl_goodsdetailgoods_customservice);
        shopcarServiceRl = (RelativeLayout) rootView.findViewById(R.id.rl_goodsdetailgoods_shopcar);
        buyNowRl = (RelativeLayout) rootView.findViewById(R.id.rl_goodsdetailgoods_buynow);
        addShopcarRl = (RelativeLayout) rootView.findViewById(R.id.rl_goodsdetailgoods_addshopcar);
        headGv = (GridView) headView.findViewById(R.id.gv_head_goodsdetailgoods);
        headVp = (ViewPager) headView.findViewById(R.id.vp_head_goodsdetailgoods);
        pointsLl = (LinearLayout) headView.findViewById(R.id.ll_head_goodsdetailgoods_points);
        loadMoreRl = (RelativeLayout) footView.findViewById(R.id.rl_foot_goodsdetailgoods_loadmore);
        rightLv.addHeaderView(headView);
        rightLv.addFooterView(footView);
    }

    private void initAnim() {
        translationXOa = ObjectAnimator.ofFloat(popAnimIv, "TranslationX", 0, 250);
        translationYOa = ObjectAnimator.ofFloat(popAnimIv, "TranslationY", 0, 1250);
        scaleXOa = ObjectAnimator.ofFloat(popAnimIv, "ScaleX", 1f, 0f);
        scaleYOa = ObjectAnimator.ofFloat(popAnimIv, "ScaleY", 1f, 0f);
        rotationOa = ObjectAnimator.ofFloat(popAnimIv, "rotation", 0, 1080);
    }

    private void startAnim() {
        AnimatorSet addInShopCarAs = new AnimatorSet();
        addInShopCarAs.playTogether(translationXOa, translationYOa, scaleXOa, scaleYOa, rotationOa);
        addInShopCarAs.setDuration(400);
        addInShopCarAs.start();
    }

    private void initData() {
        sizeList.add("13");
        sizeList.add("16");
        sizeList.add("19");
        sizeList.add("22");
        sizeList.add("32");
        sizeList.add("38");
        sizeList.add("51");
        sizeList.add("63");
        sizeList.add("76");
        sizeList.add("89");
        sizeList.add("102");
        sizeList.add("114");
        sizeList.add("133");
        sizeList.add("159");
        sizeList.add("25");
        thickList.add("0.45");
        thickList.add("0.5");
        thickList.add("0.55");
        thickList.add("0.6");
        thickList.add("0.65");
        thickList.add("0.7");
        thickList.add("0.75");
        thickList.add("0.8");
        thickList.add("0.85");
        thickList.add("0.9");
        thickList.add("0.95");
        thickList.add("1.05");
        thickList.add("1.45");
    }

    private void initFlow(String size, String thick) {
        final ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        for (int i = 0; i < sizeList.size(); i++) {
            final SizeThickTextView sizeTv = new SizeThickTextView(getActivity());
            sizeTv.setPadding(10, 10, 10, 10);
            sizeTv.setTextSize(15);
            sizeTv.setText(sizeList.get(i));
            popSizeFl.addView(sizeTv, lp);
            if (sizeTv.getText().toString().equals(size)) {
                sizeTv.setBackgroundColor(Color.RED);
            }
            final int finalI = i;
            sizeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < popSizeFl.getChildCount(); j++) {
                        if (j == finalI) {
                            popSizeFl.getChildAt(j).setBackgroundColor(Color.RED);
                            SizeThickTextView sTv = (SizeThickTextView) popSizeFl.getChildAt(j);
                            ToastUtils.toast(getActivity(), "规格:" + sTv.getText().toString());
                            goodsDetailGoods.setSize(sTv.getText().toString());
                            createThickBySize(sTv.getText().toString(), true);
                        } else {
                            popSizeFl.getChildAt(j).setBackgroundColor(Color.WHITE);
                        }
                    }
                }
            });
        }
        createThickBySize(goodsDetailGoods.getSize(), false);
    }

    private void createThickBySize(String size, boolean fault) {
        popThickFl.removeAllViews();
        thickList.clear();
        if (getThickListBySize(size) != null) {
            thickList.addAll(getThickListBySize(size));
            createThick(fault);
        }
    }

    private void createThick(boolean fault) {
        final ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        for (int i = 0; i < thickList.size(); i++) {
            SizeThickTextView thickTv = new SizeThickTextView(getActivity());
            thickTv.setPadding(10, 10, 10, 10);
            thickTv.setTextSize(15);
            thickTv.setText(thickList.get(i));
            popThickFl.addView(thickTv, lp);
            final int finalI = i;
            thickTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < popThickFl.getChildCount(); j++) {
                        if (j == finalI) {
                            popThickFl.getChildAt(j).setBackgroundColor(Color.RED);
                            SizeThickTextView sTv = (SizeThickTextView) popThickFl.getChildAt(j);
                            ToastUtils.toast(getActivity(), "厚度:" + sTv.getText().toString());
                            goodsDetailGoods.setThick(sTv.getText().toString());
                        } else {
                            popThickFl.getChildAt(j).setBackgroundColor(Color.WHITE);
                        }
                    }
                }
            });
        }
        if (fault) {
            popThickFl.getChildAt(0).setBackgroundColor(Color.RED);
            SizeThickTextView sTv = (SizeThickTextView) popThickFl.getChildAt(0);
            ToastUtils.toast(getActivity(), "厚度:" + sTv.getText().toString());
            goodsDetailGoods.setThick(sTv.getText().toString());
        } else {
            for (int i = 0; i < popThickFl.getChildCount(); i++) {
                SizeThickTextView sTv = (SizeThickTextView) popThickFl.getChildAt(i);
                if (sTv.getText().toString().equals(goodsDetailGoods.getThick())) {
                    sTv.setBackgroundColor(Color.RED);
                }
            }
        }
    }

    private List<String> getThickListBySize(String size) {
        List<String> tempList = null;
        switch (size) {
            case "13":
                tempList = new ArrayList<>();
                tempList.add("0.3");
                tempList.add("0.35");
                tempList.add("0.4");
                break;
            case "16":
                tempList = new ArrayList<>();
                tempList.add("0.3");
                tempList.add("0.35");
                tempList.add("0.4");
                tempList.add("0.45");
                tempList.add("0.5");
                tempList.add("0.55");
                tempList.add("0.6");
                tempList.add("0.65");
                tempList.add("0.7");
                break;
            case "19":
                tempList = new ArrayList<>();
                tempList.add("0.3");
                tempList.add("0.35");
                tempList.add("0.4");
                tempList.add("0.45");
                tempList.add("0.5");
                tempList.add("0.55");
                tempList.add("0.6");
                tempList.add("0.65");
                tempList.add("0.7");
                tempList.add("0.75");
                tempList.add("0.8");
                tempList.add("0.85");
                tempList.add("0.9");
                tempList.add("0.95");
                tempList.add("1.05");
                break;
            case "32":
                tempList = new ArrayList<>();
                tempList.add("0.45");
                tempList.add("0.5");
                tempList.add("0.55");
                tempList.add("0.6");
                tempList.add("0.65");
                tempList.add("0.7");
                tempList.add("0.75");
                tempList.add("0.8");
                tempList.add("0.85");
                tempList.add("0.9");
                tempList.add("0.95");
                tempList.add("1.05");
                tempList.add("1.45");
                break;
            default:
                break;
        }
        return tempList;
    }

    private void setData() {
        leftAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, leftList);
        leftLv.setAdapter(leftAdapter);
        goodsDetailGoodsLvAdapter = new GoodsDetailGoodsLvAdapter(getActivity(), mMainList, mOtherList, this);
        rightLv.setAdapter(goodsDetailGoodsLvAdapter);
        gridAdapter = new GoodsDetailGoodsHeadGvAdapter(getActivity(), gridList, this);
        headGv.setAdapter(gridAdapter);
    }

    private void loadData() {
        leftList.add("圆管");
        leftList.add("方管");
        leftList.add("矩管");
        leftList.add("花管");
        for (int i = 0; i < 30; i++) {
            rightList.add("圆管" + i);
        }
        for (int i = 0; i < 10; i++) {
            gridList.add("门系列");
        }
        HeightUtils.setGridViewHeight(headGv);
        goodsDetailGoods = new GoodsDetailGoods();
        goodsDetailGoods.setSendAddress("全国");
        goodsDetailGoods.setSize("32");
        goodsDetailGoods.setThick("0.55");
        mMainList.add(goodsDetailGoods);
        for (int i = 0; i < 10; i++) {
            mOtherList.add("");
        }
    }

    private void createVp(int imgcount) {
        for (int i = 0; i < imgcount; i++) {
            ImageView iv = new ImageView(getActivity());
            iv.setImageResource(R.mipmap.ic_launcher);
            imageList.add(iv);
            ImageView pi = new ImageView(getActivity());
            pi.setImageResource(R.drawable.points_off);
            pi.setPadding(5, 0, 5, 0);
            pi.setTag("" + i);
            pointsLl.addView(pi);
        }
        ImageView point = (ImageView) pointsLl.findViewWithTag("0");
        point.setImageResource(R.drawable.points_on);
        myPagerAdapter = new MyPagerAdapter(imageList);
        headVp.setAdapter(myPagerAdapter);
        headVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < imageList.size(); i++) {
                    ImageView ivPoint = (ImageView) pointsLl.findViewWithTag("" + i);
                    ivPoint.setImageResource(R.drawable.points_off);
                }
                ImageView point = (ImageView) pointsLl.findViewWithTag("" + position);
                point.setImageResource(R.drawable.points_on);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setListener() {
        leftTopLl.setOnClickListener(this);
        customServiceRl.setOnClickListener(this);
        shopcarServiceRl.setOnClickListener(this);
        buyNowRl.setOnClickListener(this);
        addShopcarRl.setOnClickListener(this);
        loadMoreRl.setOnClickListener(this);
        popcloseIv.setOnClickListener(this);
        popMinusTv.setOnClickListener(this);
        popAddTv.setOnClickListener(this);
        popCustomServiceRl.setOnClickListener(this);
        popShopCarRl.setOnClickListener(this);
        popBuyNowRl.setOnClickListener(this);
        popAddShopCarRl.setOnClickListener(this);
    }

    @Override
    protected void onFragmentFirstVisible() {
        dataRl.setVisibility(View.INVISIBLE);
        loadTv.setVisibility(View.VISIBLE);
        loadData();
        firstloadHandler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_goodsdetailgoods_lefttop:
                startActivity(new Intent(getActivity(), ShopDetailActivity.class));
                break;
            case R.id.iv_pop_goodsdetailgoods_close:
                popWindow.dismiss();
                break;
            case R.id.tv_pop_goodsdetailgoods_buyamount_minus:
                if (popAmount != 1) {
                    popAmount--;
                    popAmountTv.setText(popAmount + "");
                }
                break;
            case R.id.tv_pop_goodsdetailgoods_buyamount_add:
                popAmount++;
                popAmountTv.setText(popAmount + "");
                break;
            case R.id.rl_pop_goodsdetailgoods_customservice:
                ToastUtils.toast(getActivity(), "客服");
                break;
            case R.id.rl_pop_goodsdetailgoods_shopcar:
                Intent intentPop = new Intent(getActivity(), HomeActivity.class);
                intentPop.putExtra("what", 3);
                getActivity().startActivity(intentPop);
                break;
            case R.id.rl_pop_goodsdetailgoods_buynow:
                startActivity(new Intent(getActivity(), MakeSureOrderActivity.class));
                break;
            case R.id.rl_pop_goodsdetailgoods_addshopcar:
                ToastUtils.toast(getActivity(), "加入购物车");
                startAnim();
                break;
            case R.id.rl_goodsdetailgoods_customservice:
                ToastUtils.toast(getActivity(), "客服");
                break;
            case R.id.rl_goodsdetailgoods_shopcar:
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("what", 3);
                getActivity().startActivity(intent);
                break;
            case R.id.rl_goodsdetailgoods_buynow:
                ToastUtils.toast(getActivity(), "立即购买");
                break;
            case R.id.rl_goodsdetailgoods_addshopcar:
                initFlow(goodsDetailGoods.getSize(), goodsDetailGoods.getThick());
                popWindow.showAtLocation(bottomLl, Gravity.BOTTOM, 0, 0);
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 0.7f;
                getActivity().getWindow().setAttributes(lp);
                popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                        lp.alpha = 1f;
                        getActivity().getWindow().setAttributes(lp);
                        popSizeFl.removeAllViews();
                        popThickFl.removeAllViews();
                        goodsDetailGoodsLvAdapter.notifyDataSetChanged();
                    }
                });
                break;
            case R.id.rl_foot_goodsdetailgoods_loadmore:
                for (int i = 0; i < 10; i++) {
                    mOtherList.add("");
                }
                goodsDetailGoodsLvAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View item, View widget, int position, int which, boolean isCheckeed) {
        switch (which) {
            case R.id.tv_item_gridview_head_goodsdetailgoods:
                ToastUtils.toast(getActivity(), gridList.get(position) + ":" + position);
                break;
            case R.id.tv_item_goodsdetailgoods_address:
                startActivityForResult(new Intent(getActivity(), ChooseAddressActivity.class), 1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            if (data != null) {
                String sendAddress = data.getStringExtra("sendAddress");
                mMainList.get(0).setSendAddress(sendAddress);
                goodsDetailGoodsLvAdapter.notifyDataSetChanged();
            }
        }
    }

}
