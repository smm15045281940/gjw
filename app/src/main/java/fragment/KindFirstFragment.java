package fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gangjianwang.www.gangjianwang.KindActivity;
import com.gangjianwang.www.gangjianwang.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class KindFirstFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView mLv;
    private List<String> mDataList;
    private ArrayAdapter<String> mAdapter;

    private Handler handler;

    private String firstStr;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KindActivity kindActivity = (KindActivity) getActivity();
        handler = kindActivity.mbuttonStatehandler;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kind, null);
        initView(view);
        initData();
        setData();
        loadData();
        setListener();
        return view;
    }

    private void initView(View view) {
        mLv = (ListView) view.findViewById(R.id.lv_kind);
    }

    private void initData(){
        mDataList = new ArrayList<>();
    }

    private void setData(){
        mAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,mDataList);
        mLv.setAdapter(mAdapter);
    }

    private void loadData(){
        mDataList.add("不锈钢系列");
        mDataList.add("钢铁钢板系列");
        mAdapter.notifyDataSetChanged();
    }

    private void setListener() {
        mLv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        firstStr = mDataList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("firstStr",firstStr);
        Message msg = Message.obtain();
        msg.setData(bundle);
        msg.what = 0 ;
        handler.sendMessage(msg);
    }
}
