package com.example.wemarketandroid.viewmodels.buyer;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.wemarketandroid.models.CartItem;
import com.example.wemarketandroid.models.Delivery;
import com.example.wemarketandroid.models.Food;
import com.example.wemarketandroid.models.Order;
import com.example.wemarketandroid.models.OrderDetail;
import com.example.wemarketandroid.models.User;
import com.example.wemarketandroid.repository.Repo;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartSharedViewModel extends ViewModel {

    private MutableLiveData<HashMap<Integer, CartItem>> cartItems;
    private LiveData<Integer> cartTotalCost;
    private LiveData<Integer> cartItemCount;
    private MutableLiveData<Boolean> isCheckoutConfirmed;
    private MutableLiveData<String> mAddressLiveData;
    private MutableLiveData<Location> mLocationLiveData;
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
        mAddressLiveData = new MutableLiveData<>();
        mLocationLiveData = new MutableLiveData<>();
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
            cartItems.getValue().replace(food.getId().intValue(), new CartItem(food,price));
        } else {
            cartItems.getValue().put(food.getId().intValue(), new CartItem(food,price));
        }
        notifyFoodsChanged();
    }

    public void removeFromCart(long foodId){
        if(cartItems.getValue().containsKey(foodId)) {
            cartItems.getValue().remove(foodId);
            notifyFoodsChanged();
        }
    }
    public void makeDelivery(Context context){
        Order order = new Order(null,cartTotalCost.getValue());
        mRepo.getmRetrofitClient().getmService().saveOrder(order).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if(response.isSuccessful() && response.code()==200){
                    Order newOrder = response.body();
                    HashMap<Integer, CartItem> cartItemHashMap = cartItems.getValue();
                    for(CartItem cartItem : cartItemHashMap.values()){
                        OrderDetail orderDetail = new OrderDetail(null,cartItem.getFood(),newOrder,cartItem.getQuantity());
                        mRepo.getmRetrofitClient().getmService().saveOrderDetail(orderDetail).enqueue(new Callback<OrderDetail>() {
                            @Override
                            public void onResponse(Call<OrderDetail> call, Response<OrderDetail> response) {
                                if(!response.isSuccessful() || response.code()!=200){
                                    Toast.makeText(context, response.code()+", "+response.message(),Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<OrderDetail> call, Throwable t) {
                                Toast.makeText(context, t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    // creating a delivery object
                    String address = mAddressLiveData.getValue();
                    User user = mRepo.getUser().getValue();
                    Location location = mLocationLiveData.getValue();
                    Delivery delivery = new Delivery(null,null,user,newOrder,address,location.getLatitude(), location.getLongitude());
                    // saves to server
                    mRepo.getmRetrofitClient().getmService().saveDelivery(delivery).enqueue(new Callback<Delivery>() {
                        @Override
                        public void onResponse(Call<Delivery> call, Response<Delivery> response) {
                            if(!response.isSuccessful() || response.code()!=200){
                                Toast.makeText(context, response.code()+", "+response.message(),Toast.LENGTH_SHORT).show();
                            } else{
                                initComponents();   // clears cart content
                            }
                        }

                        @Override
                        public void onFailure(Call<Delivery> call, Throwable t) {
                            Toast.makeText(context, t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                } else{
                    Toast.makeText(context, response.code()+", "+response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public LiveData<Integer> getCartTotalCost(){
        return cartTotalCost;
    }

    public LiveData<Integer> getCartItemCount() {
        return cartItemCount;
    }

    public LiveData<Boolean> getIsCheckoutConfirmed(){ return isCheckoutConfirmed; }
    public void setIsCheckoutConfirmed(Boolean isCheckoutConfirmed){ this.isCheckoutConfirmed.postValue(isCheckoutConfirmed);}

    public MutableLiveData<String> getmAddressLiveData() {
        return mAddressLiveData;
    }

    public MutableLiveData<Location> getmLocationLiveData() {
        return mLocationLiveData;
    }
}
