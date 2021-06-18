package com.example.wemarketandroid.repository;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.wemarketandroid.R;
import com.example.wemarketandroid.models.Delivery;
import com.example.wemarketandroid.models.Filter;
import com.example.wemarketandroid.models.Food;
import com.example.wemarketandroid.models.Market;
import com.example.wemarketandroid.models.Order;
import com.example.wemarketandroid.models.OrderDetail;
import com.example.wemarketandroid.models.Shipper;
import com.example.wemarketandroid.models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Repo {
    // these list will be fetched on creation of this repo to ensure data sharing
    // TODO: refactor these List<> to HashMap<>
    private LiveData<List<Market>> mMarketList;
    private LiveData<List<Food>> mFoodList;
    private LiveData<User> mUser;
    private LiveData<Shipper> mShipper;
    private static Repo INSTANCE;
    // TODO: creates LiveData<List<Delivery>>
    private MutableLiveData<List<Delivery>> mDeliveryList;
    private LiveData<List<Shipper>> mShipperList;

    private LiveData<User> getSeedUser(){
        // initializes user data
        MutableLiveData<User> mutableLiveDataUser = new MutableLiveData<>();
        mutableLiveDataUser.postValue(new User(1,"Mick",null,"mickgordon","123",true,null));
        return mutableLiveDataUser;
    }
    private LiveData<Shipper> getSeedShipper(){
        // initializes user data
        MutableLiveData<Shipper> mutableLiveDataShipper = new MutableLiveData<>();
        mutableLiveDataShipper.postValue(new Shipper(1,null, null, "Fort","fortminor","123"));
        return mutableLiveDataShipper;
    }

    private LiveData<List<Shipper>> getSeedShipperList(){
        MutableLiveData<List<Shipper>> shipperMutableLiveData = new MutableLiveData<>();
        List<Shipper> shippers = new ArrayList<>();
        shippers.add(new Shipper(1, null, null, "Nguyễn Văn A", null, null));
        shipperMutableLiveData.postValue(shippers);
        return shipperMutableLiveData;
    }

    public void seedDeliveryList(LifecycleOwner lifecycleOwner){
        mUser.observe(lifecycleOwner, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user==null) return;
                ArrayList<Delivery> deliveries = new ArrayList<>();
                Shipper shipper = new Shipper(1, null, null, "Nguyễn Văn A", null, null);
                Order order = new Order(1,0);
                List<Food> foods = mFoodList.getValue();
                ArrayList<OrderDetail> orderDetailArrayList = new ArrayList<>();
                OrderDetail orderDetail = null;
                Food food = null;
                food = foods.get(0);
                orderDetail = new OrderDetail(1,food.getId(),order.getId(),2);
                orderDetail.setFood(food);
                order.setTotalPrice(order.getTotalPrice()+food.getPrice()*2);
                orderDetailArrayList.add(orderDetail);
                food = foods.get(3);
                orderDetail = new OrderDetail(2,food.getId(),order.getId(),1);
                orderDetail.setFood(food);
                order.setTotalPrice(order.getTotalPrice()+food.getPrice());
                orderDetailArrayList.add(orderDetail);
                order.setOrderDetailList(orderDetailArrayList);
                Date now = new Date();
                String dateString = "23/05/2021";
                Delivery delivery = new Delivery(1,shipper.getId(),user.getId(),order.getId(),null,0, dateString,false,false,null);
                delivery.setShipper(shipper);
                delivery.setUser(user);
                delivery.setOrder(order);
                deliveries.add(delivery);
                mDeliveryList.postValue(deliveries);
            }
        });
    }

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

        // initializes live data objects
        mMarketList = mutableLiveDataMarketList;
        mFoodList = mutableLiveDataFoodList;
        mUser = new MutableLiveData<>();
        mDeliveryList = new MutableLiveData<>();
        mShipperList = getSeedShipperList();
    }

    public static Repo getInstance(){
        if(INSTANCE==null){
            INSTANCE = new Repo();
        }
        return INSTANCE;
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

    public LiveData<Food> getFoodById(int foodId){
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

    public LiveData<List<Food>> getFoodByIdList(List<Integer> idList){
        return Transformations.map(mFoodList, new Function<List<Food>, List<Food>>() {
            @Override
            public List<Food> apply(List<Food> input) {
                ArrayList<Food> foodArrayList = new ArrayList<>();
                for(Integer id : idList){
                    for(Food food : input){
                        if(food.getId() == id){
                            foodArrayList.add(food);
                            break;
                        }
                    }
                }
                return foodArrayList;
            }
        });
    }
    // not production code
    public void refreshDeliveryList(){
        mDeliveryList.postValue(mDeliveryList.getValue());
    }

    public LiveData<User> getUser(){ return mUser; }

    public LiveData<User> loginBuyer(String username, String password){
        // TODO: API login the buyer
        mUser = getSeedUser();
        return mUser;
    }
    public LiveData<Shipper> loginShipper(String username, String password){
        // TODO: API login the shipper
        mShipper = getSeedShipper();
        return mShipper;
    }

    public LiveData<List<Shipper>> getShipperList(){ return mShipperList; }

    public LiveData<List<Delivery>> getDeliveryList(){ return mDeliveryList; }

    public void insertDelivery(Delivery delivery){
        List<Delivery> deliveries = mDeliveryList.getValue();
        if(deliveries==null){
            deliveries = new ArrayList<>();
        }
        deliveries.add(delivery);
        mDeliveryList.postValue(deliveries);
    }

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
    // TODO: create method to create delivery order
}
