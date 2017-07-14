package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;
import com.gangjianwang.www.gangjianwang.ShopCarClickHelp;

import java.util.List;

import bean.ShopCar;
import utils.HeightUtils;

/**
 * Created by Administrator on 2017/6/7.
 */

public class ShopCarOuterAdapter extends BaseAdapter implements ShopCarClickHelp {

    private Context context;
    private List<ShopCar> list;
    private ShopCarClickHelp callback;
    private ListItemClickHelp lcallback;
    private ShopCarInnerAdpater mAdapter;
    private ViewHolder holder;

    public ShopCarOuterAdapter(Context context, List<ShopCar> list, ShopCarClickHelp callback, ListItemClickHelp lcallback) {
        this.context = context;
        this.list = list;
        this.callback = callback;
        this.lcallback = lcallback;
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
            convertView = View.inflate(context, R.layout.item_shopcar_outer, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ShopCar shopCar = list.get(position);
        holder.shopCb.setChecked(shopCar.isChecked());
        final View view = convertView;
        final int p = position;
        final int id = holder.shopCb.getId();
        holder.shopCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lcallback.onClick(view, parent, p, id, isChecked);
            }
        });
        holder.shopNameTv.setText(shopCar.getShopName());
        mAdapter = new ShopCarInnerAdpater(context, shopCar.getGoodsList(), this);
        holder.goodsLv.setAdapter(mAdapter);
        HeightUtils.setListViewHeight(holder.goodsLv);
        return convertView;
    }

    @Override
    public void onClick(View item, View widget, int position, int which, String storeId, boolean isChecked) {
        callback.onClick(item, widget, position, which, storeId, isChecked);
    }


    class ViewHolder {

        private CheckBox shopCb;
        private TextView shopNameTv;
        private ListView goodsLv;

        public ViewHolder(View itemView) {
            shopCb = (CheckBox) itemView.findViewById(R.id.cb_item_shopcar_outer_shop);
            shopNameTv = (TextView) itemView.findViewById(R.id.tv_item_shopcar_outer_shopname);
            goodsLv = (ListView) itemView.findViewById(R.id.lv_shopcar_goods);
        }
    }
}
