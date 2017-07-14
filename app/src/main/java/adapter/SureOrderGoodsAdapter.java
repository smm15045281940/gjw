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

import bean.SureOrderGoods;

/**
 * Created by Administrator on 2017/7/14.
 */

public class SureOrderGoodsAdapter extends BaseAdapter {

    private Context context;
    private List<SureOrderGoods> list;
    private SureOrderGoodsViewHolder holder;

    public SureOrderGoodsAdapter(Context context, List<SureOrderGoods> list) {
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
            convertView = View.inflate(context, R.layout.item_sure_order_goods, null);
            holder = new SureOrderGoodsViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SureOrderGoodsViewHolder) convertView.getTag();
        }
        SureOrderGoods sureOrderGoods = list.get(position);
        if (sureOrderGoods != null) {
            Picasso.with(context).load(sureOrderGoods.getGoodsImageUrl()).placeholder(holder.imageIv.getDrawable()).into(holder.imageIv);
            holder.nameTv.setText(sureOrderGoods.getGoodsName());
            holder.specTv.setText(sureOrderGoods.getGoodsSpec());
            holder.priceTv.setText(sureOrderGoods.getGoodsPrice());
            holder.numTv.setText(sureOrderGoods.getGoodsNum());
        }
        return convertView;
    }

    private class SureOrderGoodsViewHolder {

        private ImageView imageIv;
        private TextView nameTv, specTv, priceTv, numTv;

        public SureOrderGoodsViewHolder(View itemView) {
            imageIv = (ImageView) itemView.findViewById(R.id.iv_item_sure_order_goods_image);
            nameTv = (TextView) itemView.findViewById(R.id.tv_item_sure_order_goods_name);
            specTv = (TextView) itemView.findViewById(R.id.tv_item_sure_order_goods_spec);
            priceTv = (TextView) itemView.findViewById(R.id.tv_item_sure_order_goods_price);
            numTv = (TextView) itemView.findViewById(R.id.tv_item_sure_order_goods_num);
        }
    }
}
