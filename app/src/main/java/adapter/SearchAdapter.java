package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

/**
 * Created by Administrator on 2017/5/8 0008.
 */

public class SearchAdapter extends BaseAdapter{

    private Context context;
    private List<String> list;

    public SearchAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if(list == null || list.size() == 0){
            return 0;
        }else{
            return list.size();
        }
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
            convertView = View.inflate(context,R.layout.item_hotsearch,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String s = list.get(position);
        holder.tv.setText(s);
        return convertView;
    }

    class ViewHolder{
        private TextView tv;
        public ViewHolder(View itemView){
            tv = (TextView) itemView.findViewById(R.id.tv_item_hotsearch);
        }
    }
}
