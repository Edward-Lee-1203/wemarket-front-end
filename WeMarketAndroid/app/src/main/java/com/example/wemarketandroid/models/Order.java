package com.example.wemarketandroid.models;

import java.util.List;
import java.util.Objects;

public class Order implements IDiffable{

    private int id;
    private int totalPrice;
    private List<OrderDetail> orderDetailList;

    public Order(int id, int totalPrice) {
        this.id = id;
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                totalPrice == order.totalPrice;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totalPrice);
    }
}
