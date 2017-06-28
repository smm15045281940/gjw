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
import com.gangjianwang.www.gangjianwang.FootActivity;
import com.gangjianwang.www.gangjianwang.GoodsStoreCollectActivity;
import com.gangjianwang.www.gangjianwang.HomeActivity;
import com.gangjianwang.www.gangjianwang.IntegrateActivity;
import com.gangjianwang.www.gangjianwang.LoginActivity;
import com.gangjianwang.www.gangjianwang.MessageActivity;
import com.gangjianwang.www.gangjianwang.OrderActivity;
import com.gangjianwang.www.gangjianwang.PropertyActivity;
import com.gangjianwang.www.gangjianwang.R;
import com.gangjianwang.www.gangjianwang.RefundActivity;
import com.gangjianwang.www.gangjianwang.SettingActivity;
import com.squareup.picasso.Picasso;

import bean.UserInfo;
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

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 1:
                        loadData();
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
        } else {
            animSet.cancel();
            oa.cancel();
        }
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
                startActivity(new Intent(getActivity(), GoodsStoreCollectActivity.class));
                break;
            case R.id.rl_mine_shopcollect:
                Intent shopIntent = new Intent(getActivity(), GoodsStoreCollectActivity.class);
                shopIntent.putExtra("collect", 1);
                startActivity(shopIntent);
                break;
            case R.id.rl_mine_myfoot:
                startActivity(new Intent(getActivity(), FootActivity.class));
                break;
            case R.id.tv_mine_face:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.rl_mine_myorder:
                startActivity(new Intent(getActivity(), OrderActivity.class));
                break;
            case R.id.rl_mine_myproperty:
                startActivity(new Intent(getActivity(), PropertyActivity.class));
                break;
            case R.id.rl_mine_deliveryaddressmanage:
                startActivity(new Intent(getActivity(), AddressManagerActivity.class));
                break;
            case R.id.rl_mine_usersetting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.rl_mine_mypurchase:
                mChangeFragHandler.sendEmptyMessage(1);
                break;
            case R.id.rl_mine_messagelist:
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.rl_mine_obligation:
                Intent waitpayIntent = new Intent(getActivity(), OrderActivity.class);
                waitpayIntent.putExtra("order", 1);
                startActivity(waitpayIntent);
                break;
            case R.id.rl_mine_waitreceive:
                Intent waitreceiveIntent = new Intent(getActivity(), OrderActivity.class);
                waitreceiveIntent.putExtra("order", 2);
                startActivity(waitreceiveIntent);
                break;
            case R.id.rl_mine_waitselfbring:
                Intent waitbringIntent = new Intent(getActivity(), OrderActivity.class);
                waitbringIntent.putExtra("order", 3);
                startActivity(waitbringIntent);
                break;
            case R.id.rl_mine_waitevaluate:
                Intent waitevaluateIntent = new Intent(getActivity(), OrderActivity.class);
                waitevaluateIntent.putExtra("order", 4);
                startActivity(waitevaluateIntent);
                break;
            case R.id.rl_mine_refundreturn:
                startActivity(new Intent(getActivity(), RefundActivity.class));
                break;
            case R.id.rl_mine_integrate:
                startActivity(new Intent(getActivity(), IntegrateActivity.class));
                break;
            default:
                break;
        }
    }

    private void loadData() {
        boolean isLogined = UserUtils.isLogined(getActivity());
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
}
