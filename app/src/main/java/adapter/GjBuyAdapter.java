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

import bean.GjBuy;

/**
 * Created by Administrator on 2017/7/13.
 */

public class GjBuyAdapter extends BaseAdapter {

    private Context context;
    private List<GjBuy> list;
    private GjBuyViewHolder gjBuyViewHolder;

    public GjBuyAdapter(Context context, List<GjBuy> list) {
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
            convertView = View.inflate(context, R.layout.item_gj_buy, null);
            gjBuyViewHolder = new GjBuyViewHolder(convertView);
            convertView.setTag(gjBuyViewHolder);
        } else {
            gjBuyViewHolder = (GjBuyViewHolder) convertView.getTag();
        }
        GjBuy gjBuy = list.get(position);
        if (gjBuy != null) {
            gjBuyViewHolder.storeNameTv.setText(gjBuy.getStoreName());
            gjBuyViewHolder.goodsNameTv.setText(gjBuy.getGoodsName());
            gjBuyViewHolder.goodsJingleTv.setText(gjBuy.getGoodsJingle());
            gjBuyViewHolder.goodsSalenumTv.setText(gjBuy.getGoodsSalenum());
            gjBuyViewHolder.goodsPriceTv.setText(gjBuy.getGoodsPrice());
            Picasso.with(context).load(gjBuy.getGoodsImageUrl()).placeholder(gjBuyViewHolder.imageIv.getDrawable()).into(gjBuyViewHolder.imageIv);
        }
        return convertView;
    }

    class GjBuyViewHolder {

        private TextView storeNameTv, goodsNameTv, goodsJingleTv, goodsSalenumTv, goodsPriceTv;
        private ImageView imageIv;

        public GjBuyViewHolder(View itemView) {
            storeNameTv = (TextView) itemView.findViewById(R.id.tv_item_gj_buy_store_name);
            goodsNameTv = (TextView) itemView.findViewById(R.id.tv_item_gj_buy_goods_name);
            goodsJingleTv = (TextView) itemView.findViewById(R.id.tv_item_gj_buy_goods_jingle);
            goodsSalenumTv = (TextView) itemView.findViewById(R.id.tv_item_gj_buy_goods_salenum);
            goodsPriceTv = (TextView) itemView.findViewById(R.id.tv_item_gj_buy_goods_price);
            imageIv = (ImageView) itemView.findViewById(R.id.iv_item_gj_buy_image);
        }
    }
}
