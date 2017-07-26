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

import bean.HomeGoodsInner;

/**
 * Created by Administrator on 2017/7/12.
 */

public class HomeGoodsInnerAdapter extends BaseAdapter {

    private Context context;
    private List<HomeGoodsInner> homeGoodsInnerList;
    private ViewHolder holder;

    public HomeGoodsInnerAdapter(Context context, List<HomeGoodsInner> homeGoodsInnerList) {
        this.context = context;
        this.homeGoodsInnerList = homeGoodsInnerList;
    }

    @Override
    public int getCount() {
        return homeGoodsInnerList.size();
    }

    @Override
    public Object getItem(int position) {
        return homeGoodsInnerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_home_grid_1, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeGoodsInner homeGoodsInner = homeGoodsInnerList.get(position);
        if (homeGoodsInner != null) {
            holder.nameTv.setText(homeGoodsInner.getName());
            holder.priceTv.setText(homeGoodsInner.getPrice());
            Picasso.with(context).load(homeGoodsInner.getImage()).placeholder(holder.imageIv.getDrawable()).into(holder.imageIv);
        }
        return convertView;
    }

    class ViewHolder {

        private ImageView imageIv;
        private TextView nameTv, priceTv;

        public ViewHolder(View itemView) {
            imageIv = (ImageView) itemView.findViewById(R.id.iv_item_home_grid_1_image);
            nameTv = (TextView) itemView.findViewById(R.id.tv_item_home_grid_1_name);
            priceTv = (TextView) itemView.findViewById(R.id.tv_item_home_grid_1_price);
        }
    }
}
