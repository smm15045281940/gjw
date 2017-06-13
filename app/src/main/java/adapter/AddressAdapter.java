package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.Address;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class AddressAdapter extends BaseAdapter{

    private Context context;
    private List<Address> list;
    private ListItemClickHelp callback;

    public AddressAdapter(Context context, List<Address> list,ListItemClickHelp callback) {
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
        if(convertView == null){
            convertView = View.inflate(context,R.layout.item_address_manager,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Address address = list.get(position);
        holder.mNameTv.setText(address.getName());
        holder.mPhoneTv.setText(address.getPhone());
        holder.mAddressTv.setText(address.getAddress());
        if(address.getDefault()){
            holder.mDefaultTv.setVisibility(View.VISIBLE);
        }else{
            holder.mDefaultTv.setVisibility(View.INVISIBLE);
        }
        final View view1 = convertView;
        final int p1 = position;
        final int id1 = holder.mEditBtn.getId();
        holder.mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(view1,parent,p1,id1,false);
            }
        });
        final View view2 = convertView;
        final int p2 = position;
        final int id2 = holder.mDeleteBtn.getId();
        holder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(view2,parent,p2,id2,false);
            }
        });
        return convertView;
    }

    class ViewHolder{
        private TextView mNameTv,mPhoneTv,mAddressTv,mDefaultTv;
        private Button mEditBtn,mDeleteBtn;
        public ViewHolder(View itemView){
            mNameTv = (TextView) itemView.findViewById(R.id.tv_item_address_manager_name);
            mPhoneTv = (TextView) itemView.findViewById(R.id.tv_item_address_manager_phone);
            mAddressTv = (TextView) itemView.findViewById(R.id.tv_item_address_manager_address);
            mDefaultTv = (TextView) itemView.findViewById(R.id.tv_item_address_manager_default);
            mEditBtn = (Button) itemView.findViewById(R.id.btn_item_address_manager_edit);
            mDeleteBtn = (Button) itemView.findViewById(R.id.btn_item_address_manager_delete);
        }
    }
}
