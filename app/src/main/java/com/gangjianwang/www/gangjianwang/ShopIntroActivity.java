package com.gangjianwang.www.gangjianwang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShopIntroActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout backRl, collectRl;
    private TextView collectTv, fanTv;
    private final int YES_COLLECT_STATE = 1;
    private final int NO_COLLECT_STATE = 2;
    private int COLLECT_STATE = NO_COLLECT_STATE;
    private int fanCount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_shop_intro, null);
        setContentView(rootView);
        initView();
        setListener();
    }

    private void initView() {
        backRl = (RelativeLayout) rootView.findViewById(R.id.rl_shopintro_back);
        collectRl = (RelativeLayout) rootView.findViewById(R.id.rl_shopintro_collect);
        collectTv = (TextView) rootView.findViewById(R.id.tv_shopintro_collect);
        fanTv = (TextView) rootView.findViewById(R.id.tv_shopintro_fan);
    }

    private void setListener() {
        backRl.setOnClickListener(this);
        collectRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_shopintro_back:
                finish();
                break;
            case R.id.rl_shopintro_collect:
                switch (COLLECT_STATE) {
                    case YES_COLLECT_STATE:
                        collectTv.setText("收藏");
                        COLLECT_STATE = NO_COLLECT_STATE;
                        fanCount--;
                        fanTv.setText(fanCount + "粉丝");
                        break;
                    case NO_COLLECT_STATE:
                        collectTv.setText("已收藏");
                        COLLECT_STATE = YES_COLLECT_STATE;
                        fanCount++;
                        fanTv.setText(fanCount + "粉丝");
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
