package com.example.wemarketandroid.viewmodels.buyer;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.wemarketandroid.models.buyer.Food;
import com.example.wemarketandroid.repository.Repo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class CartSharedViewModel extends ViewModel {

    private MutableLiveData<HashMap<Integer,Integer>> foods;
    private LiveData<Integer> cartTotalCost;
    private LiveData<Integer> cartItemCount;
    private Repo mRepo;

    private void notifyFoodsChanged(){ foods.postValue(foods.getValue()); }

    public CartSharedViewModel() {
        foods = new MutableLiveData<>();
        foods.postValue(new HashMap<>());
        mRepo = Repo.getInstance();
        cartTotalCost = Transformations.map(foods, new Function<HashMap<Integer, Integer>, Integer>() {
            @Override
            public Integer apply(HashMap<Integer, Integer> input) {
                int sum = 0;
                for(Integer value : input.values()) sum+=value;
                return sum;
            }
        });
        cartItemCount = Transformations.map(foods, new Function<HashMap<Integer, Integer>, Integer>() {
            @Override
            public Integer apply(HashMap<Integer, Integer> input) {
                return input.size();
            }
        });
    }

    public LiveData<HashMap<Integer, Integer>> getFoods(){ return foods; }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addToCart(int foodId, int price){
        if(foods.getValue().containsKey(foodId)){
            foods.getValue().replace(foodId,price);
        } else {
            foods.getValue().put(foodId, price);
        }
        notifyFoodsChanged();
    }

    public void removeFromCart(int foodId){
        if(foods.getValue().containsKey(foodId)) {
            foods.getValue().remove(foodId);
            notifyFoodsChanged();
        }
    }

    public LiveData<Integer> getCartTotalCost(){
        return cartTotalCost;
    }

    public LiveData<Integer> getCartItemCount() {
        return cartItemCount;
    }
}
