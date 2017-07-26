package fragment;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.HomeActivity;
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
import config.ParaConfig;
import utils.ToastUtils;
import utils.UserUtils;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {


    private View rootView;
    private TextView mForgetpwdTv;
    private EditText mUsernameEt, mPasswordEt;
    private ImageView mClearUsernameIv, mClearPasswordIv;
    private CheckBox mAutoLoginCb;
    private RelativeLayout mLoginRl;

    private Handler mForgetpwdHandler, mLoginHandler;
    private String username, password, client, key, defeatHint;
    private UserInfo userInfo;
    private OkHttpClient okHttpClient;
    private ProgressDialog progressDialog;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case 0:
                        if (TextUtils.isEmpty(defeatHint)) {
                            ToastUtils.toast(getActivity(), ParaConfig.NETWORK_ERROR);
                        } else {
                            ToastUtils.toast(getActivity(), defeatHint);
                        }
                        break;
                    case 1:
                        getUserData();
                        break;
                    case 2:
                        UserUtils.writeLogin(getActivity(), userInfo);
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        intent.putExtra("what", 4);
                        startActivity(intent);
                        break;
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
        handler.removeMessages(1);
        handler.removeMessages(2);
    }

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
        initView();
        initData();
        setListener();
        return rootView;
    }

    private void initView() {
        initRoot();
        initProgress();
    }

    private void initRoot() {
        rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mForgetpwdTv = (TextView) rootView.findViewById(R.id.tv_login_forgetpassword);
        mUsernameEt = (EditText) rootView.findViewById(R.id.et_login_username);
        mPasswordEt = (EditText) rootView.findViewById(R.id.et_login_password);
        mClearUsernameIv = (ImageView) rootView.findViewById(R.id.iv_login_username_clear);
        mClearPasswordIv = (ImageView) rootView.findViewById(R.id.iv_login_password_clear);
        mAutoLoginCb = (CheckBox) rootView.findViewById(R.id.cb_sevenday_autologin);
        mLoginRl = (RelativeLayout) rootView.findViewById(R.id.rl_login_login);
    }

    private void initProgress() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
    }

    private void setListener() {
        mForgetpwdTv.setOnClickListener(this);
        mLoginRl.setOnClickListener(this);
        mUsernameEt.addTextChangedListener(userNameTw);
        mPasswordEt.addTextChangedListener(passwordTw);
        mClearUsernameIv.setOnClickListener(this);
        mClearPasswordIv.setOnClickListener(this);
    }

    TextWatcher userNameTw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                mClearUsernameIv.setVisibility(View.INVISIBLE);
            } else {
                mClearUsernameIv.setVisibility(View.VISIBLE);
            }
        }
    };

    TextWatcher passwordTw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                mClearPasswordIv.setVisibility(View.INVISIBLE);
            } else {
                mClearPasswordIv.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_login_username_clear:
                mUsernameEt.setText("");
                break;
            case R.id.iv_login_password_clear:
                mPasswordEt.setText("");
                break;
            case R.id.tv_login_forgetpassword:
                mForgetpwdHandler.sendEmptyMessage(100);
                break;
            case R.id.rl_login_login:
                if (judge()) {
                    login();
                }
                break;
            default:
                break;
        }
    }

    private boolean judge() {
        if (TextUtils.isEmpty(mUsernameEt.getText().toString())) {
            ToastUtils.toast(getActivity(), "请输入用户名!");
            return false;
        } else if (TextUtils.isEmpty(mPasswordEt.getText().toString())) {
            ToastUtils.toast(getActivity(), "请输入密码!");
            return false;
        } else {
            username = mUsernameEt.getText().toString();
            password = mPasswordEt.getText().toString();
            client = "wap";
            return true;
        }
    }

    private void login() {
        progressDialog.show();
        RequestBody body = new FormEncodingBuilder().add("username", username).add("password", password).add("client", client).build();
        Request request = new Request.Builder().url(NetConfig.loginUrl).post(body).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful() && parseJson(response.body().string())) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    private boolean parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            if (objBean.optInt("code") == 200) {
                JSONObject objDatas = objBean.optJSONObject("datas");
                key = objDatas.optString("key");
                return true;
            } else if (objBean.optInt("code") == 400) {
                JSONObject objDatas = objBean.optJSONObject("datas");
                defeatHint = objDatas.optString("error");
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void getUserData() {
        RequestBody body = new FormEncodingBuilder().add("key", key).build();
        Request request = new Request.Builder().url(NetConfig.loginAfterUrl).post(body).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful() && parseUserData(response.body().string())) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(0);
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
            userInfo.setAutoLogin(mAutoLoginCb.isChecked());
            userInfo.setKey(key);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
