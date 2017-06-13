package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.Myfoot;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class MyfootAdapter extends BaseAdapter {

    private Context context;
    private List<Myfoot> list;

    public MyfootAdapter(Context context, List<Myfoot> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if(list == null || list.size() == 0){
            return 0;
        }else{
            return list.size();
        }
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
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        Myfoot myfoot = list.get(position);
        vh.goodsNameTv.setText(myfoot.goodsName);
        vh.goodsPriceTv.setText(myfoot.goodsPrice);
        vh.goodsImgIv.setImageResource(R.mipmap.ic_launcher);
        return convertView;
    }

    class ViewHolder {

        private TextView goodsNameTv,goodsPriceTv;
        private ImageView goodsImgIv;

        public ViewHolder(View itemView) {
            goodsNameTv = (TextView) itemView.findViewById(R.id.tv_item_myfoot_goodsName);
            goodsPriceTv = (TextView) itemView.findViewById(R.id.tv_item_myfoot_goodsPrice);
            goodsImgIv = (ImageView) itemView.findViewById(R.id.iv_item_myfoot_goodsImg);
        }
    }
}
