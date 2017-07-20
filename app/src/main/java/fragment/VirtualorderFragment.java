package fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import config.NetConfig;
import utils.ToastUtils;
import utils.UserUtils;

/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class VirtualOrderFragment extends Fragment implements View.OnClickListener {

    private View rootView, emptyView;
    private LinearLayout allLl, waitpayLl, waituseLl;
    private TextView allTv, waitpayTv, waituseTv;
    private View allV, waitpayV, waituseV;
    private ListView lv;
    private int curPosition, tarPosition;
    private OkHttpClient okHttpClient;
    private ProgressDialog progressDialog;
    private String key, state_type, order_key;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        progressDialog.dismiss();
                        emptyView.setVisibility(View.VISIBLE);
                        ToastUtils.toast(getActivity(), "无网络");
                        break;
                    case 1:
                        progressDialog.dismiss();
                        emptyView.setVisibility(View.VISIBLE);
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
        rootView = inflater.inflate(R.layout.fragment_virtualorder, null);
        initView();
        initData();
        setListener();
        loadData();
        return rootView;
    }

    private void initView() {
        initRoot();
        initEmpty();
    }

    private void initRoot() {
        rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        allLl = (LinearLayout) rootView.findViewById(R.id.ll_virtualorder_all);
        waitpayLl = (LinearLayout) rootView.findViewById(R.id.ll_virtualorder_wait_pay);
        waituseLl = (LinearLayout) rootView.findViewById(R.id.ll_virtualorder_wait_use);
        allTv = (TextView) rootView.findViewById(R.id.tv_virtualorder_all);
        waitpayTv = (TextView) rootView.findViewById(R.id.tv_virtualorder_wait_pay);
        waituseTv = (TextView) rootView.findViewById(R.id.tv_virtualorder_wait_use);
        allV = rootView.findViewById(R.id.v_virtualorder_all);
        waitpayV = rootView.findViewById(R.id.v_virtualorder_wait_pay);
        waituseV = rootView.findViewById(R.id.v_virtualorder_wait_use);
        lv = (ListView) rootView.findViewById(R.id.lv_virtualorder);
    }

    private void initEmpty() {
        emptyView = View.inflate(getActivity(), R.layout.empty_order, null);
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ((ViewGroup) lv.getParent()).addView(emptyView);
        emptyView.setVisibility(View.GONE);
        lv.setEmptyView(emptyView);
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        key = UserUtils.readLogin(getActivity(), true).getKey();
        state_type = "";
        order_key = "";
    }

    private void setListener() {
        allLl.setOnClickListener(this);
        waitpayLl.setOnClickListener(this);
        waituseLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_virtualorder_all:
                tarPosition = 0;
                break;
            case R.id.ll_virtualorder_wait_pay:
                tarPosition = 1;
                break;
            case R.id.ll_virtualorder_wait_use:
                tarPosition = 2;
                break;
            default:
                break;
        }
        changeOrder();
    }

    private void changeOrder() {
        if (tarPosition != curPosition) {
            switch (tarPosition) {
                case 0:
                    allTv.setTextColor(Color.RED);
                    waitpayTv.setTextColor(Color.BLACK);
                    waituseTv.setTextColor(Color.BLACK);
                    allV.setVisibility(View.VISIBLE);
                    waitpayV.setVisibility(View.INVISIBLE);
                    waituseV.setVisibility(View.INVISIBLE);
                    state_type = "";
                    order_key = "";
                    break;
                case 1:
                    allTv.setTextColor(Color.BLACK);
                    waitpayTv.setTextColor(Color.RED);
                    waituseTv.setTextColor(Color.BLACK);
                    allV.setVisibility(View.INVISIBLE);
                    waitpayV.setVisibility(View.VISIBLE);
                    waituseV.setVisibility(View.INVISIBLE);
                    state_type = "state_new";
                    order_key = "";
                    break;
                case 2:
                    allTv.setTextColor(Color.BLACK);
                    waitpayTv.setTextColor(Color.BLACK);
                    waituseTv.setTextColor(Color.RED);
                    allV.setVisibility(View.INVISIBLE);
                    waitpayV.setVisibility(View.INVISIBLE);
                    waituseV.setVisibility(View.VISIBLE);
                    state_type = "state_pay";
                    order_key = "";
                    break;
                default:
                    break;
            }
            loadData();
            curPosition = tarPosition;
        }
    }

    private void loadData() {
        emptyView.setVisibility(View.GONE);
        progressDialog.show();
        RequestBody body = new FormEncodingBuilder().add("key", key).add("state_type", state_type).add("order_key", order_key).build();
        Request request = new Request.Builder().url(NetConfig.virtualorderUrl).post(body).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject objBean = new JSONObject(response.body().string());
                        if (objBean.optInt("code") == 200) {
                            handler.sendEmptyMessage(1);
                        } else {
                            handler.sendEmptyMessage(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }
}
