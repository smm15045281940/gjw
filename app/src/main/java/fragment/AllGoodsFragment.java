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
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gangjianwang.www.gangjianwang.R;
import com.gangjianwang.www.gangjianwang.StoreDetailActivity;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.AllGoodsGridViewAdapter;
import adapter.AllGoodsListViewAdapter;
import bean.AllGoods;
import config.NetConfig;
import config.ParaConfig;
import utils.ToastUtils;

/**
 * Created by Administrator on 2017/6/6.
 */

public class AllGoodsFragment extends Fragment implements View.OnClickListener, AbsListView.OnScrollListener {

    private View rootView;
    private RelativeLayout synthOrderRl, salesPriorityRl, screenRl, changeRl;
    private ListView mLv;
    private GridView mGv;
    private List<AllGoods> mList = new ArrayList<>();
    private AllGoodsListViewAdapter mListViewAdapter;
    private AllGoodsGridViewAdapter mGridViewAdapter;
    private final int LIST = 1;
    private final int GRID = 2;
    private int STATE;
    private int curPage = 1, totalPage;
    private String store_id;
    private ProgressDialog progressDialog;
    private OkHttpClient okHttpClient;
    private int LOAD_STATE = ParaConfig.FIRST;
    private boolean isLast;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case 0:
                        ToastUtils.toast(getActivity(), ParaConfig.NETWORK_ERROR);
                        break;
                    case 1:
                        break;
                }
                mListViewAdapter.notifyDataSetChanged();
                mGridViewAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
        handler.removeMessages(1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_allgoods, null);
        initView();
        initData();
        setData();
        loadData();
        setListener();
        return rootView;
    }

    private void initView() {
        initRoot();
        initProgress();
    }

    private void initRoot() {
        mLv = (ListView) rootView.findViewById(R.id.lv_allgoods);
        mGv = (GridView) rootView.findViewById(R.id.gv_allgoods);
        mGv.setVisibility(View.INVISIBLE);
        STATE = LIST;
        synthOrderRl = (RelativeLayout) rootView.findViewById(R.id.rl_allgoods_synthorder);
        salesPriorityRl = (RelativeLayout) rootView.findViewById(R.id.rl_allgoods_salespriority);
        screenRl = (RelativeLayout) rootView.findViewById(R.id.rl_allgoods_screen);
        changeRl = (RelativeLayout) rootView.findViewById(R.id.rl_allgoods_change);
    }

    private void initProgress() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
        store_id = ((StoreDetailActivity) getActivity()).getStoreId();
        mListViewAdapter = new AllGoodsListViewAdapter(getActivity(), mList);
        mGridViewAdapter = new AllGoodsGridViewAdapter(getActivity(), mList);
    }

    private void setData() {
        mLv.setAdapter(mListViewAdapter);
        mGv.setAdapter(mGridViewAdapter);
    }

    private void loadData() {
        if (LOAD_STATE == ParaConfig.FIRST) {
            progressDialog.show();
        }
        Request request = new Request.Builder().url(NetConfig.storeDetailAllGoodsHeadUrl + store_id + NetConfig.storeDetailAllGoodsFootPageHeadUrl + curPage + NetConfig.storeDetailAllGoodsFootPageFootUrl).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful() && parseJson(response.body().string())) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    private boolean parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            if (objBean.optInt("code") == 200) {
                totalPage = objBean.optInt("page_total");
                JSONObject objDatas = objBean.optJSONObject("datas");
                JSONArray arrGoodsList = objDatas.optJSONArray("goods_list");
                for (int i = 0; i < arrGoodsList.length(); i++) {
                    JSONObject o = arrGoodsList.optJSONObject(i);
                    AllGoods allGoods = new AllGoods();
                    allGoods.setGoodsName(o.optString("goods_name"));
                    allGoods.setGoodsPrice(o.optString("goods_price"));
                    allGoods.setGoodsSales(o.optString("goods_salenum"));
                    allGoods.setGoodsIcon(o.optString("goods_image_url"));
                    mList.add(allGoods);
                }
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setListener() {
        synthOrderRl.setOnClickListener(this);
        salesPriorityRl.setOnClickListener(this);
        screenRl.setOnClickListener(this);
        changeRl.setOnClickListener(this);
        mLv.setOnScrollListener(this);
        mGv.setOnScrollListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_allgoods_synthorder:
                ToastUtils.toast(getActivity(), "综合排序");
                break;
            case R.id.rl_allgoods_salespriority:
                ToastUtils.toast(getActivity(), "销量优先");
                break;
            case R.id.rl_allgoods_screen:
                ToastUtils.toast(getActivity(), "筛选");
                break;
            case R.id.rl_allgoods_change:
                switch (STATE) {
                    case LIST:
                        STATE = GRID;
                        changeByState(STATE);
                        break;
                    case GRID:
                        STATE = LIST;
                        changeByState(STATE);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    private void changeByState(int STATE) {
        switch (STATE) {
            case LIST:
                mGv.setVisibility(View.INVISIBLE);
                mLv.setVisibility(View.VISIBLE);
                break;
            case GRID:
                mLv.setVisibility(View.INVISIBLE);
                mGv.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isLast && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (curPage < totalPage) {
                LOAD_STATE = ParaConfig.LOAD;
                curPage++;
                loadData();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        isLast = (firstVisibleItem + visibleItemCount) == totalItemCount;
    }
}
