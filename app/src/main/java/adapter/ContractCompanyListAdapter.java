package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import bean.ContractCompany;

/**
 * Created by 孙明明 on 2017/5/15.
 */

public class ContractCompanyListAdapter extends BaseAdapter {

    private Context context;
    private List<ContractCompany> list;

    public ContractCompanyListAdapter(Context context, List<ContractCompany> list) {
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
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_contractcompany_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ContractCompany contractCompany = list.get(position);
        Picasso.with(context).load(contractCompany.getCompanyImgurl()).placeholder(holder.iv.getDrawable()).into(holder.iv);
        holder.tv1.setText(contractCompany.getCompanyName());
        holder.tv2.setText(contractCompany.getCompanyDuty());
        holder.tv3.setText(contractCompany.getCompanyAddress1() + " " + contractCompany.getCompanyAddress2());
        return convertView;
    }

    class ViewHolder {

        private ImageView iv;
        private TextView tv1, tv2, tv3;

        public ViewHolder(View itemView) {
            iv = (ImageView) itemView.findViewById(R.id.iv_item_contractcompany_list_imgurl);
            tv1 = (TextView) itemView.findViewById(R.id.tv_item_contractcompany_list_companyName);
            tv2 = (TextView) itemView.findViewById(R.id.tv_item_contractcompany_list_companyDuty);
            tv3 = (TextView) itemView.findViewById(R.id.tv_item_contractcompany_list_companyAddress);
        }
    }
}
