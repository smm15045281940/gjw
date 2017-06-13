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
import android.widget.TableLayout;

import com.gangjianwang.www.gangjianwang.R;

import java.util.ArrayList;
import java.util.List;

import adapter.MyFragmentPagerAdapter;


/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class RealorderFragment extends Fragment {

    private View rootView;
    private TabLayout mTl;
    private ViewPager mVp;
    private String[] mTitle;
    private List<Fragment> mRealorderFragmentList;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private int vpIndex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_realorder, null);
        initView();
        initData();
        setData();
        receiveArgument();
        return rootView;
    }

    private void initView() {
        mTl = (TabLayout) rootView.findViewById(R.id.tl_realorder);
        mVp = (ViewPager) rootView.findViewById(R.id.vp_realorder);
    }

    private void initData() {
        mTitle = getActivity().getResources().getStringArray(R.array.realorder_title);
        mRealorderFragmentList = new ArrayList<>();
        mRealorderFragmentList.add(new RealorderAllFragment());
        mRealorderFragmentList.add(new RealorderWaitpayFragment());
        mRealorderFragmentList.add(new RealorderWaitreceiveFragment());
        mRealorderFragmentList.add(new RealorderWaitselfbringFragment());
        mRealorderFragmentList.add(new RealorderWaitevaluateFragment());
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getFragmentManager(), mTitle, mRealorderFragmentList);
    }

    private void setData() {
        mVp.setAdapter(myFragmentPagerAdapter);
        mTl.setupWithViewPager(mVp);
        mTl.setTabTextColors(Color.BLACK, Color.RED);
        mTl.setSelectedTabIndicatorColor(Color.RED);
        mTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mVp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void receiveArgument(){
        Bundle bundle = getArguments();
        if(bundle != null){
            vpIndex = bundle.getInt("realorder");
            mVp.setCurrentItem(vpIndex);
        }
    }
}
