package bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class SureOrderStore {

    private String storeName;
    private String storeGoodsTotal;
    private List<SureOrderGoods> sureOrderGoodsList;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreGoodsTotal() {
        return storeGoodsTotal;
    }

    public void setStoreGoodsTotal(String storeGoodsTotal) {
        this.storeGoodsTotal = storeGoodsTotal;
    }

    public List<SureOrderGoods> getSureOrderGoodsList() {
        return sureOrderGoodsList;
    }

    public void setSureOrderGoodsList(List<SureOrderGoods> sureOrderGoodsList) {
        this.sureOrderGoodsList = sureOrderGoodsList;
    }
}
