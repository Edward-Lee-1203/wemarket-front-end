package com.example.wemarketandroid.repository;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.wemarketandroid.R;
import com.example.wemarketandroid.models.buyer.Filter;
import com.example.wemarketandroid.models.buyer.Food;
import com.example.wemarketandroid.models.buyer.Market;
import com.example.wemarketandroid.models.buyer.User;

import java.util.LinkedList;
import java.util.List;

public class Repo {
    // these list will be fetched on creation of this repo to ensure data sharing
    private final LiveData<List<Market>> mMarketList;
    private final LiveData<List<Food>> mFoodList;
    private final LiveData<User> mUser;
    private static Repo instance;

    private Repo() {
        MutableLiveData<List<Market>> mutableLiveDataMarketList = new MutableLiveData<>();
        // initializes markets list
        List<Market> marketsStub = new LinkedList<Market>();
        marketsStub.add(new Market(1,"Big C","257 Hùng Vương, Vĩnh Trung, Thanh Khê, Đà Nẵng",8,20,16.06778, 108.22083,"", null));
        marketsStub.add(new Market(2,"Chợ Cồn","257 Hùng Vương, Vĩnh Trung, Thanh Khê, Đà Nẵng",8,20,16.06778, 108.22083,"", null));
        // initializes foods list
        List<Food> foodsStub = new LinkedList<>();
        Food food = null;
        food = new Food(1,"Chicken thigh",""+R.drawable.icon_chicken_legs,18500,0.2);
        food.setMarket(marketsStub.get(0));
        foodsStub.add(food);
        food = new Food(2,"Fish",""+R.drawable.icon_fish,18500,0.2);
        food.setMarket(marketsStub.get(0));
        foodsStub.add(food);
        food = new Food(3,"Fish",""+R.drawable.icon_fish,18500,0.2);
        food.setMarket(marketsStub.get(1));
        foodsStub.add(food);
        food = new Food(4,"Salmon",""+R.drawable.icon_salmon,18500,0.2);
        food.setMarket(marketsStub.get(1));
        foodsStub.add(food);
        marketsStub.get(0).getFoodList().add(foodsStub.get(0));
        marketsStub.get(0).getFoodList().add(foodsStub.get(1));
        marketsStub.get(1).getFoodList().add(foodsStub.get(2));
        marketsStub.get(1).getFoodList().add(foodsStub.get(3));
        mutableLiveDataMarketList.postValue(marketsStub);
        MutableLiveData<List<Food>> mutableLiveDataFoodList = new MutableLiveData<>();
        mutableLiveDataFoodList.postValue(foodsStub);
        // initializes user data
        MutableLiveData<User> mutableLiveDataUser = new MutableLiveData<>();
        mutableLiveDataUser.postValue(new User(1,"Nguyễn Nhật Tùng",null,null,null,true,null));
        // initializes live data objects
        mMarketList = mutableLiveDataMarketList;
        mFoodList = mutableLiveDataFoodList;
        mUser = mutableLiveDataUser;
    }

    public static Repo getInstance(){
        if(instance==null){
            instance = new Repo();
        }
        return instance;
    }

    public LiveData<List<Market>> getMarketList() {
        return mMarketList;
    }

    public LiveData<Market> getMarketById(int marketId){
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

    public LiveData<List<Food>> getFoodList() {
        return mFoodList;
    }

    public LiveData<User> getUser(){ return mUser; }

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
