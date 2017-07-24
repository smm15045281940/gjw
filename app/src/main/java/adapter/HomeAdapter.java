package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import bean.HomeBlockOuter;
import bean.HomeGoodsOuter;
import bean.HomePost;
import utils.HeightUtils;

/**
 * Created by Administrator on 2017/7/12.
 */

public class HomeAdapter extends BaseAdapter {

    private Context context;
    private List<HomeGoodsOuter> homeGoodsOuterList;
    private List<HomeBlockOuter> homeBlockOuterList;
    private List<HomePost> homePostList;
    private ViewHolder1 holder1;
    private ViewHolder2 holder2;
    private ViewHolder3 holder3;

    public HomeAdapter(Context context, List<HomeGoodsOuter> homeGoodsOuterList, List<HomeBlockOuter> homeBlockOuterList, List<HomePost> homePostList) {
        this.context = context;
        this.homeGoodsOuterList = homeGoodsOuterList;
        this.homeBlockOuterList = homeBlockOuterList;
        this.homePostList = homePostList;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (position == 0) {
            type = 0;
        } else if (position == 2) {
            type = 2;
        } else {
            type = 1;
        }
        return type;
    }

    @Override
    public int getCount() {
        int count;
        if (homeGoodsOuterList.size() == 0 || homeBlockOuterList.size() == 0 || homePostList.size() == 0) {
            count = 0;
        } else {
            count = homeGoodsOuterList.size() + homeBlockOuterList.size() + homePostList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return homeGoodsOuterList.get(0);
        } else if (position == 1) {
            return homeBlockOuterList.get(0);
        } else if (position == 2) {
            return homePostList.get(0);
        } else {
            return homeBlockOuterList.get(position - 2);
        }
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
                    convertView = View.inflate(context, R.layout.item_home_1, null);
                    holder1 = new ViewHolder1(convertView);
                    convertView.setTag(holder1);
                } else {
                    holder1 = (ViewHolder1) convertView.getTag();
                }
                HomeGoodsOuter homeGoodsOuter = homeGoodsOuterList.get(0);
                if (homeGoodsOuter != null) {
                    holder1.titleTv.setText(homeGoodsOuter.getTitle());
                    holder1.gv.setAdapter(new HomeGoodsInnerAdapter(context, homeGoodsOuter.getHomeGoodsInnerList()));
                    HeightUtils.setGridViewHeight(holder1.gv,3);
                }
                break;
            case 1:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_home_2, null);
                    holder2 = new ViewHolder2(convertView);
                    convertView.setTag(holder2);
                } else {
                    holder2 = (ViewHolder2) convertView.getTag();
                }
                HomeBlockOuter homeBlockOuter;
                if (position == 1) {
                    homeBlockOuter = homeBlockOuterList.get(0);
                } else {
                    homeBlockOuter = homeBlockOuterList.get(position - 2);
                }
                if (homeBlockOuter != null) {
                    holder2.titleTv.setText(homeBlockOuter.getTitle());
                    holder2.gv.setAdapter(new HomeBlockInnerAdapter(context, homeBlockOuter.getHomeBlockInnerList()));
                    HeightUtils.setGridViewHeight(holder2.gv,3);
                }
                break;
            case 2:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_home_3, null);
                    holder3 = new ViewHolder3(convertView);
                    convertView.setTag(holder3);
                } else {
                    holder3 = (ViewHolder3) convertView.getTag();
                }
                HomePost homePost = homePostList.get(0);
                if (homePost != null) {
                    Picasso.with(context).load(homePost.getImage()).placeholder(holder3.postIv.getDrawable()).into(holder3.postIv);
                }
                break;
            default:
                break;
        }
        return convertView;
    }

    class ViewHolder1 {

        private TextView titleTv;
        private GridView gv;

        public ViewHolder1(View itemView) {
            titleTv = (TextView) itemView.findViewById(R.id.tv_item_home_1_title);
            gv = (GridView) itemView.findViewById(R.id.gv_item_home_1);
        }
    }

    class ViewHolder2 {

        private TextView titleTv;
        private GridView gv;

        public ViewHolder2(View itemView) {
            titleTv = (TextView) itemView.findViewById(R.id.tv_item_home_2_title);
            gv = (GridView) itemView.findViewById(R.id.gv_item_home_2);
        }
    }

    class ViewHolder3 {

        private ImageView postIv;

        public ViewHolder3(View itemView) {
            postIv = (ImageView) itemView.findViewById(R.id.iv_item_home_3_post);
        }
    }
}
