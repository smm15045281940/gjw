package bean;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class Purchase {

    private String purchaseNumber;
    private String goodsName;
    private String maxPrice;
    private String purchaseAmount;
    private String purchaseName;
    private String billType;
    private String transportType;

    public Purchase() {
    }

    public Purchase(String purchaseNumber, String goodsName, String maxPrice, String purchaseAmount, String purchaseName, String billType, String transportType) {
        this.purchaseNumber = purchaseNumber;
        this.goodsName = goodsName;
        this.maxPrice = maxPrice;
        this.purchaseAmount = purchaseAmount;
        this.purchaseName = purchaseName;
        this.billType = billType;
        this.transportType = transportType;
    }

    public String getPurchaseNumber() {
        return purchaseNumber;
    }

    public void setPurchaseNumber(String purchaseNumber) {
        this.purchaseNumber = purchaseNumber;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(String purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public String getPurchaseName() {
        return purchaseName;
    }

    public void setPurchaseName(String purchaseName) {
        this.purchaseName = purchaseName;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }
}
