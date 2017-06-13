package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gangjianwang.www.gangjianwang.AddOrderActivity;
import com.gangjianwang.www.gangjianwang.R;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class CalculateFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private Button mAddOrderBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_calculate, null);
        rootView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        initView(rootView);
        setListener();
        return rootView;
    }

    private void initView(View view) {
        mAddOrderBtn = (Button) view.findViewById(R.id.btn_add_order);
    }

    private void setListener() {
        mAddOrderBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_order:
                startActivity(new Intent(getActivity(), AddOrderActivity.class));
                break;
            default:
                break;
        }
    }
}
