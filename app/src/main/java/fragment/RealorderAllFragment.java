package fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gangjianwang.www.gangjianwang.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.OrderOuterAdapter;
import bean.OrderOuter;
import config.NetConfig;
import customview.LazyFragment;
import utils.ToastUtils;
import utils.UserUtils;

/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class RealorderAllFragment extends LazyFragment {

    private View rootView, emptyView;
    private ListView lv;
    private List<OrderOuter> orderOuterList = new ArrayList<>();
    private OrderOuterAdapter orderOuterAdapter;
    private ProgressDialog progressDialog;
    private OkHttpClient okHttpClient;
    private String key;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        progressDialog.dismiss();
                        ToastUtils.toast(getActivity(), "无网络");
                        break;
                    case 1:
                        progressDialog.dismiss();
                        orderOuterAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_series_order, null);
        initView();
        initData();
        setData();
        return rootView;
    }

    private void initView() {
        initRoot();
        initEmpty();
    }

    private void initRoot() {
        rootView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        lv = (ListView) rootView.findViewById(R.id.lv_series_order);
    }

    private void initEmpty() {
        emptyView = View.inflate(getActivity(), R.layout.empty_order, null);
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ((ViewGroup) lv.getParent()).addView(emptyView);
        emptyView.setVisibility(View.GONE);
    }

    private void initData() {
        orderOuterAdapter = new OrderOuterAdapter(getActivity(), orderOuterList);
        progressDialog = new ProgressDialog(getActivity());
        okHttpClient = new OkHttpClient();
        key = UserUtils.readLogin(getActivity(), true).getKey();
    }

    private void setData() {
        lv.setAdapter(orderOuterAdapter);
    }

    private void loadData() {
        progressDialog.show();
        RequestBody body = new FormEncodingBuilder()
                .add("key", key)
                .add("state_type", "")
                .add("order_key", "")
                .build();
        Request request = new Request.Builder()
                .url(NetConfig.realallorderUrl)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (parseJson(response.body().string())) {
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    private boolean parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            JSONObject objDatas = objBean.optJSONObject("datas");
            JSONArray arrOrderGroupList = objDatas.optJSONArray("order_group_list");
            for (int i = 0; i < arrOrderGroupList.length(); i++) {
                OrderOuter orderOuter = new OrderOuter();
                JSONObject obj = arrOrderGroupList.getJSONObject(i);
                JSONArray arrOrderList = obj.optJSONArray("order_list");
                //TODO 解析
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onFragmentFirstVisible() {
        loadData();
    }
}
