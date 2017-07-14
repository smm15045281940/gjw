package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.SureOrderStore;
import utils.HeightUtils;

/**
 * Created by Administrator on 2017/7/14.
 */

public class SureOrderStoreAdapter extends BaseAdapter {

    private Context context;
    private List<SureOrderStore> list;
    SureOrderStoreViewHolder holder;

    public SureOrderStoreAdapter(Context context, List<SureOrderStore> list) {
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
            convertView = View.inflate(context, R.layout.item_sure_order_store, null);
            holder = new SureOrderStoreViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SureOrderStoreViewHolder) convertView.getTag();
        }
        SureOrderStore sureOrderStore = list.get(position);
        if (sureOrderStore != null) {
            holder.nameTv.setText(sureOrderStore.getStoreName());
            holder.goodsTotalTv.setText(sureOrderStore.getStoreGoodsTotal());
            holder.goodsLv.setAdapter(new SureOrderGoodsAdapter(context, sureOrderStore.getSureOrderGoodsList()));
            HeightUtils.setListViewHeight(holder.goodsLv);
        }
        return convertView;
    }

    class SureOrderStoreViewHolder {

        private TextView nameTv, goodsTotalTv;
        private ListView goodsLv;

        public SureOrderStoreViewHolder(View itemView) {
            nameTv = (TextView) itemView.findViewById(R.id.tv_item_sure_order_store_name);
            goodsTotalTv = (TextView) itemView.findViewById(R.id.tv_item_sure_order_store_goods_total);
            goodsLv = (ListView) itemView.findViewById(R.id.lv_sure_order_goods);
        }
    }
}
