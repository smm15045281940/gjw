package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import bean.OrderInner;

/**
 * Created by Administrator on 2017/5/19.
 */

public class OrderInnerAdapter extends BaseAdapter {

    private Context context;
    private List<OrderInner> list;
    private ViewHolder holder;

    public OrderInnerAdapter(Context context, List<OrderInner> list) {
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_order_inner, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OrderInner orderInner = list.get(position);
        Picasso.with(context).load(orderInner.getGoodsImageUrl()).placeholder(holder.goodsImageUrlIv.getDrawable()).into(holder.goodsImageUrlIv);
        holder.goodsNameTv.setText(orderInner.getGoodsName());
        if (orderInner.getGoodsSpec().equals("null")) {
            holder.goodsSpecTv.setText("");
        } else {
            holder.goodsSpecTv.setText(orderInner.getGoodsSpec());
        }
        holder.goodsPriceTv.setText("¥" + orderInner.getGoodsPrice());
        holder.goodsNumTv.setText("x" + orderInner.getGoodsNum());
        return convertView;
    }

    class ViewHolder {

        private ImageView goodsImageUrlIv;
        private TextView goodsNameTv, goodsSpecTv, goodsPriceTv, goodsNumTv;

        public ViewHolder(View itemView) {
            goodsImageUrlIv = (ImageView) itemView.findViewById(R.id.iv_item_order_inner_goods_image_url);
            goodsNameTv = (TextView) itemView.findViewById(R.id.tv_item_order_inner_goods_name);
            goodsSpecTv = (TextView) itemView.findViewById(R.id.tv_item_order_inner_goods_spec);
            goodsPriceTv = (TextView) itemView.findViewById(R.id.tv_item_order_inner_goods_price);
            goodsNumTv = (TextView) itemView.findViewById(R.id.tv_item_order_inner_goods_num);
        }
    }
}
