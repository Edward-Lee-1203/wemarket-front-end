package com.example.wemarketandroid.models;

import java.util.Objects;

public class OrderDetail implements IDiffable{
    
    private int id;
    private int foodId;
    private int orderId;
    private int kg;
    private Order order;
    private Food food;

    public OrderDetail(int id, int foodId, int orderId, int kg) {
        this.id = id;
        this.foodId = foodId;
        this.orderId = orderId;
        this.kg = kg;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getKg() {
        return kg;
    }

    public void setKg(int kg) {
        this.kg = kg;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetail that = (OrderDetail) o;
        return id == that.id &&
                foodId == that.foodId &&
                orderId == that.orderId &&
                kg == that.kg;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, foodId, orderId, kg);
    }
}
