package fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.GoodsDetailActivity;
import com.gangjianwang.www.gangjianwang.R;
import com.gangjianwang.www.gangjianwang.StoreDetailActivity;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.ShopHostRecAdapter;
import bean.ShopHostRec;
import config.NetConfig;
import config.ParaConfig;
import customview.HeaderGridView;
import utils.ToastUtils;
import utils.UserUtils;

import static com.gangjianwang.www.gangjianwang.R.id.v_head_shopfirstpage_headgridview_collectrank;

/**
 * Created by Administrator on 2017/6/6.
 */

public class ShopFirstpageFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private View rootView, headView;
    private RelativeLayout collectRankRl, salesRankRl;
    private TextView collectRankTv, salesRankTv;
    private View collectRankV, salesRankV;
    private HeaderGridView mHgv;
    private List<ShopHostRec> mList = new ArrayList<>();
    private ShopHostRecAdapter mAdapter;

    private final int COLLECT = 1;
    private final int SALES = 2;
    private int RANK_STATE;
    private int TARGET_STATE;

    private String key;
    private String store_id;
    private String ordertype;
    private String num;
    private ProgressDialog progressDialog;
    private OkHttpClient okHttpClient;

    private ImageView rankIv1, rankIv2, rankIv3;
    private TextView rankPriceTv, saleNumTv1, saleNumTv2, saleNumTv3;
    private String rankImageUrl1, rankImageUrl2, rankImageUrl3;
    private String rankPrice, saleNum1, saleNum2, saleNum3;
    private String rankId1, rankId2, rankId3;


    private Handler handler = new Handler() {
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
                    case 2:
                        setRankView();
                        break;
                    default:
                        break;
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
        handler.removeMessages(1);
        handler.removeMessages(2);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_shopfirstpage, null);
        initView();
        initData();
        setData();
        setListener();
        loadData();
        return rootView;
    }

    private void initView() {
        initRoot();
        initHead();
        initProgress();
    }

    private void initRoot() {
        mHgv = (HeaderGridView) rootView.findViewById(R.id.hgv_shopfirstpage);
    }

    private void initHead() {
        headView = View.inflate(getActivity(), R.layout.head_shopfirstpage_headgridview, null);
        collectRankRl = (RelativeLayout) headView.findViewById(R.id.rl_head_shopfirstpage_headgridview_collectrank);
        collectRankTv = (TextView) headView.findViewById(R.id.tv_head_shopfirstpage_headgridview_collectrank);
        collectRankV = headView.findViewById(v_head_shopfirstpage_headgridview_collectrank);
        salesRankRl = (RelativeLayout) headView.findViewById(R.id.rl_head_shopfirstpage_headgridview_salesrank);
        salesRankTv = (TextView) headView.findViewById(R.id.tv_head_shopfirstpage_headgridview_salestrank);
        salesRankV = headView.findViewById(R.id.v_head_shopfirstpage_headgridview_salestrank);
        collectRankTv.setTextColor(Color.RED);
        collectRankV.setBackgroundColor(Color.RED);
        rankIv1 = (ImageView) headView.findViewById(R.id.iv_head_shopfirstpage_headgridview_shoprank1);
        rankIv2 = (ImageView) headView.findViewById(R.id.iv_head_shopfirstpage_headgridview_shoprank2);
        rankIv3 = (ImageView) headView.findViewById(R.id.iv_head_shopfirstpage_headgridview_shoprank3);
        rankPriceTv = (TextView) headView.findViewById(R.id.tv_head_shopfirstpage_headgridview_rankprice);
        saleNumTv1 = (TextView) headView.findViewById(R.id.tv_head_shopfirstpage_headgridview_salenum1);
        saleNumTv2 = (TextView) headView.findViewById(R.id.tv_head_shopfirstpage_headgridview_salenum2);
        saleNumTv3 = (TextView) headView.findViewById(R.id.tv_head_shopfirstpage_headgridview_salenum3);
        RANK_STATE = COLLECT;
        mHgv.addHeaderView(headView);
    }

    private void initProgress() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initData() {
        ordertype = "collectdesc";
        num = "3";
        okHttpClient = new OkHttpClient();
        key = UserUtils.readLogin(getActivity(), true).getKey();
        store_id = ((StoreDetailActivity) getActivity()).getStoreId();
        mAdapter = new ShopHostRecAdapter(getActivity(), mList);
    }

    private void setData() {
        mHgv.setAdapter(mAdapter);
    }

    private void loadData() {
        progressDialog.show();
        loadRankData();
        loadBottomData();
    }

    private void loadRankData() {
        RequestBody body = new FormEncodingBuilder().add("store_id", store_id).add("ordertype", ordertype).add("num", num).build();
        Request request = new Request.Builder().url(NetConfig.storeDetailCollectUrl).post(body).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful() && parseRankJson(response.body().string())) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    private boolean parseRankJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            if (objBean.optInt("code") == 200) {
                JSONObject objDatas = objBean.optJSONObject("datas");
                JSONArray arrGoodsList = objDatas.optJSONArray("goods_list");
                for (int i = 0; i < arrGoodsList.length(); i++) {
                    JSONObject o = arrGoodsList.optJSONObject(i);
                    if (i == 0) {
                        rankPrice = o.optString("goods_price");
                        rankImageUrl1 = o.optString("goods_image_url");
                        saleNum1 = o.optString("goods_salenum");
                        rankId1 = o.optString("goods_id");
                    } else if (i == 1) {
                        rankImageUrl2 = o.optString("goods_image_url");
                        saleNum2 = o.optString("goods_salenum");
                        rankId2 = o.optString("goods_id");
                    } else if (i == 2) {
                        rankImageUrl3 = o.optString("goods_image_url");
                        saleNum3 = o.optString("goods_salenum");
                        rankId3 = o.optString("goods_id");
                    }
                }
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setRankView() {
        saleNumTv1.setText(saleNum1);
        saleNumTv2.setText(saleNum2);
        saleNumTv3.setText(saleNum3);
        rankPriceTv.setText(rankPrice);
        Picasso.with(getActivity()).load(rankImageUrl1).placeholder(rankIv1.getDrawable()).into(rankIv1);
        Picasso.with(getActivity()).load(rankImageUrl2).placeholder(rankIv2.getDrawable()).into(rankIv2);
        Picasso.with(getActivity()).load(rankImageUrl3).placeholder(rankIv3.getDrawable()).into(rankIv3);
    }

    private void loadBottomData() {
        RequestBody body = new FormEncodingBuilder().add("key", key).add("store_id", store_id).build();
        Request request = new Request.Builder().url(NetConfig.storeDetailUrl).post(body).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful() && parseBottomJson(response.body().string())) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    private boolean parseBottomJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            if (objBean.optInt("code") == 200) {
                JSONObject objDatas = objBean.optJSONObject("datas");
                JSONArray arrRecGoodsList = objDatas.optJSONArray("rec_goods_list");
                for (int i = 0; i < arrRecGoodsList.length(); i++) {
                    JSONObject o = arrRecGoodsList.optJSONObject(i);
                    ShopHostRec shopHostRec = new ShopHostRec();
                    shopHostRec.setGoodsId(o.optString("goods_id"));
                    shopHostRec.setGoodsName(o.optString("goods_name"));
                    shopHostRec.setGoodsPrice(o.optString("goods_price"));
                    shopHostRec.setImageUrl(o.optString("goods_image_url"));
                    mList.add(shopHostRec);
                }
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setListener() {
        collectRankRl.setOnClickListener(this);
        salesRankRl.setOnClickListener(this);
        rankIv1.setOnClickListener(this);
        rankIv2.setOnClickListener(this);
        rankIv3.setOnClickListener(this);
        mHgv.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_head_shopfirstpage_headgridview_collectrank:
                TARGET_STATE = COLLECT;
                changeByState(TARGET_STATE);
                ordertype = "collectdesc";
                break;
            case R.id.rl_head_shopfirstpage_headgridview_salesrank:
                TARGET_STATE = SALES;
                changeByState(TARGET_STATE);
                ordertype = "salenumdesc";
                break;
            case R.id.iv_head_shopfirstpage_headgridview_shoprank1:
                Intent intent1 = new Intent(getActivity(), GoodsDetailActivity.class);
                intent1.putExtra("goods_id", rankId1);
                intent1.putExtra("store_id", store_id);
                startActivity(intent1);
                break;
            case R.id.iv_head_shopfirstpage_headgridview_shoprank2:
                Intent intent2 = new Intent(getActivity(), GoodsDetailActivity.class);
                intent2.putExtra("goods_id", rankId2);
                intent2.putExtra("store_id", store_id);
                startActivity(intent2);
                break;
            case R.id.iv_head_shopfirstpage_headgridview_shoprank3:
                Intent intent3 = new Intent(getActivity(), GoodsDetailActivity.class);
                intent3.putExtra("goods_id", rankId3);
                intent3.putExtra("store_id", store_id);
                startActivity(intent3);
                break;
            default:
                break;
        }
        loadRankData();
    }

    private void changeByState(int TARGET_STATE) {
        if (RANK_STATE != TARGET_STATE) {
            switch (TARGET_STATE) {
                case COLLECT:
                    collectRankTv.setTextColor(Color.RED);
                    collectRankV.setBackgroundColor(Color.RED);
                    salesRankTv.setTextColor(Color.BLACK);
                    salesRankV.setBackgroundColor(Color.WHITE);
                    RANK_STATE = COLLECT;
                    break;
                case SALES:
                    collectRankTv.setTextColor(Color.BLACK);
                    collectRankV.setBackgroundColor(Color.WHITE);
                    salesRankTv.setTextColor(Color.RED);
                    salesRankV.setBackgroundColor(Color.RED);
                    RANK_STATE = SALES;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
        intent.putExtra("goods_id", mList.get(position).getGoodsId());
        intent.putExtra("store_id", store_id);
        startActivity(intent);
    }
}
