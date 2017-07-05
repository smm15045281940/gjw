package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    private ViewHolder holder;
    private OrderInnerAdapter orderInnerAdapter;

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
        OrderOuter orderOuter = list.get(position);
        holder.storeNameTv.setText(orderOuter.getStoreName());
        holder.stateDescTv.setText(orderOuter.getStateDesc());
        holder.goodsAmountTv.setText(orderOuter.getGoodsAmount());
        holder.orderAmountTv.setText(orderOuter.getOrderAmount());
        orderInnerAdapter = new OrderInnerAdapter(context, orderOuter.getOrderInnerList());
        holder.orderInnerLv.setAdapter(orderInnerAdapter);
        orderInnerAdapter.notifyDataSetChanged();
        HeightUtils.setListViewHeight(holder.orderInnerLv);
        return convertView;
    }

    class ViewHolder {

        private TextView storeNameTv, stateDescTv, goodsAmountTv, orderAmountTv;
        private ListView orderInnerLv;
        private LinearLayout removeLl;
        private TextView clickTv;

        public ViewHolder(View itemView) {
            storeNameTv = (TextView) itemView.findViewById(R.id.tv_item_order_outer_store_name);
            stateDescTv = (TextView) itemView.findViewById(R.id.tv_item_order_outer_state_desc);
            goodsAmountTv = (TextView) itemView.findViewById(R.id.tv_item_order_outer_goods_amount);
            orderAmountTv = (TextView) itemView.findViewById(R.id.tv_item_order_outer_order_amount);
            orderInnerLv = (ListView) itemView.findViewById(R.id.lv_order_inner);
            removeLl = (LinearLayout) itemView.findViewById(R.id.ll_item_order_outer_remove);
            clickTv = (TextView) itemView.findViewById(R.id.tv_item_order_outer_click);
        }
    }
}