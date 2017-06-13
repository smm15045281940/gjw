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

public class KindSecondFragment extends Fragment implements AdapterView.OnItemClickListener{

    private ListView mLv;
    private List<String> mDataList;
    private ArrayAdapter<String> mAdapter;

    private Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KindActivity kindActivity = (KindActivity) getActivity();
        handler = kindActivity.mbuttonStatehandler;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kind,null);
        initView(view);
        initData();
        setData();
        loadData(0);
        setListener();
        return view;
    }

    private void initView(View view){
        mLv = (ListView) view.findViewById(R.id.lv_kind);
    }

    private void initData(){
        mDataList = new ArrayList<>();
    }

    private void setData(){
        mAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,mDataList);
        mLv.setAdapter(mAdapter);
    }

    private void loadData(int a){
        switch (a){
            case 0:
                mDataList.add("工业管");
                mDataList.add("棒材");
                mAdapter.notifyDataSetChanged();
                break;
            case 1:
                mDataList.add("钢管");
                mDataList.add("钢板");
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void setListener(){
        mLv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putString("secondStr",mDataList.get(position));
        Message msg = Message.obtain();
        msg.setData(bundle);
        msg.what = 1;
        handler.sendMessage(msg);
    }
}
