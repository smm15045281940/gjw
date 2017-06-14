package fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gangjianwang.www.gangjianwang.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import config.NetConfig;
import customview.LazyFragment;
import utils.ToastUtils;

/**
 * Created by Administrator on 2017/4/20 0020.
 */

public class AccountBalanceFragment extends LazyFragment {

    private View rootView, mEmptyView;
    private ListView mLv;
    private ProgressDialog mPd;
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_accountbalance, null);
        initView();
        initData();
        return rootView;
    }

    private void initView() {
        mLv = (ListView) rootView.findViewById(R.id.lv_accountbalance);
        mEmptyView = View.inflate(getActivity(), R.layout.empty_account_balance, null);
        mEmptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ((ViewGroup) mLv.getParent()).addView(mEmptyView);
        mEmptyView.setVisibility(View.GONE);
        mLv.setEmptyView(mEmptyView);
    }

    private void initData() {
        mPd = new ProgressDialog(getActivity());
        mPd.setMessage("加载中..");
        handler = new Handler();
    }

    @Override
    protected void onFragmentFirstVisible() {
        loadData();
    }

    private void loadData() {
        mEmptyView.setVisibility(View.GONE);
        mPd.show();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(NetConfig.cityUrl).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mPd.dismiss();
                ToastUtils.toast(getActivity(), "无网络");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    new Thread() {
                        @Override
                        public void run() {
                            handler.post(runnableUi);
                        }
                    }.start();
                }
            }
        });
    }

    Runnable runnableUi = new Runnable() {
        @Override
        public void run() {
            mPd.dismiss();
            mEmptyView.setVisibility(View.VISIBLE);
        }
    };
}
