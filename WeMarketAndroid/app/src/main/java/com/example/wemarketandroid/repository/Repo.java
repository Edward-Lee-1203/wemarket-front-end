package com.example.wemarketandroid.repository;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.wemarketandroid.R;
import com.example.wemarketandroid.models.AuthResponse;
import com.example.wemarketandroid.models.Delivery;
import com.example.wemarketandroid.models.Filter;
import com.example.wemarketandroid.models.Food;
import com.example.wemarketandroid.models.Market;
import com.example.wemarketandroid.models.Order;
import com.example.wemarketandroid.models.OrderDetail;
import com.example.wemarketandroid.models.Shipper;
import com.example.wemarketandroid.models.SignInRequest;
import com.example.wemarketandroid.models.SignInShipperResponse;
import com.example.wemarketandroid.models.SignInUserResponse;
import com.example.wemarketandroid.models.User;
import com.example.wemarketandroid.repository.services.RetrofitClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repo {
    // these list will be fetched on creation of this repo to ensure data sharing
    private static final int N_THREADS = 4;
    private static Repo INSTANCE;
    private RetrofitClient mRetrofitClient;
    private ExecutorService mExecutorService;

    private MutableLiveData<List<Market>> mMarketList;
    private MutableLiveData<List<Food>> mFoodList;
    private MutableLiveData<User> mUser;
    private MutableLiveData<Shipper> mShipper;
    private MutableLiveData<List<Delivery>> mDeliveryList;
    private MutableLiveData<List<OrderDetail>> mOrderDetailList;
//    private LiveData<List<Shipper>> mShipperList;
    // contains deliveries matching mShipper's id
    // serverless demo: using MutableLiveData
    // TODO: moving to select from list to confirm
//    private MutableLiveData<Delivery> mShipperDelivery;

    private Repo() {
        mExecutorService = Executors.newFixedThreadPool(N_THREADS);
        mRetrofitClient = RetrofitClient.getInstance();
        // initialize components
        mMarketList = new MutableLiveData<>();
        mFoodList = new MutableLiveData<>();
        mUser = new MutableLiveData<>();
        mShipper = new MutableLiveData<>();
        mDeliveryList = new MutableLiveData<>();
        mOrderDetailList = new MutableLiveData<>();

    }

    public static Repo getInstance(){
        if(INSTANCE==null){
            INSTANCE = new Repo();
        }
        return INSTANCE;
    }

    public LiveData<List<Market>> getMarketList(Context context) {
        mRetrofitClient.getmService().getMarkets().enqueue(new Callback<List<Market>>() {
            @Override
            public void onResponse(Call<List<Market>> call, Response<List<Market>> response) {
                if(response.code()==200){
                    mMarketList.postValue(response.body());     // save the markets to live data
                } else{
                    Toast.makeText(context, response.code()+", "+response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Market>> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        return mMarketList;
    }

    public LiveData<Market> getMarketById(long marketId){
        return Transformations.map(mMarketList, new Function<List<Market>, Market>() {
            @Override
            public Market apply(List<Market> input) {
                for(Market market : input)
                    if(market.getId()==marketId)
                        return market;
                return null;
            }
        });
    }

    public LiveData<List<Food>> getFoodList(Context context) {
        mRetrofitClient.getmService().getFoods().enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if(response.code()==200){
                    mFoodList.postValue(response.body());
                } else{
                    Toast.makeText(context, response.code()+", "+response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        return mFoodList;
    }

    public LiveData<Food> getFoodById(long foodId){
        LiveData<Food> foodLiveData = Transformations.map(mFoodList, new Function<List<Food>, Food>() {
            @Override
            public Food apply(List<Food> input) {
                for(Food food : input)
                    if(food.getId()==foodId)
                        return food;
                return null;
            }
        });
        return foodLiveData;
    }

    /**
     *
     * @param marketId
     * @return
     */
    public LiveData<List<Food>> getFoodsByMarketId(long marketId){
        LiveData<List<Food>> foodsLiveData = Transformations.map(mFoodList, new Function<List<Food>, List<Food>>() {
            @Override
            public List<Food> apply(List<Food> input) {
                ArrayList<Food> foodArrayList = new ArrayList<>();
                for(Food food : input)
                    if(food.getMarket().getId()==marketId)
                        foodArrayList.add(food);
                return foodArrayList;
            }
        });
        return foodsLiveData;
    }

//    public LiveData<List<Food>> getFoodByIdList(List<Integer> idList){
//        return Transformations.map(mFoodList, new Function<List<Food>, List<Food>>() {
//            @Override
//            public List<Food> apply(List<Food> input) {
//                ArrayList<Food> foodArrayList = new ArrayList<>();
//                for(Integer id : idList){
//                    for(Food food : input){
//                        if(food.getId() == id){
//                            foodArrayList.add(food);
//                            break;
//                        }
//                    }
//                }
//                return foodArrayList;
//            }
//        });
//    }
    // not production code
    public void refreshDeliveryList(){
        mDeliveryList.postValue(mDeliveryList.getValue());
    }

    public LiveData<User> getUser(){ return mUser; }

    public LiveData<Shipper> getmShipper(){return mShipper;}

    public LiveData<User> loginBuyer(String username, String password, Handler handler, Context context){
        // TODO: API login the buyer
//        mUser = getSeedUser();
        mRetrofitClient.getmService().signInUser(new SignInRequest(username,password)).enqueue(new Callback<SignInUserResponse>() {
            @Override
            public void onResponse(Call<SignInUserResponse> call, Response<SignInUserResponse> response) {
                SignInUserResponse signInUserResponse = response.body();
                if(response.code()==200 && signInUserResponse.getUser()!=null){
                    mUser.postValue(signInUserResponse.getUser());
                    mRetrofitClient.setTOKEN(signInUserResponse.getAccessToken());
                } else{
//                    activity.runOnUiThread(()->{Toast.makeText(activity.getApplicationContext(), response.code() + ", " + response.message(), Toast.LENGTH_SHORT).show();});
                    handler.post(()->{Toast.makeText(context, response.code() + ", " + response.message(), Toast.LENGTH_SHORT).show();});
                }
            }

            @Override
            public void onFailure(Call<SignInUserResponse> call, Throwable t) {
                handler.post(()->{Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();});
            }
        });
        return mUser;
    }
    public LiveData<Shipper> loginShipper(String username, String password, Activity activity){
        mRetrofitClient.getmService().signInShipper(new SignInRequest(username,password)).enqueue(new Callback<SignInShipperResponse>() {
            @Override
            public void onResponse(Call<SignInShipperResponse> call, Response<SignInShipperResponse> response) {
                SignInShipperResponse signInShipperResponse = response.body();
                if(response.code()==200 && signInShipperResponse.getShipper()!=null){
                    mShipper.postValue(signInShipperResponse.getShipper());
                    mRetrofitClient.setTOKEN(signInShipperResponse.getAccessToken());
                } else{
                    activity.runOnUiThread(()->{Toast.makeText(activity.getApplicationContext(), response.code() + ", " + response.message(), Toast.LENGTH_SHORT).show();});
                }
            }

            @Override
            public void onFailure(Call<SignInShipperResponse> call, Throwable t) {
                activity.runOnUiThread(()->{Toast.makeText(activity.getApplicationContext(), t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();});
            }
        });
        return mShipper;
    }




//    public LiveData<List<Shipper>> getShipperList(){ return mShipperList; }

    public LiveData<List<Delivery>> getDeliveryList(Context context){
        mRetrofitClient.getmService().getDeliveries().enqueue(new Callback<List<Delivery>>() {
            @Override
            public void onResponse(Call<List<Delivery>> call, Response<List<Delivery>> response) {
                if(response.isSuccessful() && response.code()==200){
                    mDeliveryList.postValue(response.body());
                } else{
                    Toast.makeText(context, response.code()+", "+response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Delivery>> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        return mDeliveryList;
    }
    public LiveData<List<Delivery>> getDeliveryByUser(User user){
        return Transformations.map(mDeliveryList, new Function<List<Delivery>, List<Delivery>>() {
            @Override
            public List<Delivery> apply(List<Delivery> input) {
                ArrayList<Delivery> deliveryArrayList = new ArrayList<>();
                for(Delivery delivery : input)
                    if(delivery.getUser().equals(user))
                        deliveryArrayList.add(delivery);
                return deliveryArrayList;
            }
        });
    }
    public LiveData<List<Delivery>> getDeliveryByShipper(Shipper shipper){
        return Transformations.map(mDeliveryList, new Function<List<Delivery>, List<Delivery>>() {
            @Override
            public List<Delivery> apply(List<Delivery> input) {
                ArrayList<Delivery> deliveryArrayList = new ArrayList<>();
                for(Delivery delivery : input)
                    if(delivery.getShipper()!=null && delivery.getShipper().equals(shipper))
                        deliveryArrayList.add(delivery);
                return deliveryArrayList;
            }
        });
    }

    public void getOrderDetails(Context context) {
        mRetrofitClient.getmService().getOrderDetails().enqueue(new Callback<List<OrderDetail>>() {
            @Override
            public void onResponse(Call<List<OrderDetail>> call, Response<List<OrderDetail>> response) {
                if(response.isSuccessful() && response.code()==200){
                    mOrderDetailList.postValue(response.body());
                } else{
                    Toast.makeText(context, response.code()+", "+response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderDetail>> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public LiveData<List<OrderDetail>> getOrderDetailsByOrderId(long orderId){
        return Transformations.map(mOrderDetailList, new Function<List<OrderDetail>, List<OrderDetail>>() {
            @Override
            public List<OrderDetail> apply(List<OrderDetail> input) {
                ArrayList<OrderDetail> orderDetailArrayList = new ArrayList<>();
                for(OrderDetail orderDetail : input){
                    if(orderDetail.getOrder().getId()==orderId)
                        orderDetailArrayList.add(orderDetail);
                }
                return orderDetailArrayList;
            }
        });
    }

    public RetrofitClient getmRetrofitClient(){return mRetrofitClient;}

    public void insertDelivery(Delivery delivery, Context context){
//        List<Delivery> deliveries = mDeliveryList.getValue();
//        if(deliveries==null){
//            deliveries = new ArrayList<>();
//        }
//        deliveries.add(delivery);
//        mDeliveryList.postValue(deliveries);
        mRetrofitClient.getmService().saveDelivery(delivery).enqueue(new Callback<Delivery>() {
            @Override
            public void onResponse(Call<Delivery> call, Response<Delivery> response) {
                if(response.isSuccessful() && response.code()==200){
                    List<Delivery> deliveries = mDeliveryList.getValue();
                    deliveries.add(response.body());
                    mDeliveryList.postValue(deliveries);
                } else{
                    Toast.makeText(context, response.code()+", "+response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Delivery> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void saveUser(User user, Activity activity){
        mRetrofitClient.getmService().saveUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // TODO: post new user object
                if(!response.isSuccessful() && response.code()!=200){
                    mUser.postValue(response.body());
                    activity.runOnUiThread(()->{Toast.makeText(activity.getApplicationContext(), response.code() + ", " + response.message(), Toast.LENGTH_SHORT).show();});
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                activity.runOnUiThread(()->{Toast.makeText(activity.getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();});
            }
        });
    }

    public void saveShipper(Shipper shipper, Activity activity){
        mRetrofitClient.getmService().saveShipper(shipper).enqueue(new Callback<Shipper>() {
            @Override
            public void onResponse(Call<Shipper> call, Response<Shipper> response) {
                if(!response.isSuccessful() && response.code()!=200){
                    activity.runOnUiThread(()->{Toast.makeText(activity.getApplicationContext(), response.code() + ", " + response.message(), Toast.LENGTH_SHORT).show();});
                } else{

                    mShipper.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Shipper> call, Throwable t) {
                activity.runOnUiThread(()->{Toast.makeText(activity.getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();});
            }
        });
    }

    public void saveDelivery(Delivery delivery, Context context){
        mRetrofitClient.getmService().saveDelivery(delivery).enqueue(new Callback<Delivery>() {
            @Override
            public void onResponse(Call<Delivery> call, Response<Delivery> response) {
                if(response.isSuccessful() && response.code()==200){
                    getDeliveryList(context);   // refreshs deliveries
                } else{
                    Toast.makeText(context, response.code()+", "+response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Delivery> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public MutableLiveData<Delivery> getmShipperDelivery() {
//        // TODO: implement synchronize with server to get newest delivery & refactor this to List, also refactor the UI
//        return mShipperDelivery;
//    }

    public Filter[] getHomeFilters(){
        return new Filter[]{
                new Filter(R.drawable.icon_heart,"Favourite"),
                new Filter(R.drawable.icon_pricetag,"Cheap"),
                new Filter(R.drawable.icon_trending,"Trend"),
                new Filter(R.drawable.icon_grid,"More")
        };
    }
    public Filter[] getChooseFoodFilters(){
        return new Filter[]{
                new Filter(R.drawable.icon_settings,"Filters"),
                new Filter(R.drawable.icon_nearby,"Nearby"),
                new Filter(R.drawable.icon_star,"Above 4.5"),
                new Filter(R.drawable.icon_pricetag,"Cheap"),
                new Filter(R.drawable.icon_heart,"Favourite")
        };
    }

}
