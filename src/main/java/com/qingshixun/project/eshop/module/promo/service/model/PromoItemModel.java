package com.qingshixun.project.eshop.module.promo.service.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 商品的领域模型
 *
 */
public class PromoItemModel {

    //商品的秒杀活动聚合模型,如果不为空则有还在进行中的秒杀活动
    //包括未开始和正在进行的
    private PromoModel promoModel;

    //商品id
    private Integer id;

    //商品名称
    @NotBlank(message = "商品名称不能为空")
    private String title;

    //商品价格
    @NotNull(message = "商品价格不能为空")
    @Min(value = 0, message = "商品价格不能为0")
    private BigDecimal price;

    //商品库存
    @NotNull(message = "商品的库存必填")
    private Integer stock;

    //商品描述
    @NotBlank(message = "商品描述不能为空")
    private String description;

    //商品销量
    private Integer sales;

    @NotBlank(message = "商品图片不能为空")
    private String imgUrl;

    public PromoModel getPromoModel() {
        return promoModel;
    }

    public void setPromoModel(PromoModel promoModel) {
        this.promoModel = promoModel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    @Override
    public String toString() {
        return "ItemModel{" +
                "promoModel=" + promoModel +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", description='" + description + '\'' +
                ", sales=" + sales +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
