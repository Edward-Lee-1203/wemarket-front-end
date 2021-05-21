package com.example.wemarketandroid.viewmodels.buyer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wemarketandroid.models.buyer.Food;

import java.util.ArrayList;
import java.util.List;

public class CartSharedViewModel extends ViewModel {

    private MutableLiveData<List<Food>> foods;

    private void notifyFoodsChanged(){ foods.postValue(foods.getValue()); }

    public CartSharedViewModel() {
        foods = new MutableLiveData<>();
        foods.postValue(new ArrayList<>());
    }
    public LiveData<List<Food>> getFoods(){ return foods; }

    public void addFood(Food food){
        foods.getValue().add(food);
        notifyFoodsChanged();
    }

    public void removeFood(Food food){
        foods.getValue().remove(food);
        notifyFoodsChanged();
    }
}
