package bean;

/**
 * Created by Administrator on 2017/6/7.
 */

public class ShopCarGoods {

    private boolean isChecked;
    private String goodsName;
    private String goodsSize;
    private String goodsPrice;
    private String goodsCount;

    public ShopCarGoods() {
    }

    public ShopCarGoods(boolean isChecked, String goodsName, String goodsSize, String goodsPrice, String goodsCount) {
        this.isChecked = isChecked;
        this.goodsName = goodsName;
        this.goodsSize = goodsSize;
        this.goodsPrice = goodsPrice;
        this.goodsCount = goodsCount;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsSize() {
        return goodsSize;
    }

    public void setGoodsSize(String goodsSize) {
        this.goodsSize = goodsSize;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(String goodsCount) {
        this.goodsCount = goodsCount;
    }
}
