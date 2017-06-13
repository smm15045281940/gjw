package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

/**
 * Created by Administrator on 2017/4/20 0020.
 */

public class RechargeDetailFragment extends Fragment{

    private ListView mLv;
    private View mEmptyView;
    private TextView mEmptyHintTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accountbalance,null);
        initView(view);
        initEmptyView();
        setEmptyView();
        return view;
    }

    private void initView(View view){
        mLv = (ListView) view.findViewById(R.id.lv_accountbalance);
    }

    private void initEmptyView(){
        mEmptyView = View.inflate(getActivity(),R.layout.empty_account_balance,null);
        mEmptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ((ViewGroup)mLv.getParent()).addView(mEmptyView);
        mEmptyHintTv = (TextView) mEmptyView.findViewById(R.id.tv_accountbalance_emptyhint);
        mEmptyHintTv.setText("您尚未充值过预存款");
    }

    private void setEmptyView(){
        mLv.setEmptyView(mEmptyView);
    }
}
