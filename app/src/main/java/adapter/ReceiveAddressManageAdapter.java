package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.AddAddress;

/**
 * Created by Administrator on 2017/5/26.
 */

public class ReceiveAddressManageAdapter extends BaseAdapter{

    private Context context;
    private List<AddAddress> list;

    public ReceiveAddressManageAdapter(Context context, List<AddAddress> list) {
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
            convertView = View.inflate(context,R.layout.item_receiveaddressmanage,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        AddAddress addAddress = list.get(position);
        holder.nameTv.setText(addAddress.getName());
        holder.numberTv.setText(addAddress.getNumber());
        holder.roughAddressTv.setText(addAddress.getRoughAddress());
        holder.detailAddressTv.setText(addAddress.getDetailAddress());
        return convertView;
    }

    class ViewHolder{

        private TextView nameTv,numberTv,roughAddressTv,detailAddressTv;

        public ViewHolder(View itemView) {
            nameTv = (TextView) itemView.findViewById(R.id.tv_item_receiveaddressmanage_name);
            numberTv = (TextView) itemView.findViewById(R.id.tv_item_receiveaddressmanage_number);
            roughAddressTv = (TextView) itemView.findViewById(R.id.tv_item_receiveaddressmanage_roughaddress);
            detailAddressTv = (TextView) itemView.findViewById(R.id.tv_item_receiveaddressmanage_detailaddress);
        }
    }
}
