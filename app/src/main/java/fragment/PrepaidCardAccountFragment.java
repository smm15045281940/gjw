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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import config.ParaConfig;
import utils.ToastUtils;

/**
 * Created by Administrator on 2017/4/20 0020.
 */

public class PrepaidCardAccountFragment extends Fragment {

    private View rootView, emptyView;
    private ListView lv;
    private ProgressDialog progressDialog;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case ParaConfig.DEFEAT:
                        progressDialog.dismiss();
                        emptyView.setVisibility(View.VISIBLE);
                        ToastUtils.toast(getActivity(), ParaConfig.NETWORK_ERROR);
                        break;
                    case ParaConfig.SUCCESS:
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
        rootView = inflater.inflate(R.layout.fragment_prepaidcard_account, null);
        initView();
        initData();
        loadData();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(ParaConfig.DEFEAT);
        handler.removeMessages(ParaConfig.SUCCESS);
    }

    private void initView() {
        initRoot();
        initEmpty();
    }

    private void initRoot() {
        rootView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        lv = (ListView) rootView.findViewById(R.id.lv_prepaidcard_account);
    }

    private void initEmpty() {
        emptyView = View.inflate(getActivity(), R.layout.empty, null);
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ImageView) emptyView.findViewById(R.id.iv_empty_icon)).setImageResource(ParaConfig.CARD_BALANCE_ICON);
        ((TextView) emptyView.findViewById(R.id.tv_empty_hint)).setText(ParaConfig.CARD_BALANCE_HINT);
        ((TextView) emptyView.findViewById(R.id.tv_empty_content)).setText(ParaConfig.CARD_BALANCE_CONTENT);
        ((ViewGroup) lv.getParent()).addView(emptyView);
        emptyView.setVisibility(View.GONE);
        lv.setEmptyView(emptyView);
    }

    private void initData() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void loadData() {
        emptyView.setVisibility(View.INVISIBLE);
        progressDialog.show();
        handler.sendEmptyMessageDelayed(ParaConfig.SUCCESS, 500);
    }
}
