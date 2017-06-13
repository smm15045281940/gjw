package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.AddOrder;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

public class AddOrderAdapter extends BaseAdapter {

    private Context context;
    private List<AddOrder> list;
    private ListItemClickHelp callback;

    public AddOrderAdapter(Context context, List<AddOrder> list, ListItemClickHelp callback) {
        this.context = context;
        this.list = list;
        this.callback = callback;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getAddOrderType() - 1;
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
        ViewHolder1 holder1;
        ViewHolder2 holder2;
        ViewHolder3 holder3;
        ViewHolder4 holder4;
        switch (getItemViewType(position)) {
            case 0:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_addorder_type1, null);
                    holder1 = new ViewHolder1(convertView);
                    convertView.setTag(holder1);
                } else {
                    holder1 = (ViewHolder1) convertView.getTag();
                }
                AddOrder addOrder1 = list.get(position);
                holder1.tv.setText(addOrder1.getAddOrderName());
                holder1.et.setHint(addOrder1.getAddOrderContent());
                break;
            case 1:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_addorder_type2, null);
                    holder2 = new ViewHolder2(convertView);
                    convertView.setTag(holder2);
                } else {
                    holder2 = (ViewHolder2) convertView.getTag();
                }
                AddOrder addOrder2 = list.get(position);
                holder2.tvName.setText(addOrder2.getAddOrderName());
                holder2.tvContent.setText(addOrder2.getAddOrderContent());
                final View view = convertView;
                final int p = position;
                final int kind = holder2.tvContent.getId();
                holder2.tvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onClick(view, parent, p, kind, false);
                    }
                });
                break;
            case 2:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_addorder_type3, null);
                    holder3 = new ViewHolder3(convertView);
                    convertView.setTag(holder3);
                } else {
                    holder3 = (ViewHolder3) convertView.getTag();
                }
                AddOrder addOrder3 = list.get(position);
                holder3.tv.setText(addOrder3.getAddOrderName());
                final View view1 = convertView;
                final int p1 = position;
                final int kind2 = holder3.btn.getId();
                holder3.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onClick(view1, parent, p1, kind2, false);
                    }
                });
                break;
            case 3:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_addorder_type4, null);
                    holder4 = new ViewHolder4(convertView);
                    convertView.setTag(holder4);
                } else {
                    holder4 = (ViewHolder4) convertView.getTag();
                }
                AddOrder addOrder4 = list.get(position);
                holder4.ivContent.setImageBitmap(addOrder4.getAddOrderBitmap());
                final View view4 = convertView;
                final int p4 = position;
                final int kind4 = holder4.ivClose.getId();
                holder4.ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onClick(view4, parent, p4, kind4, false);
                    }
                });
                break;
            default:
                break;
        }
        return convertView;
    }

    class ViewHolder1 {
        private TextView tv;
        private EditText et;

        public ViewHolder1(View itemView) {
            tv = (TextView) itemView.findViewById(R.id.tv_item_addorder_type1_name);
            et = (EditText) itemView.findViewById(R.id.et_item_addorder_type1);
        }
    }

    class ViewHolder2 {
        private TextView tvName, tvContent;

        public ViewHolder2(View itemView) {
            tvName = (TextView) itemView.findViewById(R.id.tv_item_addorder_type2_name);
            tvContent = (TextView) itemView.findViewById(R.id.tv_item_addorder_type2_content);
        }
    }

    class ViewHolder3 {
        private TextView tv;
        private Button btn;

        public ViewHolder3(View itemView) {
            tv = (TextView) itemView.findViewById(R.id.tv_item_addorder_type3_name);
            btn = (Button) itemView.findViewById(R.id.btn_item_addorder_type3_add);
        }
    }

    class ViewHolder4 {
        private ImageView ivClose, ivContent;
        private EditText etContent;

        public ViewHolder4(View itemView) {
            ivClose = (ImageView) itemView.findViewById(R.id.iv_item_addorder_type4_close);
            ivContent = (ImageView) itemView.findViewById(R.id.iv_item_addorder_type4_content);
            etContent = (EditText) itemView.findViewById(R.id.et_item_addorder_type4_content);
        }
    }

}

