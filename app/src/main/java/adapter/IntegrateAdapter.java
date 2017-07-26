package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.Integrate;

/**
 * Created by Administrator on 2017/7/4.
 */

public class IntegrateAdapter extends BaseAdapter {

    private Context context;
    private List<Integrate> list;
    private ViewHolder holder;

    public IntegrateAdapter(Context context, List<Integrate> list) {
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
            convertView = View.inflate(context, R.layout.item_integrate, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Integrate integrate = list.get(position);
        holder.stageTextTv.setText(integrate.getStageText());
        holder.plDescTv.setText(integrate.getPlDesc());
        holder.plPointsTv.setText("+" + integrate.getPlPoints());
        holder.addTimeTextTv.setText(integrate.getAddTimeText());
        return convertView;
    }

    class ViewHolder {

        private TextView stageTextTv, plDescTv, plPointsTv, addTimeTextTv;

        public ViewHolder(View itemView) {
            stageTextTv = (TextView) itemView.findViewById(R.id.tv_item_integrate_stagetext);
            plDescTv = (TextView) itemView.findViewById(R.id.tv_item_integrate_pl_desc);
            plPointsTv = (TextView) itemView.findViewById(R.id.tv_item_integrate_pl_points);
            addTimeTextTv = (TextView) itemView.findViewById(R.id.tv_item_integrate_addtimetext);
        }
    }
}
