package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

/**
 * Created by Administrator on 2017/5/25.
 */

public class UnitPopAdapter extends BaseAdapter{

    private Context context;
    private List<String> list;

    public UnitPopAdapter(Context context, List<String> list) {
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
        if(convertView == null){
            convertView = View.inflate(context,R.layout.item_unit_pop,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String s = list.get(position);
        holder.unitNameTv.setText(s);
        if(position != 0){
            holder.unitNameTv.setTextColor(Color.GRAY);
        }
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        boolean b = true;
        if(position == 0){
            b = false;
        }else{
            b = true;
        }
        return b;
    }

    class ViewHolder{

        private TextView unitNameTv;

        public ViewHolder(View itemView) {
            unitNameTv = (TextView) itemView.findViewById(R.id.tv_item_unit_pop_unitname);
        }
    }
}
