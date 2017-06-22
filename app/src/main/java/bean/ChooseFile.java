package bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/6/22.
 */

public class ChooseFile {

    private Bitmap bitmap;
    private String content;


    public ChooseFile() {
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
