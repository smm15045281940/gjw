package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gangjianwang.www.gangjianwang.GjBuyActivity;
import com.gangjianwang.www.gangjianwang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import bean.HomeBlockInner;

/**
 * Created by Administrator on 2017/7/12.
 */

public class HomeBlockInnerAdapter extends BaseAdapter {

    private Context context;
    private List<HomeBlockInner> homeBlockInnerList;
    private ViewHolder holder;

    public HomeBlockInnerAdapter(Context context, List<HomeBlockInner> homeBlockInnerList) {
        this.context = context;
        this.homeBlockInnerList = homeBlockInnerList;
    }

    @Override
    public int getCount() {
        return homeBlockInnerList.size();
    }

    @Override
    public Object getItem(int position) {
        return homeBlockInnerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_home_grid_2, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final HomeBlockInner homeBlockInner = homeBlockInnerList.get(position);
        if (homeBlockInner != null) {
            Picasso.with(context).load(homeBlockInner.getImage()).placeholder(holder.imageIv.getDrawable()).into(holder.imageIv);
            holder.imageIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GjBuyActivity.class);
                    intent.putExtra("keyword", homeBlockInner.getData());
                    context.startActivity(intent);
                }
            });

        }
        return convertView;
    }

    class ViewHolder {

        private ImageView imageIv;

        public ViewHolder(View itemView) {
            imageIv = (ImageView) itemView.findViewById(R.id.iv_item_home_grid_2_image);
        }
    }
}
