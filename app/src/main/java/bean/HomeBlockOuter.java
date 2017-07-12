package bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */

public class HomeBlockOuter {

    private String title;
    private List<HomeBlockInner> homeBlockInnerList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<HomeBlockInner> getHomeBlockInnerList() {
        return homeBlockInnerList;
    }

    public void setHomeBlockInnerList(List<HomeBlockInner> homeBlockInnerList) {
        this.homeBlockInnerList = homeBlockInnerList;
    }
}
