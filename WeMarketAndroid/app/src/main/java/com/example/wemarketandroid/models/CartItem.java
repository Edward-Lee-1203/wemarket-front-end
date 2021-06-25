package com.example.wemarketandroid.models;

import java.util.Objects;

public class CartItem implements IDiffable {

    private Long foodId;
    private Food food;
    private double quantity;
    private int price;

    public CartItem(Food food, int price) {
        this.food = food;
        this.foodId = food.getId();
        this.price = price;
        this.quantity = price/getDiscountPrice();
    }

    public int getDiscountPrice(){
        return food.getPrice()-(int)(food.getPrice()*food.getDiscount()/100);
    }

    @Override
    public Long getId() {
        return foodId;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return foodId == cartItem.foodId &&
                price == cartItem.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodId, price);
    }
}
