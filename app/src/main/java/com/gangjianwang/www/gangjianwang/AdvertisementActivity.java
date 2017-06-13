package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class AdvertisementActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mBackIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_advertisement);
        initView();
        setListener();
    }

    private void initView() {
        mBackIv = (ImageView) findViewById(R.id.iv_advertisement_back);
    }

    private void setListener() {
        mBackIv.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_advertisement_back:
                startActivity(new Intent(this,HomeActivity.class));
                finish();
                break;
            default:
                break;
        }
    }
}
