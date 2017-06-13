package bean;

/**
 * Created by 孙明明 on 2017/5/15.
 */

public class GoodsShow {

    private String title;
    private String imgUrl1,imgUrl2,imgUrl3,imgUrl4,imgUrl5,imgUrl6;

    public GoodsShow(String title, String imgUrl1, String imgUrl2, String imgUrl3, String imgUrl4, String imgUrl5, String imgUrl6) {
        this.title = title;
        this.imgUrl1 = imgUrl1;
        this.imgUrl2 = imgUrl2;
        this.imgUrl3 = imgUrl3;
        this.imgUrl4 = imgUrl4;
        this.imgUrl5 = imgUrl5;
        this.imgUrl6 = imgUrl6;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getImgUrl3() {
        return imgUrl3;
    }

    public void setImgUrl3(String imgUrl3) {
        this.imgUrl3 = imgUrl3;
    }

    public String getImgUrl4() {
        return imgUrl4;
    }

    public void setImgUrl4(String imgUrl4) {
        this.imgUrl4 = imgUrl4;
    }

    public String getImgUrl5() {
        return imgUrl5;
    }

    public void setImgUrl5(String imgUrl5) {
        this.imgUrl5 = imgUrl5;
    }

    public String getImgUrl6() {
        return imgUrl6;
    }

    public void setImgUrl6(String imgUrl6) {
        this.imgUrl6 = imgUrl6;
    }

    @Override
    public String toString() {
        return "GoodsShow{" +
                "title='" + title + '\'' +
                ", imgUrl1='" + imgUrl1 + '\'' +
                ", imgUrl2='" + imgUrl2 + '\'' +
                ", imgUrl3='" + imgUrl3 + '\'' +
                ", imgUrl4='" + imgUrl4 + '\'' +
                ", imgUrl5='" + imgUrl5 + '\'' +
                ", imgUrl6='" + imgUrl6 + '\'' +
                '}';
    }
}
