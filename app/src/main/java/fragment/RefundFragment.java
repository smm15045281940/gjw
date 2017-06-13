package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gangjianwang.www.gangjianwang.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class RefundFragment extends Fragment {

    private View rootView;
    private ListView mLv;
    private ArrayAdapter<String> mAdapter;
    private List<String> mList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_refundreturn, null);
        initView();
        initData();
        setData();
        loadData();
        return rootView;
    }

    private void initView() {
        mLv = (ListView) rootView.findViewById(R.id.lv_refundreturn);
    }

    private void initData() {
        mList = new ArrayList<>();
        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mList);
    }

    private void setData() {
        mLv.setAdapter(mAdapter);
    }

    private void loadData() {
        for (int i = 0; i < 20; i++) {
            mList.add("退款" + i);
        }
        mAdapter.notifyDataSetChanged();
    }
}
