package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.ClassifyRightInner;

/**
 * Created by Administrator on 2017/6/20.
 */

public class ClassifyRightInnerGridAdapter extends BaseAdapter {

    private Context context;
    private List<ClassifyRightInner> list;
    private ViewHolder holder;

    public ClassifyRightInnerGridAdapter(Context context, List<ClassifyRightInner> list) {
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
            convertView = View.inflate(context, R.layout.item_classify_right_inner, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ClassifyRightInner classifyRightInner = list.get(position);
        holder.nameTv.setText(classifyRightInner.getName());
        return convertView;
    }

    class ViewHolder {

        private TextView nameTv;

        public ViewHolder(View itemView) {
            nameTv = (TextView) itemView.findViewById(R.id.tv_item_classify_right_inner_name);
        }
    }
}
