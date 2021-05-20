package com.example.wemarketandroid.models.buyer;

public class Filter {
    private int resId;
    private String label;

    public Filter(int resId, String label) {
        this.resId = resId;
        this.label = label;
    }

    public int getResId() {
        return resId;
    }

    public String getLabel() {
        return label;
    }
}
