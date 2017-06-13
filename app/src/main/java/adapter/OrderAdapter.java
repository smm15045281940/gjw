package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.Order;

/**
 * Created by Administrator on 2017/5/19.
 */

public class OrderAdapter extends BaseAdapter {

    private Context context;
    private List<Order> list;
    private ListItemClickHelp callback;

    public OrderAdapter(Context context, List<Order> list, ListItemClickHelp callback) {
        this.context = context;
        this.list = list;
        this.callback = callback;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_order, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Order order = list.get(position);
        holder.companyIconIv.setImageResource(R.mipmap.ic_launcher);
        holder.companyNameTv.setText(order.getCompanyName());
        holder.orderStateTv.setText(order.getOrderState());
        holder.goodsImgIv.setImageResource(R.mipmap.ic_launcher);
        holder.goodsNameTv.setText(order.getGoodsName());
        holder.goodsPriceTv.setText(order.getGoodsPrice());
        holder.goodsAmountTv.setText(order.getGoodsAmount());
        holder.numAmountTv.setText(order.getNumAmount());
        holder.numPriceTv.setText(order.getNumPrice());
        final View cancelView = convertView;
        final int cancelPosition = position;
        final int cancelId = holder.cancelRl.getId();
        holder.cancelRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(cancelView,parent,cancelPosition,cancelId,false);
            }
        });
        return convertView;
    }

    class ViewHolder {

        private ImageView companyIconIv, goodsImgIv;
        private TextView companyNameTv, orderStateTv, goodsNameTv, goodsPriceTv, goodsAmountTv, numAmountTv, numPriceTv;
        private RelativeLayout cancelRl;

        public ViewHolder(View itemView) {
            companyIconIv = (ImageView) itemView.findViewById(R.id.iv_item_order_companyicon);
            companyNameTv = (TextView) itemView.findViewById(R.id.tv_item_order_companyname);
            orderStateTv = (TextView) itemView.findViewById(R.id.tv_item_order_orderstate);
            goodsImgIv = (ImageView) itemView.findViewById(R.id.iv_item_order_goodsimg);
            goodsNameTv = (TextView) itemView.findViewById(R.id.tv_item_order_goodsname);
            goodsPriceTv = (TextView) itemView.findViewById(R.id.tv_item_order_goodsprice);
            goodsAmountTv = (TextView) itemView.findViewById(R.id.tv_item_order_goodsamount);
            numAmountTv = (TextView) itemView.findViewById(R.id.tv_item_order_numamount);
            numPriceTv = (TextView) itemView.findViewById(R.id.tv_item_order_numprice);
            cancelRl = (RelativeLayout) itemView.findViewById(R.id.rl_item_order_cancel);
        }
    }
}
