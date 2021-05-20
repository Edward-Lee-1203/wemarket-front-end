package com.example.wemarketandroid.models.buyer;

import java.util.Objects;

public class Food {
    private int id;
    private String name;
    private String imageUri;
    private double price;
    private double discount;
//    private long count;     //TODO: notify server team for this field
    private Market market;

    public Food(int id, String name, String imageUri, double price, double discount) {
        this.id = id;
        this.name = name;
        this.imageUri = imageUri;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
                Double.compare(food.price, price) == 0 &&
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
