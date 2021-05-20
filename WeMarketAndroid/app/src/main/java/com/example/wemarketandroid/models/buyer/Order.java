package com.example.wemarketandroid.models.buyer;

import java.util.List;
import java.util.Objects;

public class Order {
    private int id;
    private int price;   // TODO: notify server team to set type to double
    List<OrderDetail> orderDetailList;

    public Order(int id, int price) {
        this.id = id;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                price == order.price &&
                Objects.equals(orderDetailList, order.orderDetailList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, orderDetailList);
    }
}
