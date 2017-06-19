package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.Province;

/**
 * Created by Administrator on 2017/6/19.
 */

public class AddressSpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<Province> list;
    private ViewHolder holder;

    public AddressSpinnerAdapter(Context context, List<Province> list) {
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
            convertView = View.inflate(context, R.layout.item_province, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Province province = list.get(position);
        holder.provinceTv.setText(province.getName());
        return convertView;
    }

    class ViewHolder {

        private TextView provinceTv;

        public ViewHolder(View itemView) {
            provinceTv = (TextView) itemView.findViewById(R.id.tv_item_province);
        }
    }
}
