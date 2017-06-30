package fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gangjianwang.www.gangjianwang.R;

import utils.ToastUtils;

/**
 * Created by Administrator on 2017/5/23.
 */

public class GoodsDetailEvaluateFragment extends Fragment implements View.OnClickListener {

    private View rootView, emptyView;
    private RelativeLayout allRl, goodRl, midRl, poorRl, orderphotoRl, addRl;
    private GradientDrawable allGd, goodGd, midGd, poorGd, orderphotoGd, addGd;
    private ListView mlv;
    private ProgressDialog mPd;

    Handler loadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == 1) {
                    mPd.dismiss();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goodsdetail_evaluate, null);
        emptyView = inflater.inflate(R.layout.empty_goodsdetailevaluate, null);
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        initView();
        setListener();
        loadData();
        return rootView;
    }

    private void initView() {
        initPro();
        allRl = (RelativeLayout) rootView.findViewById(R.id.rl_goodsdetailevaluate_all);
        goodRl = (RelativeLayout) rootView.findViewById(R.id.rl_goodsdetailevaluate_good);
        midRl = (RelativeLayout) rootView.findViewById(R.id.rl_goodsdetailevaluate_mid);
        poorRl = (RelativeLayout) rootView.findViewById(R.id.rl_goodsdetailevaluate_poor);
        orderphotoRl = (RelativeLayout) rootView.findViewById(R.id.rl_goodsdetailevaluate_orderphoto);
        addRl = (RelativeLayout) rootView.findViewById(R.id.rl_goodsdetailevaluate_add);
        allGd = (GradientDrawable) allRl.getBackground();
        goodGd = (GradientDrawable) goodRl.getBackground();
        midGd = (GradientDrawable) midRl.getBackground();
        poorGd = (GradientDrawable) poorRl.getBackground();
        orderphotoGd = (GradientDrawable) orderphotoRl.getBackground();
        addGd = (GradientDrawable) addRl.getBackground();
        allGd.setColor(Color.RED);
        goodGd.setColor(Color.parseColor("#AAAAAA"));
        midGd.setColor(Color.parseColor("#AAAAAA"));
        poorGd.setColor(Color.parseColor("#AAAAAA"));
        orderphotoGd.setColor(Color.parseColor("#AAAAAA"));
        addGd.setColor(Color.parseColor("#AAAAAA"));
        mlv = (ListView) rootView.findViewById(R.id.lv_goodsdetailevaluate);
        ((ViewGroup) mlv.getParent()).addView(emptyView);
        emptyView.setVisibility(View.GONE);
        mlv.setEmptyView(emptyView);
    }

    private void initPro() {
        mPd = new ProgressDialog(getActivity());
    }

    private void setListener() {
        allRl.setOnClickListener(this);
        goodRl.setOnClickListener(this);
        midRl.setOnClickListener(this);
        poorRl.setOnClickListener(this);
        orderphotoRl.setOnClickListener(this);
        addRl.setOnClickListener(this);
    }

    private void loadData() {
        mPd.show();
        loadHandler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_goodsdetailevaluate_all:
                allGd.setColor(Color.RED);
                goodGd.setColor(Color.parseColor("#AAAAAA"));
                midGd.setColor(Color.parseColor("#AAAAAA"));
                poorGd.setColor(Color.parseColor("#AAAAAA"));
                orderphotoGd.setColor(Color.parseColor("#AAAAAA"));
                addGd.setColor(Color.parseColor("#AAAAAA"));
                ToastUtils.toast(getActivity(), "全部评价");
                break;
            case R.id.rl_goodsdetailevaluate_good:
                allGd.setColor(Color.parseColor("#AAAAAA"));
                goodGd.setColor(Color.RED);
                midGd.setColor(Color.parseColor("#AAAAAA"));
                poorGd.setColor(Color.parseColor("#AAAAAA"));
                orderphotoGd.setColor(Color.parseColor("#AAAAAA"));
                addGd.setColor(Color.parseColor("#AAAAAA"));
                ToastUtils.toast(getActivity(), "好评");
                break;
            case R.id.rl_goodsdetailevaluate_mid:
                allGd.setColor(Color.parseColor("#AAAAAA"));
                goodGd.setColor(Color.parseColor("#AAAAAA"));
                midGd.setColor(Color.RED);
                poorGd.setColor(Color.parseColor("#AAAAAA"));
                orderphotoGd.setColor(Color.parseColor("#AAAAAA"));
                addGd.setColor(Color.parseColor("#AAAAAA"));
                ToastUtils.toast(getActivity(), "中评");
                break;
            case R.id.rl_goodsdetailevaluate_poor:
                allGd.setColor(Color.parseColor("#AAAAAA"));
                goodGd.setColor(Color.parseColor("#AAAAAA"));
                midGd.setColor(Color.parseColor("#AAAAAA"));
                poorGd.setColor(Color.RED);
                orderphotoGd.setColor(Color.parseColor("#AAAAAA"));
                addGd.setColor(Color.parseColor("#AAAAAA"));
                ToastUtils.toast(getActivity(), "差评");
                break;
            case R.id.rl_goodsdetailevaluate_orderphoto:
                allGd.setColor(Color.parseColor("#AAAAAA"));
                goodGd.setColor(Color.parseColor("#AAAAAA"));
                midGd.setColor(Color.parseColor("#AAAAAA"));
                poorGd.setColor(Color.parseColor("#AAAAAA"));
                orderphotoGd.setColor(Color.RED);
                addGd.setColor(Color.parseColor("#AAAAAA"));
                ToastUtils.toast(getActivity(), "订单晒图");
                break;
            case R.id.rl_goodsdetailevaluate_add:
                allGd.setColor(Color.parseColor("#AAAAAA"));
                goodGd.setColor(Color.parseColor("#AAAAAA"));
                midGd.setColor(Color.parseColor("#AAAAAA"));
                poorGd.setColor(Color.parseColor("#AAAAAA"));
                orderphotoGd.setColor(Color.parseColor("#AAAAAA"));
                addGd.setColor(Color.RED);
                ToastUtils.toast(getActivity(), "追加评价");
                break;
            default:
                break;
        }
    }
}
