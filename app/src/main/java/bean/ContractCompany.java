package bean;

/**
 * Created by 孙明明 on 2017/5/15.
 */

public class ContractCompany {

    private String companyImgurl;
    private String companyName;
    private String companyDuty;
    private String companyAddress;

    public ContractCompany(String companyImgurl, String companyName, String companyDuty, String companyAddress) {
        this.companyImgurl = companyImgurl;
        this.companyName = companyName;
        this.companyDuty = companyDuty;
        this.companyAddress = companyAddress;
    }

    public String getCompanyImgurl() {
        return companyImgurl;
    }

    public void setCompanyImgurl(String companyImgurl) {
        this.companyImgurl = companyImgurl;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDuty() {
        return companyDuty;
    }

    public void setCompanyDuty(String companyDuty) {
        this.companyDuty = companyDuty;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    @Override
    public String toString() {
        return "ContractCompany{" +
                "companyImgurl='" + companyImgurl + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyDuty='" + companyDuty + '\'' +
                ", companyAddress='" + companyAddress + '\'' +
                '}';
    }
}
