package bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class SureOrder {

    private String trueName;
    private String areaInfo;
    private String address;
    private String payWay;
    private String billInfo;
    private String orderAmount;
    private List<SureOrderStore> sureOrderStoreList;

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(String areaInfo) {
        this.areaInfo = areaInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getBillInfo() {
        return billInfo;
    }

    public void setBillInfo(String billInfo) {
        this.billInfo = billInfo;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public List<SureOrderStore> getSureOrderStoreList() {
        return sureOrderStoreList;
    }

    public void setSureOrderStoreList(List<SureOrderStore> sureOrderStoreList) {
        this.sureOrderStoreList = sureOrderStoreList;
    }
}
