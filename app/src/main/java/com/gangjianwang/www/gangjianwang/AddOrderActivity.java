package com.gangjianwang.www.gangjianwang;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.AddOrderAdapter;
import adapter.UnitPopAdapter;
import bean.AddOrder;
import customview.ElasticScrollView;
import utils.HeightUtils;
import utils.ToastUtils;


public class AddOrderActivity extends AppCompatActivity implements ListItemClickHelp, View.OnClickListener, AdapterView.OnItemClickListener {

    private View rootView;
    private View mUnitPopView;
    private ImageView mBackIv;
    private PopupWindow mUnitPopWindow;
    private ListView mUnitLv;
    private RelativeLayout mUnitCancelRl;
    private static int RESULT_LOAD_IMAGE = 1;
    private int imgIndex = 7;
    private int imgCount = 0;
    private ListView mAddOrderLv;
    private List<AddOrder> mAddOrderList;
    private AddOrderAdapter mAddOrderAdapter;
    private AddOrder ao1, ao2, ao3, ao4, ao5, ao6, ao7, ao8, ao9, ao10, ao11, ao12, ao13, ao14, ao15, ao16, ao17, ao18, ao19;

    private List<String> mUnitList = new ArrayList<>();
    private UnitPopAdapter mUnitPopAdapter;

    private final int POP_STATE_UNIT = 1;
    private final int POP_STATE_BILL = 2;
    private final int POP_STATE_POST = 3;
    private int POP_STATE;

    private final int DATE_STATE_OFFEREND = 1;
    private final int DATE_STATE_PUBLISHRESULT = 2;
    private int DATE_STATE;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(AddOrderActivity.this, R.layout.activity_add_order, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        loadData();
        setListener();
    }

