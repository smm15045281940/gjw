package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.City;

/**
 * Created by Administrator on 2017/6/8.
 */

public class ChangeCityAdapter extends BaseAdapter {

    private Context context;
    private List<City> list;
    private ViewHolder holder;

    public ChangeCityAdapter(Context context, List<City> list) {
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
    public boolean isEnabled(int position) {
        if (list.get(position).getCityId().equals("0")) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_home_city, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        City city = list.get(position);
        if (city.getCityName() != null) {
            if (city.getCityId().equals("0")) {
                holder.cityNameTv.setTextColor(Color.BLACK);
            } else {
                holder.cityNameTv.setTextColor(Color.GRAY);
            }
            holder.cityNameTv.setText(city.getCityName());
        }
        return convertView;
    }

    class ViewHolder {

        private TextView cityNameTv;

        public ViewHolder(View itemView) {
            cityNameTv = (TextView) itemView.findViewById(R.id.tv_item_home_city);
        }
    }
}
