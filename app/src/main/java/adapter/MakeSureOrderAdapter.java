package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;

import bean.MakeSureOrder;

/**
 * Created by Administrator on 2017/5/26.
 */

public class MakeSureOrderAdapter extends BaseAdapter{

    private Context context;
    private MakeSureOrder makeSureOrder;

    public MakeSureOrderAdapter(Context context, MakeSureOrder makeSureOrder) {
        this.context = context;
        this.makeSureOrder = makeSureOrder;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return makeSureOrder;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 holder1;
        ViewHolder2 holder2;
        switch (getItemViewType(position)){
            case 0:
                if(convertView == null){
                    convertView = View.inflate(context,R.layout.item_makesureorder_receive,null);
                    holder1 = new ViewHolder1(convertView);
                    convertView.setTag(holder1);
                }else{
                    holder1 = (ViewHolder1) convertView.getTag();
                }
                holder1.nameNumberTv.setText(makeSureOrder.getName()+" "+makeSureOrder.getNumber());
                holder1.addressTv.setText(makeSureOrder.getAddress());
                break;
            case 1:
                if(convertView == null){
                    convertView = View.inflate(context,R.layout.item_makesureorder_pay,null);
                    holder2 = new ViewHolder2(convertView);
                    convertView.setTag(holder2);
                }else{
                    holder2 = (ViewHolder2) convertView.getTag();
                }
                if(position == 1){
                    holder2.titleTv.setText("支付方式：");
                    holder2.contentTv.setText(makeSureOrder.getPayway());
                }
                if(position == 2){
                    holder2.titleTv.setText("发票信息：");
                    holder2.contentTv.setText(makeSureOrder.getBillinfo());
                }
                break;
            default:
                break;
        }
        return convertView;
    }

    class ViewHolder1{

        private TextView nameNumberTv,addressTv;

        public ViewHolder1(View itemView) {
            nameNumberTv = (TextView) itemView.findViewById(R.id.tv_item_makesureorder_receive_namenumber);
            addressTv = (TextView) itemView.findViewById(R.id.tv_item_makesureorder_receive_address);
        }
    }

    class ViewHolder2{

        private TextView titleTv,contentTv;

        public ViewHolder2(View itemView) {
            titleTv = (TextView) itemView.findViewById(R.id.tv_item_makesureorder_pay_title);
            contentTv = (TextView) itemView.findViewById(R.id.tv_item_makesureorder_pay_content);
        }
    }
}
