package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import adapter.ContractCompanyGridAdapter;
import adapter.ContractCompanyListAdapter;
import bean.ContractCompany;
import config.NetConfig;
import utils.ToastUtils;

public class ContractProjectActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private View rootView, mMultiRankPopView;
    private RelativeLayout mBackRl, mSearchRl, mChangeStyleRl, mSceenRl, mPriorityRl, mMutiRankRl;
    private EditText mEtSearchContentEt;
    private TextView mMultiRankTv;
    private ImageView mChangeStyleIv;
    private PopupWindow mMultiRankPop;
    private TextView mPopMultiRankTv, mPopUptoDownTv, mPopDowntoUpTv, mPopPopularityTv;
    private RelativeLayout mPopMultiRankRl, mPopUptoDownRl, mPopDowntoUpRl, mPopPopularityRl;
    private ListView mContractProjectLv;
    private GridView mContractProjectGv;
    private List<ContractCompany> mDataList = new ArrayList<>();
    private ContractCompanyListAdapter mListAdapter;
    private ContractCompanyGridAdapter mGridAdapter;
    private int CURRENT_STYLE_STATE;
    private final int LIST_STATE = 0;
    private final int GRID_STATE = 1;
    private OkHttpClient okHttpClient;
    private ProgressDialog mPd;
    private final int SHOP_LIST_STATE = 1;
    private final int CONTRACT_PROJECT_STATE = 2;
    private int STATE;
    private String gcId = "";


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        mPd.dismiss();
                        ToastUtils.toast(ContractProjectActivity.this, "无网络");
                        break;
                    case 1:
                        mPd.dismiss();
                        mListAdapter.notifyDataSetChanged();
                        mGridAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_contract_project, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
        loadData(1, gcId, STATE);
    }

    private void initView() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_contractproject_back);
        mSearchRl = (RelativeLayout) rootView.findViewById(R.id.rl_contractproject_search);
        mChangeStyleRl = (RelativeLayout) rootView.findViewById(R.id.rl_contractProject_changeStyle);
        mSceenRl = (RelativeLayout) rootView.findViewById(R.id.rl_contractProject_screen);
        mPriorityRl = (RelativeLayout) rootView.findViewById(R.id.rl_contractProject_salesPriority);
        mMutiRankRl = (RelativeLayout) rootView.findViewById(R.id.rl_contractProject_multiRank);
        mEtSearchContentEt = (EditText) rootView.findViewById(R.id.et_contractproject_searchcontent);
        mMultiRankTv = (TextView) rootView.findViewById(R.id.tv_contractProject_multiRank);
        mMultiRankTv.setTextColor(Color.RED);
        mChangeStyleIv = (ImageView) rootView.findViewById(R.id.iv_contractProject_changeStyle);
        mContractProjectLv = (ListView) rootView.findViewById(R.id.lv_contractProject);
        mContractProjectGv = (GridView) rootView.findViewById(R.id.gv_contractProject);
        mMultiRankPopView = View.inflate(ContractProjectActivity.this, R.layout.pop_multirank, null);
        mMultiRankPop = new PopupWindow(mMultiRankPopView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mMultiRankPop.setFocusable(true);
        mMultiRankPop.setTouchable(true);
        mMultiRankPop.setOutsideTouchable(true);
        mPopMultiRankTv = (TextView) mMultiRankPopView.findViewById(R.id.tv_pop_multirank_multirank);
        mPopUptoDownTv = (TextView) mMultiRankPopView.findViewById(R.id.tv_pop_multirank_uptodown);
        mPopDowntoUpTv = (TextView) mMultiRankPopView.findViewById(R.id.tv_pop_multirank_downtoup);
        mPopPopularityTv = (TextView) mMultiRankPopView.findViewById(R.id.tv_pop_multirank_popularity);
        mPopMultiRankRl = (RelativeLayout) mMultiRankPopView.findViewById(R.id.rl_pop_multirank_multirank);
        mPopUptoDownRl = (RelativeLayout) mMultiRankPopView.findViewById(R.id.rl_pop_multirank_uptodown);
        mPopDowntoUpRl = (RelativeLayout) mMultiRankPopView.findViewById(R.id.rl_pop_multirank_downtoup);
        mPopPopularityRl = (RelativeLayout) mMultiRankPopView.findViewById(R.id.rl_pop_multirank_popularity);
    }

    private void initData() {
        Intent intent = getIntent();
        int stateId = intent.getIntExtra("stateId", 0);
        gcId = intent.getStringExtra("gc_id");
        switch (stateId) {
            case 0:
                STATE = SHOP_LIST_STATE;
                break;
            case 1:
                STATE = CONTRACT_PROJECT_STATE;
                break;
            default:
                break;
        }
        mPd = new ProgressDialog(this);
        mPd.setMessage("加载中..");
        okHttpClient = new OkHttpClient();
    }

    private void setData() {
        mListAdapter = new ContractCompanyListAdapter(ContractProjectActivity.this, mDataList);
        mContractProjectLv.setAdapter(mListAdapter);
        mGridAdapter = new ContractCompanyGridAdapter(ContractProjectActivity.this, mDataList);
        mContractProjectGv.setAdapter(mGridAdapter);
    }

    private void setListener() {
        mMultiRankPopView.setOnClickListener(this);
        mBackRl.setOnClickListener(this);
        mSearchRl.setOnClickListener(this);
        mChangeStyleRl.setOnClickListener(this);
        mSceenRl.setOnClickListener(this);
        mPriorityRl.setOnClickListener(this);
        mMutiRankRl.setOnClickListener(this);
        mPopMultiRankRl.setOnClickListener(this);
        mPopUptoDownRl.setOnClickListener(this);
        mPopDowntoUpRl.setOnClickListener(this);
        mPopPopularityRl.setOnClickListener(this);
        mContractProjectLv.setOnItemClickListener(this);
        mContractProjectGv.setOnItemClickListener(this);
    }

    private void loadData(int pageIndex, String gcId, int state) {
        switch (state) {
            case SHOP_LIST_STATE:
                String url;
                if (gcId.equals("")) {
                    url = NetConfig.shopListUrl;
                } else {
                    url = NetConfig.shopListUrl + "&gc_id=" + gcId;
                }
                mPd.show();
                Request requestShopList = new Request.Builder().url(url).get().build();
                okHttpClient.newCall(requestShopList).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String json = response.body().string();
                            mDataList.clear();
                            if (parseJson(json))
                                handler.sendEmptyMessage(1);
                        }
                    }
                });
                break;
            case CONTRACT_PROJECT_STATE:
                mPd.show();
                Request request = new Request.Builder().url(NetConfig.contractprojectUrl + pageIndex).get().build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String json = response.body().string();
                            mDataList.clear();
                            if (parseJson(json))
                                handler.sendEmptyMessage(1);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    private boolean parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            JSONArray arrDatas = objBean.optJSONArray("datas");
            for (int i = 0; i < arrDatas.length(); i++) {
                JSONObject o = arrDatas.optJSONObject(i);
                ContractCompany contractCompany = new ContractCompany();
                contractCompany.setCompanyName(o.optString("store_name"));
                contractCompany.setCompanyDuty(o.optString("store_zy"));
                contractCompany.setCompanyAddress1(o.optString("area_info"));
                contractCompany.setCompanyAddress2(o.optString("store_address"));
                contractCompany.setCompanyImgurl(o.optString("store_avatar"));
                contractCompany.setGoodsId(o.optString("goods_id"));
                mDataList.add(contractCompany);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void closeSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_contractproject_back:
                finish();
                break;
            case R.id.rl_contractproject_search:
                String searchContentStr = mEtSearchContentEt.getText().toString();
                if (TextUtils.isEmpty(searchContentStr)) {
                    ToastUtils.toast(ContractProjectActivity.this, "搜索内容不能为空!");
                } else {
                    ToastUtils.toast(ContractProjectActivity.this, "正在搜索:" + searchContentStr);
                }
                closeSoftKeyboard();
                break;
            case R.id.rl_contractProject_multiRank:
                mMultiRankPop.showAsDropDown(mMultiRankTv);
                break;
            case R.id.rl_pop_multirank_multirank:
                mMultiRankPop.dismiss();
                mMultiRankTv.setText(mPopMultiRankTv.getText().toString());
                break;
            case R.id.rl_pop_multirank_uptodown:
                mMultiRankPop.dismiss();
                mMultiRankTv.setText(mPopUptoDownTv.getText().toString());
                break;
            case R.id.rl_pop_multirank_downtoup:
                mMultiRankPop.dismiss();
                mMultiRankTv.setText(mPopDowntoUpTv.getText().toString());
                break;
            case R.id.rl_pop_multirank_popularity:
                mMultiRankPop.dismiss();
                mMultiRankTv.setText(mPopPopularityTv.getText().toString());
                break;
            case R.id.rl_contractProject_salesPriority:
                Toast.makeText(ContractProjectActivity.this, "销量优先", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_contractProject_screen:
                startActivity(new Intent(ContractProjectActivity.this, GoodsScreenActivity.class));
                break;
            case R.id.rl_contractProject_changeStyle:
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ContractProjectActivity.this, GoodsDetailActivity.class);
        intent.putExtra("goods_id", mDataList.get(position).getGoodsId());
        startActivity(intent);
    }
}
