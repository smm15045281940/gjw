package fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.KindActivity;
import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;
import com.gangjianwang.www.gangjianwang.SupplyActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.CalculateChooseFileAdapter;
import adapter.UnitPopAdapter;
import bean.ChooseFile;
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
    private ListView mUnitLv;
    private RelativeLayout mUnitCancelRl, mSaveRl;
    private static int RESULT_LOAD_IMAGE = 1;
    private TextView ofKindTv, specialSupplyTv, unitTv, billRequireTv, tranRequireTv, bidEndTimeTv, resultTimeTv, receiveAreaTv;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_calculate, null);
        initView();
        initData();
        setData();
        setListener();
        return rootView;
    }

    private void initView() {
        mChooseFileLv = (ListView) rootView.findViewById(R.id.lv_calculate_choosefile);
        ofKindTv = (TextView) rootView.findViewById(R.id.tv_calculate_ofkind);
        specialSupplyTv = (TextView) rootView.findViewById(R.id.tv_calculate_specialsupply);
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
    }

    private void initData() {
        mUnitPopAdapter = new UnitPopAdapter(getActivity(), mUnitList);
        mCalculateChooseFileAdapter = new CalculateChooseFileAdapter(getActivity(), mChooseFileList, this);
    }

    private void setData() {
        mUnitLv.setAdapter(mUnitPopAdapter);
        mChooseFileLv.setAdapter(mCalculateChooseFileAdapter);
    }

    private void setListener() {
        ofKindTv.setOnClickListener(this);
        specialSupplyTv.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_calculate_ofkind:
                startActivityForResult(new Intent(getActivity(), KindActivity.class), 0);
                break;
            case R.id.tv_calculate_specialsupply:
                startActivityForResult(new Intent(getActivity(), SupplyActivity.class), 0);
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
                ToastUtils.toast(getActivity(), "收货地");
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
                ToastUtils.toast(getActivity(), "保存");
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
                    break;
                case 1:
                    specialSupplyTv.setText(data.getStringExtra("supply"));
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
