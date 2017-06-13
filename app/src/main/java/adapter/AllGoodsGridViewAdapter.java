package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.AllGoods;

/**
 * Created by Administrator on 2017/6/6.
 */

public class AllGoodsGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<AllGoods> list;
    private ViewHolder holder;

    public AllGoodsGridViewAdapter(Context context, List<AllGoods> list) {
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
            convertView = View.inflate(context, R.layout.item_allgoods_gridview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AllGoods allGoods = list.get(position);
        holder.goodsNameTv.setText(allGoods.getGoodsName());
        holder.goodsPriceTv.setText(allGoods.getGoodsPrice());
        holder.goodsSalesTv.setText(allGoods.getGoodsSales());
        return convertView;
    }

    class ViewHolder {

        private TextView goodsNameTv, goodsPriceTv, goodsSalesTv;

        public ViewHolder(View itemView) {
            goodsNameTv = (TextView) itemView.findViewById(R.id.tv_item_allgoods_gridview_goodsname);
            goodsPriceTv = (TextView) itemView.findViewById(R.id.tv_item_allgoods_gridview_goodsprice);
            goodsSalesTv = (TextView) itemView.findViewById(R.id.tv_item_allgoods_gridview_goodssales);
        }
    }
}
