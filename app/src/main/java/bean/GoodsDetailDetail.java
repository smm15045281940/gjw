package bean;

/**
 * Created by Administrator on 2017/5/23.
 */

public class GoodsDetailDetail {

    private String imgurl;

    public GoodsDetailDetail() {
    }

    public GoodsDetailDetail(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    @Override
    public String toString() {
        return "GoodsDetailDetail{" +
                "imgurl='" + imgurl + '\'' +
                '}';
    }
}
