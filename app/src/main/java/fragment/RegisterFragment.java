package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.AgreeMentActivity;
import com.gangjianwang.www.gangjianwang.R;

import utils.ToastUtils;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private EditText mUsernameEt, mSetpwdEt, mSurepwdEt, mEmailEt;
    private CheckBox mRegisterAgreeCb;
    private TextView mAgreeMentTv;
    private RelativeLayout mRegisterRl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register, null);
        rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initView();
        setListener();
        return rootView;
    }

    private void initView() {
        mUsernameEt = (EditText) rootView.findViewById(R.id.et_register_username);
        mSetpwdEt = (EditText) rootView.findViewById(R.id.et_register_setpwd);
        mSurepwdEt = (EditText) rootView.findViewById(R.id.et_register_surepwd);
        mEmailEt = (EditText) rootView.findViewById(R.id.et_register_email);
        mRegisterAgreeCb = (CheckBox) rootView.findViewById(R.id.cb_register_agree);
        mAgreeMentTv = (TextView) rootView.findViewById(R.id.tv_register_agreement);
        mRegisterRl = (RelativeLayout) rootView.findViewById(R.id.rl_register_register);
    }

    private void setListener() {
        mAgreeMentTv.setOnClickListener(this);
        mRegisterRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register_agreement:
                startActivity(new Intent(getActivity(), AgreeMentActivity.class));
                break;
            case R.id.rl_register_register:
                ToastUtils.toast(getActivity(), "注册");
                break;
            default:
                break;
        }
    }
}
