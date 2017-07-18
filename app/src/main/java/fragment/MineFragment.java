package fragment;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.AddressManagerActivity;
import com.gangjianwang.www.gangjianwang.CollectActivity;
import com.gangjianwang.www.gangjianwang.FootActivity;
import com.gangjianwang.www.gangjianwang.HomeActivity;
import com.gangjianwang.www.gangjianwang.IntegrateActivity;
import com.gangjianwang.www.gangjianwang.LoginActivity;
import com.gangjianwang.www.gangjianwang.MessageActivity;
import com.gangjianwang.www.gangjianwang.OrderActivity;
import com.gangjianwang.www.gangjianwang.PropertyActivity;
import com.gangjianwang.www.gangjianwang.R;
import com.gangjianwang.www.gangjianwang.RefundActivity;
import com.gangjianwang.www.gangjianwang.SettingActivity;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import bean.UserInfo;
import config.NetConfig;
import config.ParaConfig;
import utils.ToastUtils;
import utils.UserUtils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class MineFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private RelativeLayout mSettingRl;
    private ImageView mMineFaceIv, mMineCircleFaceIv;
    private RelativeLayout mBackcoloranimRl;
    private RelativeLayout mGoodscollectRl, mShopcollectRl, mMyfootRl;
    private RelativeLayout mMypropertyRl, mAddressManagerRl, mUsersettingRl, mMyPurchaseRl, mMessageListRl;
    private RelativeLayout mWaitpayRl, mWaitreceiveRl, mWaitbringRl, mWaitevaluateRl, mRefundRl;
    private RelativeLayout mIntegrateRl;
    private RelativeLayout mLoginRl;
    private RelativeLayout mMyorderRl;
    private TextView mLoginTv, mLevelTv, mGoodsCollectCountTv, mStoreCollectCountTv;
    private AnimatorSet animSet;
    private ObjectAnimator oa;
    private static int RESULT_LOAD_IMAGE = 1;
    private Handler mChangeFragHandler;
    private boolean isLogined;
    private OkHttpClient okHttpClient;
    private String key;
    private boolean autoLogin;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        ToastUtils.toast(getActivity(), ParaConfig.NETWORK_ERROR);
                        break;
                    case 1:
                        loadData();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeActivity homeActivity = (HomeActivity) getActivity();
        mChangeFragHandler = homeActivity.mChangeFragHandler;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mine, null);
        initView();
        initAnim();
        initData();
        setListener();
        loadData();
        return rootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            animSet.start();
            oa.start();
            onRefresh();
        } else {
            animSet.cancel();
            oa.cancel();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
        handler.removeMessages(1);
    }

    private void initView() {
        mSettingRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_setting);
        mMineFaceIv = (ImageView) rootView.findViewById(R.id.iv_mine_face);
        mMineCircleFaceIv = (ImageView) rootView.findViewById(R.id.iv_mine_circle_face);
        mLoginRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_login);
        mBackcoloranimRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_backcoloranim);
        mGoodscollectRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_goodscollect);
        mShopcollectRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_shopcollect);
        mMyfootRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_myfoot);
        mMyorderRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_myorder);
        mMypropertyRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_myproperty);
        mAddressManagerRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_deliveryaddressmanage);
        mUsersettingRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_usersetting);
        mMyPurchaseRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_mypurchase);
        mMessageListRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_messagelist);
        mWaitpayRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_obligation);
        mWaitreceiveRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_waitreceive);
        mWaitbringRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_waitselfbring);
        mWaitevaluateRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_waitevaluate);
        mRefundRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_refundreturn);
        mIntegrateRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_integrate);
        mLoginTv = (TextView) rootView.findViewById(R.id.tv_mine_face);
        mLevelTv = (TextView) rootView.findViewById(R.id.tv_mine_level);
        mGoodsCollectCountTv = (TextView) rootView.findViewById(R.id.tv_mine_goodscollect_count);
        mStoreCollectCountTv = (TextView) rootView.findViewById(R.id.tv_mine_storecollect_count);
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
    }

    private void setListener() {
        mSettingRl.setOnClickListener(this);
        mMineFaceIv.setOnClickListener(this);
        mGoodscollectRl.setOnClickListener(this);
        mShopcollectRl.setOnClickListener(this);
        mMyfootRl.setOnClickListener(this);
        mMyorderRl.setOnClickListener(this);
        mMypropertyRl.setOnClickListener(this);
        mAddressManagerRl.setOnClickListener(this);
        mUsersettingRl.setOnClickListener(this);
        mMyPurchaseRl.setOnClickListener(this);
        mMessageListRl.setOnClickListener(this);
        mWaitpayRl.setOnClickListener(this);
        mWaitreceiveRl.setOnClickListener(this);
        mWaitbringRl.setOnClickListener(this);
        mWaitevaluateRl.setOnClickListener(this);
        mRefundRl.setOnClickListener(this);
        mIntegrateRl.setOnClickListener(this);
        mLoginTv.setOnClickListener(this);
    }

    private void initAnim() {
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(mLoginRl, "translationY", -200.0F, 10.0F);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(mLoginRl, "translationY", 10.0F, 2.0F);
        ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(mLoginRl, "translationY", 2.0F, 10.0F);
        objectAnimator1.setDuration(500);
        objectAnimator2.setDuration(200);
        objectAnimator3.setDuration(300);
        animSet = new AnimatorSet();
        animSet.play(objectAnimator3).after(objectAnimator2).after(objectAnimator1);
        animSet.start();
        int colorA = Color.parseColor("#EE6170");
        int colorB = Color.parseColor("#A0D676");
        int colorC = Color.parseColor("#B399EC");
        oa = ObjectAnimator.ofInt(mBackcoloranimRl, "backgroundColor", colorA, colorB, colorC);
        oa.setDuration(20000);
        oa.setEvaluator(new ArgbEvaluator());
        oa.setRepeatCount(-1);
        oa.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10;
            options.inTempStorage = new byte[1024];
            mMineCircleFaceIv.setImageBitmap(BitmapFactory.decodeFile(picturePath, options));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_mine_setting:
                if (UserUtils.isLogined(getActivity())) {
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.iv_mine_face:
                if (UserUtils.isLogined(getActivity())) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.rl_mine_goodscollect:
                if (UserUtils.isLogined(getActivity())) {
                    startActivity(new Intent(getActivity(), CollectActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.rl_mine_shopcollect:
                if (UserUtils.isLogined(getActivity())) {
                    Intent shopIntent = new Intent(getActivity(), CollectActivity.class);
                    shopIntent.putExtra("collect", 1);
                    startActivity(shopIntent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.rl_mine_myfoot:
                if (UserUtils.isLogined(getActivity())) {
                    startActivity(new Intent(getActivity(), FootActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.tv_mine_face:
                if (!UserUtils.isLogined(getActivity())) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.rl_mine_myorder:
                if (UserUtils.isLogined(getActivity())) {
                    startActivity(new Intent(getActivity(), OrderActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.rl_mine_myproperty:
                if (UserUtils.isLogined(getActivity())) {
                    startActivity(new Intent(getActivity(), PropertyActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.rl_mine_deliveryaddressmanage:
                if (UserUtils.isLogined(getActivity())) {
                    startActivity(new Intent(getActivity(), AddressManagerActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.rl_mine_usersetting:
                if (UserUtils.isLogined(getActivity())) {
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.rl_mine_mypurchase:
                if (UserUtils.isLogined(getActivity())) {
                    mChangeFragHandler.sendEmptyMessage(1);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.rl_mine_messagelist:
                if (UserUtils.isLogined(getActivity())) {
                    startActivity(new Intent(getActivity(), MessageActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.rl_mine_obligation:
                if (UserUtils.isLogined(getActivity())) {
                    Intent waitpayIntent = new Intent(getActivity(), OrderActivity.class);
                    waitpayIntent.putExtra("order", 1);
                    startActivity(waitpayIntent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.rl_mine_waitreceive:
                if (UserUtils.isLogined(getActivity())) {
                    Intent waitreceiveIntent = new Intent(getActivity(), OrderActivity.class);
                    waitreceiveIntent.putExtra("order", 2);
                    startActivity(waitreceiveIntent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.rl_mine_waitselfbring:
                if (UserUtils.isLogined(getActivity())) {
                    Intent waitbringIntent = new Intent(getActivity(), OrderActivity.class);
                    waitbringIntent.putExtra("order", 3);
                    startActivity(waitbringIntent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.rl_mine_waitevaluate:
                if (UserUtils.isLogined(getActivity())) {
                    Intent waitevaluateIntent = new Intent(getActivity(), OrderActivity.class);
                    waitevaluateIntent.putExtra("order", 4);
                    startActivity(waitevaluateIntent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.rl_mine_refundreturn:
                if (UserUtils.isLogined(getActivity())) {
                    startActivity(new Intent(getActivity(), RefundActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.rl_mine_integrate:
                if (UserUtils.isLogined(getActivity())) {
                    startActivity(new Intent(getActivity(), IntegrateActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            default:
                break;
        }
    }

    private void loadData() {
        isLogined = UserUtils.isLogined(getActivity());
        UserInfo userInfo = UserUtils.readLogin(getActivity(), isLogined);
        mLoginTv.setText(userInfo.getUserName());
        mLevelTv.setText(userInfo.getLevelName());
        mGoodsCollectCountTv.setText(userInfo.getFavoritersGoods());
        mStoreCollectCountTv.setText(userInfo.getFavoritesStore());
        if (isLogined) {
            mLoginTv.setEnabled(false);
            mLevelTv.setVisibility(View.VISIBLE);
            mMineCircleFaceIv.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(userInfo.getAvatar()).placeholder(mMineCircleFaceIv.getDrawable()).into(mMineCircleFaceIv);
        } else {
            mLoginTv.setEnabled(true);
            mLevelTv.setVisibility(View.INVISIBLE);
            mMineCircleFaceIv.setVisibility(View.INVISIBLE);
        }
    }

    private boolean parseUserData(String json) {
        try {
            JSONObject objBean = new JSONObject(json);
            JSONObject objDatas = objBean.optJSONObject("datas");
            JSONObject objInfo = objDatas.optJSONObject("member_info");
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName(objInfo.optString("user_name"));
            userInfo.setAvatar(objInfo.optString("avatar"));
            userInfo.setLevelName(objInfo.optString("level_name"));
            userInfo.setFavoritesStore(objInfo.optString("favorites_store"));
            userInfo.setFavoritersGoods(objInfo.optString("favorites_goods"));
            userInfo.setAutoLogin(autoLogin);
            userInfo.setKey(key);
            UserUtils.writeLogin(getActivity(), userInfo);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void onRefresh() {
        UserInfo userInfo = UserUtils.readLogin(getActivity(), isLogined);
        key = userInfo.getKey();
        autoLogin = userInfo.isAutoLogin();
        RequestBody body = new FormEncodingBuilder()
                .add("key", key)
                .build();
        Request request = new Request.Builder()
                .url(NetConfig.loginAfterUrl)
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
                    UserUtils.clearLogin(getActivity());
                    if (parseUserData(response.body().string())) {
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
}
