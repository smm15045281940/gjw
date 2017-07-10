package config;

import com.gangjianwang.www.gangjianwang.R;

/**
 * Created by Administrator on 2017/7/9.
 */

public interface ParaConfig {

    public static final int DEFEAT = 0;
    public static final int SUCCESS = 1;
    public static final String NETWORK_ERROR = "网络异常";
    public static final String REFRESH_SUCCESS = "刷新成功";
    public static final String REFRESH_DEFEAT_ERROR = "刷新失败，请检查网络";
    public static final String DELETE_SUCCESS = "删除成功";
    //账户余额空
    public static final int ACCOUNT_BALANCE_ICON = R.mipmap.mcc_06_w;
    public static final String ACCOUNT_BALANCE_HINT = "您尚无预存款收支信息";
    public static final String ACCOUNT_BALANCE_CONTENT = "使用商城预存款结算更方便";
    //充值明细空
    public static final int RECHARGE_DETAIL_ICON = R.mipmap.mcc_06_w;
    public static final String RECHARGE_DETAIL_HINT = "您尚未充值过预存款";
    public static final String RECHARGE_DETAIL_CONTENT = "使用商城预存款结算更方便";
    //余额提现空
    public static final int ACCOUNT_CASH_ICON = R.mipmap.mcc_06_w;
    public static final String ACCOUNT_CASH_HINT = "您尚未提现过预存款";
    public static final String ACCOUNT_CASH_CONTENT = "使用商城预存款结算更方便";
    //充值卡空
    public static final int CARD_BALANCE_ICON = R.mipmap.mcc_07_w;
    public static final String CARD_BALANCE_HINT = "您尚无充值卡使用信息";
    public static final String CARD_BALANCE_CONTENT = "使用充值卡充值余额结算更方便";
    //代金券空
    public static final int VOUCHER_ICON = R.mipmap.mcc_08_w;
    public static final String VOUCHER_HINT = "您还没有相关的代金券";
    public static final String VOUCHER_CONTENT = "店铺代金券可享受商品折扣";
    //红包空
    public static final int REDBAG_ICON = R.mipmap.mcc_09_w;
    public static final String REDBAG_HINT = "您还没有相关的红包";
    public static final String REDBAG_CONTENT = "平台红包可抵扣现金结算";
    //收货地址空
    public static final int ADDRESS_ICON = R.mipmap.address_w;
    public static final String ADDRESS_HINT = "您还没有过添加收货地址";
    public static final String ADDRESS_CONTENT = "正确填写常用收货地址方便购物";
    public static final String ADDRESS_CLICK = "添加新地址";
    //消息空
    public static final int MESSAGE_ICON = R.mipmap.talk_w;
    public static final String MESSAGE_HINT = "您还没有和任何人联系过";
    public static final String MESSAGE_CONTENT = "对话后可在此找到最近联系的客服";
    //删除对话
    public static final String DELETE_MESSAGE = "确认删除？";
    public static final String DELETE_CLEAR = "确认清空？";
    public static final String DELETE_YES = "确认";
    public static final String DELETE_NO = "取消";
    //加载状态
    public static final int FIRST = 0;
    public static final int LOAD = 1;
    public static final int REFRESH = 2;
}
