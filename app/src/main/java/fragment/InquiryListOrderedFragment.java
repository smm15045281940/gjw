package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gangjianwang.www.gangjianwang.R;

import customview.LazyFragment;
import utils.ToastUtils;

/**
 * Created by Administrator on 2017/6/9.
 */

public class InquiryListOrderedFragment extends LazyFragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_inquirylist, null);
        return rootView;
    }

    @Override
    protected void onFragmentFirstVisible() {
        ToastUtils.toast(getActivity(), "InquiryListOrderedFragment:onFragmentFirstVisible");
    }
}
