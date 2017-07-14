package fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import adapter.OrderOuterAdapter;
import bean.OrderInner;
import bean.OrderOuter;
import config.NetConfig;
import config.ParaConfig;
import utils.ToastUtils;
import utils.UserUtils;


/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class RealorderFragment extends Fragment implements View.OnClickListener {

    private View rootView, emptyView;
    private LinearLayout allLl, waitpayLl, waitreceiveLl, waitselfLl, waitevaluateLl;
    private TextView allTv, waitpayTv, waitreceiveTv, waitselfTv, waitevaluateTv;
    private View allV, waitpayV, waitreceiveV, waitselfV, waitevaluateV;
    private ListView lv;
    private List<OrderOuter> orderOuterList = new ArrayList<>();
    private OrderOuterAdapter orderOuterAdapter;
    private OkHttpClient okHttpClient;
    private ProgressDialog progressDialog;
    private int curPosition = 0, tarPosition = 0;

    private String key, state_type, order_key;
    private int curPage = 1;
    private final int REFRESH = 0;
    private final int LOAD = 1;
    private int LOAD_STATE = REFRESH;

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
                orderOuterAdapter.notifyDataSetChanged();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_realorder, null);
        initView();
        initData();
        setData();
        setListener();
        loadData();
        return rootView;
    }

    private void initView() {
        initRoot();
        initEmpty();
    }

    private void initRoot() {
        rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        allLl = (LinearLayout) rootView.findViewById(R.id.ll_realorder_all);
        waitpayLl = (LinearLayout) rootView.findViewById(R.id.ll_realorder_wait_pay);
        waitreceiveLl = (LinearLayout) rootView.findViewById(R.id.ll_realorder_wait_receive);
        waitselfLl = (LinearLayout) rootView.findViewById(R.id.ll_realorder_wait_self);
        waitevaluateLl = (LinearLayout) rootView.findViewById(R.id.ll_realorder_wait_evaluate);
        allTv = (TextView) rootView.findViewById(R.id.tv_realorder_all);
        waitpayTv = (TextView) rootView.findViewById(R.id.tv_realorder_wait_pay);
        waitreceiveTv = (TextView) rootView.findViewById(R.id.tv_realorder_wait_receive);
        waitselfTv = (TextView) rootView.findViewById(R.id.tv_realorder_wait_self);
        waitevaluateTv = (TextView) rootView.findViewById(R.id.tv_realorder_wait_evaluate);
        allV = rootView.findViewById(R.id.v_realorder_all);
        waitpayV = rootView.findViewById(R.id.v_realorder_wait_pay);
        waitreceiveV = rootView.findViewById(R.id.v_realorder_wait_receive);
        waitselfV = rootView.findViewById(R.id.v_realorder_wait_self);
        waitevaluateV = rootView.findViewById(R.id.v_realorder_wait_evaluate);
        lv = (ListView) rootView.findViewById(R.id.lv_realorder);
    }

    private void initEmpty() {
        emptyView = View.inflate(getActivity(), R.layout.empty_order, null);
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ((ViewGroup) lv.getParent()).addView(emptyView);
        emptyView.setVisibility(View.GONE);
        lv.setEmptyView(emptyView);
    }

    private void initData() {
        orderOuterAdapter = new OrderOuterAdapter(getActivity(), orderOuterList);
        okHttpClient = new OkHttpClient();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        key = UserUtils.readLogin(getActivity(), true).getKey();
        state_type = "";
        order_key = "";
        Bundle bundle = getArguments();
        if (bundle != null) {
            tarPosition = bundle.getInt("realorder");
            changeOrder();
        }
    }

    private void setData() {
        lv.setAdapter(orderOuterAdapter);
    }

    private void setListener() {
        allLl.setOnClickListener(this);
        waitpayLl.setOnClickListener(this);
        waitreceiveLl.setOnClickListener(this);
        waitselfLl.setOnClickListener(this);
        waitevaluateLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_realorder_all:
                tarPosition = 0;
                break;
            case R.id.ll_realorder_wait_pay:
                tarPosition = 1;
                break;
            case R.id.ll_realorder_wait_receive:
                tarPosition = 2;
                break;
            case R.id.ll_realorder_wait_self:
                tarPosition = 3;
                break;
            case R.id.ll_realorder_wait_evaluate:
                tarPosition = 4;
                break;
            default:
                break;
        }
        changeOrder();
    }

    private void changeOrder() {
        if (tarPosition != curPosition) {
            LOAD_STATE = REFRESH;
            switch (tarPosition) {
                case 0:
                    allTv.setTextColor(Color.RED);
                    waitpayTv.setTextColor(Color.BLACK);
                    waitreceiveTv.setTextColor(Color.BLACK);
                    waitselfTv.setTextColor(Color.BLACK);
                    waitevaluateTv.setTextColor(Color.BLACK);
                    allV.setVisibility(View.VISIBLE);
                    waitpayV.setVisibility(View.INVISIBLE);
                    waitreceiveV.setVisibility(View.INVISIBLE);
                    waitselfV.setVisibility(View.INVISIBLE);
                    waitevaluateV.setVisibility(View.INVISIBLE);
                    state_type = "";
                    order_key = "";
                    break;
                case 1:
                    allTv.setTextColor(Color.BLACK);
                    waitpayTv.setTextColor(Color.RED);
                    waitreceiveTv.setTextColor(Color.BLACK);
                    waitselfTv.setTextColor(Color.BLACK);
                    waitevaluateTv.setTextColor(Color.BLACK);
                    allV.setVisibility(View.INVISIBLE);
                    waitpayV.setVisibility(View.VISIBLE);
                    waitreceiveV.setVisibility(View.INVISIBLE);
                    waitselfV.setVisibility(View.INVISIBLE);
                    waitevaluateV.setVisibility(View.INVISIBLE);
                    state_type = "state_new";
                    order_key = "";
                    break;
                case 2:
                    allTv.setTextColor(Color.BLACK);
                    waitpayTv.setTextColor(Color.BLACK);
                    waitreceiveTv.setTextColor(Color.RED);
                    waitselfTv.setTextColor(Color.BLACK);
                    waitevaluateTv.setTextColor(Color.BLACK);
                    allV.setVisibility(View.INVISIBLE);
                    waitpayV.setVisibility(View.INVISIBLE);
                    waitreceiveV.setVisibility(View.VISIBLE);
                    waitselfV.setVisibility(View.INVISIBLE);
                    waitevaluateV.setVisibility(View.INVISIBLE);
                    state_type = "state_send";
                    order_key = "";
                    break;
                case 3:
                    allTv.setTextColor(Color.BLACK);
                    waitpayTv.setTextColor(Color.BLACK);
                    waitreceiveTv.setTextColor(Color.BLACK);
                    waitselfTv.setTextColor(Color.RED);
                    waitevaluateTv.setTextColor(Color.BLACK);
                    allV.setVisibility(View.INVISIBLE);
                    waitpayV.setVisibility(View.INVISIBLE);
                    waitreceiveV.setVisibility(View.INVISIBLE);
                    waitselfV.setVisibility(View.VISIBLE);
                    waitevaluateV.setVisibility(View.INVISIBLE);
                    state_type = "state_notakes";
                    order_key = "";
                    break;
                case 4:
                    allTv.setTextColor(Color.BLACK);
                    waitpayTv.setTextColor(Color.BLACK);
                    waitreceiveTv.setTextColor(Color.BLACK);
                    waitselfTv.setTextColor(Color.BLACK);
                    waitevaluateTv.setTextColor(Color.RED);
                    allV.setVisibility(View.INVISIBLE);
                    waitpayV.setVisibility(View.INVISIBLE);
                    waitreceiveV.setVisibility(View.INVISIBLE);
                    waitselfV.setVisibility(View.INVISIBLE);
                    waitevaluateV.setVisibility(View.VISIBLE);
                    state_type = "state_noeval";
                    order_key = "";
                    break;
                default:
                    break;
            }
            loadData();
            curPosition = tarPosition;
        }
    }

    private void loadData() {
        emptyView.setVisibility(View.GONE);
        progressDialog.show();
        RequestBody body = new FormEncodingBuilder()
                .add("key", key)
                .add("state_type", state_type)
                .add("order_key", order_key)
                .build();
        Request request = new Request.Builder()
                .url(NetConfig.realallorderUrl + curPage)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (LOAD_STATE == REFRESH) {
                        orderOuterList.clear();
                    }
                    if (parseJson(response.body().string())) {
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(0);
                    }
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
                JSONArray arrOrderGroupList = objDatas.optJSONArray("order_group_list");
                if (arrOrderGroupList.length() != 0) {
                    JSONArray arrOrderList = arrOrderGroupList.optJSONObject(0).optJSONArray("order_list");
                    for (int i = 0; i < arrOrderList.length(); i++) {
                        JSONObject objArr = arrOrderList.optJSONObject(i);
                        OrderOuter orderOuter = new OrderOuter();
                        orderOuter.setOrderId(objArr.optString("order_id"));
                        orderOuter.setStoreName(objArr.optString("store_name"));
                        orderOuter.setStateDesc(objArr.optString("state_desc"));
                        orderOuter.setGoodsAmount("?");
                        orderOuter.setOrderAmount(objArr.optString("order_amount"));
                        List<OrderInner> list = new ArrayList<>();
                        JSONArray arrArr = objArr.optJSONArray("extend_order_goods");
                        for (int j = 0; j < arrArr.length(); j++) {
                            OrderInner orderInner = new OrderInner();
                            JSONObject o = arrArr.optJSONObject(j);
                            orderInner.setGoodsImageUrl(o.optString("goods_image_url"));
                            orderInner.setGoodsName(o.optString("goods_name"));
                            orderInner.setGoodsSpec(o.optString("goods_spec"));
                            orderInner.setGoodsPrice(o.optString("goods_price"));
                            orderInner.setGoodsNum(o.optString("goods_num"));
                            list.add(orderInner);
                        }
                        orderOuter.setOrderInnerList(list);
                        orderOuterList.add(orderOuter);
                    }
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

}
