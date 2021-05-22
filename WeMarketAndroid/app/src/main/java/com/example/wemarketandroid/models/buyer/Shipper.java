package com.example.wemarketandroid.models.buyer;

import java.util.Objects;

public class Shipper implements IDiffable{

    private int id;
    private String workingStatus;
    private String phone;
    private String name;
    private String username;
    private String password;

    public Shipper(int id, String workingStatus, String phone, String name, String username, String password) {
        this.id = id;
        this.workingStatus = workingStatus;
        this.phone = phone;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getWorkingStatus() {
        return workingStatus;
    }

    public void setWorkingStatus(String workingStatus) {
        this.workingStatus = workingStatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shipper shipper = (Shipper) o;
        return id == shipper.id &&
                Objects.equals(workingStatus, shipper.workingStatus) &&
                Objects.equals(phone, shipper.phone) &&
                Objects.equals(name, shipper.name) &&
                Objects.equals(username, shipper.username) &&
                Objects.equals(password, shipper.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workingStatus, phone, name, username, password);
    }
}
