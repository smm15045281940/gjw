package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.OfKind;

/**
 * Created by Administrator on 2017/6/22.
 */

public class OfKindAdapter extends BaseAdapter {

    private Context context;
    private List<OfKind> list;
    private ViewHolder holder;

    public OfKindAdapter(Context context, List<OfKind> list) {
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
            convertView = View.inflate(context, R.layout.item_chooseaddress, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OfKind ofKind = list.get(position);
        holder.contentTv.setText(ofKind.getName());
        return convertView;
    }

    class ViewHolder {

        private TextView contentTv;

        public ViewHolder(View itemView) {
            contentTv = (TextView) itemView.findViewById(R.id.tv_item_chooseaddress_address);
        }
    }
}
