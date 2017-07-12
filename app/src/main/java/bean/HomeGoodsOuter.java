package bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */

public class HomeGoodsOuter {

    private String title;
    private List<HomeGoodsInner> homeGoodsInnerList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<HomeGoodsInner> getHomeGoodsInnerList() {
        return homeGoodsInnerList;
    }

    public void setHomeGoodsInnerList(List<HomeGoodsInner> homeGoodsInnerList) {
        this.homeGoodsInnerList = homeGoodsInnerList;
    }
}
