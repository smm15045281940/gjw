package bean;

/**
 * Created by Administrator on 2017/5/26.
 */

public class MakeSureOrder {

    private String name;
    private String number;
    private String address;
    private String payway;
    private String billinfo;

    public MakeSureOrder() {
    }

    public MakeSureOrder(String name, String number, String address, String payway, String billinfo) {
        this.name = name;
        this.number = number;
        this.address = address;
        this.payway = payway;
        this.billinfo = billinfo;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public String getBillinfo() {
        return billinfo;
    }

    public void setBillinfo(String billinfo) {
        this.billinfo = billinfo;
    }

    @Override
    public String toString() {
        return "MakeSureOrder{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", address='" + address + '\'' +
                ", payway='" + payway + '\'' +
                ", billinfo='" + billinfo + '\'' +
                '}';
    }
}
