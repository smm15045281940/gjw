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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import config.ParaConfig;
import utils.ToastUtils;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class RefundFragment extends Fragment {

    private View rootView, emptyView;
    private ListView lv;
    private ProgressDialog progressDialog;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        ToastUtils.toast(getActivity(), ParaConfig.NETWORK_ERROR);
                        break;
                    case 1:
                        progressDialog.dismiss();
                        emptyView.setVisibility(View.VISIBLE);
                        break;
                }
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
        rootView = inflater.inflate(R.layout.fragment_refundreturn, null);
        initView();
        initData();
        loadData();
        return rootView;
    }

    private void initView() {
        initRoot();
        initEmpty();
    }

    private void initRoot() {
        rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        lv = (ListView) rootView.findViewById(R.id.lv_refundreturn);
    }

    private void initEmpty() {
        emptyView = View.inflate(getActivity(), R.layout.empty_refund, null);
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((TextView) emptyView.findViewById(R.id.tv_empty_refund_hint)).setText("您还没有退款信息");
        ((TextView) emptyView.findViewById(R.id.tv_empty_refund_tips)).setText("已购订单详情可申请退款");
        ((ViewGroup) lv.getParent()).addView(emptyView);
        emptyView.setVisibility(View.GONE);
        lv.setEmptyView(emptyView);
    }

    private void initData() {
        progressDialog = new ProgressDialog(getActivity());
    }

    private void loadData() {
        emptyView.setVisibility(View.GONE);
        progressDialog.show();
        handler.sendEmptyMessage(1);
    }
}
