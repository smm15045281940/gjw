package fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gangjianwang.www.gangjianwang.HomeActivity;
import com.gangjianwang.www.gangjianwang.InquiryListActivity;
import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;
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

public class ShopCarFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, ListItemClickHelp {

    private View rootView, emptyView;
    private ListView mLv;
    private RelativeLayout lookAroundRl;
    private Handler changehandler;
    private CheckBox cbAll;
    private RelativeLayout createOrderPriceRl, sureInfoRl;
    private ProgressDialog progressDialog;
    private OkHttpClient okHttpClient;
    private List<ShopCar> mList = new ArrayList<>();
    private ShopCarOuterAdapter mAdapter;
    private String key;

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
    }

    private void initRoot() {
        mLv = (ListView) rootView.findViewById(R.id.lv_shopcar);
        cbAll = (CheckBox) rootView.findViewById(R.id.cb_shopcar_cbAll);
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

    private void initData() {
        okHttpClient = new OkHttpClient();
        key = UserUtils.readLogin(getActivity(), true).getKey();
        mAdapter = new ShopCarOuterAdapter(getActivity(), mList, this);
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
                    shopCar.setShopName(out.optString("store_name"));
                    List<ShopCarGoods> shopCarGoodsList = new ArrayList<>();
                    JSONArray arrGoods = out.optJSONArray("goods");
                    for (int j = 0; j < arrGoods.length(); j++) {
                        JSONObject in = arrGoods.optJSONObject(j);
                        ShopCarGoods shopCarGoods = new ShopCarGoods();
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

    private void allChecked(boolean b) {
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setChecked(b);
            for (int j = 0; j < mList.get(i).getGoodsList().size(); j++) {
                mList.get(i).getGoodsList().get(j).setChecked(b);
            }
        }
        mAdapter.notifyDataSetChanged();
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
            case R.id.cb_item_shopcar_inner:
                ToastUtils.log(getActivity(), "inner:" + position);
                break;
            default:
                break;
        }
    }
}
