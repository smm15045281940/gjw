package bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/21.
 */

public class PurchaseDetail {

    private String purchaseState;
    private String purchaseNumber;
    private String purchaseName;
    private String createTime;
    private String goodsName;
    private String goodsKind;
    private String purchaseAmount;
    private String goodsDescription;
    private List<PurchaseDetailPhotoDescription> photoDescriptionList;
    private String billRequire;
    private String transportRequire;
    private String receiveArea;
    private String detailArea;
    private String deliverTime;
    private String description;
    private String bidendTime;
    private String resultTime;
    private String maxPrice;
    private String memberName;
    private String memberPhone;
    private String memberMobile;

    public PurchaseDetail() {

    }

    public String getPurchaseState() {
        return purchaseState;
    }

    public void setPurchaseState(String purchaseState) {
        this.purchaseState = purchaseState;
    }

    public String getPurchaseNumber() {
        return purchaseNumber;
    }

    public void setPurchaseNumber(String purchaseNumber) {
        this.purchaseNumber = purchaseNumber;
    }

    public String getPurchaseName() {
        return purchaseName;
    }

    public void setPurchaseName(String purchaseName) {
        this.purchaseName = purchaseName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsKind() {
        return goodsKind;
    }

    public void setGoodsKind(String goodsKind) {
        this.goodsKind = goodsKind;
    }

    public String getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(String purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
    }

    public List<PurchaseDetailPhotoDescription> getPhotoDescriptionList() {
        return photoDescriptionList;
    }

    public void setPhotoDescriptionList(List<PurchaseDetailPhotoDescription> photoDescriptionList) {
        this.photoDescriptionList = photoDescriptionList;
    }

    public String getBillRequire() {
        return billRequire;
    }

    public void setBillRequire(String billRequire) {
        this.billRequire = billRequire;
    }

    public String getTransportRequire() {
        return transportRequire;
    }

    public void setTransportRequire(String transportRequire) {
        this.transportRequire = transportRequire;
    }

    public String getReceiveArea() {
        return receiveArea;
    }

    public void setReceiveArea(String receiveArea) {
        this.receiveArea = receiveArea;
    }

    public String getDetailArea() {
        return detailArea;
    }

    public void setDetailArea(String detailArea) {
        this.detailArea = detailArea;
    }

    public String getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(String deliverTime) {
        this.deliverTime = deliverTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBidendTime() {
        return bidendTime;
    }

    public void setBidendTime(String bidendTime) {
        this.bidendTime = bidendTime;
    }

    public String getResultTime() {
        return resultTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getMemberMobile() {
        return memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }
}
