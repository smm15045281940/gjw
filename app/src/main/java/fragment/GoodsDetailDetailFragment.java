package fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.ArrayList;
import java.util.List;

import adapter.GoodsDetailDetailAdapter;
import bean.GoodsDetailDetail;

/**
 * Created by Administrator on 2017/5/23.
 */

public class GoodsDetailDetailFragment extends Fragment {

    private View rootView;
    private ListView mLv;
    private List<GoodsDetailDetail> mDataList;
    private GoodsDetailDetailAdapter mAdapter;
    private ProgressDialog mPd;

    Handler firstloadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == 1) {
                    mPd.dismiss();
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goodsdetail_detail, null);
        initView(rootView);
        initData();
        setData();
        loadData();
        return rootView;
    }

    private void initView(View rootView) {
        mLv = (ListView) rootView.findViewById(R.id.lv_goodsdetaildetail);
        initProgress();
    }

    private void initProgress() {
        mPd = new ProgressDialog(getActivity());
    }

    private void initData() {
        mDataList = new ArrayList<>();
    }

    private void setData() {
        mAdapter = new GoodsDetailDetailAdapter(getActivity(), mDataList);
        mLv.setAdapter(mAdapter);
    }

    private void loadData() {
        mPd.show();
        for (int i = 0; i < 10; i++) {
            mDataList.add(new GoodsDetailDetail(""));
        }
        firstloadHandler.sendEmptyMessage(1);
    }
}
