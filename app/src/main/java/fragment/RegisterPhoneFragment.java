package fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.AgreeMentActivity;
import com.gangjianwang.www.gangjianwang.R;

import utils.CodeUtils;
import utils.RegularUtils;
import utils.ToastUtils;

/**
 * Created by Administrator on 2017/6/30.
 */

public class RegisterPhoneFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private EditText photoNumberEt, photoCodeEt;
    private ImageView photoCodeIv;
    private RelativeLayout getCodeRl;
    private TextView agreeTv;
    private CodeUtils codeUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register_phone, null);
        initView();
        initData();
        setListener();
        return rootView;
    }

    private void initView() {
        initRoot();
    }

    private void initRoot() {
        photoNumberEt = (EditText) rootView.findViewById(R.id.et_register_phone_photonumber);
        photoCodeEt = (EditText) rootView.findViewById(R.id.et_register_phone_photocode);
        photoCodeIv = (ImageView) rootView.findViewById(R.id.iv_register_phone_photocode);
        agreeTv = (TextView) rootView.findViewById(R.id.tv_register_phone_agreement);
        getCodeRl = (RelativeLayout) rootView.findViewById(R.id.rl_register_phone_getcode);
    }

    private void initData() {
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        photoCodeIv.setImageBitmap(bitmap);
    }

    private void setListener() {
        photoCodeIv.setOnClickListener(this);
        agreeTv.setOnClickListener(this);
        getCodeRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_register_phone_photocode:
                Bitmap bitmap = codeUtils.createBitmap();
                photoCodeIv.setImageBitmap(bitmap);
                break;
            case R.id.tv_register_phone_agreement:
                startActivity(new Intent(getActivity(), AgreeMentActivity.class));
                break;
            case R.id.rl_register_phone_getcode:
                judge();
                break;
            default:
                break;
        }
    }

    private void judge() {
        if (!TextUtils.isEmpty(photoNumberEt.getText().toString())) {
            if (RegularUtils.isPhonenumber(photoNumberEt.getText().toString())) {
                if (!TextUtils.isEmpty(photoCodeEt.getText().toString())) {
                    if (photoCodeEt.getText().toString().equalsIgnoreCase(codeUtils.getCode())) {
                        ToastUtils.toast(getActivity(), "获取验证码");
                    } else {
                        ToastUtils.toast(getActivity(), "图形验证码不正确");
                    }
                } else {
                    ToastUtils.toast(getActivity(), "请输入验证码");
                }
            } else {
                ToastUtils.toast(getActivity(), "手机号不正确");
            }
        } else {
            ToastUtils.toast(getActivity(), "请输入手机号");
        }
    }
}
