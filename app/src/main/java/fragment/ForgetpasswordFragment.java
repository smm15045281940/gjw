package fragment;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gangjianwang.www.gangjianwang.R;

import utils.ToastUtils;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class ForgetpasswordFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private EditText mPhonenumberEt, mSecuritycodeEt, mNewpwdEt, mSurenewpwdEt;
    private Button mGettestcodeBtn;
    private GradientDrawable mGettestcodeBtnGd;
    private RelativeLayout mSureRl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_forgetpassword, null);
        rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initView();
        setListener();
        return rootView;
    }

    private void initView() {
        mPhonenumberEt = (EditText) rootView.findViewById(R.id.et_forgetpassword_phonenumber);
        mSecuritycodeEt = (EditText) rootView.findViewById(R.id.et_forgetpassword_securitycode);
        mNewpwdEt = (EditText) rootView.findViewById(R.id.et_forgetpassword_newpwd);
        mSurenewpwdEt = (EditText) rootView.findViewById(R.id.et_forgetpassword_surenewpwd);
        mGettestcodeBtn = (Button) rootView.findViewById(R.id.btn_gettestcode);
        mSureRl = (RelativeLayout) rootView.findViewById(R.id.rl_forgetpwd_sure);
    }

    private void setListener() {
        mGettestcodeBtn.setOnClickListener(this);
        mSureRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_gettestcode:
                ToastUtils.toast(getActivity(), "获取验证码");
                break;
            case R.id.rl_forgetpwd_sure:
                ToastUtils.toast(getActivity(), "确认");
                break;
            default:
                break;
        }
    }

}
