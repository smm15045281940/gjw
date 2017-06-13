package fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import customview.LazyFragment;
import customview.MyRefreshListView;
import utils.ToastUtils;

/**
 * Created by Administrator on 2017/5/23.
 */

public class GoodsDetailEvaluateFragment extends LazyFragment implements View.OnClickListener {

    private View rootView, emptyView;
    private LinearLayout ll;
    private RelativeLayout allRl, goodRl, midRl, poorRl, orderphotoRl, addRl;
    private GradientDrawable allGd, goodGd, midGd, poorGd, orderphotoGd, addGd;
    private MyRefreshListView mlv;
    private TextView loadTv;

    Handler loadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == 1) {
                    loadTv.setVisibility(View.INVISIBLE);
                    ll.setVisibility(View.VISIBLE);
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
        initView(rootView);
        setListener();
        return rootView;
    }

    private void initView(View rootView) {
        ll = (LinearLayout) rootView.findViewById(R.id.ll_goodsdetailevaluate);
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
        mlv = (MyRefreshListView) rootView.findViewById(R.id.mlv_goodsdetailevaluate);
        loadTv = (TextView) rootView.findViewById(R.id.tv_goodsdetailevaluate_load);
        ((ViewGroup) mlv.getParent()).addView(emptyView);
        emptyView.setVisibility(View.GONE);
        mlv.setEmptyView(emptyView);
    }

    private void setListener() {
        allRl.setOnClickListener(this);
        goodRl.setOnClickListener(this);
        midRl.setOnClickListener(this);
        poorRl.setOnClickListener(this);
        orderphotoRl.setOnClickListener(this);
        addRl.setOnClickListener(this);
    }

    @Override
    protected void onFragmentFirstVisible() {
        ll.setVisibility(View.INVISIBLE);
        loadTv.setVisibility(View.VISIBLE);
        loadData();
        loadHandler.sendEmptyMessageDelayed(1, 1000);
    }

    private void loadData() {

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
                ToastUtils.toast(getActivity(),"全部评价");
                break;
            case R.id.rl_goodsdetailevaluate_good:
                allGd.setColor(Color.parseColor("#AAAAAA"));
                goodGd.setColor(Color.RED);
                midGd.setColor(Color.parseColor("#AAAAAA"));
                poorGd.setColor(Color.parseColor("#AAAAAA"));
                orderphotoGd.setColor(Color.parseColor("#AAAAAA"));
                addGd.setColor(Color.parseColor("#AAAAAA"));
                ToastUtils.toast(getActivity(),"好评");
                break;
            case R.id.rl_goodsdetailevaluate_mid:
                allGd.setColor(Color.parseColor("#AAAAAA"));
                goodGd.setColor(Color.parseColor("#AAAAAA"));
                midGd.setColor(Color.RED);
                poorGd.setColor(Color.parseColor("#AAAAAA"));
                orderphotoGd.setColor(Color.parseColor("#AAAAAA"));
                addGd.setColor(Color.parseColor("#AAAAAA"));
                ToastUtils.toast(getActivity(),"中评");
                break;
            case R.id.rl_goodsdetailevaluate_poor:
                allGd.setColor(Color.parseColor("#AAAAAA"));
                goodGd.setColor(Color.parseColor("#AAAAAA"));
                midGd.setColor(Color.parseColor("#AAAAAA"));
                poorGd.setColor(Color.RED);
                orderphotoGd.setColor(Color.parseColor("#AAAAAA"));
                addGd.setColor(Color.parseColor("#AAAAAA"));
                ToastUtils.toast(getActivity(),"差评");
                break;
            case R.id.rl_goodsdetailevaluate_orderphoto:
                allGd.setColor(Color.parseColor("#AAAAAA"));
                goodGd.setColor(Color.parseColor("#AAAAAA"));
                midGd.setColor(Color.parseColor("#AAAAAA"));
                poorGd.setColor(Color.parseColor("#AAAAAA"));
                orderphotoGd.setColor(Color.RED);
                addGd.setColor(Color.parseColor("#AAAAAA"));
                ToastUtils.toast(getActivity(),"订单晒图");
                break;
            case R.id.rl_goodsdetailevaluate_add:
                allGd.setColor(Color.parseColor("#AAAAAA"));
                goodGd.setColor(Color.parseColor("#AAAAAA"));
                midGd.setColor(Color.parseColor("#AAAAAA"));
                poorGd.setColor(Color.parseColor("#AAAAAA"));
                orderphotoGd.setColor(Color.parseColor("#AAAAAA"));
                addGd.setColor(Color.RED);
                ToastUtils.toast(getActivity(),"追加评价");
                break;
            default:
                break;
        }
    }
}
