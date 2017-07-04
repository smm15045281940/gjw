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

import bean.MyFoot;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class MyfootAdapter extends BaseAdapter {

    private Context context;
    private List<MyFoot> list;

    public MyfootAdapter(Context context, List<MyFoot> list) {
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
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_myfoot, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        MyFoot myfoot = list.get(position);
        vh.goodsNameTv.setText(myfoot.getGoodsName());
        vh.goodsPriceTv.setText("¥" + myfoot.getGoodsPrice());
        Picasso.with(context).load(myfoot.getGoodsImgUrl()).placeholder(vh.goodsImgIv.getDrawable()).into(vh.goodsImgIv);
        return convertView;
    }

    class ViewHolder {

        private TextView goodsNameTv, goodsPriceTv;
        private ImageView goodsImgIv;

        public ViewHolder(View itemView) {
            goodsNameTv = (TextView) itemView.findViewById(R.id.tv_item_myfoot_goodsName);
            goodsPriceTv = (TextView) itemView.findViewById(R.id.tv_item_myfoot_goodsPrice);
            goodsImgIv = (ImageView) itemView.findViewById(R.id.iv_item_myfoot_goodsImg);
        }
    }
}
