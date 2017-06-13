package fragment;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.LoginActivity;
import com.gangjianwang.www.gangjianwang.R;

import config.PersonConfig;
import utils.ToastUtils;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private TextView mForgetpwdTv;
    private EditText mUsernameEt, mPasswordEt;
    private int userNameLength, passwordLength;
    private CheckBox mAutoLoginCb;
    private Button mLoginBtn;
    private GradientDrawable mLoginBtnGd;
    private Handler mForgetpwdHandler;

    Handler canButtonHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        mLoginBtn.setEnabled(false);
                        mLoginBtnGd.setColor(PersonConfig.loginBtnDefaultColor);
                        break;
                    case 1:
                        mLoginBtn.setEnabled(true);
                        mLoginBtnGd.setColor(PersonConfig.loginBtnChooseColor);
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginActivity loginActivity = (LoginActivity) getActivity();
        mForgetpwdHandler = loginActivity.forgetpwdHandler;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, null);
        rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initView();
        setListener();
        return rootView;
    }

    private void initView() {
        mForgetpwdTv = (TextView) rootView.findViewById(R.id.tv_login_forgetpassword);
        mUsernameEt = (EditText) rootView.findViewById(R.id.et_login_username);
        mPasswordEt = (EditText) rootView.findViewById(R.id.et_login_password);
        mAutoLoginCb = (CheckBox) rootView.findViewById(R.id.cb_sevenday_autologin);
        mLoginBtn = (Button) rootView.findViewById(R.id.btn_login_login);
        mLoginBtn.setEnabled(false);
        mLoginBtnGd = (GradientDrawable) mLoginBtn.getBackground();
        mLoginBtnGd.setColor(PersonConfig.loginBtnDefaultColor);
    }

    private void setListener() {
        mUsernameEt.addTextChangedListener(userNameWatcher);
        mPasswordEt.addTextChangedListener(passWordWatcher);
        mForgetpwdTv.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_forgetpassword:
                mForgetpwdHandler.sendEmptyMessage(100);
                break;
            case R.id.btn_login_login:
                loginJudge();
                break;
            default:
                break;
        }
    }

    private void loginJudge() {
        if (TextUtils.isEmpty(mUsernameEt.getText().toString())) {
            ToastUtils.toast(getActivity(), "请输入用户名!");
            return;
        } else if (TextUtils.isEmpty(mPasswordEt.getText().toString())) {
            ToastUtils.toast(getActivity(), "请输入密码!");
            return;
        } else {
            ToastUtils.toast(getActivity(), "用户名:" + mUsernameEt.getText().toString() + "\n" + "密码:" + mPasswordEt.getText().toString() + "\n" + "七天自动登陆:" + mAutoLoginCb.isChecked() + "\n" + "正在登陆...");
        }
    }

    TextWatcher userNameWatcher = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void afterTextChanged(Editable s) {
            userNameLength = temp.length();
            sendHandler();
        }
    };

    TextWatcher passWordWatcher = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void afterTextChanged(Editable s) {
            passwordLength = temp.length();
            sendHandler();
        }
    };

    private void sendHandler() {
        if (userNameLength != 0 && passwordLength != 0) {
            canButtonHandler.sendEmptyMessage(1);
        } else {
            canButtonHandler.sendEmptyMessage(0);
        }
    }
}
