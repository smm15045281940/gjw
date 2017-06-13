package fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gangjianwang.www.gangjianwang.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import config.PersonConfig;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText mUsernameEt, mSetpwdEt, mSurepwdEt, mEmailEt;
    private CheckBox mRegisterAgreeCb;
    private Button mRegisterBtn;
    private GradientDrawable mRegisterBtnGd;
    private int mUsernameLength, mSetpwdLength, mSurepwdLength, mEmailLength;
    private boolean isAgree, judgeResult;

    Handler isornotRegisterHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == 666) {
                    mRegisterBtn.setEnabled(true);
                    mRegisterBtnGd.setColor(PersonConfig.loginBtnChooseColor);
                } else if (msg.what == 777) {
                    mRegisterBtn.setEnabled(false);
                    mRegisterBtnGd.setColor(PersonConfig.loginBtnDefaultColor);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View registerFragmentView = inflater.inflate(R.layout.fragment_register, null);
        registerFragmentView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initView(registerFragmentView);
        setListener();
        return registerFragmentView;
    }

    private void initView(View view) {
        mUsernameEt = (EditText) view.findViewById(R.id.et_register_username);
        mSetpwdEt = (EditText) view.findViewById(R.id.et_register_setpwd);
        mSurepwdEt = (EditText) view.findViewById(R.id.et_register_surepwd);
        mEmailEt = (EditText) view.findViewById(R.id.et_register_email);
        mRegisterAgreeCb = (CheckBox) view.findViewById(R.id.cb_register_agree);
        mRegisterBtn = (Button) view.findViewById(R.id.btn_register);
        mRegisterBtn.setEnabled(false);
        mRegisterBtnGd = (GradientDrawable) mRegisterBtn.getBackground();
        mRegisterBtnGd.setColor(PersonConfig.loginBtnDefaultColor);
    }

    private void setListener() {
        mUsernameEt.addTextChangedListener(mUsernameTextWatcher);
        mSetpwdEt.addTextChangedListener(mSetpwdTextWatcher);
        mSurepwdEt.addTextChangedListener(mSurepwdTextWatcher);
        mEmailEt.addTextChangedListener(mEmailTextWatcher);
        mRegisterBtn.setOnClickListener(this);
        mRegisterAgreeCb.setOnCheckedChangeListener(this);
    }

    private void sendHandler() {
        if (mUsernameLength != 0 && mSetpwdLength != 0 && mSurepwdLength != 0 && mEmailLength != 0 && isAgree) {
            isornotRegisterHandler.sendEmptyMessage(666);
        } else {
            isornotRegisterHandler.sendEmptyMessage(777);
        }
    }

    private boolean toJudge() {
        if (!mSetpwdEt.getText().toString().equals(mSurepwdEt.getText().toString())) {
            Toast.makeText(getActivity(), "两次密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean b = judgeEmail();
        if (b) {
            return true;
        } else {
            Toast.makeText(getActivity(), "邮箱格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isRightPwd(String s) {
        String str = "^[a-zA-Z0-9]+$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(s);
        return m.matches();
    }

    private boolean judgeEmail() {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(mEmailEt.getText().toString());
        return m.matches();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                boolean b = isRightPwd(mSetpwdEt.getText().toString());
                if (b) {
                    judgeResult = toJudge();
                    if (judgeResult) {
                        Toast.makeText(getActivity(), "正在向服务器传递注册信息", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "密码必须为字母或数字或其组合!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    TextWatcher mUsernameTextWatcher = new TextWatcher() {

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
            mUsernameLength = temp.length();
            sendHandler();
        }
    };

    TextWatcher mSetpwdTextWatcher = new TextWatcher() {

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
            mSetpwdLength = temp.length();
            sendHandler();
        }
    };

    TextWatcher mSurepwdTextWatcher = new TextWatcher() {

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
            mSurepwdLength = temp.length();
            sendHandler();
        }
    };

    TextWatcher mEmailTextWatcher = new TextWatcher() {

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
            mEmailLength = temp.length();
            sendHandler();
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isAgree = isChecked;
        sendHandler();
    }
}
