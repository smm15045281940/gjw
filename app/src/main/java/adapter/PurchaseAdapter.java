package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.Purchase;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class PurchaseAdapter extends BaseAdapter {

    private Context context;
    private List<Purchase> list;
    private ListItemClickHelp callback;


    public PurchaseAdapter(Context context, List<Purchase> list, ListItemClickHelp callback) {
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
        PurchaseViewHolder purchaseViewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_purchase, null);
            purchaseViewHolder = new PurchaseViewHolder(convertView);
            convertView.setTag(purchaseViewHolder);
        } else {
            purchaseViewHolder = (PurchaseViewHolder) convertView.getTag();
        }
        Purchase purchase = list.get(position);
        purchaseViewHolder.mPurchaseNumberTv.setText(purchase.getPurchaseNumber());
        purchaseViewHolder.mGoodsNameTv.setText(purchase.getGoodsName());
        purchaseViewHolder.mMaxPriceTv.setText("¥" + purchase.getMaxPrice());
        purchaseViewHolder.mPurchaseAmountTv.setText(purchase.getPurchaseAmount());
        purchaseViewHolder.mPurchaseNameTv.setText(purchase.getPurchaseName());
        purchaseViewHolder.mBillTypeTv.setText(purchase.getBillType());
        purchaseViewHolder.mTransportTypeTv.setText(purchase.getTransportType());
        purchaseViewHolder.mStateDescTv.setText(purchase.getStateDesc());
        if (purchase.getStateDesc().equals("已取消")) {
            purchaseViewHolder.lookCancelLl.setVisibility(View.INVISIBLE);
        } else {
            purchaseViewHolder.lookCancelLl.setVisibility(View.VISIBLE);
        }
        final View view = convertView;
        final int p = position;
        final int priceId = purchaseViewHolder.priceRl.getId();
        final int cancelId = purchaseViewHolder.cancelRl.getId();
        purchaseViewHolder.priceRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, priceId, false);
            }
        });
        purchaseViewHolder.cancelRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, cancelId, false);
            }
        });
        return convertView;
    }

    class PurchaseViewHolder {

        private TextView mPurchaseNumberTv, mGoodsNameTv, mMaxPriceTv, mPurchaseAmountTv, mPurchaseNameTv, mBillTypeTv, mTransportTypeTv, mStateDescTv;
        private RelativeLayout priceRl, cancelRl;
        private LinearLayout lookCancelLl;

        public PurchaseViewHolder(View itemView) {
            mPurchaseNumberTv = (TextView) itemView.findViewById(R.id.tv_purchase_purchaseNumber);
            mGoodsNameTv = (TextView) itemView.findViewById(R.id.tv_purchase_goodsName);
            mMaxPriceTv = (TextView) itemView.findViewById(R.id.tv_purchase_maxPrice);
            mPurchaseAmountTv = (TextView) itemView.findViewById(R.id.tv_purchase_purchaseAmount);
            mPurchaseNameTv = (TextView) itemView.findViewById(R.id.tv_purchase_purchaseName);
            mBillTypeTv = (TextView) itemView.findViewById(R.id.tv_purchase_billType);
            mTransportTypeTv = (TextView) itemView.findViewById(R.id.tv_purchase_transportType);
            mStateDescTv = (TextView) itemView.findViewById(R.id.tv_purchase_state_desc);
            priceRl = (RelativeLayout) itemView.findViewById(R.id.rl_purchase_price);
            cancelRl = (RelativeLayout) itemView.findViewById(R.id.rl_purchase_cancel);
            lookCancelLl = (LinearLayout) itemView.findViewById(R.id.ll_item_purchase_lookcancel);
        }
    }
}
