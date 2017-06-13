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

public class ForgetpasswordFragment extends Fragment implements View.OnClickListener {

    private EditText mPhonenumberEt, mSecuritycodeEt, mNewpwdEt, mSurenewpwdEt;
    private int mPhonenumberLength, mSecuritycodeLength, mNewpwdLength, mSurenewpwdLength;
    private Button mGettestcodeBtn, mSureBtn;
    private GradientDrawable mGettestcodeBtnGd, mSureBtnGd;
    private boolean judgeResult;
    private int SEC = 3;

    Handler isornotGetcodeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == 444) {
                    mGettestcodeBtn.setEnabled(true);
                    mGettestcodeBtnGd.setColor(PersonConfig.loginBtnChooseColor);
                } else if (msg.what == 555) {
                    mGettestcodeBtn.setEnabled(false);
                    mGettestcodeBtnGd.setColor(PersonConfig.loginBtnDefaultColor);
                }
            }
        }
    };

    Handler isornotSureHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == 666) {
                    mSureBtn.setEnabled(true);
                    mSureBtnGd.setColor(PersonConfig.loginBtnChooseColor);
                } else if (msg.what == 777) {
                    mSureBtn.setEnabled(false);
                    mSureBtnGd.setColor(PersonConfig.loginBtnDefaultColor);
                }
            }
        }
    };


    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mGettestcodeBtn.setText(SEC + "秒重新获取");
            SEC--;
            if (SEC == -2) {
                Message message = new Message();
                message.what = 1;
                handlerStop.sendMessage(message);
            }
            handler.postDelayed(this, 1000);
        }
    };

    final Handler handlerStop = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    handler.removeCallbacks(runnable);
                    mGettestcodeBtn.setEnabled(true);
                    mGettestcodeBtnGd.setColor(PersonConfig.loginBtnChooseColor);
                    mGettestcodeBtn.setText("获取验证码");
                    SEC = 3;
                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View forgetpasswordFragmentView = inflater.inflate(R.layout.fragment_forgetpassword, null);
        forgetpasswordFragmentView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initView(forgetpasswordFragmentView);
        setListener();
        return forgetpasswordFragmentView;
    }

    private void initView(View view) {
        mPhonenumberEt = (EditText) view.findViewById(R.id.et_forgetpassword_phonenumber);
        mSecuritycodeEt = (EditText) view.findViewById(R.id.et_forgetpassword_securitycode);
        mNewpwdEt = (EditText) view.findViewById(R.id.et_forgetpassword_newpwd);
        mSurenewpwdEt = (EditText) view.findViewById(R.id.et_forgetpassword_surenewpwd);
        mGettestcodeBtn = (Button) view.findViewById(R.id.btn_gettestcode);
        mGettestcodeBtn.setEnabled(false);
        mGettestcodeBtnGd = (GradientDrawable) mGettestcodeBtn.getBackground();
        mGettestcodeBtnGd.setColor(PersonConfig.loginBtnDefaultColor);
        mSureBtn = (Button) view.findViewById(R.id.btn_forgetpassword_sure);
        mSureBtn.setEnabled(false);
        mSureBtnGd = (GradientDrawable) mSureBtn.getBackground();
        mSureBtnGd.setColor(PersonConfig.loginBtnDefaultColor);
    }

    private void setListener() {
        mPhonenumberEt.addTextChangedListener(mPhonenumberTextWatcher);
        mSecuritycodeEt.addTextChangedListener(mSecuritycodeTextWatcher);
        mNewpwdEt.addTextChangedListener(mNewpwdTextWatcher);
        mSurenewpwdEt.addTextChangedListener(mSurenewpwdTextWatcher);
        mGettestcodeBtn.setOnClickListener(this);
        mSureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_gettestcode:
                Toast.makeText(getActivity(), "正在获取验证码", Toast.LENGTH_SHORT).show();
                mGettestcodeBtn.setEnabled(false);
                mGettestcodeBtnGd.setColor(PersonConfig.loginBtnDefaultColor);
                handler.postDelayed(runnable, 1000);
                break;
            case R.id.btn_forgetpassword_sure:
                judgeResult = toJudge();
                if (judgeResult) {
                    Toast.makeText(getActivity(), "正在将你的信息上传至服务器", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private boolean toJudge() {
        boolean b = isPhonenumber();
        boolean c = isPwd();
        if (b) {
            if (c) {
                if (!mNewpwdEt.getText().toString().equals(mSurenewpwdEt.getText().toString())) {
                    Toast.makeText(getActivity(), "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(getActivity(), "密码必须为字母或数字或其组合!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(getActivity(), "手机格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isPhonenumber() {
        String str = "^(13[0-9]|14[57]|15[0-35-9]|17[6-8]|18[0-9])[0-9]{8}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(mPhonenumberEt.getText().toString());
        return m.matches();
    }

    private boolean isPwd() {
        String str = "^[a-zA-Z0-9]+$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(mNewpwdEt.getText().toString());
        return m.matches();
    }

    private void cangetTestcode() {
        if (mPhonenumberLength == 11) {
            boolean b = isPhonenumber();
            if (b) {
                isornotGetcodeHandler.sendEmptyMessage(444);
            }
        } else {
            isornotGetcodeHandler.sendEmptyMessage(555);
        }
    }

    private void sendHandler() {
        if (mPhonenumberLength != 0 && mSecuritycodeLength != 0 && mNewpwdLength != 0 && mSurenewpwdLength != 0) {
            isornotSureHandler.sendEmptyMessage(666);
        } else {
            isornotSureHandler.sendEmptyMessage(777);
        }
    }

    TextWatcher mPhonenumberTextWatcher = new TextWatcher() {

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
            mPhonenumberLength = temp.length();
            cangetTestcode();
            sendHandler();
        }
    };

    TextWatcher mSecuritycodeTextWatcher = new TextWatcher() {

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
            mSecuritycodeLength = temp.length();
            sendHandler();
        }
    };

    TextWatcher mNewpwdTextWatcher = new TextWatcher() {

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
            mNewpwdLength = temp.length();
            sendHandler();
        }
    };

    TextWatcher mSurenewpwdTextWatcher = new TextWatcher() {

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
            mSurenewpwdLength = temp.length();
            sendHandler();
        }
    };

}
