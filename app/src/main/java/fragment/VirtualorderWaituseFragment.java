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
 * Created by Administrator on 2017/4/14 0014.
 */

public class VirtualorderWaituseFragment extends Fragment{

    private ListView mLv;
    private TextView mEmptyTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_virtualorder_waituse,null);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        initView(view);
        return view;
    }

    private void initView(View view){
        mLv = (ListView) view.findViewById(R.id.lv_virtualorderwaituse);
        mEmptyTv = (TextView) view.findViewById(R.id.tv_virtualorderwaituse_empty);
        mLv.setEmptyView(mEmptyTv);
    }
}
