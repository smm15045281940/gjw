package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

/**
 * Created by Administrator on 2017/6/2.
 */

public class ManageBillInfoAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private ListItemClickHelp callback;

    public ManageBillInfoAdapter(Context context, List<String> list, ListItemClickHelp callback) {
        this.context = context;
        this.list = list;
        this.callback = callback;
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_manage_bill_info, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String s = list.get(position);
        holder.historyTv.setText(s);
        final View view = convertView;
        final int p = position;
        final int id = holder.deleteIv.getId();
        holder.deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, id,false);
            }
        });
        return convertView;
    }

    class ViewHolder {

        private TextView historyTv;
        private ImageView deleteIv;

        public ViewHolder(View itemView) {
            historyTv = (TextView) itemView.findViewById(R.id.tv_managebillinfo_billhistory);
            deleteIv = (ImageView) itemView.findViewById(R.id.iv_managebillinfo_billhistory_delete);
        }
    }
}
