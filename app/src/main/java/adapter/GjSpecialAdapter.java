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

import bean.GjSpecial;

/**
 * Created by Administrator on 2017/6/20.
 */

public class GjSpecialAdapter extends BaseAdapter {

    private Context context;
    private List<GjSpecial> list;
    private ViewHolder holder;

    public GjSpecialAdapter(Context context, List<GjSpecial> list) {
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
            convertView = View.inflate(context, R.layout.item_gj_special_sale, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GjSpecial gjSpecial = list.get(position);
        Picasso.with(context).load(gjSpecial.getImgUrl()).placeholder(holder.iconIv.getDrawable()).into(holder.iconIv);
        holder.nameTv.setText(gjSpecial.getName());
        holder.priceTv.setText("¥" + gjSpecial.getPrice());
        return convertView;
    }

    class ViewHolder {

        private ImageView iconIv;
        private TextView nameTv, priceTv;

        public ViewHolder(View itemView) {
            iconIv = (ImageView) itemView.findViewById(R.id.iv_item_gj_special_sale_icon);
            nameTv = (TextView) itemView.findViewById(R.id.tv_item_gj_special_sale_name);
            priceTv = (TextView) itemView.findViewById(R.id.tv_item_gj_special_sale_price);
        }
    }
}
