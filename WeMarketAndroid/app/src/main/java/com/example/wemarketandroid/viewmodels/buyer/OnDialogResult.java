package com.example.wemarketandroid.viewmodels.buyer;

import com.example.wemarketandroid.models.buyer.Food;

public interface OnDialogResult {
    void handle(Food food, int price);
}
