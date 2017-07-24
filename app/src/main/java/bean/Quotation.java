package bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/24.
 */

public class Quotation {

    private String storeId;
    private String quotationId;
    private String storeName;
    private String quotationMod;
    private String quotationUsed;
    private String quotationSn;
    private String addTime;
    private String orderAmount;
    private List<Goods> goodsList;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(String quotationId) {
        this.quotationId = quotationId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getQuotationMod() {
        return quotationMod;
    }

    public void setQuotationMod(String quotationMod) {
        this.quotationMod = quotationMod;
    }

    public String getQuotationUsed() {
        return quotationUsed;
    }

    public void setQuotationUsed(String quotationUsed) {
        this.quotationUsed = quotationUsed;
    }

    public String getQuotationSn() {
        return quotationSn;
    }

    public void setQuotationSn(String quotationSn) {
        this.quotationSn = quotationSn;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }
}
