package bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */

public class OrderOuter {

    private String storeName;
    private String stateDesc;
    private String goodsAmount;
    private String orderAmount;
    private List<OrderInner> orderInnerList;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    public String getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(String goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public List<OrderInner> getOrderInnerList() {
        return orderInnerList;
    }

    public void setOrderInnerList(List<OrderInner> orderInnerList) {
        this.orderInnerList = orderInnerList;
    }
}
