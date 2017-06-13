package fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.ArrayList;
import java.util.List;

import adapter.GoodsDetailDetailAdapter;
import bean.GoodsDetailDetail;
import customview.LazyFragment;
import customview.MyRefreshListView;
import customview.OnRefreshListener;

/**
 * Created by Administrator on 2017/5/23.
 */

public class GoodsDetailDetailFragment extends LazyFragment implements OnRefreshListener{

    private View rootView;
    private MyRefreshListView mlv;
    private TextView loadTv;
    private List<GoodsDetailDetail> mDataList;
    private GoodsDetailDetailAdapter mAdapter;

    Handler firstloadHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg != null){
                if(msg.what == 1){
                    mAdapter.notifyDataSetChanged();
                    loadTv.setVisibility(View.INVISIBLE);
                    mlv.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goodsdetail_detail,null);
        initView(rootView);
        initData();
        setData();
        setListener();
        return rootView;
    }

    private void initView(View rootView){
        mlv = (MyRefreshListView) rootView.findViewById(R.id.mlv_goodsdetaildetail);
        loadTv = (TextView) rootView.findViewById(R.id.tv_goodsdetaildetail);
    }

    private void initData(){
        mDataList = new ArrayList<>();
    }

    private void setData(){
        mAdapter = new GoodsDetailDetailAdapter(getActivity(),mDataList);
        mlv.setAdapter(mAdapter);
    }

    private void setListener(){
        mlv.setOnRefreshListener(this);
    }

    @Override
    protected void onFragmentFirstVisible() {
        mlv.setVisibility(View.INVISIBLE);
        loadTv.setVisibility(View.VISIBLE);
        loadData();
        firstloadHandler.sendEmptyMessageDelayed(1,1000);
    }

    private void loadData(){
        for (int i = 0; i < 10; i++) {
            mDataList.add(new GoodsDetailDetail(""));
        }
    }


    @Override
    public void onDownPullRefresh() {
        mlv.hideHeadView();
    }

    @Override
    public void onLoadingMore() {
        mlv.hideFootView();
    }
}
