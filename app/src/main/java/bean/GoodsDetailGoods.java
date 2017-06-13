package bean;

/**
 * Created by Administrator on 2017/5/24.
 */

public class GoodsDetailGoods {

    private String sendAddress;
    private String size;
    private String thick;

    public GoodsDetailGoods() {

    }

    public GoodsDetailGoods(String sendAddress, String size, String thick) {
        this.sendAddress = sendAddress;
        this.size = size;
        this.thick = thick;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getThick() {
        return thick;
    }

    public void setThick(String thick) {
        this.thick = thick;
    }

    @Override
    public String toString() {
        return "GoodsDetailGoods{" +
                "sendAddress='" + sendAddress + '\'' +
                ", size='" + size + '\'' +
                ", thick='" + thick + '\'' +
                '}';
    }
}
