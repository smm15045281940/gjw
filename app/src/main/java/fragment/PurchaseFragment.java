package fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.PurchaseDetailActivity;
import com.gangjianwang.www.gangjianwang.R;
import com.gangjianwang.www.gangjianwang.StoreOfferActivity;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.PurchaseAdapter;
import bean.Purchase;
import config.NetConfig;
import customview.MyRefreshListView;
import customview.OnRefreshListener;
import utils.ToastUtils;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class PurchaseFragment extends Fragment implements View.OnClickListener, OnRefreshListener, ListItemClickHelp, AdapterView.OnItemClickListener {

    private View rootView, purchaseCancelDialogView;
    private TextView purchaseNumberTv;
    private EditText cancelReasonEt;
    private RelativeLayout cancelDialogSureRl, cancelDialogCancelRl;
    private EditText mSearchEt;
    private RelativeLayout mSearchRl;
    private MyRefreshListView mLv;
    private List<Purchase> mDataList = new ArrayList<>();
    private PurchaseAdapter mAdapter;
    private OkHttpClient okHttpClient;
    private ProgressDialog mPd;
    private AlertDialog mAd;
    private final int FIRST_LOAD = 1;
    private final int REFRESH_LOAD = 2;
    private final int LOAD_LOAD = 3;
    private final int FIRST_NO_NET = 1;
    private final int REFRESH_NO_NET = 2;
    private final int LOAD_NO_NET = 3;
    private final int FIRST_DONE = 4;
    private final int REFRESH_DONE = 5;
    private final int LOAD_DONE = 6;
    private int cancelIndex;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case FIRST_NO_NET://首次无网络
                        mPd.dismiss();
                        ToastUtils.toast(getActivity(), "无网络");
                        break;
                    case REFRESH_NO_NET://刷新无网络
                        mLv.hideHeadView();
                        ToastUtils.toast(getActivity(), "无网络");
                        break;
                    case LOAD_NO_NET://加载无网络
                        mLv.hideFootView();
                        ToastUtils.toast(getActivity(), "无网络");
                        break;
                    case FIRST_DONE://首次完成
                        mPd.dismiss();
                        mAdapter.notifyDataSetChanged();
                        break;
                    case REFRESH_DONE://刷新完成
                        mLv.hideHeadView();
                        mAdapter.notifyDataSetChanged();
                        break;
                    case LOAD_DONE://加载完成
                        mLv.hideFootView();
                        mAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_purchase, null);
        initView();
        initData();
        setData();
        setListener();
        loadData(FIRST_LOAD);
        return rootView;
    }

    private void initView() {
        mSearchEt = (EditText) rootView.findViewById(R.id.et_purchase_search);
        mSearchRl = (RelativeLayout) rootView.findViewById(R.id.rl_purchase_search);
        mLv = (MyRefreshListView) rootView.findViewById(R.id.lv_purchase);
        purchaseCancelDialogView = View.inflate(getActivity(), R.layout.dialog_purchase_cancel, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(purchaseCancelDialogView);
        mAd = builder.create();
        purchaseNumberTv = (TextView) purchaseCancelDialogView.findViewById(R.id.tv_dialog_purchase_cancel_purchasenumber);
        cancelReasonEt = (EditText) purchaseCancelDialogView.findViewById(R.id.et_dialog_purchase_cancel_reason);
        cancelDialogSureRl = (RelativeLayout) purchaseCancelDialogView.findViewById(R.id.rl_dialog_purchase_cancel_sure);
        cancelDialogCancelRl = (RelativeLayout) purchaseCancelDialogView.findViewById(R.id.rl_dialog_purchase_cancel_cancel);
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
        mPd = new ProgressDialog(getActivity());
        mPd.setMessage("加载中..");
        mAdapter = new PurchaseAdapter(getActivity(), mDataList, this);
    }

    private void setData() {
        mLv.setAdapter(mAdapter);
    }

    private void loadData(int LOAD_STATE) {
        switch (LOAD_STATE) {
            case FIRST_LOAD:
                mPd.show();
                Request requestFirst = new Request.Builder().url(NetConfig.purchaseUrl).get().build();
                okHttpClient.newCall(requestFirst).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        handler.sendEmptyMessage(FIRST_NO_NET);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String json = response.body().string();
                            mDataList.clear();
                            if (parseJson(json))
                                handler.sendEmptyMessage(FIRST_DONE);
                        }
                    }
                });
                break;
            case REFRESH_LOAD:
                Request requestRefresh = new Request.Builder().url(NetConfig.purchaseUrl).get().build();
                okHttpClient.newCall(requestRefresh).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        handler.sendEmptyMessage(REFRESH_NO_NET);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String json = response.body().string();
                            mDataList.clear();
                            if (parseJson(json))
                                handler.sendEmptyMessage(REFRESH_DONE);
                        }
                    }
                });
                break;
            case LOAD_LOAD:
                handler.sendEmptyMessage(5);
                break;
            default:
                break;
        }
    }

    private boolean parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            JSONObject objDatas = objBean.optJSONObject("datas");
            JSONArray arrGroupList = objDatas.optJSONArray("purchase_group_list");
            for (int i = 0; i < arrGroupList.length(); i++) {
                Purchase purchase = new Purchase();
                JSONObject o = arrGroupList.optJSONObject(i);
                JSONObject ob = o.optJSONObject("purchase_list");
                purchase.setPurchaseNumber(o.optString("purchase_sn"));
                purchase.setGoodsName(ob.optString("goods_name"));
                purchase.setPurchaseId(ob.optString("purchase_id"));
                purchase.setMaxPrice(ob.optString("max_price"));
                purchase.setPurchaseAmount(ob.optString("purchase_num"));
                purchase.setPurchaseName(ob.optString("purchase_name"));
                purchase.setBillType(ob.optString("goods_vat_desc"));
                purchase.setTransportType(ob.optString("shipping_fee_desc"));
                purchase.setStateDesc(ob.optString("purchase_state_desc"));
                mDataList.add(purchase);
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setListener() {
        mSearchRl.setOnClickListener(this);
        mLv.setOnRefreshListener(this);
        mLv.setOnItemClickListener(this);
        cancelDialogSureRl.setOnClickListener(this);
        cancelDialogCancelRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_purchase_search:
                if (!TextUtils.isEmpty(mSearchEt.getText().toString())) {
                    ToastUtils.toast(getActivity(), "正在搜索" + mSearchEt.getText().toString());
                } else {
                    ToastUtils.toast(getActivity(), "搜索内容不能为空");
                }
                break;
            case R.id.rl_dialog_purchase_cancel_sure:
                mAd.dismiss();
                mDataList.remove(cancelIndex);
                mAdapter.notifyDataSetChanged();
                ToastUtils.toast(getActivity(), "取消理由：" + cancelReasonEt.getText().toString());
                cancelReasonEt.setText("");
                break;
            case R.id.rl_dialog_purchase_cancel_cancel:
                cancelReasonEt.setText("");
                mAd.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDownPullRefresh() {
        loadData(REFRESH_LOAD);
    }

    @Override
    public void onLoadingMore() {
//        loadData(LOAD_LOAD);
        mLv.hideFootView();
    }

    @Override
    public void onClick(View item, View widget, int position, int which, boolean isChecked) {
        switch (which) {
            case R.id.rl_purchase_price:
                getActivity().startActivity(new Intent(getActivity(), StoreOfferActivity.class));
                break;
            case R.id.rl_purchase_cancel:
                purchaseNumberTv.setText(mDataList.get(position).getPurchaseNumber());
                cancelIndex = position;
                mAd.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), PurchaseDetailActivity.class);
        intent.putExtra("purchaseId", mDataList.get(position - 1).getPurchaseId());
        startActivity(intent);
    }
}
