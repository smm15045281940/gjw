package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.ShopHostRec;

/**
 * Created by Administrator on 2017/6/7.
 */

public class ShopHostRecAdapter extends BaseAdapter {

    private Context context;
    private List<ShopHostRec> list;
    private ViewHolder holder;

    public ShopHostRecAdapter(Context context, List<ShopHostRec> list) {
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
            convertView = View.inflate(context, R.layout.item_shopfirstpage_headergridview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.shopHostRecTv.setVisibility(View.VISIBLE);
        } else {
            holder.shopHostRecTv.setVisibility(View.INVISIBLE);
        }
        ShopHostRec shopHostRec1 = list.get(position);
        holder.goodsNameTv.setText(shopHostRec1.getGoodsName());
        holder.goodsPriceTv.setText(shopHostRec1.getGoodsPrice());
        return convertView;
    }

    class ViewHolder {

        private TextView shopHostRecTv;
        private TextView goodsNameTv, goodsPriceTv;

        public ViewHolder(View itemView) {
            shopHostRecTv = (TextView) itemView.findViewById(R.id.tv_item_shopfirstpage_headergridview_shophostrec);
            goodsNameTv = (TextView) itemView.findViewById(R.id.tv_item_shopfirstpage_headergridview_goodsname);
            goodsPriceTv = (TextView) itemView.findViewById(R.id.tv_item_shopfirstpage_headergridview_goodsprice);
        }
    }
}
