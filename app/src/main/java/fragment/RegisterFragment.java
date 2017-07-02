package fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private RelativeLayout commonRl, phoneRl;
    private TextView commonTv, phoneTv;
    private View commonV, phoneV;
    private Fragment commonFragment, phoneFragment;
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentManager fragmentManager;
    private int curPosition = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register, null);
        rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initView();
        initData();
        setListener();
        return rootView;
    }

    private void initView() {
        initRoot();
    }

    private void initRoot() {
        commonRl = (RelativeLayout) rootView.findViewById(R.id.rl_register_common);
        phoneRl = (RelativeLayout) rootView.findViewById(R.id.rl_register_phone);
        commonTv = (TextView) rootView.findViewById(R.id.tv_register_common);
        phoneTv = (TextView) rootView.findViewById(R.id.tv_register_phone);
        commonV = rootView.findViewById(R.id.v_register_common);
        phoneV = rootView.findViewById(R.id.v_register_phone);
        changeColor(0);
    }

    private void initData() {
        fragmentManager = getFragmentManager();
        commonFragment = new RegisterCommonFragment();
        phoneFragment = new RegisterPhoneFragment();
        fragmentList.add(commonFragment);
        fragmentList.add(phoneFragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.ll_register_sit, fragmentList.get(curPosition));
        transaction.commit();
    }

    private void setListener() {
        commonRl.setOnClickListener(this);
        phoneRl.setOnClickListener(this);
    }

    private void changeColor(int tarPosition) {
        switch (tarPosition) {
            case 0:
                commonTv.setTextColor(Color.RED);
                phoneTv.setTextColor(Color.BLACK);
                commonV.setVisibility(View.VISIBLE);
                phoneV.setVisibility(View.INVISIBLE);
                break;
            case 1:
                commonTv.setTextColor(Color.BLACK);
                phoneTv.setTextColor(Color.RED);
                commonV.setVisibility(View.INVISIBLE);
                phoneV.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void changeFrag(int tarPosition) {
        if (tarPosition != curPosition) {
            Fragment curFragment = fragmentList.get(curPosition);
            Fragment tarFragment = fragmentList.get(tarPosition);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(curFragment);
            if (tarFragment.isAdded()) {
                transaction.show(tarFragment);
            } else {
                transaction.add(R.id.ll_register_sit, tarFragment);
            }
            transaction.commit();
            curPosition = tarPosition;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_register_common:
                changeColor(0);
                changeFrag(0);
                break;
            case R.id.rl_register_phone:
                changeColor(1);
                changeFrag(1);
                break;
            default:
                break;
        }
    }
}
