package bean;

/**
 * Created by Administrator on 2017/5/19.
 */

public class Order {

    private String companyIconUrl;
    private String companyName;
    private String orderState;
    private String goodsImgUrl;
    private String goodsName;
    private String goodsPrice;
    private String goodsAmount;
    private String numAmount;
    private String numPrice;

    public Order(String companyIconUrl, String companyName, String orderState, String goodsImgUrl, String goodsName, String goodsPrice, String goodsAmount, String numAmount, String numPrice) {
        this.companyIconUrl = companyIconUrl;
        this.companyName = companyName;
        this.orderState = orderState;
        this.goodsImgUrl = goodsImgUrl;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsAmount = goodsAmount;
        this.numAmount = numAmount;
        this.numPrice = numPrice;
    }

    public String getCompanyIconUrl() {
        return companyIconUrl;
    }

    public void setCompanyIconUrl(String companyIconUrl) {
        this.companyIconUrl = companyIconUrl;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getGoodsImgUrl() {
        return goodsImgUrl;
    }

    public void setGoodsImgUrl(String goodsImgUrl) {
        this.goodsImgUrl = goodsImgUrl;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(String goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public String getNumAmount() {
        return numAmount;
    }

    public void setNumAmount(String numAmount) {
        this.numAmount = numAmount;
    }

    public String getNumPrice() {
        return numPrice;
    }

    public void setNumPrice(String numPrice) {
        this.numPrice = numPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "companyIconUrl='" + companyIconUrl + '\'' +
                ", companyName='" + companyName + '\'' +
                ", orderState='" + orderState + '\'' +
                ", goodsImgUrl='" + goodsImgUrl + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPrice='" + goodsPrice + '\'' +
                ", goodsAmount='" + goodsAmount + '\'' +
                ", numAmount='" + numAmount + '\'' +
                ", numPrice='" + numPrice + '\'' +
                '}';
    }
}
