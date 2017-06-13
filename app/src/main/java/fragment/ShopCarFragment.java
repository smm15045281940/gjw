package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gangjianwang.www.gangjianwang.HomeActivity;
import com.gangjianwang.www.gangjianwang.InquiryListActivity;
import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.ShopCarOuterAdapter;
import bean.ShopCar;
import bean.ShopCarGoods;
import utils.ToastUtils;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class ShopCarFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, ListItemClickHelp {

    private View rootView, emptyView;
    private ListView mLv;
    private RelativeLayout lookAroundRl;
    private Handler handler;
    private CheckBox cbAll;
    private RelativeLayout createOrderPriceRl, sureInfoRl;

    private List<ShopCar> mList = new ArrayList<>();
    private ShopCarOuterAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeActivity homeActivity = (HomeActivity) getActivity();
        handler = homeActivity.mChangeFragHandler;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_shopcar, null);
        rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView = inflater.inflate(R.layout.empty_shopcar, null);
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initView();
        setData();
        loadData();
        setListener();
        return rootView;
    }

    private void initView() {
        mLv = (ListView) rootView.findViewById(R.id.lv_shopcar);
        ((ViewGroup) mLv.getParent()).addView(emptyView);
        emptyView.setVisibility(View.GONE);
        mLv.setEmptyView(emptyView);
        lookAroundRl = (RelativeLayout) emptyView.findViewById(R.id.rl_shopcar_lookaround);
        cbAll = (CheckBox) rootView.findViewById(R.id.cb_shopcar_cbAll);
        createOrderPriceRl = (RelativeLayout) rootView.findViewById(R.id.rl_shopcar_createorderprice);
        sureInfoRl = (RelativeLayout) rootView.findViewById(R.id.rl_shopcar_sureinfo);
    }

    private void setData() {
        mAdapter = new ShopCarOuterAdapter(getActivity(), mList, this);
        mLv.setAdapter(mAdapter);
    }

    private void loadData() {
        for (int i = 0; i < 10; i++) {
            ShopCar shopCar = new ShopCar();
            shopCar.setChecked(false);
            shopCar.setShopName("金坤成不锈钢金属装饰有限公司");
            List<ShopCarGoods> shopCarGoodsList = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                ShopCarGoods shopCarGoods = new ShopCarGoods();
                shopCarGoods.setGoodsName("圆管");
                shopCarGoods.setGoodsSize("10+20");
                shopCarGoods.setGoodsPrice("30");
                shopCarGoodsList.add(shopCarGoods);
            }
            shopCar.setGoodsList(shopCarGoodsList);
            mList.add(shopCar);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void setListener() {
        lookAroundRl.setOnClickListener(this);
        cbAll.setOnCheckedChangeListener(this);
        createOrderPriceRl.setOnClickListener(this);
        sureInfoRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_shopcar_lookaround:
                handler.sendEmptyMessage(0);
                break;
            case R.id.rl_shopcar_createorderprice:
                startActivity(new Intent(getActivity(), InquiryListActivity.class));
                break;
            case R.id.rl_shopcar_sureinfo:
                ToastUtils.toast(getActivity(), "确认信息");
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        allChecked(isChecked);
    }

    @Override
    public void onClick(View item, View widget, int position, int which, boolean isChecked) {
        switch (which) {
            case R.id.cb_item_shopcar_outer_shop:
                mList.get(position).setChecked(isChecked);
                for (int i = 0; i < mList.get(position).getGoodsList().size(); i++) {
                    mList.get(position).getGoodsList().get(i).setChecked(isChecked);
                }
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void allChecked(boolean b) {
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setChecked(b);
            for (int j = 0; j < mList.get(i).getGoodsList().size(); j++) {
                mList.get(i).getGoodsList().get(j).setChecked(b);
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
