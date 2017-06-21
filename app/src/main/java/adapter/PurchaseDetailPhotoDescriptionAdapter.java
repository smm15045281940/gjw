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

import bean.PurchaseDetailPhotoDescription;

/**
 * Created by Administrator on 2017/6/21.
 */

public class PurchaseDetailPhotoDescriptionAdapter extends BaseAdapter {

    private Context context;
    private List<PurchaseDetailPhotoDescription> list;
    private ViewHolder holder;

    public PurchaseDetailPhotoDescriptionAdapter(Context context, List<PurchaseDetailPhotoDescription> list) {
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
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_purchasedetail_photodescription, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PurchaseDetailPhotoDescription purchaseDetailPhotoDescription = list.get(position);
        Picasso.with(context).load(purchaseDetailPhotoDescription.getImgUrl()).placeholder(holder.iconIv.getDrawable()).into(holder.iconIv);
        holder.descriptionTv.setText("说明：" + purchaseDetailPhotoDescription.getDescription());
        return convertView;
    }

    class ViewHolder {

        private ImageView iconIv;
        private TextView descriptionTv;

        public ViewHolder(View itemView) {
            iconIv = (ImageView) itemView.findViewById(R.id.iv_item_purchasedetail_photodescription_icon);
            descriptionTv = (TextView) itemView.findViewById(R.id.tv_item_purchasedetail_photodescription_description);
        }
    }
}
