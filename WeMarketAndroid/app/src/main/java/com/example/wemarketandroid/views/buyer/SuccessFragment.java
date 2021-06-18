package com.example.wemarketandroid.views.buyer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;

import com.example.wemarketandroid.R;
import com.example.wemarketandroid.databinding.FragmentBuyerSuccessBinding;
import com.example.wemarketandroid.models.CartItem;
import com.example.wemarketandroid.models.Delivery;
import com.example.wemarketandroid.models.Order;
import com.example.wemarketandroid.models.OrderDetail;
import com.example.wemarketandroid.models.Shipper;
import com.example.wemarketandroid.models.User;
import com.example.wemarketandroid.repository.Repo;
import com.example.wemarketandroid.viewmodels.buyer.CartSharedViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SuccessFragment extends Fragment {

    private MainActivity mContainingActivity;
    private FragmentBuyerSuccessBinding mViewBinding;
    private CartSharedViewModel mCartSharedViewModel;
    private Repo mRepo;
    private SimpleDateFormat dateFormat;
    private Delivery mDelivery;

    private void submitDelivery(){
        ArrayList<OrderDetail> orderDetailArrayList = new ArrayList<>();
        Order order = new Order((int)(Math.random()*1000),0);
        for(CartItem cartItem : mCartSharedViewModel.getCartItems().getValue().values()){
            OrderDetail orderDetail = new OrderDetail((int)(Math.random()*1000),cartItem.getId(),order.getId(),cartItem.getQuantity());
            orderDetail.setFood(cartItem.getFood());
            orderDetailArrayList.add(orderDetail);
            order.setTotalPrice(order.getTotalPrice()+cartItem.getPrice());
        }
        order.setOrderDetailList(orderDetailArrayList);
        Shipper shipper = mRepo.getShipperList().getValue().get(0);
        User currentUser = mRepo.getUser().getValue();
        String dateString = dateFormat.format(new Date());
        Delivery delivery = new Delivery((int)(Math.random()*1000), shipper.getId(), currentUser.getId(),order.getId(),null,0,dateString,false,false,null);
        delivery.setOrder(order);
        delivery.setShipper(shipper);
        delivery.setUser(currentUser);
        mDelivery = delivery;
        mRepo.insertDelivery(delivery);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewBinding = FragmentBuyerSuccessBinding.inflate(inflater,container,false);
        View root = mViewBinding.getRoot();
        mContainingActivity = (MainActivity)getActivity();
        mCartSharedViewModel = new ViewModelProvider(getActivity()).get(CartSharedViewModel.class);
        mRepo = Repo.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        // observes and handles cart checkout confirmed status
        mCartSharedViewModel.getIsCheckoutConfirmed().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isCheckoutConfirmed) {
                NavController navController = mContainingActivity.getNavController();
                if(isCheckoutConfirmed==null){  // checks if user hasn't enter checkout page yet
                    // navigates to checkout page
                    navController.navigate(R.id.destination_buyer_checkout);
                } else if (isCheckoutConfirmed == false){   // checks if user didn't click the place order button
                    // navigates to start destination and reset back stack
                    int startDestination = navController.getGraph().getStartDestination();
                    NavOptions navOptions = new NavOptions.Builder().setPopUpTo(startDestination, true).build();
                    // sends user to choose food page
                    navController.navigate(R.id.destination_buyer_choose_food, null, navOptions);
                    mCartSharedViewModel.clearCart();   // clears cart content
                } else{
                    submitDelivery();
                    mCartSharedViewModel.clearCart();   // clears cart content
                }
            }
        });
        // registers check order button click handler
        mViewBinding.buttonBuyerSuccessCheckOrderStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = mContainingActivity.getNavController();
                int startDestination = navController.getGraph().getStartDestination();
                NavOptions navOptions = new NavOptions.Builder().setPopUpTo(startDestination, true).build();
                // auto complete the delivery
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep((int)(Math.random()*5000));
                            mDelivery.setConfirmed(true);
                            mRepo.refreshDeliveryList();
                        } catch(Exception err) {
                            Log.d("SuccessFragment", err.toString());
                        }
                    }
                }).start();
                // TODO: sends user straight to orders page at home fragment
                navController.navigate(R.id.destination_buyer_orders, null, navOptions);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mContainingActivity.getmBottomNavBar().setVisibility(View.GONE);
        mContainingActivity.getmToolbar().setVisibility(View.INVISIBLE);
        Menu menu = mContainingActivity.getmToolbar().getMenu();
        for(int i = 0; i<menu.size();++i) {
            menu.getItem(i).setVisible(true);
        }
    }

    // Template code
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mViewBinding = null;
    }


}