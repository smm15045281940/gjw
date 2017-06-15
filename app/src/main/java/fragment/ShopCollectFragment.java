package fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import customview.MyRefreshListView;
import customview.OnRefreshListener;
import utils.ToastUtils;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class ShopCollectFragment extends Fragment implements OnRefreshListener {

    private View rootView, emptyView;
    private MyRefreshListView mLv;
    private List<String> testList = new ArrayList<>();
    private ArrayAdapter<String> testAdapter;
    private ProgressDialog mPd;
    private OkHttpClient okHttpClient;
    private Handler handler;
    private final int LOAD_FIRST = 1;
    private final int LOAD_REFRESH = 2;
    private final int LOAD_LOAD = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_shopcollect, null);
        rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initView();
        initData();
        setData();
        setListener();
        loadData(LOAD_FIRST);
        return rootView;
    }

    private void initView() {
        emptyView = View.inflate(getActivity(), R.layout.empty_type_myfoot, null);
        ((ImageView) emptyView.findViewById(R.id.iv_empty_type_myfoot_icon)).setImageResource(R.mipmap.empty_store_collect);
        ((TextView) emptyView.findViewById(R.id.tv_empty_type_myfoot_hint)).setText("您还没有关注任何店铺");
        ((TextView) emptyView.findViewById(R.id.tv_empty_type_myfoot_recommend)).setText("可以去看看哪些店铺值得收藏");
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        mLv = (MyRefreshListView) rootView.findViewById(R.id.lv_shopcollect);
        ((ViewGroup) mLv.getParent()).addView(emptyView);
        emptyView.setVisibility(View.GONE);
        mLv.setEmptyView(emptyView);
    }

    private void initData() {
        mPd = new ProgressDialog(getActivity());
        mPd.setMessage("加载中..");
        okHttpClient = new OkHttpClient();
        handler = new Handler();
        testAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, testList);
    }

    private void setData() {
        mLv.setAdapter(testAdapter);
    }

    private void setListener() {
        mLv.setOnRefreshListener(this);
    }

    private void loadData(int LOAD_STATE) {
        switch (LOAD_STATE) {
            case LOAD_FIRST:
                emptyView.setVisibility(View.GONE);
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
                                    for (int i = 0; i < 20; i++) {
                                        testList.add("店铺收藏-首次-" + i);
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
                                    testList.clear();
                                    for (int i = 0; i < 20; i++) {
                                        testList.add("店铺收藏-刷新-" + i);
                                    }
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
                                        testList.add("店铺收藏-加载-" + i);
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

    @Override
    public void onDownPullRefresh() {
        loadData(LOAD_REFRESH);
    }

    @Override
    public void onLoadingMore() {
        loadData(LOAD_LOAD);
    }

    Runnable runnableFirst = new Runnable() {
        @Override
        public void run() {
            mPd.dismiss();
            emptyView.setVisibility(View.VISIBLE);
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
}
