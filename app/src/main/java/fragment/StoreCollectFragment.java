package fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;
import com.gangjianwang.www.gangjianwang.StoreDetailActivity;
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

import adapter.StoreCollectListAdapter;
import bean.StoreCollect;
import config.NetConfig;
import config.ParaConfig;
import utils.ToastUtils;
import utils.UserUtils;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class StoreCollectFragment extends Fragment implements ListItemClickHelp, AdapterView.OnItemClickListener {

    private View rootView, emptyView;
    private ListView lv;
    private ProgressDialog progressDialog;
    private OkHttpClient okHttpClient;
    private List<StoreCollect> storeCollectList = new ArrayList<>();
    private StoreCollectListAdapter storeCollectListAdapter;
    private String key;
    private int delPosition;
    private AlertDialog alertDialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case 0:
                        ToastUtils.toast(getActivity(), ParaConfig.NETWORK_ERROR);
                        break;
                    case 1:
                        break;
                    case 2:
                        storeCollectList.remove(delPosition);
                        ToastUtils.toast(getActivity(), ParaConfig.DELETE_SUCCESS);
                        break;
                    default:
                        break;
                }
                storeCollectListAdapter.notifyDataSetChanged();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_shopcollect, null);
        initView();
        initData();
        setData();
        setListener();
        loadData();
        return rootView;
    }

    private void initView() {
        initRoot();
        initEmpty();
        initDialog();
    }

    private void initRoot() {
        rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        lv = (ListView) rootView.findViewById(R.id.lv_store_collect);
    }

    private void initEmpty() {
        emptyView = View.inflate(getActivity(), R.layout.empty_type_myfoot, null);
        ((ImageView) emptyView.findViewById(R.id.iv_empty_type_myfoot_icon)).setImageResource(R.mipmap.empty_store_collect);
        ((TextView) emptyView.findViewById(R.id.tv_empty_type_myfoot_hint)).setText("您还没有关注任何店铺");
        ((TextView) emptyView.findViewById(R.id.tv_empty_type_myfoot_recommend)).setText("可以去看看哪些店铺值得收藏");
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        lv = (ListView) rootView.findViewById(R.id.lv_store_collect);
        ((ViewGroup) lv.getParent()).addView(emptyView);
        emptyView.setVisibility(View.GONE);
        lv.setEmptyView(emptyView);
    }

    private void initDialog() {
        alertDialog = new AlertDialog.Builder(getActivity()).setMessage(ParaConfig.DELETE_MESSAGE).setNegativeButton(ParaConfig.DELETE_YES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                delete(delPosition);
            }
        }).setPositiveButton(ParaConfig.DELETE_NO, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        }).create();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private void initData() {
        progressDialog = new ProgressDialog(getActivity());
        okHttpClient = new OkHttpClient();
        storeCollectListAdapter = new StoreCollectListAdapter(getActivity(), storeCollectList, this);
        key = UserUtils.readLogin(getActivity(), true).getKey();
    }

    private void setData() {
        lv.setAdapter(storeCollectListAdapter);
    }

    private void setListener() {
        lv.setOnItemClickListener(this);
    }

    private void loadData() {
        emptyView.setVisibility(View.INVISIBLE);
        progressDialog.show();
        Request request = new Request.Builder().url(NetConfig.storeCollectHeadUrl + key + NetConfig.storeCollectFootUrl).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (parseJson(response.body().string())) {
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    private boolean parseJson(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            JSONObject objDatas = objBean.optJSONObject("datas");
            JSONArray arrFavoritesList = objDatas.optJSONArray("favorites_list");
            for (int i = 0; i < arrFavoritesList.length(); i++) {
                StoreCollect storeCollect = new StoreCollect();
                JSONObject o = arrFavoritesList.optJSONObject(i);
                storeCollect.setStoreId(o.optString("store_id"));
                storeCollect.setStoreName(o.optString("store_name"));
                storeCollect.setFavTime(o.optString("fav_time"));
                storeCollect.setFavTimeText(o.optString("fav_time_text"));
                storeCollect.setGoodsCount(o.optString("goods_count"));
                storeCollect.setStoreCollect(o.optString("store_collect"));
                storeCollect.setStoreAvatar(o.optString("store_avatar"));
                storeCollect.setStoreAvatarUrl(o.optString("store_avatar_url"));
                storeCollectList.add(storeCollect);
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
            case R.id.iv_item_store_collect_delete:
                delPosition = position;
                alertDialog.show();
                break;
            default:
                break;
        }
    }

    private void delete(int position) {
        progressDialog.show();
        String store_id = storeCollectList.get(position).getStoreId();
        RequestBody body = new FormEncodingBuilder()
                .add("key", key)
                .add("store_id", store_id)
                .build();
        Request request = new Request.Builder()
                .url(NetConfig.storeDeleteUrl)
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), StoreDetailActivity.class);
        intent.putExtra("store_id", storeCollectList.get(position).getStoreId());
        startActivity(intent);
    }
}
