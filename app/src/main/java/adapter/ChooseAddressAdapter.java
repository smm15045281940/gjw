package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

/**
 * Created by Administrator on 2017/5/24.
 */

public class ChooseAddressAdapter extends BaseAdapter{

    private Context context;
    private List<String> list;

    public ChooseAddressAdapter(Context context, List<String> list) {
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
            convertView = View.inflate(context,R.layout.item_chooseaddress,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String s = list.get(position);
        holder.addressTv.setText(s);
        return convertView;
    }

    class ViewHolder{

        private TextView addressTv;

        public ViewHolder(View itemView) {
            addressTv = (TextView) itemView.findViewById(R.id.tv_item_chooseaddress_address);
        }
    }
}
