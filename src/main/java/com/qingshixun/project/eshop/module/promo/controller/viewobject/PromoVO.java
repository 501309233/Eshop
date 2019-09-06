package com.qingshixun.project.eshop.module.promo.controller.viewobject;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class PromoVO {
    //自增id
    private Long id;

    //秒杀活动名称
    private String promoName;

    //活动开始时间
    private String startDate;

    //活动结束时间
    private String endDate;

    //活动商品id
    private Long itemId;

    //商品原价////
    private BigDecimal itemPrice;

    //商品图片存储路径////
    private String productImage;

    //活动商品秒杀价格
    private BigDecimal promoItemPrice;

    //活动商品秒杀数量
    private Integer promoItemNum;

    //商品促销活动状态
    // 0表示没有秒杀活动，1表示秒杀活动待开始，2表示秒杀活动进行中
    private Integer promoStatus;

    @Override
    public String toString() {
        return "PromoVO{" +
                "id=" + id +
                ", promoName='" + promoName + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", itemId=" + itemId +
                ", itemPrice=" + itemPrice +
                ", productImage='" + productImage + '\'' +
                ", promoItemPrice=" + promoItemPrice +
                ", promoItemNum=" + promoItemNum +
                ", promoStatus=" + promoStatus +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public BigDecimal getPromoItemPrice() {
        return promoItemPrice;
    }

    public void setPromoItemPrice(BigDecimal promoItemPrice) {
        this.promoItemPrice = promoItemPrice;
    }

    public Integer getPromoItemNum() {
        return promoItemNum;
    }

    public void setPromoItemNum(Integer promoItemNum) {
        this.promoItemNum = promoItemNum;
    }

    public Integer getPromoStatus() {
        return promoStatus;
    }

    public void setPromoStatus(Integer promoStatus) {
        this.promoStatus = promoStatus;
    }
}
