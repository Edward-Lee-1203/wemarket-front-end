package com.example.wemarketandroid.models.buyer;

import java.util.Objects;

public class OrderDetail {
    private int id;
    private int kg;
    Order order;

    public OrderDetail(int id, int kg) {
        this.id = id;
        this.kg = kg;
    }

    public int getId() {
        return id;
    }

    public int getKg() {
        return kg;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetail that = (OrderDetail) o;
        return id == that.id &&
                kg == that.kg &&
                Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, kg, order);
    }
}
