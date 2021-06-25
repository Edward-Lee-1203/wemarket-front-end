package com.example.wemarketandroid.views.shipper;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.example.wemarketandroid.R;
import com.example.wemarketandroid.databinding.FragmentShipperHomeBinding;
import com.example.wemarketandroid.models.Delivery;
import com.example.wemarketandroid.models.Food;
import com.example.wemarketandroid.models.Order;
import com.example.wemarketandroid.models.OrderDetail;
import com.example.wemarketandroid.models.Shipper;
import com.example.wemarketandroid.models.User;
import com.example.wemarketandroid.models.UserStatus;
import com.example.wemarketandroid.viewmodels.buyer.CartSharedViewModel;
import com.example.wemarketandroid.viewmodels.shipper.DeliverySharedViewModel;
import com.example.wemarketandroid.viewmodels.shipper.HomeViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements ChooseDeliveryDialogFragment.ChooseDeliveryListener {

    private MainActivity mContainingActivity;
    private FragmentShipperHomeBinding mViewBinding;
    private DeliverySharedViewModel mViewModel;
    private View.OnClickListener mActivateDriveModeHandler, mCheckOrderStatusHandler;
    private final String CHOOSE_DELIVERY_DIALOG_FRAGMENT_TAG = "CHOOSE_DELIVERY_DIALOG";

    // TODO: setup server synchronizing here

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewBinding = FragmentShipperHomeBinding.inflate(inflater,container,false);
        View rootView = mViewBinding.getRoot();
        mContainingActivity = (MainActivity) getActivity();
        mViewModel = new ViewModelProvider(requireActivity()).get(DeliverySharedViewModel.class);   // initializes shared view model
        mViewBinding.textShipperTitle.setText("Hello, "+mViewModel.getmShipperLiveData().getValue().getName());
        mViewBinding.switchDriveMode.setChecked(mViewModel.isShipperActive());
        // register click handlers
        mViewBinding.switchDriveMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwitchCompat switchCompat = (SwitchCompat)view;
                boolean isChecked = switchCompat.isChecked();
                Shipper shipper = mViewModel.getmShipperLiveData().getValue();
                shipper.setShipperStatus(isChecked?UserStatus.ACTIVE.toString():UserStatus.NONACTIVE.toString());
                mViewModel.saveShipper(shipper,mContainingActivity);
            }
        });
        mActivateDriveModeHandler = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isActive = true;
                mViewBinding.switchDriveMode.setChecked(isActive);
                Shipper shipper = mViewModel.getmShipperLiveData().getValue();
                shipper.setShipperStatus(isActive?UserStatus.ACTIVE.toString():UserStatus.NONACTIVE.toString());
                mViewModel.saveShipper(shipper,mContainingActivity);
            }
        };
        mCheckOrderStatusHandler = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mViewModel.getShipperDeliveryListLiveData().getValue().size()>1){
                    ChooseDeliveryDialogFragment dialogFragment = new ChooseDeliveryDialogFragment();
                    dialogFragment.setTargetFragment(HomeFragment.this, 0);
                    dialogFragment.show(getParentFragmentManager(),CHOOSE_DELIVERY_DIALOG_FRAGMENT_TAG);

                }
                else {
                    Delivery onlyDelivery = mViewModel.getShipperDeliveryListLiveData().getValue().get(0);
                    mViewModel.getmCurrentDeliveryMutableLiveData().postValue(onlyDelivery);
                    mContainingActivity.getNavController().navigate(R.id.destination_shipper_receieve_order);
                }
            }
        };
        mViewModel.getShipperDeliveryListLiveData().observe(getViewLifecycleOwner(), new Observer<List<Delivery>>() {
            @Override
            public void onChanged(List<Delivery> delivery) {
                if(delivery!=null && delivery.size()>0){
                    setupUiWithOrders();
                } else{
                    setupUiWithoutOrders();
                }
            }
        });
        // TODO: observe shared viewmodel's isReceived to deny order or do nothing
        // demo purpose:
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    Thread.sleep(5000);
//                    // create seed delivery
//                    Order order = new Order(1,0);
//                    List<Food> foods = mViewModel.getmRepo().getFoodList().getValue();
//                    ArrayList<OrderDetail> orderDetailArrayList = new ArrayList<>();
//                    OrderDetail orderDetail = null;
//                    Food food = null;
//                    food = foods.get(0);
//                    orderDetail = new OrderDetail(1,food.getId(),order.getId(),2);
//                    orderDetail.setFood(food);
//                    order.setTotalPrice(order.getTotalPrice()+food.getPrice()*2);
//                    orderDetailArrayList.add(orderDetail);
//                    food = foods.get(3);
//                    orderDetail = new OrderDetail(2,food.getId(),order.getId(),1);
//                    orderDetail.setFood(food);
//                    order.setTotalPrice(order.getTotalPrice()+food.getPrice());
//                    orderDetailArrayList.add(orderDetail);
//                    order.setOrderDetailList(orderDetailArrayList);
//                    Date now = new Date();
//                    String dateString = "23/05/2021";
//                    User user = new User(1,"Mark","012345678",null,null,true,null);
//                    Shipper shipper = mViewModel.getmRepo().getmShipper().getValue();
//                    Delivery delivery = new Delivery(1,shipper.getId(),user.getId(),order.getId(),null,0, dateString,false,false,null);
//                    delivery.setShipper(shipper);
//                    delivery.setUser(user);
//                    delivery.setOrder(order);
//                    mViewModel.getmRepo().getmShipperDelivery().postValue(delivery);
//                } catch(Exception err){
//                    // pass
//                }
//            }
//        }).start();

        return rootView;
    }

    private void setupUiWithoutOrders(){
        mViewBinding.imageShipperHome.setImageResource(R.drawable.delivery_image);
        mViewBinding.textShipperHomeMessage.setText(R.string.shipper_zero_orders_message);
        mViewBinding.textShipperHomeSubmessage.setText(R.string.shipper_zero_orders_submessage);
        mViewBinding.buttonShipperHome.setText(R.string.shipper_zero_orders_button_text);
        mViewBinding.buttonShipperHome.setOnClickListener(mActivateDriveModeHandler);
    }
    private void setupUiWithOrders(){
        mViewBinding.imageShipperHome.setImageResource(R.drawable.confirm_image);
        mViewBinding.textShipperHomeMessage.setText(R.string.shipper_arrived_orders_message);
        mViewBinding.textShipperHomeSubmessage.setText(R.string.shipper_arrived_orders_submessage);
        mViewBinding.buttonShipperHome.setText(R.string.shipper_arrived_orders_button_text);
        mViewBinding.buttonShipperHome.setOnClickListener(mCheckOrderStatusHandler);
    }

    @Override
    public void onResume() {
        super.onResume();

        mContainingActivity.getmBottomNavBar().setVisibility(View.VISIBLE);
        mContainingActivity.getmToolbar().setVisibility(View.VISIBLE);
        Menu menu = mContainingActivity.getmToolbar().getMenu();
        for(int i = 0; i<menu.size();++i) {
            menu.getItem(i).setVisible(true);
        }
        mViewModel.getmRepo().getDeliveryList(mContainingActivity.getApplicationContext());
    }

    // Template code
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mViewBinding = null;
    }

    @Override
    public void onChooseDelivery(Delivery delivery) {
        mContainingActivity.getNavController().navigate(R.id.destination_shipper_receieve_order);
    }
}