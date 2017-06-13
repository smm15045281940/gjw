package bean;

/**
 * Created by Administrator on 2017/6/6.
 */

public class AllGoods {

    private String goodsIcon;
    private String goodsName;
    private String goodsSales;
    private String goodsPrice;

    public AllGoods() {
    }

    public AllGoods(String goodsIcon, String goodsName, String goodsSales, String goodsPrice) {
        this.goodsIcon = goodsIcon;
        this.goodsName = goodsName;
        this.goodsSales = goodsSales;
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsIcon() {
        return goodsIcon;
    }

    public void setGoodsIcon(String goodsIcon) {
        this.goodsIcon = goodsIcon;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsSales() {
        return goodsSales;
    }

    public void setGoodsSales(String goodsSales) {
        this.goodsSales = goodsSales;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }
}
