package com.example.wemarketandroid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class OrderDetail implements IDiffable{

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("product")
    @Expose
    private Food food;
    @SerializedName("orders")
    @Expose
    private Order order;
    @SerializedName("kilogram")
    @Expose
    private Double kilogram;

    /**
     * No args constructor for use in serialization
     *
     */
    public OrderDetail() {
    }

    /**
     *
     * @param food
     * @param order
     * @param id
     * @param kilogram
     */
    public OrderDetail(Long id, Food food, Order order, Double kilogram) {
        super();
        this.id = id;
        this.food = food;
        this.order = order;
        this.kilogram = kilogram;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Double getKilogram() {
        return kilogram;
    }

    public void setKilogram(Double kilogram) {
        this.kilogram = kilogram;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetail that = (OrderDetail) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(food, that.food) &&
                Objects.equals(order, that.order) &&
                Objects.equals(kilogram, that.kilogram);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, food, order, kilogram);
    }
}
