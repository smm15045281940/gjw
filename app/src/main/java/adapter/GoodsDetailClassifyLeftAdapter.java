package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.GoodsDetailClassifyLeft;

/**
 * Created by Administrator on 2017/7/12.
 */

public class GoodsDetailClassifyLeftAdapter extends BaseAdapter {

    private Context context;
    private List<GoodsDetailClassifyLeft> list;
    private ViewHolder holder;

    public GoodsDetailClassifyLeftAdapter(Context context, List<GoodsDetailClassifyLeft> list) {
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
            convertView = View.inflate(context, R.layout.item_goods_detail_classify, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GoodsDetailClassifyLeft goodsDetailClassify = list.get(position);
        if (goodsDetailClassify != null) {
            holder.tv.setText(goodsDetailClassify.getGcName());
        }
        return convertView;
    }

    class ViewHolder {

        private TextView tv;

        public ViewHolder(View itemView) {
            tv = (TextView) itemView.findViewById(R.id.tv_item_goods_detail_classify);
        }
    }
}
