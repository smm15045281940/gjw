package fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.GoodsCollectGridAdapter;
import bean.GoodsCollect;
import bean.UserInfo;
import config.NetConfig;
import utils.ToastUtils;
import utils.UserUtils;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class GoodsCollectFragment extends Fragment implements ListItemClickHelp {

    private View rootView, emptyView;
    private GridView gv;
    private List<GoodsCollect> goodsCollectList = new ArrayList<>();
    private GoodsCollectGridAdapter goodsCollectGridAdapter;
    private boolean isLogined;
    private OkHttpClient okHttpClient;
    private ProgressDialog progressDialog;
    private String key;
    private int delPosition;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        progressDialog.dismiss();
                        ToastUtils.toast(getActivity(), "无网络");
                        gv.setEmptyView(emptyView);
                        break;
                    case 1:
                        progressDialog.dismiss();
                        goodsCollectGridAdapter.notifyDataSetChanged();
                        gv.setEmptyView(emptyView);
                        break;
                    case 2:
                        goodsCollectList.remove(delPosition);
                        goodsCollectGridAdapter.notifyDataSetChanged();
                        ToastUtils.toast(getActivity(), "删除成功");
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goodscollect, null);
        initView();
        initData();
        setData();
        loadData();
        return rootView;
    }

    private void initView() {
        initRoot();
        initEmpty();
    }

    private void initRoot() {
        rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        gv = (GridView) rootView.findViewById(R.id.gv_goodscollect);
    }

    private void initEmpty() {
        emptyView = View.inflate(getActivity(), R.layout.empty_type_myfoot, null);
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ImageView) emptyView.findViewById(R.id.iv_empty_type_myfoot_icon)).setImageResource(R.mipmap.empty_goods_collect);
        ((TextView) emptyView.findViewById(R.id.tv_empty_type_myfoot_hint)).setText("您还没有关注任何商品");
        ((TextView) emptyView.findViewById(R.id.tv_empty_type_myfoot_recommend)).setText("可以去看看哪些商品值得收藏");
        ((ViewGroup) gv.getParent()).addView(emptyView);
        emptyView.setVisibility(View.GONE);
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
        progressDialog = new ProgressDialog(getActivity());
        isLogined = UserUtils.isLogined(getActivity());
        if (isLogined) {
            UserInfo userInfo = UserUtils.readLogin(getActivity(), true);
            key = userInfo.getKey();
        }
        goodsCollectGridAdapter = new GoodsCollectGridAdapter(getActivity(), goodsCollectList, this);
    }

    private void setData() {
        gv.setAdapter(goodsCollectGridAdapter);
    }

    private void loadData() {
        if (isLogined) {
            progressDialog.show();
            Request request = new Request.Builder().url(NetConfig.goodsCollectHeadUrl + key + NetConfig.goodsCollectFootUrl).get().build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    handler.sendEmptyMessage(0);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        if (parseJson(response.body().string()))
                            handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                }
            });
        }
    }

    private boolean parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            JSONObject objDatas = objBean.optJSONObject("datas");
            JSONArray arrFavoritesList = objDatas.optJSONArray("favorites_list");
            for (int i = 0; i < arrFavoritesList.length(); i++) {
                GoodsCollect goodsCollect = new GoodsCollect();
                JSONObject o = arrFavoritesList.optJSONObject(i);
                goodsCollect.setGoodsId(o.optString("goods_id"));
                goodsCollect.setGoodsName(o.optString("goods_name"));
                goodsCollect.setGoodsImage(o.optString("goods_image"));
                goodsCollect.setStoreId(o.optString("store_id"));
                goodsCollect.setFavId(o.optString("fav_id"));
                goodsCollect.setGoodsImageUrl(o.optString("goods_image_url"));
                goodsCollect.setGoodsPrice(o.optString("goods_price"));
                goodsCollectList.add(goodsCollect);
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onClick(View item, View widget, int position, int which, boolean isChecked) {
        switch (which) {
            case R.id.iv_item_goods_collect_delete:
                delPosition = position;
                delete(delPosition);
                break;
            default:
                break;
        }
    }

    private void delete(int position) {
        String fav_id = goodsCollectList.get(position).getFavId();
        RequestBody body = new FormEncodingBuilder()
                .add("key", key)
                .add("fav_id", fav_id)
                .build();
        Request request = new Request.Builder().url(NetConfig.goodsDeleteUrl)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (parseDelJson(response.body().string())) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    private boolean parseDelJson(String json) {
        boolean b = false;
        try {
            JSONObject objBean = new JSONObject(json);
            int code = objBean.optInt("code");
            if (code == 200) {
                b = true;
            } else {
                b = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return b;
    }
}
