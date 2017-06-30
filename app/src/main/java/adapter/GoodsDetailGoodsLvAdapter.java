package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import bean.GoodsDetailGoods;
import bean.GoodsDetailOtherGoods;

/**
 * Created by Administrator on 2017/5/23.
 */

public class GoodsDetailGoodsLvAdapter extends BaseAdapter {

    private Context context;
    private List<GoodsDetailGoods> mainList;
    private List<GoodsDetailOtherGoods> otherList;
    private ListItemClickHelp callback;

    public GoodsDetailGoodsLvAdapter(Context context, List<GoodsDetailGoods> mainList, List<GoodsDetailOtherGoods> otherList, ListItemClickHelp callback) {
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
        if (mainList.size() == 0 || otherList.size() == 0) {
            return 0;
        } else {
            return mainList.size() + otherList.size();
        }
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
            holder1.goodsNameTv.setText(goodsDetailGoods.getGoodsName());
            holder1.goodsJingleTv.setText(goodsDetailGoods.getGoodsJingle());
            holder1.goodsPriceTv.setText(goodsDetailGoods.getGoodsPrice());
            holder1.goodsSalenumTv.setText(goodsDetailGoods.getGoodsSalenum());
            holder1.areaNameTv.setText(goodsDetailGoods.getAreaName());
            holder1.ifStoreCnTv.setText(goodsDetailGoods.getIfStoreCn());
            holder1.contentTv.setText(goodsDetailGoods.getContent());
            holder1.tvSize.setText(goodsDetailGoods.getSize());
            holder1.tvThick.setText(goodsDetailGoods.getThick());
            final View view = convertView;
            final int p = position;
            final int id = holder1.areaNameTv.getId();
            holder1.areaNameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(view, parent, p, id, false);
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
            } else {
                holder2.otherTv.setVisibility(View.VISIBLE);
            }
            GoodsDetailOtherGoods goodsDetailOtherGoods = otherList.get(position - 1);
            Picasso.with(context).load(goodsDetailOtherGoods.getIcon()).placeholder(holder2.iconIv.getDrawable()).into(holder2.iconIv);
            holder2.goodsNameTv.setText(goodsDetailOtherGoods.getGoodsName());
            holder2.specTv.setText(goodsDetailOtherGoods.getSpec());
            holder2.sizeTv.setText(goodsDetailOtherGoods.getSize());
            holder2.priceTv.setText("¥"+goodsDetailOtherGoods.getPrice());
        }
        return convertView;
    }

    class ViewHolder1 {

        private TextView goodsNameTv, goodsJingleTv, goodsPriceTv, goodsSalenumTv, areaNameTv, ifStoreCnTv;
        private TextView contentTv, tvSize, tvThick;

        public ViewHolder1(View itemView) {
            goodsNameTv = (TextView) itemView.findViewById(R.id.tv_item_goodsdetailgoods_goodsname);
            goodsJingleTv = (TextView) itemView.findViewById(R.id.tv_item_goodsdetailgoods_goodsjingle);
            goodsPriceTv = (TextView) itemView.findViewById(R.id.tv_item_goodsdetailgoods_goodsprice);
            goodsSalenumTv = (TextView) itemView.findViewById(R.id.tv_item_goodsdetailgoods_goodssalenum);
            areaNameTv = (TextView) itemView.findViewById(R.id.tv_item_goodsdetailgoods_areaname);
            ifStoreCnTv = (TextView) itemView.findViewById(R.id.tv_item_goodsdetailgoods_ifstorecn);
            contentTv = (TextView) itemView.findViewById(R.id.tv_item_goodsdetailgoods_content);
            tvSize = (TextView) itemView.findViewById(R.id.tv_item_goodsdetailgoods_size);
            tvThick = (TextView) itemView.findViewById(R.id.tv_item_goodsdetailgoods_thick);
        }
    }

    class ViewHolder2 {

        private TextView otherTv;
        private ImageView iconIv;
        private TextView goodsNameTv, specTv, sizeTv, priceTv;

        public ViewHolder2(View itemView) {
            otherTv = (TextView) itemView.findViewById(R.id.tv_item_goodsdetailgoods_othergoods);
            iconIv = (ImageView) itemView.findViewById(R.id.iv_item_goodsdetailgoods_2_icon);
            goodsNameTv = (TextView) itemView.findViewById(R.id.tv_item_goodsdetailgoods_2_goodsname);
            specTv = (TextView) itemView.findViewById(R.id.tv_item_goodsdetailgoods_2_spec);
            sizeTv = (TextView) itemView.findViewById(R.id.tv_item_goodsdetailgoods_2_size);
            priceTv = (TextView) itemView.findViewById(R.id.tv_item_goodsdetailgoods_2_price);
        }
    }
}
