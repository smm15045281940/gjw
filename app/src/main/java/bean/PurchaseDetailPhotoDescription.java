package bean;

/**
 * Created by Administrator on 2017/6/21.
 */

public class PurchaseDetailPhotoDescription {

    private String imgUrl;
    private String description;

    public PurchaseDetailPhotoDescription() {
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PurchaseDetailPhotoDescription{" +
                "imgUrl='" + imgUrl + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
