package com.example.wemarketandroid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Delivery implements IDiffable{

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("shipper")
    @Expose
    private Shipper shipper;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("order")
    @Expose
    private Order order;
    @SerializedName("timeLimit")
    @Expose
    private Integer timeLimit;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("is_confirm")
    @Expose
    private Integer isConfirm;
    @SerializedName("deliveryFee")
    @Expose
    private Double deliveryFee;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("is_keep_social")
    @Expose
    private Boolean isKeepSocial;
    @SerializedName("delivery")
    @Expose
    private String delivery;

    public Delivery() {
    }

    public Delivery(Long id, Shipper shipper, User user, Order order, Integer timeLimit, String address, String date, Integer isConfirm, Double deliveryFee, Double longitude, Double latitude, Boolean isKeepSocial, String delivery) {
        this.id = id;
        this.shipper = shipper;
        this.user = user;
        this.order = order;
        this.timeLimit = timeLimit;
        this.address = address;
        this.date = date;
        this.isConfirm = isConfirm;
        this.deliveryFee = deliveryFee;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isKeepSocial = isKeepSocial;
        this.delivery = delivery;
    }

    public Delivery(Long id, Shipper shipper, User user, Order order, String address, Double longitude, Double latitude) {
        this.id = id;
        this.shipper = shipper;
        this.user = user;
        this.order = order;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getConfirm() {
        return isConfirm;
    }

    public void setConfirm(Integer confirm) {
        isConfirm = confirm;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Boolean getKeepSocial() {
        return isKeepSocial;
    }

    public void setKeepSocial(Boolean keepSocial) {
        isKeepSocial = keepSocial;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delivery delivery1 = (Delivery) o;
        return Objects.equals(id, delivery1.id) &&
                Objects.equals(shipper, delivery1.shipper) &&
                Objects.equals(user, delivery1.user) &&
                Objects.equals(order, delivery1.order) &&
                Objects.equals(timeLimit, delivery1.timeLimit) &&
                Objects.equals(address, delivery1.address) &&
                Objects.equals(date, delivery1.date) &&
                Objects.equals(isConfirm, delivery1.isConfirm) &&
                Objects.equals(deliveryFee, delivery1.deliveryFee) &&
                Objects.equals(longitude, delivery1.longitude) &&
                Objects.equals(latitude, delivery1.latitude) &&
                Objects.equals(isKeepSocial, delivery1.isKeepSocial) &&
                Objects.equals(delivery, delivery1.delivery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shipper, user, order, timeLimit, address, date, isConfirm, deliveryFee, longitude, latitude, isKeepSocial, delivery);
    }
}
