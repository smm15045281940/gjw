package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import bean.StoreCollect;

/**
 * Created by Administrator on 2017/7/4.
 */

public class StoreCollectListAdapter extends BaseAdapter {

    private Context context;
    private List<StoreCollect> list;
    private ListItemClickHelp callback;
    private ViewHolder holder;

    public StoreCollectListAdapter(Context context, List<StoreCollect> list, ListItemClickHelp callback) {
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
            convertView = View.inflate(context, R.layout.item_store_collect, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StoreCollect storeCollect = list.get(position);
        holder.nameTv.setText(storeCollect.getStoreName());
        holder.storeCollectTv.setText(storeCollect.getStoreCollect());
        holder.goodsCountTv.setText(storeCollect.getGoodsCount());
        Picasso.with(context).load(storeCollect.getStoreAvatarUrl()).placeholder(holder.imgIv.getDrawable()).into(holder.imgIv);
        final View view = convertView;
        final int p = position;
        final int id = holder.delIv.getId();
        holder.delIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, id, false);
            }
        });
        return convertView;
    }

    class ViewHolder {

        private ImageView imgIv, delIv;
        private TextView nameTv, storeCollectTv, goodsCountTv;

        public ViewHolder(View itemView) {
            imgIv = (ImageView) itemView.findViewById(R.id.iv_item_store_collect_img);
            delIv = (ImageView) itemView.findViewById(R.id.iv_item_store_collect_delete);
            nameTv = (TextView) itemView.findViewById(R.id.tv_item_store_collect_storeName);
            storeCollectTv = (TextView) itemView.findViewById(R.id.tv_item_store_collect_storeCollect);
            goodsCountTv = (TextView) itemView.findViewById(R.id.tv_item_store_collect_goodsCount);
        }
    }
}
