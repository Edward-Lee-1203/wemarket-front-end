package com.example.wemarketandroid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Food implements IDiffable{

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("discount")
    @Expose
    private Integer discount;
    @SerializedName("urlImg")
    @Expose
    private String urlImg;
    @SerializedName("market")
    @Expose
    private Market market;
    @SerializedName("product_type")
    @Expose
    private String productType;

    public Food(Long id, String name, Integer price, Integer discount, String urlImg, Market market, String productType) {
        super();
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.urlImg = urlImg;
        this.market = market;
        this.productType = productType;
    }
    public Food(){}

    public Long getId() {
        return id;
    }

    public double getDiscountPrice(){return price-price*discount/100;}

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return Objects.equals(id, food.id) &&
                Objects.equals(name, food.name) &&
                Objects.equals(price, food.price) &&
                Objects.equals(discount, food.discount) &&
                Objects.equals(urlImg, food.urlImg) &&
                Objects.equals(market, food.market) &&
                Objects.equals(productType, food.productType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, discount, urlImg, market, productType);
    }
}
