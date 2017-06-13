package com.gangjianwang.www.gangjianwang;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.ContractCompanyGridAdapter;
import adapter.ContractCompanyListAdapter;
import bean.ContractCompany;

public class ContractProjectActivity extends AppCompatActivity implements View.OnClickListener {

    private View mMultiRankPopView;
    private ImageView mBackIv;
    private TextView mMultiRankTv, mSalesPriorityTv, mScreenTv;
    private ImageView mChangeStyleIv;
    private PopupWindow mMultiRankPop;
    private TextView mPopMultiRankTv, mPopUptoDownTv, mPopDowntoUpTv, mPopPopularityTv;
    private ListView mContractProjectLv;
    private GridView mContractProjectGv;
    private List<ContractCompany> mDataList;
    private ContractCompanyListAdapter mListAdapter;
    private ContractCompanyGridAdapter mGridAdapter;
    private int CURRENT_STYLE_STATE;
    private final int LIST_STATE = 0;
    private final int GRID_STATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contract_project);
        initView();
        initData();
        setData();
        setListener();
        loadData();
    }

    private void initView() {
        mBackIv = (ImageView) findViewById(R.id.iv_contractProject_back);
        mMultiRankTv = (TextView) findViewById(R.id.tv_contractProject_multiRank);
        mMultiRankTv.setTextColor(Color.RED);
        mSalesPriorityTv = (TextView) findViewById(R.id.tv_contractProject_salesPriority);
        mScreenTv = (TextView) findViewById(R.id.tv_contractProject_screen);
        mChangeStyleIv = (ImageView) findViewById(R.id.iv_contractProject_changeStyle);
        mContractProjectLv = (ListView) findViewById(R.id.lv_contractProject);
        mContractProjectGv = (GridView) findViewById(R.id.gv_contractProject);
        mMultiRankPopView = View.inflate(ContractProjectActivity.this, R.layout.pop_multirank, null);
        mMultiRankPop = new PopupWindow(mMultiRankPopView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mMultiRankPop.setFocusable(true);
        mMultiRankPop.setTouchable(true);
        mMultiRankPop.setOutsideTouchable(true);
        mPopMultiRankTv = (TextView) mMultiRankPopView.findViewById(R.id.tv_pop_multirank_multirank);
        mPopUptoDownTv = (TextView) mMultiRankPopView.findViewById(R.id.tv_pop_multirank_uptodown);
        mPopDowntoUpTv = (TextView) mMultiRankPopView.findViewById(R.id.tv_pop_multirank_downtoup);
        mPopPopularityTv = (TextView) mMultiRankPopView.findViewById(R.id.tv_pop_multirank_popularity);
    }

    private void initData() {
        mDataList = new ArrayList<>();
    }

    private void setData() {
        mListAdapter = new ContractCompanyListAdapter(ContractProjectActivity.this, mDataList);
        mContractProjectLv.setAdapter(mListAdapter);
        mGridAdapter = new ContractCompanyGridAdapter(ContractProjectActivity.this, mDataList);
        mContractProjectGv.setAdapter(mGridAdapter);
    }

    private void setListener() {
        mMultiRankPopView.setOnClickListener(ContractProjectActivity.this);
        mBackIv.setOnClickListener(ContractProjectActivity.this);
        mMultiRankTv.setOnClickListener(ContractProjectActivity.this);
        mSalesPriorityTv.setOnClickListener(ContractProjectActivity.this);
        mScreenTv.setOnClickListener(ContractProjectActivity.this);
        mChangeStyleIv.setOnClickListener(ContractProjectActivity.this);
        mPopMultiRankTv.setOnClickListener(ContractProjectActivity.this);
        mPopUptoDownTv.setOnClickListener(ContractProjectActivity.this);
        mPopDowntoUpTv.setOnClickListener(ContractProjectActivity.this);
        mPopPopularityTv.setOnClickListener(ContractProjectActivity.this);
    }

    private void loadData() {
        for (int i = 0; i < 10; i++) {
            mDataList.add(new ContractCompany("123", "金坤城", "金属材料批发", "道外区"));
        }
        mListAdapter.notifyDataSetChanged();
        mGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_contractProject_back:
                finish();
                break;
            case R.id.tv_contractProject_multiRank:
                mMultiRankPop.showAsDropDown(mMultiRankTv);
                break;
            case R.id.tv_pop_multirank_multirank:
                mMultiRankPop.dismiss();
                mMultiRankTv.setText(mPopMultiRankTv.getText().toString());
                break;
            case R.id.tv_pop_multirank_uptodown:
                mMultiRankPop.dismiss();
                mMultiRankTv.setText(mPopUptoDownTv.getText().toString());
                break;
            case R.id.tv_pop_multirank_downtoup:
                mMultiRankPop.dismiss();
                mMultiRankTv.setText(mPopDowntoUpTv.getText().toString());
                break;
            case R.id.tv_pop_multirank_popularity:
                mMultiRankPop.dismiss();
                mMultiRankTv.setText(mPopPopularityTv.getText().toString());
                break;
            case R.id.tv_contractProject_salesPriority:
                Toast.makeText(ContractProjectActivity.this, "销量优先", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_contractProject_screen:
                Toast.makeText(ContractProjectActivity.this, "筛选", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_contractProject_changeStyle:
                switch (CURRENT_STYLE_STATE) {
                    case LIST_STATE:
                        mChangeStyleIv.setImageResource(R.mipmap.browse_grid);
                        mContractProjectLv.setVisibility(View.INVISIBLE);
                        mContractProjectGv.setVisibility(View.VISIBLE);
                        CURRENT_STYLE_STATE = GRID_STATE;
                        break;
                    case GRID_STATE:
                        mChangeStyleIv.setImageResource(R.mipmap.browse_list);
                        mContractProjectGv.setVisibility(View.INVISIBLE);
                        mContractProjectLv.setVisibility(View.VISIBLE);
                        CURRENT_STYLE_STATE = LIST_STATE;
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
}
