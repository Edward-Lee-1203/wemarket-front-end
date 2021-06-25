package com.example.wemarketandroid.models;

public enum UserStatus {
    ACTIVE("ACTIVE"), NONACTIVE("NONACTIVE");
    private String text;
    UserStatus(String txt){
        text = txt;
    }
    @Override
    public String toString(){return text;}
}
