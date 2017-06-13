package fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gangjianwang.www.gangjianwang.R;

import java.util.ArrayList;
import java.util.List;

import adapter.MyorderFragmentPagerAdapter;

/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class VirtualorderFragment extends Fragment{

    private TabLayout mVirtualorderTb;
    private ViewPager mVirtualorderVp;

    private String[] mVirtualorderTitle;
    private List<Fragment> mVirtualorderFragmentList;
    private MyorderFragmentPagerAdapter myorderFragmentPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View virtualorderFragment = inflater.inflate(R.layout.fragment_virtualorder,null);
        initView(virtualorderFragment);
        initData();
        setAdapter();
        return virtualorderFragment;
    }

    private void initView(View view){
        mVirtualorderTb = (TabLayout) view.findViewById(R.id.tb_virtualorder);
        mVirtualorderVp = (ViewPager) view.findViewById(R.id.vp_virtualorder);
    }

    private void initData(){
        mVirtualorderTitle = getActivity().getResources().getStringArray(R.array.virtualorder_title);
        mVirtualorderFragmentList = new ArrayList<>();
        mVirtualorderFragmentList.add(new VirtualorderAllFragment());
        mVirtualorderFragmentList.add(new VirtualorderWaitpayFragment());
        mVirtualorderFragmentList.add(new VirtualorderWaituseFragment());
        myorderFragmentPagerAdapter = new MyorderFragmentPagerAdapter(getFragmentManager(),mVirtualorderTitle,mVirtualorderFragmentList);
    }

    private void setAdapter(){
        mVirtualorderVp.setAdapter(myorderFragmentPagerAdapter);
        mVirtualorderTb.setupWithViewPager(mVirtualorderVp);
        mVirtualorderTb.setTabTextColors(Color.BLACK,Color.RED);
    }
}
