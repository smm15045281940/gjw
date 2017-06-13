package bean;

/**
 * Created by 孙明明 on 2017/5/15.
 */

public class GoodsRecommend {

    private String imgUrl1,imgUrl2;
    private String goodsName1,goodsName2;
    private String goodsPrice1,goodsPrice2;

    public GoodsRecommend(String imgUrl1, String imgUrl2, String goodsName1, String goodsName2, String goodsPrice1, String goodsPrice2) {
        this.imgUrl1 = imgUrl1;
        this.imgUrl2 = imgUrl2;
        this.goodsName1 = goodsName1;
        this.goodsName2 = goodsName2;
        this.goodsPrice1 = goodsPrice1;
        this.goodsPrice2 = goodsPrice2;
    }

    public String getImgUrl1() {
        return imgUrl1;
    }

    public void setImgUrl1(String imgUrl1) {
        this.imgUrl1 = imgUrl1;
    }

    public String getImgUrl2() {
        return imgUrl2;
    }

    public void setImgUrl2(String imgUrl2) {
        this.imgUrl2 = imgUrl2;
    }

    public String getGoodsName1() {
        return goodsName1;
    }

    public void setGoodsName1(String goodsName1) {
        this.goodsName1 = goodsName1;
    }

    public String getGoodsName2() {
        return goodsName2;
    }

    public void setGoodsName2(String goodsName2) {
        this.goodsName2 = goodsName2;
    }

    public String getGoodsPrice1() {
        return goodsPrice1;
    }

    public void setGoodsPrice1(String goodsPrice1) {
        this.goodsPrice1 = goodsPrice1;
    }

    public String getGoodsPrice2() {
        return goodsPrice2;
    }

    public void setGoodsPrice2(String goodsPrice2) {
        this.goodsPrice2 = goodsPrice2;
    }

    @Override
    public String toString() {
        return "GoodsRecommend{" +
                "imgUrl1='" + imgUrl1 + '\'' +
                ", imgUrl2='" + imgUrl2 + '\'' +
                ", goodsName1='" + goodsName1 + '\'' +
                ", goodsName2='" + goodsName2 + '\'' +
                ", goodsPrice1='" + goodsPrice1 + '\'' +
                ", goodsPrice2='" + goodsPrice2 + '\'' +
                '}';
    }
}
