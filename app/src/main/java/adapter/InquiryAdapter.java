package adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.Quotation;
import utils.HeightUtils;

/**
 * Created by Administrator on 2017/7/24.
 */

public class InquiryAdapter extends BaseAdapter {

    private Context context;
    private List<Quotation> list;
    private ListItemClickHelp callback;
    private ViewHolder holder;

    public InquiryAdapter(Context context, List<Quotation> list, ListItemClickHelp callback) {
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_inquiry, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Quotation quotation = list.get(position);
        if (quotation != null) {
            final View view = convertView;
            final int p = position;
            if (!TextUtils.isEmpty(quotation.getStoreName())) {
                holder.storeNameTv.setText(quotation.getStoreName());
            }
            if (!TextUtils.isEmpty(quotation.getQuotationMod())) {
                holder.quotationModTv.setText(quotation.getQuotationMod());
            }
            if (!TextUtils.isEmpty(quotation.getQuotationUsed())) {
                holder.quotationUsedTv.setText(quotation.getQuotationUsed());
                if (quotation.getQuotationUsed().equals("已生成订单")) {
                    holder.genBackRl.setVisibility(View.GONE);
                } else {
                    holder.genBackRl.setVisibility(View.VISIBLE);
                }
            }
            if (!TextUtils.isEmpty(quotation.getQuotationSn())) {
                holder.quotationSnTv.setText(quotation.getQuotationSn());
            }
            if (!TextUtils.isEmpty(quotation.getAddTime())) {
                holder.addTimeTv.setText(quotation.getAddTime());
            }
            if (!TextUtils.isEmpty(quotation.getOrderAmount())) {
                holder.orderAmountTv.setText(quotation.getOrderAmount());
                holder.orderAmountGenerateTv.setText(quotation.getOrderAmount());
            }
            if (quotation.getGoodsList() != null && quotation.getGoodsList().size() != 0) {
                holder.inquiryGoodsLv.setAdapter(new InquiryGoodsAdapter(context, quotation.getGoodsList()));
                HeightUtils.setListViewHeight(holder.inquiryGoodsLv);
            }
            final int delId = holder.delRl.getId();
            holder.delRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(view, parent, p, delId, false);
                }
            });
            final int genId = holder.genRl.getId();
            holder.genRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(view, parent, p, genId, false);
                }
            });
        }
        return convertView;
    }

    private class ViewHolder {

        private TextView storeNameTv, quotationModTv, quotationUsedTv, quotationSnTv, addTimeTv, orderAmountTv, orderAmountGenerateTv;
        private ListView inquiryGoodsLv;
        private RelativeLayout delRl, genRl, genBackRl;

        public ViewHolder(View itemView) {
            storeNameTv = (TextView) itemView.findViewById(R.id.tv_item_inquiry_store_name);
            quotationModTv = (TextView) itemView.findViewById(R.id.tv_item_inquiry_quotation_mod);
            quotationUsedTv = (TextView) itemView.findViewById(R.id.tv_item_inquiry_quotation_used);
            quotationSnTv = (TextView) itemView.findViewById(R.id.tv_item_inquiry_quotation_sn);
            addTimeTv = (TextView) itemView.findViewById(R.id.tv_item_inquiry_add_time);
            orderAmountTv = (TextView) itemView.findViewById(R.id.tv_item_inquiry_order_amount);
            orderAmountGenerateTv = (TextView) itemView.findViewById(R.id.tv_item_inquiry_order_amount_generate);
            inquiryGoodsLv = (ListView) itemView.findViewById(R.id.lv_item_inquiry_quotation_list);
            delRl = (RelativeLayout) itemView.findViewById(R.id.rl_item_inquiry_del);
            genRl = (RelativeLayout) itemView.findViewById(R.id.rl_item_inquiry_gen);
            genBackRl = (RelativeLayout) itemView.findViewById(R.id.rl_item_inquiry_genback);
        }
    }
}
