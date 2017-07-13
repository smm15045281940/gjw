package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import bean.ShopCarGoods;

/**
 * Created by Administrator on 2017/6/7.
 */

public class ShopCarInnerAdpater extends BaseAdapter {

    private Context context;
    private List<ShopCarGoods> list;
    private int outPosition;
    private ListItemClickHelp callback;
    private ViewHolder holder;

    public ShopCarInnerAdpater(Context context, List<ShopCarGoods> list, int outPosition,ListItemClickHelp callback) {
        this.context = context;
        this.list = list;
        this.outPosition = outPosition;
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shopcar_inner, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ShopCarGoods shopCarGoods = list.get(position);
        holder.goodsCb.setChecked(shopCarGoods.isChecked());
        final View view = convertView;
        final int p = position;
        final int id = holder.goodsCb.getId();
        holder.goodsCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                callback.onClick(view, parent, p, id, isChecked);
            }
        });
        Picasso.with(context).load(shopCarGoods.getGoodsImageUrl()).placeholder(holder.imageIv.getDrawable()).into(holder.imageIv);
        holder.goodsNameTv.setText(shopCarGoods.getGoodsName());
        holder.goodsSizeTv.setText(shopCarGoods.getGoodsSize());
        holder.goodsPriceTv.setText("¥" + shopCarGoods.getGoodsPrice());
        return convertView;
    }

    class ViewHolder {

        private CheckBox goodsCb;
        private ImageView imageIv;
        private TextView goodsNameTv, goodsSizeTv, goodsPriceTv;

        public ViewHolder(View itemView) {
            goodsCb = (CheckBox) itemView.findViewById(R.id.cb_item_shopcar_inner);
            imageIv = (ImageView) itemView.findViewById(R.id.iv_item_shopcar_inner_goodsicon);
            goodsNameTv = (TextView) itemView.findViewById(R.id.tv_item_shopcar_inner_goodsname);
            goodsSizeTv = (TextView) itemView.findViewById(R.id.tv_item_shopcar_inner_goodssize);
            goodsPriceTv = (TextView) itemView.findViewById(R.id.tv_item_shopcar_inner_goodsprice);
        }
    }
}
