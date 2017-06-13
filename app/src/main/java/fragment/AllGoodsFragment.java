package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gangjianwang.www.gangjianwang.R;

import java.util.ArrayList;
import java.util.List;

import adapter.AllGoodsGridViewAdapter;
import adapter.AllGoodsListViewAdapter;
import bean.AllGoods;
import utils.ToastUtils;

/**
 * Created by Administrator on 2017/6/6.
 */

public class AllGoodsFragment extends Fragment implements View.OnClickListener {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_allgoods, null);
        initView();
        setData();
        loadData();
        setListener();
        return rootView;
    }

    private void initView() {
        mLv = (ListView) rootView.findViewById(R.id.lv_allgoods);
        mGv = (GridView) rootView.findViewById(R.id.gv_allgoods);
        mGv.setVisibility(View.INVISIBLE);
        STATE = LIST;
        synthOrderRl = (RelativeLayout) rootView.findViewById(R.id.rl_allgoods_synthorder);
        salesPriorityRl = (RelativeLayout) rootView.findViewById(R.id.rl_allgoods_salespriority);
        screenRl = (RelativeLayout) rootView.findViewById(R.id.rl_allgoods_screen);
        changeRl = (RelativeLayout) rootView.findViewById(R.id.rl_allgoods_change);
    }

    private void setData() {
        mListViewAdapter = new AllGoodsListViewAdapter(getActivity(), mList);
        mLv.setAdapter(mListViewAdapter);
        mGridViewAdapter = new AllGoodsGridViewAdapter(getActivity(), mList);
        mGv.setAdapter(mGridViewAdapter);
    }

    private void loadData() {
        for (int i = 0; i < 10; i++) {
            AllGoods allGoods = new AllGoods();
            allGoods.setGoodsName("花管");
            allGoods.setGoodsSales("1");
            allGoods.setGoodsPrice("¥10.00");
            mList.add(allGoods);
        }
        mListViewAdapter.notifyDataSetChanged();
        mGridViewAdapter.notifyDataSetChanged();
    }

    private void setListener() {
        synthOrderRl.setOnClickListener(this);
        salesPriorityRl.setOnClickListener(this);
        screenRl.setOnClickListener(this);
        changeRl.setOnClickListener(this);
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
}
