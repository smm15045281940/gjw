package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.GoodsDetailClassifyTop;

/**
 * Created by Administrator on 2017/7/12.
 */

public class GoodsDetailClassifyTopAdapter extends BaseAdapter {

    private Context context;
    private List<GoodsDetailClassifyTop> list;
    private ViewHolder holder;

    public GoodsDetailClassifyTopAdapter(Context context, List<GoodsDetailClassifyTop> list) {
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
            convertView = View.inflate(context, R.layout.item_goods_detail_classify_top, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final GoodsDetailClassifyTop goodsDetailClassifyTop = list.get(position);
        if (goodsDetailClassifyTop != null) {
            holder.tv.setText(goodsDetailClassifyTop.getGcName());
        }
        return convertView;
    }

    class ViewHolder {

        private TextView tv;

        public ViewHolder(View itemView) {
            tv = (TextView) itemView.findViewById(R.id.tv_item_goods_detail_classify_top);
        }
    }
}
