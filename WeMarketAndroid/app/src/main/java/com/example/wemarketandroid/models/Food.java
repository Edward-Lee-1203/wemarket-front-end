package com.example.wemarketandroid.models;

import java.util.Objects;

public class Food implements IDiffable{
    private int id;
    private String name;
    private String imageUri;
    private int basePrice;
    private int price;
    private double discount;
//    private long count;     //TODO: notify server team for this field
    private Market market;

    public Food(int id, String name, String imageUri, int basePrice, double discount) {
        this.id = id;
        this.name = name;
        this.imageUri = imageUri;
        this.basePrice = basePrice;
        price = basePrice - (int)(basePrice*discount);
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getPrice() {
        return price;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return id == food.id &&
                Integer.compare(food.basePrice, basePrice) == 0 &&
                Double.compare(food.discount, discount) == 0 &&
                Objects.equals(name, food.name) &&
                Objects.equals(imageUri, food.imageUri) &&
                Objects.equals(market, food.market);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, imageUri, price, discount, market);
    }
}
