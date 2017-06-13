package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gangjianwang.www.gangjianwang.HomeActivity;
import com.gangjianwang.www.gangjianwang.R;

import utils.ToastUtils;

/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class RealorderWaitevaluateFragment extends Fragment{

    private View rootView,emptyView;
    private ListView mLv;
    private RelativeLayout lookAroundRl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_series_order,null);
        rootView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        emptyView = inflater.inflate(R.layout.empty_order,null);
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
        initView(rootView);
        return rootView;
    }

    private void initView(View view){
        mLv = (ListView) view.findViewById(R.id.lv_series_order);
        ((ViewGroup)mLv.getParent()).addView(emptyView);
        emptyView.setVisibility(View.GONE);
        mLv.setEmptyView(emptyView);
        lookAroundRl = (RelativeLayout) emptyView.findViewById(R.id.rl_empty_order_stroll);
        lookAroundRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),HomeActivity.class);
                intent.putExtra("what",0);
                startActivity(intent);
            }
        });
    }

}
