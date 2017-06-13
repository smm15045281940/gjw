package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;

import java.util.ArrayList;
import java.util.List;

import adapter.PurchaseAdapter;
import bean.Purchase;
import customview.MyRefreshListView;
import customview.OnRefreshListener;
import utils.ToastUtils;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class PurchaseFragment extends Fragment implements View.OnClickListener, OnRefreshListener, ListItemClickHelp {

    private View rootView;
    private EditText mSearchEt;
    private RelativeLayout mSearchRl;
    private MyRefreshListView mLv;
    private List<Purchase> mDataList = new ArrayList<>();
    private PurchaseAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_purchase, null);
        initView();
        setData();
        loadData();
        setListener();
        return rootView;
    }

    private void initView() {
        mSearchEt = (EditText) rootView.findViewById(R.id.et_purchase_search);
        mSearchRl = (RelativeLayout) rootView.findViewById(R.id.rl_purchase_search);
        mLv = (MyRefreshListView) rootView.findViewById(R.id.lv_purchase);
    }

    private void setData() {
        mAdapter = new PurchaseAdapter(getActivity(), mDataList, this);
        mLv.setAdapter(mAdapter);
    }

    private void loadData() {
        for (int i = 0; i < 10; i++) {
            Purchase purchase = new Purchase();
            purchase.setPurchaseNumber("123456");
            purchase.setGoodsName("测试");
            purchase.setMaxPrice("¥123.00");
            purchase.setPurchaseAmount("123");
            purchase.setPurchaseName("测试");
            purchase.setBillType("无需发票");
            purchase.setTransportType("包邮");
            mDataList.add(purchase);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void setListener() {
        mSearchRl.setOnClickListener(this);
        mLv.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_purchase_search:
                if (!TextUtils.isEmpty(mSearchEt.getText().toString())) {
                    ToastUtils.toast(getActivity(), "正在搜索" + mSearchEt.getText().toString());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDownPullRefresh() {
        mLv.hideHeadView();
    }

    @Override
    public void onLoadingMore() {
        mLv.hideFootView();
    }

    @Override
    public void onClick(View item, View widget, int position, int which, boolean isChecked) {
        switch (which) {
            case R.id.rl_purchase_price:
                ToastUtils.toast(getActivity(), "查询报价" + position);
                break;
            case R.id.rl_purchase_cancel:
                mDataList.remove(position);
                mAdapter.notifyDataSetChanged();
                ToastUtils.toast(getActivity(), "已取消");
                break;
            default:
                break;
        }
    }

}
