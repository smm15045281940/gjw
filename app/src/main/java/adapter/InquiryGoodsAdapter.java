package adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import bean.Goods;

/**
 * Created by Administrator on 2017/7/24.
 */

public class InquiryGoodsAdapter extends BaseAdapter {

    private Context context;
    private List<Goods> list;
    private ViewHolder holder;

    public InquiryGoodsAdapter(Context context, List<Goods> list) {
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
            convertView = View.inflate(context, R.layout.item_inquiry_goods, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Goods goods = list.get(position);
        if (goods != null) {
            if (!TextUtils.isEmpty(goods.getGoodsName())) {
                holder.goodsNameTv.setText(goods.getGoodsName());
            }
            if (!TextUtils.isEmpty(goods.getGoodsPayPrice())) {
                holder.goodsPayPriceTv.setText(goods.getGoodsPayPrice());
            }
            if (!TextUtils.isEmpty(goods.getGoodsNum())) {
                holder.goodsNumTv.setText(goods.getGoodsNum());
            }
            if (!TextUtils.isEmpty(goods.getGoodsImage())) {
                Picasso.with(context).load(goods.getGoodsImage()).placeholder(holder.imageIv.getDrawable()).into(holder.imageIv);
            }
        }
        return convertView;
    }

    private class ViewHolder {

        private TextView goodsNameTv, goodsPayPriceTv, goodsNumTv;
        private ImageView imageIv;

        public ViewHolder(View itemView) {
            goodsNameTv = (TextView) itemView.findViewById(R.id.tv_item_inquiry_goods_goods_name);
            goodsPayPriceTv = (TextView) itemView.findViewById(R.id.tv_item_inquiry_goods_goods_pay_price);
            goodsNumTv = (TextView) itemView.findViewById(R.id.tv_item_inquiry_goods_goods_num);
            imageIv = (ImageView) itemView.findViewById(R.id.iv_item_inquiry_goods_goods_image);
        }
    }
}
