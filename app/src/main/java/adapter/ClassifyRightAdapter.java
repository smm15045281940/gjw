package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.ClassifyRightOuter;
import utils.HeightUtils;

/**
 * Created by Administrator on 2017/6/20.
 */

public class ClassifyRightAdapter extends BaseAdapter {

    private Context context;
    private List<ClassifyRightOuter> list;
    private ViewHolder holder;

    public ClassifyRightAdapter(Context context, List<ClassifyRightOuter> list) {
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
            convertView = View.inflate(context, R.layout.item_classify_right_outer, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ClassifyRightOuter classifyRightOuter = list.get(position);
        holder.titleTv.setText(classifyRightOuter.getName());
        ClassifyRightInnerGridAdapter gridAdapter = new ClassifyRightInnerGridAdapter(context, classifyRightOuter.getList());
        holder.gv.setAdapter(gridAdapter);
        HeightUtils.setGridViewHeight(holder.gv);
        return convertView;
    }

    class ViewHolder {

        private TextView titleTv;
        private GridView gv;

        public ViewHolder(View itemView) {
            titleTv = (TextView) itemView.findViewById(R.id.tv_item_classify_right_outer_name);
            gv = (GridView) itemView.findViewById(R.id.gv_item_classify_right_outer);
        }
    }
}
