package com.example.wemarketandroid.models;

import java.util.Objects;

public class User implements IDiffable {
    private int id;
    private String name;
    private String phone;
    private String username;
    private String password;
    private boolean isMale;
    private String userStatus;

    public User(int id, String name, String phone, String username, String password, boolean isMale, String userStatus) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.isMale = isMale;
        this.userStatus = userStatus;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                isMale == user.isMale &&
                Objects.equals(name, user.name) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(userStatus, user.userStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phone, username, password, isMale, userStatus);
    }
}
