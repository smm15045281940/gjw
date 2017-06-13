package bean;

/**
 * Created by Administrator on 2017/6/7.
 */

public class ShopHostRec {

    private String goodsName;
    private String goodsPrice;

    public ShopHostRec() {
    }

    public ShopHostRec(String goodsName, String goodsPrice) {
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
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
}
