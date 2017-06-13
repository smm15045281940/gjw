package com.gangjianwang.www.gangjianwang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout mBackRl;
    private EditText mProblemEt;
    private Button mCommitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_feedback, null);
        setContentView(rootView);
        initView();
        setListener();
    }

    private void initView() {
        mBackRl = (RelativeLayout) rootView.findViewById(R.id.rl_feedback_back);
        mProblemEt = (EditText) rootView.findViewById(R.id.et_feedback_problem);
        mCommitBtn = (Button) rootView.findViewById(R.id.btn_feedback_commit);
    }

    private void setListener() {
        mBackRl.setOnClickListener(this);
        mCommitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_feedback_back:
                finish();
                break;
            case R.id.btn_feedback_commit:
                Toast.makeText(FeedbackActivity.this, mProblemEt.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
