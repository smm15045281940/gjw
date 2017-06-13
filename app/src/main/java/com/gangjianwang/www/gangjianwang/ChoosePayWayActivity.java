package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChoosePayWayActivity extends AppCompatActivity implements View.OnClickListener{

    private View rootView;
    private RelativeLayout backRl,cashOndeliveryRl;
    private TextView cashOndeliveryTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(ChoosePayWayActivity.this,R.layout.activity_choose_pay_way,null);
        setContentView(rootView);
        initView(rootView);
        setListener();
    }

    private void initView(View rootView) {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_choosepayway_back);
        cashOndeliveryRl = (RelativeLayout) rootView.findViewById(R.id.rl_choosepayway_cashondelivery);
        cashOndeliveryTv = (TextView) rootView.findViewById(R.id.tv_choosepayway_cashondelivery);
    }

    private void setListener(){
        backRl.setOnClickListener(ChoosePayWayActivity.this);
        cashOndeliveryRl.setOnClickListener(ChoosePayWayActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_choosepayway_back:
                finish();
                break;
            case R.id.rl_choosepayway_cashondelivery:
                Intent intent = new Intent();
                intent.putExtra("payWay",cashOndeliveryTv.getText().toString());
                setResult(1,intent);
                finish();
                break;
            default:
                break;
        }
    }
}
