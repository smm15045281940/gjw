package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import adapter.GuidePagerAdapter;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewPager mVp;
    private View guideFirstView,guideSecondView,guideThirdView;
    private List<View> mViewList;
    private GuidePagerAdapter mGuidePagerAdapter;
    private Button mJumpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        initView();
        bindListener();
    }

    private void initView(){
        mVp = (ViewPager) findViewById(R.id.vp_guide);
        guideFirstView = View.inflate(GuideActivity.this,R.layout.guide_first,null);
        guideSecondView = View.inflate(GuideActivity.this,R.layout.guide_second,null);
        guideThirdView = View.inflate(GuideActivity.this,R.layout.guide_third,null);
        mViewList = new ArrayList<>();
        mViewList.add(guideFirstView);
        mViewList.add(guideSecondView);
        mViewList.add(guideThirdView);
        mGuidePagerAdapter = new GuidePagerAdapter(mViewList);
        mVp.setAdapter(mGuidePagerAdapter);
        mVp.setCurrentItem(0);
        mJumpBtn = (Button) guideThirdView.findViewById(R.id.btn_jump);
    }

    private void bindListener(){
        mJumpBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_jump:
                startActivity(new Intent(GuideActivity.this,HomeActivity.class));
                finish();
                break;
            default:
                break;
        }
    }
}
