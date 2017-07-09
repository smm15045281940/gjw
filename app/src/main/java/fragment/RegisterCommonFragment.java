package fragment;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.AgreeMentActivity;
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
import utils.RegularUtils;
import utils.ToastUtils;
import utils.UserUtils;

/**
 * Created by Administrator on 2017/6/30.
 */

public class RegisterCommonFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private EditText nameEt, pwdEt, surePwdEt, emailEt;
    private CheckBox autoCb;
    private RelativeLayout registerRl;
    private OkHttpClient okHttpClient;
    private String name, pwd, surePwd, email, client = "wap";
    private ProgressDialog progressDialog;
    private String json;
    private TextView agreeTv;
    private String getUserName, getKey;
    private int getUserId;
    private boolean autoLogin;
    private String hintResult;
    private UserInfo userInfo;
    private Handler loginHandler;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        progressDialog.dismiss();
                        ToastUtils.toast(getActivity(), ParaConfig.NETWORK_ERROR);
                        break;
                    case 1:
                        progressDialog.dismiss();
                        ToastUtils.toast(getActivity(), hintResult);
                        break;
                    case 2:
                        UserUtils.writeLogin(getActivity(), userInfo);
                        loginHandler.sendEmptyMessage(1);
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
        loginHandler = loginActivity.loginHandler;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register_common, null);
        initView();
        initData();
        setListener();
        return rootView;
    }

    private void initView() {
        initRoot();
    }

    private void initRoot() {
        nameEt = (EditText) rootView.findViewById(R.id.et_register_common_username);
        pwdEt = (EditText) rootView.findViewById(R.id.et_register_common_setpwd);
        surePwdEt = (EditText) rootView.findViewById(R.id.et_register_common_surepwd);
        emailEt = (EditText) rootView.findViewById(R.id.et_register_common_email);
        autoCb = (CheckBox) rootView.findViewById(R.id.cb_register_common_agree);
        agreeTv = (TextView) rootView.findViewById(R.id.tv_register_common_agreement);
        registerRl = (RelativeLayout) rootView.findViewById(R.id.rl_register_common_register);
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void setListener() {
        agreeTv.setOnClickListener(this);
        registerRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register_common_agreement:
                startActivity(new Intent(getActivity(), AgreeMentActivity.class));
                break;
            case R.id.rl_register_common_register:
                judge();
                break;
            default:
                break;
        }
    }

    private void judge() {
        name = nameEt.getText().toString();
        pwd = pwdEt.getText().toString();
        surePwd = surePwdEt.getText().toString();
        email = emailEt.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.toast(getActivity(), "用户名不能为空!");
        } else if (name.length() < 6 || name.length() > 20) {
            ToastUtils.toast(getActivity(), "用户名必须为6-20个字符!");
        } else if (TextUtils.isEmpty(pwd)) {
            ToastUtils.toast(getActivity(), "密码不能为空!");
        } else if (pwd.length() < 6 || pwd.length() > 20) {
            ToastUtils.toast(getActivity(), "密码必须为6-20位!");
        } else if (TextUtils.isEmpty(surePwd)) {
            ToastUtils.toast(getActivity(), "确认密码不能为空!");
        } else if (!surePwd.equals(pwd)) {
            ToastUtils.toast(getActivity(), "两次密码不一致!");
        } else if (!TextUtils.isEmpty(email) && !RegularUtils.isEmail(email)) {
            ToastUtils.toast(getActivity(), "邮箱格式不正确!");
        } else {
            register();
        }
    }

    private void register() {
        progressDialog.show();
        RequestBody body = new FormEncodingBuilder()
                .add("username", name)
                .add("password", pwd)
                .add("password_confirm", surePwd)
                .add("email", email)
                .add("client", client)
                .build();
        Request request = new Request.Builder()
                .url(NetConfig.registerUrl)
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
                    json = response.body().string();
                    parseJson(json);
                }
            }
        });
    }

    private void parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            String code = objBean.optString("code");
            JSONObject objDatas = objBean.optJSONObject("datas");
            if (code.equals("200")) {
                getUserName = objDatas.optString("username");
                getUserId = objDatas.optInt("userid");
                getKey = objDatas.optString("key");
                hintResult = "注册成功";
                getUserData();
            } else if (code.equals("400")) {
                hintResult = objDatas.optString("error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(1);
    }

    private void getUserData() {
        RequestBody body = new FormEncodingBuilder()
                .add("key", getKey)
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
            userInfo.setKey(getKey);
            userInfo.setLevelName(objInfo.optString("level_name"));
            userInfo.setFavoritesStore(objInfo.optString("favorites_store"));
            userInfo.setFavoritersGoods(objInfo.optString("favorites_goods"));
            userInfo.setAutoLogin(autoLogin);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
