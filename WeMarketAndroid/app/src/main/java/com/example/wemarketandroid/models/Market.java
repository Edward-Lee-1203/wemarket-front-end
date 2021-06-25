package com.example.wemarketandroid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Market implements IDiffable {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("open_time")
    @Expose
    private Integer openTime;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("itemsLeft")
    @Expose
    private Object itemsLeft;
    @SerializedName("close_time")
    @Expose
    private Integer closeTime;
    @SerializedName("market_type")
    @Expose
    private String marketType;

    public Market(Long id, String name, String address, Integer openTime, Double longitude, Double latitude, Object itemsLeft, Integer closeTime, String marketType) {
        super();
        this.id = id;
        this.name = name;
        this.address = address;
        this.openTime = openTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.itemsLeft = itemsLeft;
        this.closeTime = closeTime;
        this.marketType = marketType;
    }
    public Market(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Integer openTime) {
        this.openTime = openTime;
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

    public Object getItemsLeft() {
        return itemsLeft;
    }

    public void setItemsLeft(Object itemsLeft) {
        this.itemsLeft = itemsLeft;
    }

    public Integer getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Integer closeTime) {
        this.closeTime = closeTime;
    }

    public String getMarketType() {
        return marketType;
    }

    public void setMarketType(String marketType) {
        this.marketType = marketType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Market market = (Market) o;
        return Objects.equals(id, market.id) &&
                Objects.equals(name, market.name) &&
                Objects.equals(address, market.address) &&
                Objects.equals(openTime, market.openTime) &&
                Objects.equals(longitude, market.longitude) &&
                Objects.equals(latitude, market.latitude) &&
                Objects.equals(itemsLeft, market.itemsLeft) &&
                Objects.equals(closeTime, market.closeTime) &&
                Objects.equals(marketType, market.marketType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, openTime, longitude, latitude, itemsLeft, closeTime, marketType);
    }
}
