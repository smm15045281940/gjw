package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.ChooseAddressActivity;
import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.GoodsDetailGoods;
import utils.ToastUtils;

/**
 * Created by Administrator on 2017/5/23.
 */

public class GoodsDetailGoodsLvAdapter extends BaseAdapter {

    private Context context;
    private List<GoodsDetailGoods> mainList;
    private List<String> otherList;
    private ListItemClickHelp callback;

    public GoodsDetailGoodsLvAdapter(Context context, List<GoodsDetailGoods> mainList, List<String> otherList,ListItemClickHelp callback) {
        this.context = context;
        this.mainList = mainList;
        this.otherList = otherList;
        this.callback = callback;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mainList.size() + otherList.size();
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return mainList.get(position);
        } else {
            return otherList.get(position - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder1 holder1;
        ViewHolder2 holder2;
        if (getItemViewType(position) == 0) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_goodsdetailgoods_1, null);
                holder1 = new ViewHolder1(convertView);
                convertView.setTag(holder1);
            } else {
                holder1 = (ViewHolder1) convertView.getTag();
            }
            GoodsDetailGoods goodsDetailGoods = mainList.get(position);
            holder1.tvAddress.setText(goodsDetailGoods.getSendAddress());
            holder1.tvSize.setText(goodsDetailGoods.getSize());
            holder1.tvThick.setText(goodsDetailGoods.getThick());
            final View view = convertView;
            final int p = position;
            final int id = holder1.tvAddress.getId();
            holder1.tvAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(view,parent,p,id,false);
                }
            });
        } else if (getItemViewType(position) == 1) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_goodsdetailgoods_2, null);
                holder2 = new ViewHolder2(convertView);
                convertView.setTag(holder2);
            } else {
                holder2 = (ViewHolder2) convertView.getTag();
            }
            if (position > 1) {
                holder2.otherTv.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    class ViewHolder1 {

        private TextView tvAddress;
        private TextView tvSize;
        private TextView tvThick;

        public ViewHolder1(View itemView) {
            tvAddress = (TextView) itemView.findViewById(R.id.tv_item_goodsdetailgoods_address);
            tvSize = (TextView) itemView.findViewById(R.id.tv_item_goodsdetailgoods_size);
            tvThick = (TextView) itemView.findViewById(R.id.tv_item_goodsdetailgoods_thick);
        }
    }

    class ViewHolder2 {

        private TextView otherTv;

        public ViewHolder2(View itemView) {
            otherTv = (TextView) itemView.findViewById(R.id.tv_item_goodsdetailgoods_othergoods);
        }
    }
}
