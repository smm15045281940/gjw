package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.Address;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class AddressAdapter extends BaseAdapter {

    private Context context;
    private List<Address> list;
    private ListItemClickHelp callback;
    private ViewHolder holder;

    public AddressAdapter(Context context, List<Address> list, ListItemClickHelp callback) {
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
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_address_manager, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Address address = list.get(position);
        holder.trueNameTv.setText(address.getTrueName());
        holder.mobPhoneTv.setText(address.getMobPhone());
        holder.areaInfoTv.setText(address.getAreaInfo());
        holder.addressTv.setText(address.getAddress());
        if (address.getIsDefault().equals("1")) {
            holder.isDefaultTv.setVisibility(View.VISIBLE);
        } else {
            holder.isDefaultTv.setVisibility(View.INVISIBLE);
        }
        final View view = convertView;
        final int p = position;
        final int editId = holder.editTv.getId();
        final int delId = holder.delTv.getId();
        holder.editTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, editId, false);
            }
        });
        holder.delTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, delId, false);
            }
        });
        return convertView;
    }

    class ViewHolder {

        private TextView trueNameTv, mobPhoneTv, areaInfoTv, addressTv, isDefaultTv;
        private TextView editTv, delTv;

        public ViewHolder(View itemView) {
            trueNameTv = (TextView) itemView.findViewById(R.id.tv_item_address_manager_true_name);
            mobPhoneTv = (TextView) itemView.findViewById(R.id.tv_item_address_manager_mob_phone);
            areaInfoTv = (TextView) itemView.findViewById(R.id.tv_item_address_manager_area_info);
            addressTv = (TextView) itemView.findViewById(R.id.tv_item_address_manager_address);
            isDefaultTv = (TextView) itemView.findViewById(R.id.tv_item_address_manager_isdefault);
            editTv = (TextView) itemView.findViewById(R.id.tv_item_address_manager_edit);
            delTv = (TextView) itemView.findViewById(R.id.tv_item_address_manager_delete);
        }
    }
}
