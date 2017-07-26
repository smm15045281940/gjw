package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.GoodsDetailDetail;

/**
 * Created by Administrator on 2017/5/23.
 */

public class GoodsDetailDetailAdapter extends BaseAdapter {

    private Context context;
    private List<GoodsDetailDetail> list;

    public GoodsDetailDetailAdapter(Context context, List<GoodsDetailDetail> list) {
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_goodsdetaildetail, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv.setImageResource(R.mipmap.img_default);
        return convertView;
    }

    class ViewHolder {

        private ImageView iv;

        public ViewHolder(View itemView) {
            iv = (ImageView) itemView.findViewById(R.id.iv_item_goodsdetaildetail);
        }
    }
}
