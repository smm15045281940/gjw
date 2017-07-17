package fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.ArrayList;
import java.util.List;

import adapter.ShopHostRecAdapter;
import bean.ShopHostRec;
import customview.HeaderGridView;
import utils.ToastUtils;

import static com.gangjianwang.www.gangjianwang.R.id.v_head_shopfirstpage_headgridview_collectrank;

/**
 * Created by Administrator on 2017/6/6.
 */

public class ShopFirstpageFragment extends Fragment implements View.OnClickListener {

    private View rootView, headView;
    private RelativeLayout collectRankRl, salesRankRl;
    private TextView collectRankTv, salesRankTv;
    private View collectRankV, salesRankV;
    private HeaderGridView mHgv;
    private List<ShopHostRec> mList = new ArrayList<>();
    private ShopHostRecAdapter mAdapter;

    private final int COLLECT = 1;
    private final int SALES = 2;
    private int RANK_STATE;
    private int TARGET_STATE;

    private String key;
    private String store_id;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    default:
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
        handler.removeMessages(2);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_shopfirstpage, null);
        headView = inflater.inflate(R.layout.head_shopfirstpage_headgridview, null);
        initView();
        initData();
        setData();
        setListener();
        loadData();
        return rootView;
    }

    private void initView() {
        mHgv = (HeaderGridView) rootView.findViewById(R.id.hgv_shopfirstpage);
        mHgv.addHeaderView(headView);
        collectRankRl = (RelativeLayout) headView.findViewById(R.id.rl_head_shopfirstpage_headgridview_collectrank);
        collectRankTv = (TextView) headView.findViewById(R.id.tv_head_shopfirstpage_headgridview_collectrank);
        collectRankV = headView.findViewById(v_head_shopfirstpage_headgridview_collectrank);
        salesRankRl = (RelativeLayout) headView.findViewById(R.id.rl_head_shopfirstpage_headgridview_salesrank);
        salesRankTv = (TextView) headView.findViewById(R.id.tv_head_shopfirstpage_headgridview_salestrank);
        salesRankV = headView.findViewById(R.id.v_head_shopfirstpage_headgridview_salestrank);
        collectRankTv.setTextColor(Color.RED);
        collectRankV.setBackgroundColor(Color.RED);
        RANK_STATE = COLLECT;
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String test = bundle.getString("store_id");
            ToastUtils.log(getActivity(), test);
        }
//        mAdapter = new ShopHostRecAdapter(getActivity(), mList);
    }

    private void setData() {
//        mHgv.setAdapter(mAdapter);
    }

    private void loadData() {
        for (int i = 0; i < 11; i++) {
            ShopHostRec shopHostRec = new ShopHostRec();
            shopHostRec.setGoodsName("圆管");
            shopHostRec.setGoodsPrice("¥10.00");
            shopHostRec.setImageUrl("");
            mList.add(shopHostRec);
        }
//        mAdapter.notifyDataSetChanged();
    }

    private void setListener() {
        collectRankRl.setOnClickListener(this);
        salesRankRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_head_shopfirstpage_headgridview_collectrank:
                TARGET_STATE = COLLECT;
                changeByState(TARGET_STATE);
                break;
            case R.id.rl_head_shopfirstpage_headgridview_salesrank:
                TARGET_STATE = SALES;
                changeByState(TARGET_STATE);
                break;
            default:
                break;
        }
    }

    private void changeByState(int TARGET_STATE) {
        if (RANK_STATE != TARGET_STATE) {
            switch (TARGET_STATE) {
                case COLLECT:
                    collectRankTv.setTextColor(Color.RED);
                    collectRankV.setBackgroundColor(Color.RED);
                    salesRankTv.setTextColor(Color.BLACK);
                    salesRankV.setBackgroundColor(Color.WHITE);
                    RANK_STATE = COLLECT;
                    break;
                case SALES:
                    collectRankTv.setTextColor(Color.BLACK);
                    collectRankV.setBackgroundColor(Color.WHITE);
                    salesRankTv.setTextColor(Color.RED);
                    salesRankV.setBackgroundColor(Color.RED);
                    RANK_STATE = SALES;
                    break;
                default:
                    break;
            }
        }
    }
}
