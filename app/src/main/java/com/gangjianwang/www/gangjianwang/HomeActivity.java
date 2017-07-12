package com.gangjianwang.www.gangjianwang;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

import java.util.ArrayList;
import java.util.List;

import config.PersonConfig;
import fragment.CalculateFragment;
import fragment.HomeFragment;
import fragment.MineFragment;
import fragment.PurchaseFragment;
import fragment.ShopCarFragment;
import utils.UserUtils;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout mFirstpageRl, mPurchaseRl, mCalculateRl, mShopcarRl, mMeRl;
    private ImageView mFirstpageIv, mPurchaseIv, mShopcarIv, mMeIv;
    private TextView mFirstpageTv, mPurchaseTv, mShopcarTv, mMeTv;

    private Fragment mHomeFragment, mPurchaseFragment, mCalculateFragment, mShopcarFragment, mMineFragment;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private FragmentManager mFragmentManager;
    private int curPosition, tarPosition;
    private long exitTime = 0;
    private LocationClient locationClient;
    private BDLocationListener bdLocationListener;
    private Handler mLoadUserHandler;
    private Handler mCityHandler;

    public Handler mChangeFragHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        tarPosition = 0;
                        break;
                    case 1:
                        tarPosition = 1;
                        break;
                    case 2:
                        tarPosition = 2;
                        break;
                    case 3:
                        tarPosition = 3;
                        break;
                    case 4:
                        tarPosition = 4;
                        break;
                }
                changeFrag();
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            int a = intent.getIntExtra("what", 0);
            switch (a) {
                case 0:
                    tarPosition = 0;
                    changeFrag();
                    break;
                case 3:
                    tarPosition = 3;
                    changeFrag();
                    break;
                case 4:
                    tarPosition = 4;
                    changeFrag();
                    mLoadUserHandler.sendEmptyMessage(1);
                    break;
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(HomeActivity.this, R.layout.activity_home, null);
        setContentView(rootView);
        initView();
        initData();
        initLoc();
        setListener();
        locationClient.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_home_firstpage:
                tarPosition = 0;
                changeFrag();
                break;
            case R.id.rl_home_purchase:
                if (UserUtils.isLogined(this)) {
                    tarPosition = 1;
                    changeFrag();
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
                break;
            case R.id.rl_home_calculate:
                if (UserUtils.isLogined(this)) {
                    tarPosition = 2;
                    changeFrag();
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
                break;
            case R.id.rl_home_shopcar:
                if (UserUtils.isLogined(this)) {
                    tarPosition = 3;
                    changeFrag();
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
                break;
            case R.id.rl_home_me:
                tarPosition = 4;
                changeFrag();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        mFirstpageRl = (RelativeLayout) rootView.findViewById(R.id.rl_home_firstpage);
        mPurchaseRl = (RelativeLayout) rootView.findViewById(R.id.rl_home_purchase);
        mCalculateRl = (RelativeLayout) rootView.findViewById(R.id.rl_home_calculate);
        mShopcarRl = (RelativeLayout) rootView.findViewById(R.id.rl_home_shopcar);
        mMeRl = (RelativeLayout) rootView.findViewById(R.id.rl_home_me);
        mFirstpageIv = (ImageView) rootView.findViewById(R.id.iv_home_firstpage);
        mPurchaseIv = (ImageView) rootView.findViewById(R.id.iv_home_purchase);
        mShopcarIv = (ImageView) rootView.findViewById(R.id.iv_home_shopcar);
        mMeIv = (ImageView) rootView.findViewById(R.id.iv_home_me);
        mFirstpageTv = (TextView) rootView.findViewById(R.id.tv_home_firstpage);
        mPurchaseTv = (TextView) rootView.findViewById(R.id.tv_home_purchase);
        mShopcarTv = (TextView) rootView.findViewById(R.id.tv_home_shopcar);
        mMeTv = (TextView) rootView.findViewById(R.id.tv_home_me);
    }

    private void initData() {
        mFragmentManager = getSupportFragmentManager();
        mHomeFragment = new HomeFragment();
        mPurchaseFragment = new PurchaseFragment();
        mCalculateFragment = new CalculateFragment();
        mShopcarFragment = new ShopCarFragment();
        mMineFragment = new MineFragment();
        mCityHandler = ((HomeFragment) mHomeFragment).cityHandler;
        mLoadUserHandler = ((MineFragment) mMineFragment).handler;
        mFragmentList.add(mHomeFragment);
        mFragmentList.add(mPurchaseFragment);
        mFragmentList.add(mCalculateFragment);
        mFragmentList.add(mShopcarFragment);
        mFragmentList.add(mMineFragment);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.ll_home_sit, mFragmentList.get(0));
        transaction.commit();
        mFirstpageIv.setImageResource(R.mipmap.firstpage_choose);
        mFirstpageTv.setTextColor(PersonConfig.TV_HOME_CHOOSE);
    }

    private void initLoc() {
        locationClient = new LocationClient(this);
        bdLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(bdLocationListener);
        initLocation();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        locationClient.setLocOption(option);
    }

    private void setListener() {
        mFirstpageRl.setOnClickListener(this);
        mPurchaseRl.setOnClickListener(this);
        mCalculateRl.setOnClickListener(this);
        mShopcarRl.setOnClickListener(this);
        mMeRl.setOnClickListener(this);
    }

    private void changeFrag() {
        if (curPosition != tarPosition) {
            switch (tarPosition) {
                case 0:
                    mFirstpageIv.setImageResource(R.mipmap.firstpage_choose);
                    mPurchaseIv.setImageResource(R.mipmap.purchase_default);
                    mShopcarIv.setImageResource(R.mipmap.shopcar_default);
                    mMeIv.setImageResource(R.mipmap.mine_default);
                    mFirstpageTv.setTextColor(PersonConfig.TV_HOME_CHOOSE);
                    mPurchaseTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mShopcarTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mMeTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    break;
                case 1:
                    mFirstpageIv.setImageResource(R.mipmap.firstpage_default);
                    mPurchaseIv.setImageResource(R.mipmap.purchase_choose);
                    mShopcarIv.setImageResource(R.mipmap.shopcar_default);
                    mMeIv.setImageResource(R.mipmap.mine_default);
                    mFirstpageTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mPurchaseTv.setTextColor(PersonConfig.TV_HOME_CHOOSE);
                    mShopcarTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mMeTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    break;
                case 2:
                    mFirstpageIv.setImageResource(R.mipmap.firstpage_default);
                    mPurchaseIv.setImageResource(R.mipmap.purchase_default);
                    mShopcarIv.setImageResource(R.mipmap.shopcar_default);
                    mMeIv.setImageResource(R.mipmap.mine_default);
                    mFirstpageTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mPurchaseTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mShopcarTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mMeTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    break;
                case 3:
                    mFirstpageIv.setImageResource(R.mipmap.firstpage_default);
                    mPurchaseIv.setImageResource(R.mipmap.purchase_default);
                    mShopcarIv.setImageResource(R.mipmap.shopcar_choose);
                    mMeIv.setImageResource(R.mipmap.mine_default);
                    mFirstpageTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mPurchaseTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mShopcarTv.setTextColor(PersonConfig.TV_HOME_CHOOSE);
                    mMeTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    break;
                case 4:
                    mFirstpageIv.setImageResource(R.mipmap.firstpage_default);
                    mPurchaseIv.setImageResource(R.mipmap.purchase_default);
                    mShopcarIv.setImageResource(R.mipmap.shopcar_default);
                    mMeIv.setImageResource(R.mipmap.mine_choose);
                    mFirstpageTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mPurchaseTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mShopcarTv.setTextColor(PersonConfig.TV_HOME_DEFAULT);
                    mMeTv.setTextColor(PersonConfig.TV_HOME_CHOOSE);
                    break;
            }
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            Fragment curFragment = mFragmentList.get(curPosition);
            Fragment tarFragment = mFragmentList.get(tarPosition);
            transaction.hide(curFragment);
            if (tarFragment.isAdded()) {
                transaction.show(tarFragment);
            } else {
                transaction.add(R.id.ll_home_sit, tarFragment);
            }
            transaction.commit();
            curPosition = tarPosition;
        }
    }

    private void exit() {
        if (System.currentTimeMillis() - exitTime < 2000) {
            finish();
        } else {
            Toast.makeText(HomeActivity.this, PersonConfig.exitHint, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取定位结果
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间
            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息
            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数
                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息
            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            String str = sb.toString();
            int provinceIndex = str.indexOf("省");
            int cityIndex = str.indexOf("市");
            String s = str.substring(provinceIndex + 1, cityIndex);
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("city", s);
            msg.setData(bundle);
            mCityHandler.sendMessage(msg);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}
