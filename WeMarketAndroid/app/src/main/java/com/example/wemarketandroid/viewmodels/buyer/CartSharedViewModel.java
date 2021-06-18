package com.example.wemarketandroid.viewmodels.buyer;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.wemarketandroid.models.CartItem;
import com.example.wemarketandroid.models.Food;
import com.example.wemarketandroid.repository.Repo;

import java.util.HashMap;

public class CartSharedViewModel extends ViewModel {

    private MutableLiveData<HashMap<Integer, CartItem>> cartItems;
    private LiveData<Integer> cartTotalCost;
    private LiveData<Integer> cartItemCount;
    private MutableLiveData<Boolean> isCheckoutConfirmed;
    private Repo mRepo;

    private void notifyFoodsChanged(){ cartItems.postValue(cartItems.getValue()); }

    public CartSharedViewModel() {
        mRepo = Repo.getInstance();
        initComponents();
    }
    private void initComponents(){
        cartItems = new MutableLiveData<>();
        cartItems.postValue(new HashMap<>());
        cartTotalCost = Transformations.map(cartItems, new Function<HashMap<Integer, CartItem>, Integer>() {
            @Override
            public Integer apply(HashMap<Integer, CartItem> input) {
                int sum = 0;
                for(CartItem cartItem : input.values()) sum+=cartItem.getPrice();
                return sum;
            }
        });
        cartItemCount = Transformations.map(cartItems, new Function<HashMap<Integer, CartItem>, Integer>() {
            @Override
            public Integer apply(HashMap<Integer, CartItem> input) {
                return input.size();
            }
        });
        isCheckoutConfirmed = new MutableLiveData<>();
        isCheckoutConfirmed.postValue(null);
    }

    public void clearCart(){
        initComponents();
    }

    public MutableLiveData<HashMap<Integer, CartItem>> getCartItems() {
        return cartItems;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addToCart(Food food, int price){
        if(cartItems.getValue().containsKey(food.getId())){
            cartItems.getValue().replace(food.getId(), new CartItem(food,price));
        } else {
            cartItems.getValue().put(food.getId(), new CartItem(food,price));
        }
        notifyFoodsChanged();
    }

    public void removeFromCart(int foodId){
        if(cartItems.getValue().containsKey(foodId)) {
            cartItems.getValue().remove(foodId);
            notifyFoodsChanged();
        }
    }

    public LiveData<Integer> getCartTotalCost(){
        return cartTotalCost;
    }

    public LiveData<Integer> getCartItemCount() {
        return cartItemCount;
    }

    public LiveData<Boolean> getIsCheckoutConfirmed(){ return isCheckoutConfirmed; }
    public void setIsCheckoutConfirmed(Boolean isCheckoutConfirmed){ this.isCheckoutConfirmed.postValue(isCheckoutConfirmed);}
}
