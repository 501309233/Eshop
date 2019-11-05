package com.qingshixun.project.eshop.module.promo.service.model;

import org.joda.time.DateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PromoModel {
    //自增id
    private Long id;

    //秒杀活动名称
    private String promoName;

    //活动开始时间
    private DateTime startDate;

    //活动结束时间
    private DateTime endDate;

    //活动商品id
    private Long itemId;

    //商品原价////
    @NotNull(message = "商品价格不能为空")
    @Min(value = 0, message = "商品价格不能为0")
    private BigDecimal itemPrice;

    //商品图片存储路径////
    private String productImage;

    //活动商品秒杀价格
    @NotNull(message = "商品价格不能为空")
    @Min(value = 0, message = "商品价格不能为0")
    private BigDecimal promoItemPrice;

    //活动商品秒杀数量
    private Integer promoItemNum;

    //活动状态-->1表示未开始，2表示进行中，3表示已结束
    private Integer status;

    public Integer getPromoItemNum() {
        return promoItemNum;
    }

    public void setPromoItemNum(Integer promoItemNum) {
        this.promoItemNum = promoItemNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getPromoItemPrice() {
        return promoItemPrice;
    }

    public void setPromoItemPrice(BigDecimal promoItemPrice) {
        this.promoItemPrice = promoItemPrice;
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

    @Override
    public String toString() {
        return "PromoModel{" +
                "id=" + id +
                ", promoName='" + promoName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", itemId=" + itemId +
                ", itemPrice=" + itemPrice +
                ", productImage='" + productImage + '\'' +
                ", promoItemPrice=" + promoItemPrice +
                ", promoItemNum=" + promoItemNum +
                ", status=" + status +
                '}';
    }
}
