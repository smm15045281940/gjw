package fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.HomeActivity;
import com.gangjianwang.www.gangjianwang.InquiryListActivity;
import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.SureOrderActivity;
import com.gangjianwang.www.gangjianwang.R;
import com.gangjianwang.www.gangjianwang.ShopCarClickHelp;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.ShopCarOuterAdapter;
import bean.ShopCar;
import bean.ShopCarGoods;
import config.NetConfig;
import config.ParaConfig;
import utils.ToastUtils;
import utils.UserUtils;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class ShopCarFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, ListItemClickHelp, ShopCarClickHelp {

    private View rootView, emptyView;
    private ListView mLv;
    private RelativeLayout lookAroundRl;
    private Handler changehandler;
    private CheckBox cbAll;
    private TextView numPriceTv;
    private RelativeLayout createOrderPriceRl, sureInfoRl;
    private ProgressDialog progressDialog;
    private OkHttpClient okHttpClient;
    private List<ShopCar> mList = new ArrayList<>();
    private ShopCarOuterAdapter mAdapter;
    private String key;

    private AlertDialog delAd;
    private int delOutPosition, delInPosition;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case 0:
                        ToastUtils.toast(getActivity(), ParaConfig.NETWORK_ERROR);
                        break;
                    case 1:
                        break;
                    default:
                        break;
                }
                mAdapter.notifyDataSetChanged();
                calculate();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeActivity homeActivity = (HomeActivity) getActivity();
        changehandler = homeActivity.mChangeFragHandler;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_shopcar, null);
        initView();
        initData();
        setData();
        loadData();
        setListener();
        return rootView;
    }

    private void initView() {
        initRoot();
        initEmpty();
        initProgress();
        initDialog();
    }

    private void initRoot() {
        mLv = (ListView) rootView.findViewById(R.id.lv_shopcar);
        cbAll = (CheckBox) rootView.findViewById(R.id.cb_shopcar_cbAll);
        numPriceTv = (TextView) rootView.findViewById(R.id.tv_shopcar_summoney);
        createOrderPriceRl = (RelativeLayout) rootView.findViewById(R.id.rl_shopcar_createorderprice);
        sureInfoRl = (RelativeLayout) rootView.findViewById(R.id.rl_shopcar_sureinfo);
    }

    private void initEmpty() {
        emptyView = View.inflate(getActivity(), R.layout.empty_shopcar, null);
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ViewGroup) mLv.getParent()).addView(emptyView);
        emptyView.setVisibility(View.GONE);
        mLv.setEmptyView(emptyView);
        lookAroundRl = (RelativeLayout) emptyView.findViewById(R.id.rl_shopcar_lookaround);
    }

    private void initProgress() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initDialog() {
        delAd = new AlertDialog.Builder(getActivity()).setMessage(ParaConfig.DELETE_MESSAGE).setNegativeButton(ParaConfig.DELETE_YES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delAd.dismiss();
                delete();
            }
        }).setPositiveButton(ParaConfig.DELETE_NO, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delAd.dismiss();
            }
        }).create();
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
        key = UserUtils.readLogin(getActivity(), true).getKey();
        mAdapter = new ShopCarOuterAdapter(getActivity(), mList, this, this);
    }

    private void setData() {
        mLv.setAdapter(mAdapter);
    }

    private void loadData() {
        progressDialog.show();
        RequestBody body = new FormEncodingBuilder().add("key", key).build();
        Request request = new Request.Builder().url(NetConfig.shopCarUrl).post(body).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful() && parseJson(response.body().string())) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    private boolean parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            if (objBean.optInt("code") == 200) {
                JSONObject objDatas = objBean.optJSONObject("datas");
                JSONArray arrCartList = objDatas.optJSONArray("cart_list");
                for (int i = 0; i < arrCartList.length(); i++) {
                    JSONObject out = arrCartList.optJSONObject(i);
                    ShopCar shopCar = new ShopCar();
                    shopCar.setStoreId(out.optString("store_id"));
                    shopCar.setShopName(out.optString("store_name"));
                    shopCar.setChecked(true);
                    List<ShopCarGoods> shopCarGoodsList = new ArrayList<>();
                    JSONArray arrGoods = out.optJSONArray("goods");
                    for (int j = 0; j < arrGoods.length(); j++) {
                        JSONObject in = arrGoods.optJSONObject(j);
                        ShopCarGoods shopCarGoods = new ShopCarGoods();
                        shopCarGoods.setChecked(true);
                        shopCarGoods.setCartId(in.optString("cart_id"));
                        shopCarGoods.setGoodsNum(in.optString("goods_num"));
                        shopCarGoods.setStoreId(in.optString("store_id"));
                        shopCarGoods.setGoodsName(in.optString("goods_name"));
                        shopCarGoods.setGoodsSize(in.optString("goods_spec"));
                        shopCarGoods.setGoodsPrice(in.optString("goods_price"));
                        shopCarGoods.setGoodsImageUrl(in.optString("goods_image_url"));
                        shopCarGoodsList.add(shopCarGoods);
                    }
                    shopCar.setGoodsList(shopCarGoodsList);
                    mList.add(shopCar);
                }
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
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
                changehandler.sendEmptyMessage(0);
                break;
            case R.id.rl_shopcar_createorderprice:
                startActivity(new Intent(getActivity(), InquiryListActivity.class));
                break;
            case R.id.rl_shopcar_sureinfo:
                startActivity(new Intent(getActivity(), SureOrderActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        allChecked(isChecked);
    }

    private void allChecked(boolean b) {
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setChecked(b);
            for (int j = 0; j < mList.get(i).getGoodsList().size(); j++) {
                mList.get(i).getGoodsList().get(j).setChecked(b);
            }
        }
        mAdapter.notifyDataSetChanged();
        calculate();
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
        calculate();
    }

    @Override
    public void onClick(View item, View widget, int position, int which, String storeId, boolean isChecked) {
        switch (which) {
            case R.id.cb_item_shopcar_inner:
                for (int i = 0; i < mList.size(); i++) {
                    if (storeId.equals(mList.get(i).getStoreId())) {
                        mList.get(i).getGoodsList().get(position).setChecked(isChecked);
                    }
                }
                break;
            case R.id.tv_item_shopcar_inner_goodsnum_sub:
                for (int i = 0; i < mList.size(); i++) {
                    if (storeId.equals(mList.get(i).getStoreId())) {
                        int goodsNum = Integer.parseInt(mList.get(i).getGoodsList().get(position).getGoodsNum());
                        if (goodsNum > 1) {
                            goodsNum--;
                            mList.get(i).getGoodsList().get(position).setGoodsNum(goodsNum + "");
                        }
                    }
                }
                break;
            case R.id.tv_item_shopcar_inner_goodsnum_add:
                for (int i = 0; i < mList.size(); i++) {
                    if (storeId.equals(mList.get(i).getStoreId())) {
                        int goodsNum = Integer.parseInt(mList.get(i).getGoodsList().get(position).getGoodsNum());
                        goodsNum++;
                        mList.get(i).getGoodsList().get(position).setGoodsNum(goodsNum + "");
                    }
                }
                break;
            case R.id.iv_item_shopcar_inner_delete:
                for (int i = 0; i < mList.size(); i++) {
                    if (storeId.equals(mList.get(i).getStoreId())) {
                        delOutPosition = i;
                        delInPosition = position;
                        delAd.show();
                    }
                }
                break;
            default:
                break;
        }
        mAdapter.notifyDataSetChanged();
        calculate();
    }

    private void calculate() {
        double num = 0;
        for (int i = 0; i < mList.size(); i++) {
            for (int j = 0; j < mList.get(i).getGoodsList().size(); j++) {
                if (mList.get(i).getGoodsList().get(j).isChecked()) {
                    num += Double.parseDouble(mList.get(i).getGoodsList().get(j).getGoodsPrice()) * Double.parseDouble(mList.get(i).getGoodsList().get(j).getGoodsNum());
                }
            }
        }
        numPriceTv.setText("¥" + String.format("%.2f", num));
    }

    private void delete() {
        progressDialog.show();
        String cartId = mList.get(delOutPosition).getGoodsList().get(delInPosition).getCartId();
        RequestBody body = new FormEncodingBuilder().add("key", key).add("cart_id", cartId).build();
        Request request = new Request.Builder().url(NetConfig.shopCarDeleteUrl).post(body).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful() && parseDelJson(response.body().string())) {
                    mList.get(delOutPosition).getGoodsList().remove(delInPosition);
                    if (mList.get(delOutPosition).getGoodsList().size() == 0) {
                        mList.remove(delOutPosition);
                    }
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    private boolean parseDelJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            if (objBean.optInt("code") == 200) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
