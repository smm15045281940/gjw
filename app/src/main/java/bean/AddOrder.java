package bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

public class AddOrder {
    private int addOrderType;
    private String addOrderName;
    private String addOrderContent;
    private Bitmap addOrderBitmap;

    public int getAddOrderType() {
        return addOrderType;
    }

    public void setAddOrderType(int addOrderType) {
        this.addOrderType = addOrderType;
    }

    public String getAddOrderName() {
        return addOrderName;
    }

    public void setAddOrderName(String addOrderName) {
        this.addOrderName = addOrderName;
    }

    public String getAddOrderContent() {
        return addOrderContent;
    }

    public void setAddOrderContent(String addOrderContent) {
        this.addOrderContent = addOrderContent;
    }

    public Bitmap getAddOrderBitmap() {
        return addOrderBitmap;
    }

    public void setAddOrderBitmap(Bitmap addOrderBitmap) {
        this.addOrderBitmap = addOrderBitmap;
    }
}
