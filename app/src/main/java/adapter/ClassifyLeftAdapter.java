package adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import bean.ClassifyLeft;

/**
 * Created by Administrator on 2017/6/20.
 */

public class ClassifyLeftAdapter extends BaseAdapter {

    private Context context;
    private List<ClassifyLeft> list;
    private ViewHolder holder;

    public ClassifyLeftAdapter(Context context, List<ClassifyLeft> list) {
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
            convertView = View.inflate(context, R.layout.item_classify_left, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ClassifyLeft classifyLeft = list.get(position);
        String imgUrl = classifyLeft.getImgUrl();
        if (!TextUtils.isEmpty(imgUrl)) {
            Picasso.with(context).load(classifyLeft.getImgUrl()).placeholder(holder.iconIv.getDrawable()).into(holder.iconIv);
        } else {
            holder.iconIv.setImageResource(R.mipmap.img_default);
        }
        holder.titleTv.setText(classifyLeft.getTitle());
        if (classifyLeft.isCheck()) {
            holder.ll.setBackgroundColor(Color.GRAY);
        } else {
            holder.ll.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }

    class ViewHolder {

        private LinearLayout ll;
        private ImageView iconIv;
        private TextView titleTv;

        public ViewHolder(View itemView) {
            ll = (LinearLayout) itemView.findViewById(R.id.ll_item_classify_left);
            iconIv = (ImageView) itemView.findViewById(R.id.iv_item_classify_left_icon);
            titleTv = (TextView) itemView.findViewById(R.id.tv_item_classify_left_title);
        }
    }
}
