package com.example.wemarketandroid.models.buyer;

import java.util.Objects;

public class CartItem implements IDiffable {

    private int foodId;
    private Food food;
    private int quantity;
    private int price;

    public CartItem(Food food, int price) {
        this.food = food;
        this.foodId = food.getId();
        this.price = price;
        this.quantity = price/food.getPrice();
    }

    @Override
    public int getId() {
        return foodId;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
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