    private void initView() {
        mAddOrderLv = (ListView) findViewById(R.id.lv_addorder);
        mBackIv = (ImageView) findViewById(R.id.iv_back);
        mUnitPopView = View.inflate(AddOrderActivity.this, R.layout.pop_unit, null);
        mUnitPopWindow = new PopupWindow(mUnitPopView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mUnitPopWindow.setFocusable(true);
        mUnitPopWindow.setTouchable(true);
        mUnitPopWindow.setOutsideTouchable(false);
        mUnitPopWindow.setAnimationStyle(R.style.goodsdetailgoods_popwindow_anim);
        mUnitPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        mUnitLv = (ListView) mUnitPopView.findViewById(R.id.lv_unit);
        mUnitCancelRl = (RelativeLayout) mUnitPopView.findViewById(R.id.rl_pop_unit_cancel);
    }

    private void initData() {
        mAddOrderList = new ArrayList<>();
        mUnitList = new ArrayList<>();
    }

    private void setData() {
        mAddOrderAdapter = new AddOrderAdapter(AddOrderActivity.this, mAddOrderList, this);
        mAddOrderLv.setAdapter(mAddOrderAdapter);
        mUnitPopAdapter = new UnitPopAdapter(AddOrderActivity.this, mUnitList);
        mUnitLv.setAdapter(mUnitPopAdapter);
        HeightUtils.setListViewHeight(mUnitLv);
        mUnitPopAdapter.notifyDataSetChanged();
    }

    private void loadData() {
        ao1 = new AddOrder();
        ao1.setAddOrderType(1);
        ao1.setAddOrderName("采购单名称");
        ao2 = new AddOrder();
        ao2.setAddOrderType(1);
        ao2.setAddOrderName("商品名称");
        ao3 = new AddOrder();
        ao3.setAddOrderType(2);
        ao3.setAddOrderName("所属类目");
        ao4 = new AddOrder();
        ao4.setAddOrderType(2);
        ao4.setAddOrderName("指定供货商");
        ao4.setAddOrderContent("请选择");
        ao5 = new AddOrder();
        ao5.setAddOrderType(1);
        ao5.setAddOrderName("采购数量");
        ao6 = new AddOrder();
        ao6.setAddOrderType(2);
        ao6.setAddOrderName("单位");
        ao6.setAddOrderContent("把");
        ao7 = new AddOrder();
        ao7.setAddOrderType(3);
        ao7.setAddOrderName("图片");
        ao8 = new AddOrder();
        ao8.setAddOrderType(2);
        ao8.setAddOrderName("发票要求");
        ao8.setAddOrderContent("无需发票");
        ao9 = new AddOrder();
        ao9.setAddOrderType(2);
        ao9.setAddOrderName("邮费要求");
        ao9.setAddOrderContent("包邮");
        ao10 = new AddOrder();
        ao10.setAddOrderType(1);
        ao10.setAddOrderName("收货地");
        ao11 = new AddOrder();
        ao11.setAddOrderType(1);
        ao11.setAddOrderName("详细地址");
        ao12 = new AddOrder();
        ao12.setAddOrderType(1);
        ao12.setAddOrderName("交货期");
        ao12.setAddOrderContent("下单后多少天内交货");
        ao13 = new AddOrder();
        ao13.setAddOrderType(1);
        ao13.setAddOrderName("详细说明");
        ao14 = new AddOrder();
        ao14.setAddOrderType(2);
        ao14.setAddOrderName("出价截止时间");
        ao14.setAddOrderContent("请选择");
        ao15 = new AddOrder();
        ao15.setAddOrderType(2);
        ao15.setAddOrderName("公布结果时间");
        ao15.setAddOrderContent("请选择");
        ao16 = new AddOrder();
        ao16.setAddOrderType(1);
        ao16.setAddOrderName("最高限价");
        ao16.setAddOrderContent("单品最高报价不包含税费和运费");
        ao17 = new AddOrder();
        ao17.setAddOrderType(1);
        ao17.setAddOrderName("联系人");
        ao17.setAddOrderContent("yy1234");
        ao18 = new AddOrder();
        ao18.setAddOrderType(1);
        ao18.setAddOrderName("手机号");
        ao19 = new AddOrder();
        ao19.setAddOrderType(1);
        ao19.setAddOrderName("固定电话");
        mAddOrderList.add(ao1);
        mAddOrderList.add(ao2);
        mAddOrderList.add(ao3);
        mAddOrderList.add(ao4);
        mAddOrderList.add(ao5);
        mAddOrderList.add(ao6);
        mAddOrderList.add(ao7);
        mAddOrderList.add(ao8);
        mAddOrderList.add(ao9);
        mAddOrderList.add(ao10);
        mAddOrderList.add(ao11);
        mAddOrderList.add(ao12);
        mAddOrderList.add(ao13);
        mAddOrderList.add(ao14);
        mAddOrderList.add(ao15);
        mAddOrderList.add(ao16);
        mAddOrderList.add(ao17);
        mAddOrderList.add(ao18);
        mAddOrderList.add(ao19);
        mAddOrderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View item, View widget, int position, int which, boolean isChecked) {
        switch (which) {
            case R.id.tv_item_addorder_type2_content:
                if (position == 2) {
                    startActivityForResult(new Intent(AddOrderActivity.this, KindActivity.class), 0);
                }
                if (position == 3) {
                    startActivityForResult(new Intent(AddOrderActivity.this, SupplyActivity.class), 0);
                }
                if (position == 5) {
                    POP_STATE = POP_STATE_UNIT;
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 0.7f;
                    getWindow().setAttributes(lp);
                    mUnitList.clear();
                    mUnitList.add("单位");
                    mUnitList.add("把");
                    mUnitList.add("包");
                    mUnitList.add("本");
                    mUnitList.add("部");
                    mUnitList.add("打");
                    mUnitList.add("袋");
                    mUnitList.add("单");
                    HeightUtils.setListViewHeight(mUnitLv);
                    mUnitPopAdapter.notifyDataSetChanged();
                    mUnitPopWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                }
                if (position == 7) {
                    POP_STATE = POP_STATE_BILL;
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 0.7f;
                    getWindow().setAttributes(lp);
                    mUnitList.clear();
                    mUnitList.add("发票要求");
                    mUnitList.add("无需发票");
                    mUnitList.add("普通发票");
                    mUnitList.add("增值税发票");
                    HeightUtils.setListViewHeight(mUnitLv);
                    mUnitPopAdapter.notifyDataSetChanged();
                    mUnitPopWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                }
                if (position == 8) {
                    POP_STATE = POP_STATE_POST;
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 0.7f;
                    getWindow().setAttributes(lp);
                    mUnitList.clear();
                    mUnitList.add("邮费要求");
                    mUnitList.add("包邮");
                    mUnitList.add("提付");
                    HeightUtils.setListViewHeight(mUnitLv);
                    mUnitPopAdapter.notifyDataSetChanged();
                    mUnitPopWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                }
                if (position == 13 + imgCount) {
                    DATE_STATE = DATE_STATE_OFFEREND;
                    showDateDialog();
                }
                if (position == 14 + imgCount) {
                    DATE_STATE = DATE_STATE_PUBLISHRESULT;
                    showDateDialog();
                }
                break;
            case R.id.btn_item_addorder_type3_add:
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
                break;
            case R.id.iv_item_addorder_type4_close:
                mAddOrderList.remove(position);
                mAddOrderAdapter.notifyDataSetChanged();
                imgIndex--;
                imgCount--;
                break;
            default:
                break;
        }
    }

    private void setListener() {
        mBackIv.setOnClickListener(this);
        mUnitCancelRl.setOnClickListener(this);
        mUnitLv.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_pop_unit_cancel:
                mUnitPopWindow.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && data != null) {
            switch (resultCode) {
                case 0:
                    ao3.setAddOrderContent(data.getStringExtra("kind"));
                    mAddOrderAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    ao4.setAddOrderContent(data.getStringExtra("supply"));
                    mAddOrderAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10;
            options.inTempStorage = new byte[1024];
            bitmap = BitmapFactory.decodeFile(picturePath, options);
            List<AddOrder> tempList1 = new ArrayList<>();
            List<AddOrder> tempList2 = new ArrayList<>();
            for (int i = 0; i < imgIndex; i++) {
                tempList1.add(mAddOrderList.get(i));
            }
            for (int i = imgIndex; i < mAddOrderList.size(); i++) {
                tempList2.add(mAddOrderList.get(i));
            }
            AddOrder imgAddOrder = new AddOrder();
            imgAddOrder.setAddOrderType(4);
            imgAddOrder.setAddOrderBitmap(bitmap);
            mAddOrderList.clear();
            mAddOrderList.addAll(tempList1);
            mAddOrderList.add(imgAddOrder);
            mAddOrderList.addAll(tempList2);
            mAddOrderAdapter.notifyDataSetChanged();
            imgIndex++;
            imgCount++;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_unit:
                switch (POP_STATE) {
                    case POP_STATE_UNIT:
                        ao6.setAddOrderContent(mUnitList.get(position));
                        break;
                    case POP_STATE_BILL:
                        ao8.setAddOrderContent(mUnitList.get(position));
                        break;
                    case POP_STATE_POST:
                        ao9.setAddOrderContent(mUnitList.get(position));
                        break;
                    default:
                        break;
                }
                mUnitPopWindow.dismiss();
                mAddOrderAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void showDateDialog() {
        Calendar d = Calendar.getInstance(Locale.CHINA);
        Date myDate = new Date();
        d.setTime(myDate);
        int year = d.get(Calendar.YEAR);
        int month = d.get(Calendar.MONTH);
        int day = d.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateTime = year + "-" + (month + 1) + "-" + dayOfMonth;
                switch (DATE_STATE) {
                    case DATE_STATE_OFFEREND:
                        ao14.setAddOrderContent(dateTime);
                        break;
                    case DATE_STATE_PUBLISHRESULT:
                        ao15.setAddOrderContent(dateTime);
                        break;
                    default:
                        break;
                }
                mAddOrderAdapter.notifyDataSetChanged();
            }
        }, year, month, day);
        datePickerDialog.show();
    }
}
