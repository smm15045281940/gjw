package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.GoodsDetailActivity;
import com.gangjianwang.www.gangjianwang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import bean.GoodsPost;
import bean.GoodsRecommend;
import bean.GoodsShow;
import utils.ToastUtils;

/**
 * Created by 孙明明 on 2017/5/15.
 */

public class FirstpageAdapter extends BaseAdapter {

    private Context context;
    private List<GoodsRecommend> goodsRecommendList;
    private List<GoodsPost> goodsPostList;
    private List<GoodsShow> goodsShowList;

    private final int RECOMMEND_STATE = 1;
    private final int ASKBUY_STATE = 2;
    private final int PUPOLAR_STATE = 3;
    private int STATE;

    public FirstpageAdapter(Context context, List<GoodsRecommend> goodsRecommendList, List<GoodsPost> goodsPostList, List<GoodsShow> goodsShowList) {
        this.context = context;
        this.goodsRecommendList = goodsRecommendList;
        this.goodsPostList = goodsPostList;
        this.goodsShowList = goodsShowList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else if (position == 2) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getCount() {
        if (goodsRecommendList.size() == 0 || goodsPostList.size() == 0 || goodsShowList.size() == 0)
            return 0;
        return 2 + goodsShowList.size();
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return goodsRecommendList.get(0);
        } else if (position == 2) {
            return goodsPostList.get(0);
        } else {
            if (position == 1) {
                return goodsShowList.get(0);
            } else {
                return goodsShowList.get(position - 2);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder1 holder1;
        ViewHolder2 holder2;
        final ViewHolder3 holder3;
        if (getItemViewType(position) == 0) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_firstpage1, null);
                holder1 = new ViewHolder1(convertView);
                convertView.setTag(holder1);
            } else {
                holder1 = (ViewHolder1) convertView.getTag();
            }
            GoodsRecommend goodsRecommend = goodsRecommendList.get(0);
            holder1.tvTitle.setText(goodsRecommend.getTitle());
            holder1.tv1.setText(goodsRecommend.getGoodsName1());
            holder1.tv2.setText(goodsRecommend.getGoodsName2());
            holder1.tv3.setText("¥" + goodsRecommend.getGoodsPrice1());
            holder1.tv4.setText("¥" + goodsRecommend.getGoodsPrice2());
            Picasso.with(context).load(goodsRecommend.getImgUrl1()).placeholder(holder1.iv1.getDrawable()).error(R.mipmap.img_default).into(holder1.iv1);
            Picasso.with(context).load(goodsRecommend.getImgUrl2()).placeholder(holder1.iv2.getDrawable()).error(R.mipmap.img_default).into(holder1.iv2);
            holder1.iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.toast(context, "你点击了商品推荐0");
                    context.startActivity(new Intent(context, GoodsDetailActivity.class));
                }
            });
            holder1.iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.toast(context, "你点击了商品推荐1");
                }
            });
        } else if (getItemViewType(position) == 1) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_firstpage2, null);
                holder2 = new ViewHolder2(convertView);
                convertView.setTag(holder2);
            } else {
                holder2 = (ViewHolder2) convertView.getTag();
            }
            GoodsPost goodsPost = goodsPostList.get(0);
            Picasso.with(context).load(goodsPost.getImgUrl()).placeholder(holder2.iv.getDrawable()).error(R.mipmap.img_default).into(holder2.iv);
            holder2.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.toast(context, "你点击了广告");
                }
            });
        } else if (getItemViewType(position) == 2) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_firstpage3, null);
                holder3 = new ViewHolder3(convertView);
                convertView.setTag(holder3);
            } else {
                holder3 = (ViewHolder3) convertView.getTag();
            }
            if (position == 1) {
                GoodsShow goodsShow = goodsShowList.get(0);
                holder3.tv.setText(goodsShow.getTitle());
                Picasso.with(context).load(goodsShow.getImgUrl1()).placeholder(holder3.iv1.getDrawable()).error(R.mipmap.img_default).into(holder3.iv1);
                Picasso.with(context).load(goodsShow.getImgUrl2()).placeholder(holder3.iv2.getDrawable()).error(R.mipmap.img_default).into(holder3.iv2);
                Picasso.with(context).load(goodsShow.getImgUrl3()).placeholder(holder3.iv3.getDrawable()).error(R.mipmap.img_default).into(holder3.iv3);
                Picasso.with(context).load(goodsShow.getImgUrl4()).placeholder(holder3.iv4.getDrawable()).error(R.mipmap.img_default).into(holder3.iv4);
                Picasso.with(context).load(goodsShow.getImgUrl5()).placeholder(holder3.iv5.getDrawable()).error(R.mipmap.img_default).into(holder3.iv5);
                Picasso.with(context).load(goodsShow.getImgUrl6()).placeholder(holder3.iv6.getDrawable()).error(R.mipmap.img_default).into(holder3.iv6);
            } else {
                GoodsShow goodsShow = goodsShowList.get(position - 2);
                holder3.tv.setText(goodsShow.getTitle());
                Picasso.with(context).load(goodsShow.getImgUrl1()).placeholder(holder3.iv1.getDrawable()).error(R.mipmap.img_default).into(holder3.iv1);
                Picasso.with(context).load(goodsShow.getImgUrl2()).placeholder(holder3.iv2.getDrawable()).error(R.mipmap.img_default).into(holder3.iv2);
                Picasso.with(context).load(goodsShow.getImgUrl3()).placeholder(holder3.iv3.getDrawable()).error(R.mipmap.img_default).into(holder3.iv3);
                Picasso.with(context).load(goodsShow.getImgUrl4()).placeholder(holder3.iv4.getDrawable()).error(R.mipmap.img_default).into(holder3.iv4);
                Picasso.with(context).load(goodsShow.getImgUrl5()).placeholder(holder3.iv5.getDrawable()).error(R.mipmap.img_default).into(holder3.iv5);
                Picasso.with(context).load(goodsShow.getImgUrl6()).placeholder(holder3.iv6.getDrawable()).error(R.mipmap.img_default).into(holder3.iv6);
            }
            holder3.iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.toast(context, goodsShowList.get(2).getData1());
                }
            });
            holder3.iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.toast(context, goodsShowList.get(2).getData2());
                }
            });
            holder3.iv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.toast(context, goodsShowList.get(2).getData3());
                }
            });
            holder3.iv4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.toast(context, goodsShowList.get(2).getData4());
                }
            });
            holder3.iv5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.toast(context, goodsShowList.get(2).getData5());
                }
            });
            holder3.iv6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.toast(context, goodsShowList.get(2).getData6());
                }
            });
            holder3.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.toast(context, holder3.tv.getText().toString() + "查看更多");
                }
            });
        }
        return convertView;
    }

    class ViewHolder1 {

        private ImageView iv1, iv2;
        private TextView tvTitle, tv1, tv2, tv3, tv4;

        public ViewHolder1(View itemView) {
            tvTitle = (TextView) itemView.findViewById(R.id.tv_item_firstpage1_title);
            iv1 = (ImageView) itemView.findViewById(R.id.iv_item_firstpage1_imgurl1);
            iv2 = (ImageView) itemView.findViewById(R.id.iv_item_firstpage1_imgurl2);
            tv1 = (TextView) itemView.findViewById(R.id.tv_item_firstpage1_goodsName1);
            tv2 = (TextView) itemView.findViewById(R.id.tv_item_firstpage1_goodsName2);
            tv3 = (TextView) itemView.findViewById(R.id.tv_item_firstpage1_goodsPrice1);
            tv4 = (TextView) itemView.findViewById(R.id.tv_item_firstpage1_goodsPrice2);
        }
    }

    class ViewHolder2 {

        private ImageView iv;

        public ViewHolder2(View itemView) {
            iv = (ImageView) itemView.findViewById(R.id.iv_item_firstpage2_imgurl);
        }
    }

    class ViewHolder3 {

        private TextView tv, tvMore;
        private ImageView iv1, iv2, iv3, iv4, iv5, iv6;

        public ViewHolder3(View itemView) {
            tv = (TextView) itemView.findViewById(R.id.tv_item_firstpage3_title);
            tvMore = (TextView) itemView.findViewById(R.id.tv_item_firstpage3_more);
            iv1 = (ImageView) itemView.findViewById(R.id.iv_item_firstpage3_imgurl1);
            iv2 = (ImageView) itemView.findViewById(R.id.iv_item_firstpage3_imgurl2);
            iv3 = (ImageView) itemView.findViewById(R.id.iv_item_firstpage3_imgurl3);
            iv4 = (ImageView) itemView.findViewById(R.id.iv_item_firstpage3_imgurl4);
            iv5 = (ImageView) itemView.findViewById(R.id.iv_item_firstpage3_imgurl5);
            iv6 = (ImageView) itemView.findViewById(R.id.iv_item_firstpage3_imgurl6);
        }
    }

}
