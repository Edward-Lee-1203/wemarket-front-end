package com.example.wemarketandroid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Shipper implements IDiffable{

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("shipper_status")
    @Expose
    private String shipperStatus;
    @SerializedName("roles")
    @Expose
    private List<String> roles = null;

    public Shipper() {
    }

    public Shipper(Long id, String name, String phone, String username, Double longitude, Double latitude, String password, String shipperStatus, List<String> roles) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.username = username;
        this.longitude = longitude;
        this.latitude = latitude;
        this.password = password;
        this.shipperStatus = shipperStatus;
        this.roles = roles;
    }

    @Override
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShipperStatus() {
        return shipperStatus;
    }

    public void setShipperStatus(String shipperStatus) {
        this.shipperStatus = shipperStatus;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shipper shipper = (Shipper) o;
        return Objects.equals(id, shipper.id) &&
                Objects.equals(name, shipper.name) &&
                Objects.equals(phone, shipper.phone) &&
                Objects.equals(username, shipper.username) &&
                Objects.equals(longitude, shipper.longitude) &&
                Objects.equals(latitude, shipper.latitude) &&
                Objects.equals(password, shipper.password) &&
                Objects.equals(shipperStatus, shipper.shipperStatus) &&
                Objects.equals(roles, shipper.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phone, username, longitude, latitude, password, shipperStatus, roles);
    }
}
