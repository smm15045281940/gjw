package com.gangjianwang.www.gangjianwang;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import config.ParaConfig;

public class AccountBalanceActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView, emptyView;
    private RelativeLayout mBackRl;
    private LinearLayout accountBalanceLl, rechargeDetailLl, accountCashLl;
    private TextView accountBalanceTv, rechargeDetailTv, accountCashTv;
    private View accountBalanceV, rechargeDetailV, accountCashV;
    private ListView lv;
    private int curPosition = -1;
    private ProgressDialog progressDialog;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_account_balance, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
        loadData(0);
    }

    private void initView() {
        initRoot();
        initEmpty();
    }

    private void initRoot() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_accountbalance_back);
        accountBalanceLl = (LinearLayout) rootView.findViewById(R.id.ll_account_balance);
        rechargeDetailLl = (LinearLayout) rootView.findViewById(R.id.ll_recharge_detail);
        accountCashLl = (LinearLayout) rootView.findViewById(R.id.ll_account_cash);
        accountBalanceTv = (TextView) rootView.findViewById(R.id.tv_account_balance);
        rechargeDetailTv = (TextView) rootView.findViewById(R.id.tv_recharge_detail);
        accountCashTv = (TextView) rootView.findViewById(R.id.tv_account_cash);
        accountBalanceV = rootView.findViewById(R.id.v_account_balance);
        rechargeDetailV = rootView.findViewById(R.id.v_recharge_detail);
        accountCashV = rootView.findViewById(R.id.v_account_cash);
        lv = (ListView) rootView.findViewById(R.id.lv_account_balance);
    }

    private void initEmpty() {
        emptyView = View.inflate(this, R.layout.empty, null);
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ViewGroup) lv.getParent()).addView(emptyView);
        emptyView.setVisibility(View.GONE);
        lv.setEmptyView(emptyView);
    }

    private void initData() {
        progressDialog = new ProgressDialog(this);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        accountBalanceLl.setOnClickListener(this);
        rechargeDetailLl.setOnClickListener(this);
        accountCashLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_accountbalance_back:
                finish();
                break;
            case R.id.ll_account_balance:
                loadData(0);
                break;
            case R.id.ll_recharge_detail:
                loadData(1);
                break;
            case R.id.ll_account_cash:
                loadData(2);
                break;
            default:
                break;
        }
    }

    private void loadData(int tarPosition) {
        if (tarPosition != curPosition) {
            emptyView.setVisibility(View.INVISIBLE);
            progressDialog.show();
            switch (tarPosition) {
                case 0:
                    accountBalanceTv.setTextColor(Color.RED);
                    rechargeDetailTv.setTextColor(Color.BLACK);
                    accountCashTv.setTextColor(Color.BLACK);
                    accountBalanceV.setVisibility(View.VISIBLE);
                    rechargeDetailV.setVisibility(View.INVISIBLE);
                    accountCashV.setVisibility(View.INVISIBLE);
                    ((ImageView) emptyView.findViewById(R.id.iv_empty_icon)).setImageResource(ParaConfig.ACCOUNT_BALANCE_ICON);
                    ((TextView) emptyView.findViewById(R.id.tv_empty_hint)).setText(ParaConfig.ACCOUNT_BALANCE_HINT);
                    ((TextView) emptyView.findViewById(R.id.tv_empty_content)).setText(ParaConfig.ACCOUNT_BALANCE_CONTENT);
                    break;
                case 1:
                    accountBalanceTv.setTextColor(Color.BLACK);
                    rechargeDetailTv.setTextColor(Color.RED);
                    accountCashTv.setTextColor(Color.BLACK);
                    accountBalanceV.setVisibility(View.INVISIBLE);
                    rechargeDetailV.setVisibility(View.VISIBLE);
                    accountCashV.setVisibility(View.INVISIBLE);
                    ((ImageView) emptyView.findViewById(R.id.iv_empty_icon)).setImageResource(ParaConfig.RECHARGE_DETAIL_ICON);
                    ((TextView) emptyView.findViewById(R.id.tv_empty_hint)).setText(ParaConfig.RECHARGE_DETAIL_HINT);
                    ((TextView) emptyView.findViewById(R.id.tv_empty_content)).setText(ParaConfig.RECHARGE_DETAIL_CONTENT);
                    break;
                case 2:
                    accountBalanceTv.setTextColor(Color.BLACK);
                    rechargeDetailTv.setTextColor(Color.BLACK);
                    accountCashTv.setTextColor(Color.RED);
                    accountBalanceV.setVisibility(View.INVISIBLE);
                    rechargeDetailV.setVisibility(View.INVISIBLE);
                    accountCashV.setVisibility(View.VISIBLE);
                    ((ImageView) emptyView.findViewById(R.id.iv_empty_icon)).setImageResource(ParaConfig.ACCOUNT_CASH_ICON);
                    ((TextView) emptyView.findViewById(R.id.tv_empty_hint)).setText(ParaConfig.ACCOUNT_CASH_HINT);
                    ((TextView) emptyView.findViewById(R.id.tv_empty_content)).setText(ParaConfig.ACCOUNT_CASH_CONTENT);
                    break;
                default:
                    break;
            }
            curPosition = tarPosition;
            handler.sendEmptyMessageDelayed(1, 500);
        }
    }
}
