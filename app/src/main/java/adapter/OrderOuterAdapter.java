package adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import bean.OrderOuter;
import config.NetConfig;
import utils.HeightUtils;
import utils.ToastUtils;
import utils.UserUtils;

/**
 * Created by Administrator on 2017/7/5.
 */

public class OrderOuterAdapter extends BaseAdapter {

    private Context context;
    private List<OrderOuter> list;
    private ViewHolder holder;
    private OrderInnerAdapter orderInnerAdapter;
    private String orderId;
    private String key;
    private OkHttpClient okHttpClient;
    private ProgressDialog progressDialog;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        ToastUtils.toast(context, "无网络");
                        progressDialog.dismiss();
                        break;
                    case 1:
                        notifyDataSetChanged();
                        progressDialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public OrderOuterAdapter(Context context, List<OrderOuter> list) {
        this.context = context;
        this.list = list;
        okHttpClient = new OkHttpClient();
        progressDialog = new ProgressDialog(this.context);
        key = UserUtils.readLogin(this.context, true).getKey();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_order_outer, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final OrderOuter orderOuter = list.get(position);
        holder.storeNameTv.setText(orderOuter.getStoreName());
        holder.stateDescTv.setText(orderOuter.getStateDesc());
        String stateDesc = orderOuter.getStateDesc();
        if (stateDesc.equals("已取消")) {
            holder.removeLl.setVisibility(View.VISIBLE);
            holder.clickRl.setVisibility(View.INVISIBLE);
        } else if (stateDesc.equals("待发货")) {
            holder.removeLl.setVisibility(View.INVISIBLE);
            holder.clickRl.setVisibility(View.VISIBLE);
            holder.clickTv.setText("取消订单");
        } else if (stateDesc.equals("交易完成")) {
            holder.removeLl.setVisibility(View.VISIBLE);
            holder.clickRl.setVisibility(View.VISIBLE);
            holder.clickTv.setText("查看物流");
        }
        holder.goodsAmountTv.setText(orderOuter.getGoodsAmount());
        holder.orderAmountTv.setText("¥" + orderOuter.getOrderAmount());
        orderInnerAdapter = new OrderInnerAdapter(context, orderOuter.getOrderInnerList());
        holder.orderInnerLv.setAdapter(orderInnerAdapter);
        orderInnerAdapter.notifyDataSetChanged();
        HeightUtils.setListViewHeight(holder.orderInnerLv);
        holder.removeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toast(context, orderOuter.getOrderId());
                progressDialog.show();
                orderId = orderOuter.getOrderId();
                RequestBody body = new FormEncodingBuilder()
                        .add("order_id", orderId)
                        .add("key", key)
                        .build();
                Request request = new Request.Builder()
                        .url(NetConfig.realorderRemoveUrl)
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
                            try {
                                JSONObject objBean = new JSONObject(response.body().string());
                                if (objBean.optInt("code") == 200) {
                                    list.remove(orderOuter);
                                    handler.sendEmptyMessage(1);
                                } else {
                                    handler.sendEmptyMessage(0);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            handler.sendEmptyMessage(0);
                        }
                    }
                });
            }
        });
        holder.clickRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                orderId = orderOuter.getOrderId();
                RequestBody body = new FormEncodingBuilder()
                        .add("order_id", orderId)
                        .add("key", key)
                        .build();
                Request request = new Request.Builder()
                        .url(NetConfig.realorderCancelUrl)
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
                            try {
                                JSONObject objBean = new JSONObject(response.body().string());
                                if (objBean.optInt("code") == 200) {
                                    list.remove(orderOuter);
                                    handler.sendEmptyMessage(1);
                                } else {
                                    handler.sendEmptyMessage(0);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            handler.sendEmptyMessage(0);
                        }
                    }
                });
            }
        });
        return convertView;
    }

    class ViewHolder {

        private TextView storeNameTv, stateDescTv, goodsAmountTv, orderAmountTv;
        private ListView orderInnerLv;
        private LinearLayout removeLl;
        private RelativeLayout clickRl;
        private TextView clickTv;

        public ViewHolder(View itemView) {
            storeNameTv = (TextView) itemView.findViewById(R.id.tv_item_order_outer_store_name);
            stateDescTv = (TextView) itemView.findViewById(R.id.tv_item_order_outer_state_desc);
            goodsAmountTv = (TextView) itemView.findViewById(R.id.tv_item_order_outer_goods_amount);
            orderAmountTv = (TextView) itemView.findViewById(R.id.tv_item_order_outer_order_amount);
            orderInnerLv = (ListView) itemView.findViewById(R.id.lv_order_inner);
            removeLl = (LinearLayout) itemView.findViewById(R.id.ll_item_order_outer_remove);
            clickRl = (RelativeLayout) itemView.findViewById(R.id.rl_item_order_outer_click);
            clickTv = (TextView) itemView.findViewById(R.id.tv_item_order_outer_click);
        }
    }
}
