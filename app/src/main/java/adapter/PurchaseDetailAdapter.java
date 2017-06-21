package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.ArrayList;
import java.util.List;

import bean.PurchaseDetail;
import bean.PurchaseDetailPhotoDescription;
import utils.HeightUtils;

/**
 * Created by Administrator on 2017/6/21.
 */

public class PurchaseDetailAdapter extends BaseAdapter {

    private Context context;
    private List<PurchaseDetail> list;
    private StateViewHolder stateViewHolder;
    private InfoViewHolder infoViewHolder;
    private GoodsViewHolder goodsViewHolder;
    private OffersetViewHolder offersetViewHolder;
    private BusinessViewHolder businessViewHolder;
    private BidruleViewHolder bidruleViewHolder;
    private ContactViewHolder contactViewHolder;

    public PurchaseDetailAdapter(Context context, List<PurchaseDetail> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getViewTypeCount() {
        return 7;
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        switch (position) {
            case 0:
                type = 0;
                break;
            case 1:
                type = 1;
                break;
            case 2:
                type = 2;
                break;
            case 3:
                type = 3;
                break;
            case 4:
                type = 4;
                break;
            case 5:
                type = 5;
                break;
            case 6:
                type = 6;
                break;
            default:
                break;
        }
        return type;
    }

    @Override
    public int getCount() {
        if (list == null || list.size() == 0)
            return 0;
        return 7;
    }

    @Override
    public Object getItem(int position) {
        return list.get(0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case 0:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_purchasedetail_purchasestate, null);
                    stateViewHolder = new StateViewHolder(convertView);
                    convertView.setTag(stateViewHolder);
                } else {
                    stateViewHolder = (StateViewHolder) convertView.getTag();
                }
                stateViewHolder.stateTv.setText(list.get(0).getPurchaseState());
                break;
            case 1:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_purchasedetail_purchaseinfo, null);
                    infoViewHolder = new InfoViewHolder(convertView);
                    convertView.setTag(infoViewHolder);
                } else {
                    infoViewHolder = (InfoViewHolder) convertView.getTag();
                }
                infoViewHolder.numberTv.setText(list.get(0).getPurchaseNumber());
                infoViewHolder.nameTv.setText(list.get(0).getPurchaseName());
                infoViewHolder.createTimeTv.setText(list.get(0).getCreateTime());
                break;
            case 2:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_purchasedetail_purchasegoods, null);
                    goodsViewHolder = new GoodsViewHolder(convertView);
                    convertView.setTag(goodsViewHolder);
                } else {
                    goodsViewHolder = (GoodsViewHolder) convertView.getTag();
                }
                goodsViewHolder.goodsNameTv.setText(list.get(0).getGoodsName());
                goodsViewHolder.goodsKindTv.setText(list.get(0).getGoodsKind());
                goodsViewHolder.purchaseAmoutTv.setText(list.get(0).getPurchaseAmount());
                goodsViewHolder.goodsDescriptionTv.setText(list.get(0).getGoodsDescription());
                List<PurchaseDetailPhotoDescription> li = new ArrayList<>();
                li.addAll(list.get(0).getPhotoDescriptionList());
                PurchaseDetailPhotoDescriptionAdapter adapter = new PurchaseDetailPhotoDescriptionAdapter(context, li);
                goodsViewHolder.photoDescriptionLv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                HeightUtils.setListViewHeight(goodsViewHolder.photoDescriptionLv);
                break;
            case 3:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_purchasedetail_offerset, null);
                    offersetViewHolder = new OffersetViewHolder(convertView);
                    convertView.setTag(offersetViewHolder);
                } else {
                    offersetViewHolder = (OffersetViewHolder) convertView.getTag();
                }
                offersetViewHolder.billRequireTv.setText(list.get(0).getBillRequire());
                offersetViewHolder.transportRequireTv.setText(list.get(0).getTransportRequire());
                break;
            case 4:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_purchasedetail_businessclause, null);
                    businessViewHolder = new BusinessViewHolder(convertView);
                    convertView.setTag(businessViewHolder);
                } else {
                    businessViewHolder = (BusinessViewHolder) convertView.getTag();
                }
                businessViewHolder.receiveAreaTv.setText(list.get(0).getReceiveArea());
                businessViewHolder.detailAreaTv.setText(list.get(0).getDetailArea());
                businessViewHolder.deliverTimeTv.setText(list.get(0).getDeliverTime());
                businessViewHolder.descriptionTv.setText(list.get(0).getDescription());
                break;
            case 5:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_purchasedetail_bidrule, null);
                    bidruleViewHolder = new BidruleViewHolder(convertView);
                    convertView.setTag(bidruleViewHolder);
                } else {
                    bidruleViewHolder = (BidruleViewHolder) convertView.getTag();
                }
                bidruleViewHolder.bidendTimeTv.setText(list.get(0).getBidendTime());
                bidruleViewHolder.resultTimeTv.setText(list.get(0).getResultTime());
                bidruleViewHolder.maxPriceTv.setText(list.get(0).getMaxPrice());
                break;
            case 6:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_purchasedetail_contactway, null);
                    contactViewHolder = new ContactViewHolder(convertView);
                    convertView.setTag(contactViewHolder);
                } else {
                    contactViewHolder = (ContactViewHolder) convertView.getTag();
                }
                contactViewHolder.memberNameTv.setText(list.get(0).getMemberName());
                contactViewHolder.memberPhoneTv.setText(list.get(0).getMemberPhone());
                contactViewHolder.memberMobileTv.setText(list.get(0).getMemberMobile());
                break;
            default:
                break;
        }
        return convertView;
    }

    class StateViewHolder {

        private TextView stateTv;

        public StateViewHolder(View itemView) {
            stateTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_purchasestate_state);
        }
    }

    class InfoViewHolder {

        private TextView numberTv, nameTv, createTimeTv;

        public InfoViewHolder(View itemView) {
            numberTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_purchaseinfo_number);
            nameTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_purchaseinfo_name);
            createTimeTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_purchaseinfo_createTime);
        }
    }

    class GoodsViewHolder {

        private TextView goodsNameTv, goodsKindTv, purchaseAmoutTv, goodsDescriptionTv;
        private ListView photoDescriptionLv;

        public GoodsViewHolder(View itemView) {
            goodsNameTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_purchasegoods_goodsname);
            goodsKindTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_purchasegoods_goodskind);
            purchaseAmoutTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_purchasegoods_purchaseAmount);
            goodsDescriptionTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_purchasegoods_goodsdescription);
            photoDescriptionLv = (ListView) itemView.findViewById(R.id.lv_item_purchasedetail_photodescription);
        }
    }

    class OffersetViewHolder {

        private TextView billRequireTv, transportRequireTv;

        public OffersetViewHolder(View itemView) {
            billRequireTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_offerset_billrequire);
            transportRequireTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_offerset_transportrequire);
        }
    }

    class BusinessViewHolder {

        private TextView receiveAreaTv, detailAreaTv, deliverTimeTv, descriptionTv;

        public BusinessViewHolder(View itemView) {
            receiveAreaTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_businessclause_receivearea);
            detailAreaTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_businessclause_detailarea);
            deliverTimeTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_businessclause_delivertime);
            descriptionTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_businessclause_description);
        }
    }

    class BidruleViewHolder {

        private TextView bidendTimeTv, resultTimeTv, maxPriceTv;

        public BidruleViewHolder(View itemView) {
            bidendTimeTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_bitrule_bidendtime);
            resultTimeTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_bitrule_resulttime);
            maxPriceTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_bitrule_maxprice);
        }
    }

    class ContactViewHolder {

        private TextView memberNameTv, memberPhoneTv, memberMobileTv;

        public ContactViewHolder(View itemView) {
            memberNameTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_contactway_membername);
            memberPhoneTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_contactway_memberphone);
            memberMobileTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_contactway_membermobile);
        }
    }
}
