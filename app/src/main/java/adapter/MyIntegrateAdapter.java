package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.MyIntegrate;
import bean.MyIntegrateSum;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public class MyIntegrateAdapter extends BaseAdapter{

    private Context context;
    private List<MyIntegrateSum> myIntegrateSumList;
    private List<MyIntegrate> myIntegrateList;

    public MyIntegrateAdapter(Context context, List<MyIntegrateSum> myIntegrateSumList, List<MyIntegrate> myIntegrateList) {
        this.context = context;
        this.myIntegrateSumList = myIntegrateSumList;
        this.myIntegrateList = myIntegrateList;
    }

    @Override
    public int getCount() {
        if(myIntegrateSumList == null || myIntegrateSumList.size() == 0 || myIntegrateList == null || myIntegrateList.size() == 0){
            return 0;
        }else{
            return myIntegrateSumList.size()+myIntegrateList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if(position == 0){
            return myIntegrateSumList.get(position);
        }else{
            return myIntegrateList.get(position - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return 1;
        }else{
            return 2;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 holder1;
        ViewHolder2 holder2;
        if(getItemViewType(position) == 1){
            if(convertView == null){
                convertView = View.inflate(context,R.layout.item_integrate_sum,null);
                holder1 = new ViewHolder1(convertView);
                convertView.setTag(holder1);
            }else{
                holder1 = (ViewHolder1) convertView.getTag();
            }
            MyIntegrateSum myIntegrateSum = myIntegrateSumList.get(position);
            holder1.mNameTv.setText(myIntegrateSum.integrateSumName);
            holder1.mScoreTv.setText(myIntegrateSum.integrateSumScore);
        }else if(getItemViewType(position) == 2){
            if(convertView == null){
                convertView = View.inflate(context,R.layout.item_integrate_detail,null);
                holder2 = new ViewHolder2(convertView);
                convertView.setTag(holder2);
            }else{
                holder2 = (ViewHolder2) convertView.getTag();
            }
            MyIntegrate myIntegrate = myIntegrateList.get(position - 1);
            holder2.mTitleTv.setText(myIntegrate.integrateTitle);
            holder2.mContentTv.setText(myIntegrate.integrateContent);
            holder2.mScoreTv.setText(myIntegrate.integrateScore);
            holder2.mTimeTv.setText(myIntegrate.integrateTime);
        }
        return convertView;
    }

    class ViewHolder1{

        private TextView mNameTv,mScoreTv;

        public ViewHolder1(View itemView){
            mNameTv = (TextView) itemView.findViewById(R.id.tv_item_integrate_sum_myscore);
            mScoreTv = (TextView) itemView.findViewById(R.id.tv_item_integrate_sum_score);
        }
    }

    class ViewHolder2{

        private TextView mTitleTv,mContentTv,mScoreTv,mTimeTv;

        public ViewHolder2(View itemView){
            mTitleTv = (TextView) itemView.findViewById(R.id.tv_item_integrate_detail_title);
            mContentTv = (TextView) itemView.findViewById(R.id.tv_item_integrate_detail_content);
            mScoreTv = (TextView) itemView.findViewById(R.id.tv_item_integrate_detail_score);
            mTimeTv = (TextView) itemView.findViewById(R.id.tv_item_integrate_detail_time);
        }
    }
}
