package bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/26.
 */

public class AddAddress implements Serializable {

    private String name;
    private String number;
    private String roughAddress;
    private String detailAddress;
    private String isDefault;

    public AddAddress() {

    }

    public AddAddress(String name, String number, String roughAddress, String detailAddress) {
        this.name = name;
        this.number = number;
        this.roughAddress = roughAddress;
        this.detailAddress = detailAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRoughAddress() {
        return roughAddress;
    }

    public void setRoughAddress(String roughAddress) {
        this.roughAddress = roughAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
