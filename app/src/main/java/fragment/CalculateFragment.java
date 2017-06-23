package fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.ReceiveAreaActivity;
import com.gangjianwang.www.gangjianwang.KindActivity;
import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.CalculateChooseFileAdapter;
import adapter.UnitPopAdapter;
import bean.ChooseFile;
import config.NetConfig;
import utils.HeightUtils;
import utils.ToastUtils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class CalculateFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, ListItemClickHelp {

    private View rootView;
    private View mUnitPopView;
    private PopupWindow mUnitPopWindow;
    private Spinner mSpecialSupplySp;
    private ArrayAdapter<String> mSpecialSupplyAdapter;
    private List<String> mSpecialSupplyList = new ArrayList<>();
    private ListView mUnitLv;
    private RelativeLayout mUnitCancelRl, mSaveRl;
    private static int RESULT_LOAD_IMAGE = 1;
    private RelativeLayout chooseFileRl;
    private ListView mChooseFileLv;
    private List<String> mUnitList = new ArrayList<>();
    private List<ChooseFile> mChooseFileList = new ArrayList<>();
    private UnitPopAdapter mUnitPopAdapter;
    private CalculateChooseFileAdapter mCalculateChooseFileAdapter;
    private final int POP_STATE_UNIT = 1;
    private final int POP_STATE_BILL = 2;
    private final int POP_STATE_POST = 3;
    private int POP_STATE;

    private final int DATE_STATE_OFFEREND = 1;
    private final int DATE_STATE_PUBLISHRESULT = 2;
    private int DATE_STATE;

    private Bitmap bitmap;

    private OkHttpClient okHttpClient;
    private ProgressDialog mPd;

    private EditText purchaseNameEt, goodsNameEt, purchaseAmountEt, goodsDescriptionEt, detailAreaEt, deliverTimeEt;
    private EditText detailDescriptionEt, maxPriceEt, memberNameEt, memberPhoneEt, memberMobileEt;
    private TextView ofKindTv, unitTv, billRequireTv, tranRequireTv, bidEndTimeTv, resultTimeTv, receiveAreaTv;
    private String purchaseName, goodsName, ofKind, specialSupply, purchaseAmount, unit, goodsDescription;
    private String billRequire, tranRequire, receiveArea, detailArea, deliverTime, detailDescription;
    private String bidendTime, resultTime, maxPrice, memberName, memberPhone, memberMobile;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        mPd.dismiss();
                        ToastUtils.toast(getActivity(), "无网络");
                        break;
                    case 1:
                        mPd.dismiss();
                        mSpecialSupplyAdapter.notifyDataSetChanged();
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
        rootView = inflater.inflate(R.layout.fragment_calculate, null);
        initView();
        initData();
        setData();
        setListener();
        loadSpinner(false, "");
        return rootView;
    }

    private void initView() {
        mSpecialSupplySp = (Spinner) rootView.findViewById(R.id.sp_calculate_specialsupply);
        mChooseFileLv = (ListView) rootView.findViewById(R.id.lv_calculate_choosefile);
        ofKindTv = (TextView) rootView.findViewById(R.id.tv_calculate_ofkind);
        unitTv = (TextView) rootView.findViewById(R.id.tv_calculate_unit);
        chooseFileRl = (RelativeLayout) rootView.findViewById(R.id.rl_calculate_choosefile);
        billRequireTv = (TextView) rootView.findViewById(R.id.tv_calculate_billrequire);
        tranRequireTv = (TextView) rootView.findViewById(R.id.tv_calculate_transportrequire);
        bidEndTimeTv = (TextView) rootView.findViewById(R.id.tv_calculate_bidendtime);
        resultTimeTv = (TextView) rootView.findViewById(R.id.tv_calculate_resulttime);
        receiveAreaTv = (TextView) rootView.findViewById(R.id.tv_calculate_receivearea);
        mSaveRl = (RelativeLayout) rootView.findViewById(R.id.rl_calculate_save);
        mUnitPopView = View.inflate(getActivity(), R.layout.pop_unit, null);
        mUnitPopWindow = new PopupWindow(mUnitPopView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mUnitPopWindow.setFocusable(true);
        mUnitPopWindow.setTouchable(true);
        mUnitPopWindow.setOutsideTouchable(false);
        mUnitPopWindow.setAnimationStyle(R.style.goodsdetailgoods_popwindow_anim);
        mUnitPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        mUnitLv = (ListView) mUnitPopView.findViewById(R.id.lv_unit);
        mUnitCancelRl = (RelativeLayout) mUnitPopView.findViewById(R.id.rl_pop_unit_cancel);

        purchaseNameEt = (EditText) rootView.findViewById(R.id.et_calculate_purchasename);
        goodsNameEt = (EditText) rootView.findViewById(R.id.et_calculate_goodsname);
        purchaseAmountEt = (EditText) rootView.findViewById(R.id.et_calculate_purchaseamount);
        goodsDescriptionEt = (EditText) rootView.findViewById(R.id.et_calculate_goodsdescription);
        detailAreaEt = (EditText) rootView.findViewById(R.id.et_calculate_detailarea);
        deliverTimeEt = (EditText) rootView.findViewById(R.id.et_calculate_delivertime);
        detailDescriptionEt = (EditText) rootView.findViewById(R.id.et_calculate_detaildescription);
        maxPriceEt = (EditText) rootView.findViewById(R.id.et_calculate_maxprice);
        memberNameEt = (EditText) rootView.findViewById(R.id.et_calculate_membername);
        memberPhoneEt = (EditText) rootView.findViewById(R.id.et_calculate_memberphone);
        memberMobileEt = (EditText) rootView.findViewById(R.id.et_calculate_membermobile);
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
        mPd = new ProgressDialog(getActivity());
        mPd.setMessage("加载中..");
        mSpecialSupplyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mSpecialSupplyList);
        mUnitPopAdapter = new UnitPopAdapter(getActivity(), mUnitList);
        mCalculateChooseFileAdapter = new CalculateChooseFileAdapter(getActivity(), mChooseFileList, this);
    }

    private void setData() {
        mSpecialSupplySp.setAdapter(mSpecialSupplyAdapter);
        mUnitLv.setAdapter(mUnitPopAdapter);
        mChooseFileLv.setAdapter(mCalculateChooseFileAdapter);
    }

    private void setListener() {
        ofKindTv.setOnClickListener(this);
        unitTv.setOnClickListener(this);
        chooseFileRl.setOnClickListener(this);
        billRequireTv.setOnClickListener(this);
        tranRequireTv.setOnClickListener(this);
        bidEndTimeTv.setOnClickListener(this);
        resultTimeTv.setOnClickListener(this);
        receiveAreaTv.setOnClickListener(this);
        mUnitCancelRl.setOnClickListener(this);
        mUnitLv.setOnItemClickListener(this);
        mSaveRl.setOnClickListener(this);
    }

    private void loadSpinner(boolean b, String id) {
        if (!b) {
            mSpecialSupplyList.clear();
            mSpecialSupplyList.add("请选择");
            mSpecialSupplyAdapter.notifyDataSetChanged();
        } else {
            mPd.show();
            Request request = new Request.Builder().url(NetConfig.specialSupplyHeadUrl + id + NetConfig.specialSupplyFootUrl).get().build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    handler.sendEmptyMessage(0);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        mSpecialSupplyList.clear();
                        mSpecialSupplyList.add("请选择");
                        String result = response.body().string();
                        String json = cutJson(result);
                        if (parseJson(json))
                            handler.sendEmptyMessage(1);
                    }
                }
            });
        }
    }

    private String cutJson(String json) {
        int a = json.indexOf("(");
        int b = json.lastIndexOf(")");
        String s = json.substring(a + 1, b);
        return s;
    }

    private boolean parseJson(String json) {
        try {
            JSONArray arrBean = new JSONArray(json);
            for (int i = 0; i < arrBean.length(); i++) {
                JSONObject o = arrBean.optJSONObject(i);
                mSpecialSupplyList.add(o.optString("store_name"));
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_calculate_ofkind:
                startActivityForResult(new Intent(getActivity(), KindActivity.class), 0);
                break;
            case R.id.tv_calculate_unit:
                POP_STATE = POP_STATE_UNIT;
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 0.7f;
                getActivity().getWindow().setAttributes(lp);
                mUnitList.clear();
                mUnitList.add("单位");
                mUnitList.add("把");
                mUnitList.add("包");
                mUnitList.add("本");
                mUnitList.add("部");
                mUnitList.add("打");
                mUnitList.add("袋");
                mUnitList.add("单");
                mUnitPopAdapter.notifyDataSetChanged();
                HeightUtils.setListViewHeight(mUnitLv);
                mUnitPopWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.rl_calculate_choosefile:
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
                break;
            case R.id.tv_calculate_billrequire:
                POP_STATE = POP_STATE_BILL;
                WindowManager.LayoutParams lp1 = getActivity().getWindow().getAttributes();
                lp1.alpha = 0.7f;
                getActivity().getWindow().setAttributes(lp1);
                mUnitList.clear();
                mUnitList.add("发票要求");
                mUnitList.add("无需发票");
                mUnitList.add("普通发票");
                mUnitList.add("增值税发票");
                mUnitPopAdapter.notifyDataSetChanged();
                HeightUtils.setListViewHeight(mUnitLv);
                mUnitPopWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tv_calculate_transportrequire:
                POP_STATE = POP_STATE_POST;
                WindowManager.LayoutParams lp2 = getActivity().getWindow().getAttributes();
                lp2.alpha = 0.7f;
                getActivity().getWindow().setAttributes(lp2);
                mUnitList.clear();
                mUnitList.add("邮费要求");
                mUnitList.add("包邮");
                mUnitList.add("提付");
                HeightUtils.setListViewHeight(mUnitLv);
                mUnitPopAdapter.notifyDataSetChanged();
                mUnitPopWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tv_calculate_receivearea:
                startActivityForResult(new Intent(getActivity(), ReceiveAreaActivity.class), 0);
                break;
            case R.id.tv_calculate_bidendtime:
                DATE_STATE = DATE_STATE_OFFEREND;
                showDateDialog();
                break;
            case R.id.tv_calculate_resulttime:
                DATE_STATE = DATE_STATE_PUBLISHRESULT;
                showDateDialog();
                break;
            case R.id.rl_calculate_save:
                purchaseName = "采购单名称：" + purchaseNameEt.getText().toString();
                goodsName = "商品名称：" + goodsNameEt.getText().toString();
                ofKind = "所属类目：" + ofKindTv.getText().toString();
                specialSupply = "指定供应商：" + mSpecialSupplySp.getSelectedItem().toString();
                purchaseAmount = "采购数量：" + purchaseAmountEt.getText().toString();
                unit = "单位：" + unitTv.getText().toString();
                goodsDescription = "产品描述：" + goodsDescriptionEt.getText().toString();
                billRequire = "发票要求：" + billRequireTv.getText().toString();
                tranRequire = "邮费要求：" + tranRequireTv.getText().toString();
                receiveArea = "收货地" + receiveAreaTv.getText().toString();
                detailArea = "详细地址：" + detailAreaEt.getText().toString();
                deliverTime = "交货期：" + deliverTimeEt.getText().toString();
                detailDescription = "详细说明：" + detailDescriptionEt.getText().toString();
                bidendTime = "出价截止时间：" + bidEndTimeTv.getText().toString();
                resultTime = "公布结果时间：" + resultTimeTv.getText().toString();
                maxPrice = "最高限价" + maxPriceEt.getText().toString();
                memberName = "联系人：" + memberNameEt.getText().toString();
                memberPhone = "手机号：" + memberPhoneEt.getText().toString();
                memberMobile = "固定电话：" + memberMobileEt.getText().toString();
                ToastUtils.toast(getActivity(), purchaseName + "\n" + goodsName + "\n" + ofKind + "\n" +
                        specialSupply + "\n" + purchaseAmount + "\n" + unit + "\n" + goodsDescription + "\n" +
                        billRequire + "\n" + tranRequire + "\n" + receiveArea + "\n" + detailArea + "\n" + deliverTime + "\n" +
                        detailDescription + "\n" + bidendTime + "\n" + resultTime + "\n" + maxPrice + "\n" + memberName + "\n" +
                        memberPhone + "\n" + memberMobile);
                break;
            case R.id.rl_pop_unit_cancel:
                mUnitPopWindow.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && data != null) {
            switch (resultCode) {
                case 0:
                    ofKindTv.setText(data.getStringExtra("kind"));
                    loadSpinner(true, data.getStringExtra("kindId"));
                    break;
                case 1:
                    receiveAreaTv.setText(data.getStringExtra("sendAddress"));
                    break;
                default:
                    break;
            }
        }
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
            bitmap = BitmapFactory.decodeFile(picturePath, options);
            ChooseFile chooseFile = new ChooseFile();
            chooseFile.setBitmap(bitmap);
            mChooseFileList.add(chooseFile);
            mCalculateChooseFileAdapter.notifyDataSetChanged();
            HeightUtils.setListViewHeight(mChooseFileLv);
        }
    }

    private void showDateDialog() {
        Calendar d = Calendar.getInstance(Locale.CHINA);
        Date myDate = new Date();
        d.setTime(myDate);
        int year = d.get(Calendar.YEAR);
        int month = d.get(Calendar.MONTH);
        int day = d.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateTime = year + "-" + (month + 1) + "-" + dayOfMonth;
                switch (DATE_STATE) {
                    case DATE_STATE_OFFEREND:
                        bidEndTimeTv.setText(dateTime);
                        break;
                    case DATE_STATE_PUBLISHRESULT:
                        String bidEndTime = bidEndTimeTv.getText().toString();
                        int a = bidEndTime.indexOf("-");
                        int b = bidEndTime.lastIndexOf("-");
                        String bidYear = bidEndTime.substring(0, a);
                        String bidMonth = bidEndTime.substring(a + 1, b);
                        String bidDay = bidEndTime.substring(b + 1, bidEndTime.length());
                        int bidyear = Integer.parseInt(bidYear);
                        int bidmonth = Integer.parseInt(bidMonth);
                        int bidday = Integer.parseInt(bidDay);
                        if (year > bidyear) {
                            resultTimeTv.setText(dateTime);
                        } else if (year == bidyear) {
                            if (bidmonth > month + 1) {
                                resultTimeTv.setText(dateTime);
                            } else if (month + 1 == bidmonth) {
                                if (dayOfMonth >= bidday) {
                                    resultTimeTv.setText(dateTime);
                                } else {
                                    resultTimeTv.setText("请选择");
                                    ToastUtils.toast(getActivity(), "公布结果时间不能早于出价截止时间！" + "\n" + "请重新选择！");
                                }
                            } else {
                                resultTimeTv.setText("请选择");
                                ToastUtils.toast(getActivity(), "公布结果时间不能早于出价截止时间！" + "\n" + "请重新选择！");
                            }
                        } else {
                            resultTimeTv.setText("请选择");
                            ToastUtils.toast(getActivity(), "公布结果时间不能早于出价截止时间！" + "\n" + "请重新选择！");
                        }
                        break;
                    default:
                        break;
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_unit:
                switch (POP_STATE) {
                    case POP_STATE_UNIT:
                        unitTv.setText(mUnitList.get(position));
                        break;
                    case POP_STATE_BILL:
                        billRequireTv.setText(mUnitList.get(position));
                        break;
                    case POP_STATE_POST:
                        tranRequireTv.setText(mUnitList.get(position));
                        break;
                    default:
                        break;
                }
                mUnitPopWindow.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View item, View widget, int position, int which, boolean isChecked) {
        switch (which) {
            case R.id.iv_item_calculate_choosefile_delete:
                mChooseFileList.remove(position);
                mCalculateChooseFileAdapter.notifyDataSetChanged();
                HeightUtils.setListViewHeight(mChooseFileLv);
                break;
            default:
                break;
        }
    }
}
