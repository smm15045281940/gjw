package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

/**
 * Created by Administrator on 2017/4/20 0020.
 */

public class MineVoucherFragment extends Fragment{

    private ListView mLv;
    private View mEmptyView;
    private TextView mEmptyHintTv,mEmptyContentTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voucher_mine,null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        initView(view);
        initEmptyView();
        setEmptyView();
        return view;
    }

    private void initView(View view){
        mLv = (ListView) view.findViewById(R.id.lv_voucher_mine);
    }

    private void initEmptyView(){
        mEmptyView = View.inflate(getActivity(),R.layout.empty_account_balance,null);
        mEmptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ((ViewGroup)mLv.getParent()).addView(mEmptyView);
        mEmptyHintTv = (TextView) mEmptyView.findViewById(R.id.tv_accountbalance_emptyhint);
        mEmptyContentTv = (TextView) mEmptyView.findViewById(R.id.tv_accountbalance_emptycontent);
        mEmptyHintTv.setText("您还没有相关的代金券");
        mEmptyContentTv.setText("店铺代金券可享受商品折扣");
    }

    private void setEmptyView(){
        mLv.setEmptyView(mEmptyView);
    }
}