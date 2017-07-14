package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.OrderOuter;
import utils.HeightUtils;

/**
 * Created by Administrator on 2017/7/5.
 */

public class OrderOuterAdapter extends BaseAdapter {

    private Context context;
    private List<OrderOuter> list;
    ViewHolder holder;

    public OrderOuterAdapter(Context context, List<OrderOuter> list) {
        this.context = context;
        this.list = list;
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
        holder.orderInnerLv.setAdapter(new OrderInnerAdapter(context, orderOuter.getOrderInnerList()));
        HeightUtils.setListViewHeight(holder.orderInnerLv);
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
