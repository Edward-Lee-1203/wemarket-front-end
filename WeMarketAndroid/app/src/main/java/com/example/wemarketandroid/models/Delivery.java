package com.example.wemarketandroid.models;

import java.util.Objects;

public class Delivery implements IDiffable{

    private int id;
    private Integer shipperId;
    private Integer userId;
    private Integer orderId;
    private String address;
    private Integer timeLimit;
    private String date;
    private boolean isConfirmed;
    private boolean isKeepSocial;
    private String deliveryCol; // ?
    private Shipper shipper;
    private User user;
    private Order order;

    public Delivery(int id, Integer shipperId, Integer userId, Integer orderId, String address, Integer timeLimit, String date, boolean isConfirmed, boolean isKeepSocial, String deliveryCol) {
        this.id = id;
        this.shipperId = shipperId;
        this.userId = userId;
        this.orderId = orderId;
        this.address = address;
        this.timeLimit = timeLimit;
        this.date = date;
        this.isConfirmed = isConfirmed;
        this.isKeepSocial = isKeepSocial;
        this.deliveryCol = deliveryCol;
    }

    public int getId() {
        return id;
    }

    public Integer getShipperId() {
        return shipperId;
    }

    public void setShipperId(Integer shipperId) {
        this.shipperId = shipperId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public boolean isKeepSocial() {
        return isKeepSocial;
    }

    public void setKeepSocial(boolean keepSocial) {
        isKeepSocial = keepSocial;
    }

    public String getDeliveryCol() {
        return deliveryCol;
    }

    public void setDeliveryCol(String deliveryCol) {
        this.deliveryCol = deliveryCol;
    }

    public Shipper getShipper() {
        return shipper;
    }

    public void setShipper(Shipper shipper) {
        this.shipper = shipper;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delivery delivery = (Delivery) o;
        return isConfirmed == delivery.isConfirmed &&
                isKeepSocial == delivery.isKeepSocial &&
                Objects.equals(id, delivery.id) &&
                Objects.equals(shipperId, delivery.shipperId) &&
                Objects.equals(userId, delivery.userId) &&
                Objects.equals(orderId, delivery.orderId) &&
                Objects.equals(address, delivery.address) &&
                Objects.equals(timeLimit, delivery.timeLimit) &&
                Objects.equals(date, delivery.date) &&
                Objects.equals(deliveryCol, delivery.deliveryCol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shipperId, userId, orderId, address, timeLimit, date, isConfirmed, isKeepSocial, deliveryCol);
    }
}
