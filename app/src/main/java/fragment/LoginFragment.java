package fragment;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.LoginActivity;
import com.gangjianwang.www.gangjianwang.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import bean.UserInfo;
import config.NetConfig;
import utils.ToastUtils;
import utils.UserUtils;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {

    private String TAG = LoginFragment.class.getSimpleName();

    private View rootView;
    private TextView mForgetpwdTv;
    private EditText mUsernameEt, mPasswordEt;
    private CheckBox mAutoLoginCb;
    private RelativeLayout mLoginRl;
    private GradientDrawable mLoginBtnGd;
    private Handler mForgetpwdHandler;
    private Handler mLoginHandler;
    private String username;
    private String password;
    private String client;
    private String key;
    private UserInfo userInfo;
    private String s;

    private OkHttpClient okHttpClient = new OkHttpClient();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        ToastUtils.toast(getActivity(), "无网络");
                        break;
                    case 1:
                        getUserData();
                        break;
                    case 2:
                        UserUtils.writeLogin(getActivity(), userInfo);
                        mLoginHandler.sendEmptyMessage(1);
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
        mLoginHandler = loginActivity.loginHandler;
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
        mLoginRl = (RelativeLayout) rootView.findViewById(R.id.rl_login_login);
    }

    private void setListener() {
        mForgetpwdTv.setOnClickListener(this);
        mLoginRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_forgetpassword:
                mForgetpwdHandler.sendEmptyMessage(100);
                break;
            case R.id.rl_login_login:
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
            username = mUsernameEt.getText().toString();
            password = mPasswordEt.getText().toString();
            client = "wap";
            if (mAutoLoginCb.isChecked()) {
                s = "1";
                ToastUtils.log(getActivity(), "七天自动登录");
            } else {
                s = "0";
                ToastUtils.log(getActivity(), "七天不自动登录");
            }
            toLogin();
        }
    }

    private void toLogin() {
        RequestBody body = new FormEncodingBuilder()
                .add("username", username)
                .add("password", password)
                .add("client", client)
                .build();
        Request request = new Request.Builder()
                .url(NetConfig.loginUrl)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    if (parseJson(result))
                        handler.sendEmptyMessage(1);
                }
            }
        });
    }

    private boolean parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            JSONObject objDatas = objBean.optJSONObject("datas");
            key = objDatas.optString("key");
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void getUserData() {
        RequestBody body = new FormEncodingBuilder()
                .add("key", key)
                .build();
        Request request = new Request.Builder()
                .url(NetConfig.loginAfterUrl)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    if (parseUserData(result))
                        handler.sendEmptyMessage(2);
                }
            }
        });
    }

    private boolean parseUserData(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            JSONObject objDatas = objBean.optJSONObject("datas");
            JSONObject objInfo = objDatas.optJSONObject("member_info");
            userInfo = new UserInfo();
            userInfo.setUserName(objInfo.optString("user_name"));
            userInfo.setAvatar(objInfo.optString("avatar"));
            userInfo.setLevelName(objInfo.optString("level_name"));
            userInfo.setFavoritesStore(objInfo.optString("favorites_store"));
            userInfo.setFavoritersGoods(objInfo.optString("favorites_goods"));
            userInfo.setAutoLogin(s);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
