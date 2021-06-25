package com.example.wemarketandroid.models;

public enum DeliveryStatus {
    DELIVERED("delivered"),
    DELIVERING("delivering"),
    CONFIRMING("confirming"),
    CANCEL("cancel");


    private final String text;
    DeliveryStatus(final String text) {
        this.text = text;
    }
    @Override
    public String toString() {
        return text;
    }
}
