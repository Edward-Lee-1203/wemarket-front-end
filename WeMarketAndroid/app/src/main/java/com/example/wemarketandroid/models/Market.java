package com.example.wemarketandroid.models;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Market implements IDiffable{
    private int id;
    private String name;
    private String address;
//    private double rating;
    private int openTime;
    private int closeTime;
//    private boolean isVerified;
    private double latitude;
    private double longitude;
    private String marketType;
    private List<Food> foodList;

    public Market(int id, String name, String address, int openTime, int closeTime, double latitude, double longitude, String marketType, List<Food> foodList) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.marketType = marketType;
        if(foodList == null) foodList = new LinkedList<>();
        this.foodList = foodList;
    }

    public int getId() {
        return id;
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

    public int getOpenTime() {
        return openTime;
    }

    public void setOpenTime(int openTime) {
        this.openTime = openTime;
    }

    public int getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(int closeTime) {
        this.closeTime = closeTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public String getMarketType() {
        return marketType;
    }

    public void setMarketType(String marketType) {
        this.marketType = marketType;
    }

    //    TODO: update this method to get real distance
    public double getDistance(){
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Market market = (Market) o;
        return id == market.id &&
                openTime == market.openTime &&
                closeTime == market.closeTime &&
                Double.compare(market.latitude, latitude) == 0 &&
                Double.compare(market.longitude, longitude) == 0 &&
                Objects.equals(name, market.name) &&
                Objects.equals(address, market.address) &&
                Objects.equals(marketType, market.marketType) &&
                Objects.equals(foodList, market.foodList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, openTime, closeTime, latitude, longitude, marketType, foodList);
    }
}
