package org.example.entitys;

public class ResultJdEntiy {
    private String skuName;//商品名称
    private String skuId;//商品ID
    private String shopName;//商店名称
    private String couponUrl;//优惠券链接
    private int discount;//券面额
    private String imgUrl;//图片链接
    private String materialUrl;//商品落地页
    private String comments;//评论数
    private String goodCommentsShare;//好评率
    private int historyPriceDay;//历史最低价天数（例：当前券后价最近180天最低）
    private double price;//商品价格
    private double lowestCouponPrice;//券后价（有无券都返回此字段）
    private double lowestPrice;//促销价
    private int lowestPriceType;//促销价类型，1：无线价格；2：拼购价格； 3：秒杀价格；4：预售价格

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCouponUrl() {
        return couponUrl;
    }

    public void setCouponUrl(String couponUrl) {
        this.couponUrl = couponUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMaterialUrl() {
        return materialUrl;
    }

    public void setMaterialUrl(String materialUrl) {
        this.materialUrl = materialUrl;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getGoodCommentsShare() {
        return goodCommentsShare;
    }

    public void setGoodCommentsShare(String goodCommentsShare) {
        this.goodCommentsShare = goodCommentsShare;
    }

    public int getHistoryPriceDay() {
        return historyPriceDay;
    }

    public void setHistoryPriceDay(int historyPriceDay) {
        this.historyPriceDay = historyPriceDay;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLowestCouponPrice() {
        return lowestCouponPrice;
    }

    public void setLowestCouponPrice(double lowestCouponPrice) {
        this.lowestCouponPrice = lowestCouponPrice;
    }

    public double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public int getLowestPriceType() {
        return lowestPriceType;
    }

    public void setLowestPriceType(int lowestPriceType) {
        this.lowestPriceType = lowestPriceType;
    }
}
