package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

/**
 * Created by Administrator on 2017/6/6.
 */

public class ShopActionFragment extends Fragment {

    private View rootView, emptyView;
    private ListView lv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_shopaction, null);
        initView();
        return rootView;
    }

    private void initView() {
        initRoot();
        initEmpty();
    }

    private void initRoot() {
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        lv = (ListView) rootView.findViewById(R.id.lv_shop_action);
    }

    private void initEmpty() {
        emptyView = View.inflate(getActivity(), R.layout.empty, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ImageView) emptyView.findViewById(R.id.iv_empty_icon)).setImageResource(R.mipmap.search_w);
        ((TextView) emptyView.findViewById(R.id.tv_empty_hint)).setText("店铺最近没有促销活动");
        ((TextView) emptyView.findViewById(R.id.tv_empty_content)).setText("收藏店铺经常来逛一逛");
        ((ViewGroup) lv.getParent()).addView(emptyView);
        lv.setEmptyView(emptyView);
    }
}
