package com.example.wemarketandroid.repository.services;

import com.example.wemarketandroid.models.AuthResponse;
import com.example.wemarketandroid.models.Delivery;
import com.example.wemarketandroid.models.Food;
import com.example.wemarketandroid.models.Market;
import com.example.wemarketandroid.models.Order;
import com.example.wemarketandroid.models.OrderDetail;
import com.example.wemarketandroid.models.Shipper;
import com.example.wemarketandroid.models.SignInRequest;
import com.example.wemarketandroid.models.SignInShipperResponse;
import com.example.wemarketandroid.models.SignInUserResponse;
import com.example.wemarketandroid.models.SignUpRequest;
import com.example.wemarketandroid.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebService {
    /**
     * Market
     */
    @GET("/api/market")
    Call<List<Market>> getMarkets();
    @GET("/api/market/{id}")
    Call<Market> getMarketById(@Path("id") Long id);
    @POST("/api/market")
    Call<Market> saveMarket(@Body Market market);
    /**
     * Product
     */
    @GET("/api/product")
    Call<List<Food>> getFoods();
    @GET("/api/product/{id}")
    Call<Food> getFoodById(@Path("id") Long id);
    @GET("/api/product/market/{id}")
    Call<List<Food>> getFoodsByMarketId(@Path("id") Long id);
    @POST("/api/product")
    Call<Food> saveFood(@Body Food food);
    /**
     * OrderDetail
     */
    @GET("/api/orderDetail")
    Call<List<OrderDetail>> getOrderDetails();
    @GET("/api/orderDetail/{id}")
    Call<OrderDetail> getOrderDetailById(@Path("id") Long id);
    @POST("/api/orderDetail")
    Call<OrderDetail> saveOrderDetail(@Body OrderDetail orderDetail);
    /**
     * Order
     */
    @GET("/api/order")
    Call<List<Order>> getOrders();
    @GET("/api/order/{id}")
    Call<Order> getOrderById(@Path("id") Long id);
    @POST("/api/order")
    Call<Order> saveOrder(@Body Order order);
    /**
     * Delivery
     */
    @GET("/api/delivery")
    Call<List<Delivery>> getDeliveries();
    @GET("/api/delivery/{id}")
    Call<Delivery> getDeliveryById(@Path("id") Long id);
    @POST("/api/delivery")
    Call<Delivery> saveDelivery(@Body Delivery delivery);
//    /**
//     * Shipper
//     */
//    Call<List<Shipper>> getShippers();
//    Call<Shipper> getShipperById(Long id);
    @POST("/api/shipper")
    Call<Shipper> saveShipper(@Body Shipper shipper);
//    /**
//     * User
//     */
//    Call<List<User>> getUsers();
//    Call<User> getUserById(Long id);
    @POST("/api/user")
    Call<User> saveUser(@Body User user);
    /**
     * Auth
     */
    @POST("/api/auth/signin")
    Call<SignInUserResponse> signInUser(@Body SignInRequest signInRequest);
    @POST("/api/auth/signin")
    Call<SignInShipperResponse> signInShipper(@Body SignInRequest signInRequest);
    @POST("/api/auth/signup")
    Call<User> signUpUser(@Body SignUpRequest signUpRequest);
    @POST("/api/auth/signup")
    Call<Shipper> signUpShipper(@Body SignUpRequest signUpRequest);

}
