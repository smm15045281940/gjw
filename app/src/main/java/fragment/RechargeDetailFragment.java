package fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import config.NetConfig;
import customview.LazyFragment;
import customview.MyRefreshListView;
import customview.OnRefreshListener;
import utils.ToastUtils;

/**
 * Created by Administrator on 2017/4/20 0020.
 */

public class RechargeDetailFragment extends LazyFragment implements OnRefreshListener {

    private View rootView;
    private MyRefreshListView mLv;
    private View mEmptyView;
    private TextView mEmptyHintTv;
    private ProgressDialog mPd;
    private Handler handler;
    private OkHttpClient okHttpClient;
    private final int LOAD_FIRST = 1;
    private final int LOAD_REFRESH = 2;
    private final int LOAD_LOAD = 3;
    private List<String> testList = new ArrayList<>();
    private ArrayAdapter<String> testAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_accountbalance, null);
        initView();
        initData();
        setData();
        setListener();
        return rootView;
    }

    private void initView() {
        mLv = (MyRefreshListView) rootView.findViewById(R.id.lv_accountbalance);
        mEmptyView = View.inflate(getActivity(), R.layout.empty_account_balance, null);
        mEmptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ((ViewGroup) mLv.getParent()).addView(mEmptyView);
        mEmptyView.setVisibility(View.GONE);
        mEmptyHintTv = (TextView) mEmptyView.findViewById(R.id.tv_accountbalance_emptyhint);
        mEmptyHintTv.setText("您尚未充值过预存款");
        mLv.setEmptyView(mEmptyView);
    }

    private void initData() {
        mPd = new ProgressDialog(getActivity());
        mPd.setMessage("加载中..");
        handler = new Handler();
        okHttpClient = new OkHttpClient();
        testAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, testList);
    }

    private void setData() {
        mLv.setAdapter(testAdapter);
    }

    private void setListener() {
        mLv.setOnRefreshListener(this);
        mLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                testList.clear();
                testAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    protected void onFragmentFirstVisible() {
        loadData(LOAD_FIRST);
    }

    private void loadData(int LOAD_STATE) {
        switch (LOAD_STATE) {
            case LOAD_FIRST:
                mEmptyView.setVisibility(View.GONE);
                mPd.show();
                Request requestFirst = new Request.Builder().url(NetConfig.cityUrl).get().build();
                okHttpClient.newCall(requestFirst).enqueue(new Callback() {
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
                                    super.run();
                                    for (int i = 0; i < 10; i++) {
                                        testList.add("充值明细-第一次加载的数据-" + i);
                                    }
                                    handler.post(runnableFirst);
                                }
                            }.start();

                        } else {
                            mPd.dismiss();
                            ToastUtils.toast(getActivity(), "出小差了");
                        }
                    }
                });
                break;
            case LOAD_REFRESH:
                Request requestRefresh = new Request.Builder().url(NetConfig.cityUrl).get().build();
                okHttpClient.newCall(requestRefresh).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        mLv.hideHeadView();
                        ToastUtils.toast(getActivity(), "无网络");
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    List<String> tempList = new ArrayList<String>();
                                    tempList.addAll(testList);
                                    testList.clear();
                                    for (int i = 0; i < 1; i++) {
                                        testList.add("充值明细-刷新出来的数据-" + i);
                                    }
                                    testList.addAll(tempList);
                                    handler.post(runnableRefresh);
                                }
                            }.start();
                        } else {
                            mLv.hideHeadView();
                            ToastUtils.toast(getActivity(), "出小差了");
                        }
                    }
                });
                break;
            case LOAD_LOAD:
                Request requestLoad = new Request.Builder().url(NetConfig.cityUrl).get().build();
                okHttpClient.newCall(requestLoad).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        mLv.hideFootView();
                        ToastUtils.toast(getActivity(), "无网络");
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    for (int i = 0; i < 1; i++) {
                                        testList.add("充值明细-加载出来的数据-" + i);
                                    }
                                    handler.post(runnableLoad);
                                }
                            }.start();
                        } else {
                            mLv.hideFootView();
                            ToastUtils.toast(getActivity(), "出小差了");
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    Runnable runnableFirst = new Runnable() {
        @Override
        public void run() {
            mPd.dismiss();
            mEmptyView.setVisibility(View.VISIBLE);
            testAdapter.notifyDataSetChanged();
        }
    };

    Runnable runnableRefresh = new Runnable() {
        @Override
        public void run() {
            mLv.hideHeadView();
            testAdapter.notifyDataSetChanged();
        }
    };

    Runnable runnableLoad = new Runnable() {
        @Override
        public void run() {
            mLv.hideFootView();
            testAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onDownPullRefresh() {
        loadData(LOAD_REFRESH);
    }

    @Override
    public void onLoadingMore() {
        loadData(LOAD_LOAD);
    }
}
