package bean;

/**
 * Created by 孙明明 on 2017/5/15.
 */

public class GoodsPost {

    private String imgUrl;

    public GoodsPost(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "GoodsPost{" +
                "imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
