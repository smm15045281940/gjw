package fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.HomeActivity;
import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;

import java.util.ArrayList;
import java.util.List;

import adapter.OrderInnerAdapter;
import bean.OrderInner;
import customview.LazyFragment;
import customview.MyRefreshListView;
import customview.OnRefreshListener;
import utils.ToastUtils;

/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class RealorderWaitpayFragment extends LazyFragment implements OnRefreshListener, ListItemClickHelp {

    private View rootView, emptyView;
    private MyRefreshListView mLv;
    private TextView loadTv;
    private List<OrderInner> mDataList = new ArrayList<>();
    private RelativeLayout lookAroundRl;
    private OrderInnerAdapter mAdapter;
    private AlertDialog cancelAd;
    private int cancelIndex;

    Handler firstloadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == 1) {
                    loadTv.setVisibility(View.INVISIBLE);
                    mAdapter.notifyDataSetChanged();
                    mLv.setVisibility(View.VISIBLE);
                    mLv.setEmptyView(emptyView);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_series_order, null);
        rootView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        emptyView = inflater.inflate(R.layout.empty_order, null);
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        initView(rootView);
        setData();
        setListener();
        return rootView;
    }

    private void initView(View view) {
        mLv = (MyRefreshListView) view.findViewById(R.id.lv_series_order);
        loadTv = (TextView) view.findViewById(R.id.tv_order_loading);
        ((ViewGroup) mLv.getParent()).addView(emptyView);
        emptyView.setVisibility(View.GONE);
        lookAroundRl = (RelativeLayout) emptyView.findViewById(R.id.rl_empty_order_stroll);
        lookAroundRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("what", 0);
                startActivity(intent);
            }
        });
        AlertDialog.Builder cancelBuilder = new AlertDialog.Builder(getActivity());
        cancelBuilder.setMessage("是否删除订单？");
        cancelBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDataList.remove(cancelIndex);
                mAdapter.notifyDataSetChanged();
                cancelAd.dismiss();
                ToastUtils.toast(getActivity(), "取消订单成功");
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelAd.dismiss();
            }
        });
        cancelAd = cancelBuilder.create();
    }

    private void setData() {
        mAdapter = new OrderInnerAdapter(getActivity(), mDataList, this);
        mLv.setAdapter(mAdapter);
    }

    private void setListener() {
        mLv.setOnRefreshListener(this);
    }

    private void loadData() {
        for (int i = 0; i < 2; i++) {
            mDataList.add(new OrderInner("", "西林钢铁集团", "待付款", "", "工字钢", "¥1500.00", "x1", "1", "¥1500.00"));
        }
    }


    @Override
    protected void onFragmentFirstVisible() {
        mLv.setVisibility(View.INVISIBLE);
        loadTv.setVisibility(View.VISIBLE);
        loadData();
        firstloadHandler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(2000);
                List<OrderInner> tempList = new ArrayList<OrderInner>();
                tempList.addAll(mDataList);
                mDataList.clear();
                loadData();
                mDataList.addAll(tempList);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mAdapter.notifyDataSetChanged();
                mLv.hideHeadView();
                mLv.setSelection(0);
            }
        }.execute(new Void[]{});
    }

    @Override
    public void onLoadingMore() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(2000);
                loadData();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mAdapter.notifyDataSetChanged();
                mLv.hideFootView();
            }
        }.execute(new Void[]{});
    }

    @Override
    public void onClick(View item, View widget, int position, int which, boolean isChecked) {
        switch (which) {
            case R.id.rl_item_order_cancel:
                cancelAd.show();
                cancelIndex = position;
                break;
        }
    }
}
