package bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/3.
 */

public class ShopCar {

    private boolean isChecked;
    private String shopName;
    private List<ShopCarGoods> goodsList;

    public ShopCar() {
    }

    public ShopCar(boolean isChecked, String shopName, List<ShopCarGoods> goodsList) {
        this.isChecked = isChecked;
        this.shopName = shopName;
        this.goodsList = goodsList;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<ShopCarGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<ShopCarGoods> goodsList) {
        this.goodsList = goodsList;
    }
}
