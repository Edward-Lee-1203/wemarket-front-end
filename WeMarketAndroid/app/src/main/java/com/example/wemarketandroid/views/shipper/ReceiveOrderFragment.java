package com.example.wemarketandroid.views.shipper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wemarketandroid.R;
import com.example.wemarketandroid.databinding.FragmentShipperReceiveOrderBinding;
import com.example.wemarketandroid.models.Delivery;
import com.example.wemarketandroid.models.Market;
import com.example.wemarketandroid.models.MarketOrderDetail;
import com.example.wemarketandroid.models.OrderDetail;
import com.example.wemarketandroid.viewmodels.buyer.ViewModelHelper;
import com.example.wemarketandroid.viewmodels.shipper.DeliverySharedViewModel;
import com.example.wemarketandroid.viewmodels.shipper.ReceiveOrderViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReceiveOrderFragment extends Fragment {

    private MainActivity mContainingActivity;
    private FragmentShipperReceiveOrderBinding mViewBinding;
    private ReceiveOrderViewModel mViewModel;
    private DeliverySharedViewModel mSharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewBinding = FragmentShipperReceiveOrderBinding.inflate(inflater,container,false);
        View rootView = mViewBinding.getRoot();
        mContainingActivity = (MainActivity) getActivity();
        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()).create(ReceiveOrderViewModel.class);
        mSharedViewModel = new ViewModelProvider(mContainingActivity).get(DeliverySharedViewModel.class);   // gets shared view model
        // gets delivery object
        Delivery delivery = mSharedViewModel.getmDeliveryMutableLiveData().getValue();
        // binding delivery object to view
        mViewBinding.switchSocialDistancing.setChecked(delivery.isKeepSocial());
        mViewBinding.textShipperDeliverToAddress.setText(delivery.getAddress());
        mViewBinding.textShipperPaymentItemsCost.setText(String.format("%,d",delivery.getOrder().getTotalPrice()));
        // TODO: fix database schema to get delivery fee
        // binding recycler view
        ReceiveOrderViewModel.MarketOrderDetailAdapter adapter = mViewModel.getMarketOrderDetailAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        ViewModelHelper.addItemDivider(getContext(), mViewBinding.recyclerShipperCart,LinearLayoutManager.VERTICAL);
        mViewBinding.recyclerShipperCart.setAdapter(adapter);
        mViewBinding.recyclerShipperCart.setLayoutManager(layoutManager);
        // grouping orderDetails by market
        HashMap<Integer, List<OrderDetail>> hashMap = new HashMap<>();
        List<Market> markets = new ArrayList<>();
        for(OrderDetail orderDetail : delivery.getOrder().getOrderDetailList()){
            Market market = orderDetail.getFood().getMarket();
            if(hashMap.containsKey(market.getId())){
                hashMap.get(market.getId()).add(orderDetail);
            } else{
                ArrayList<OrderDetail> orderDetails = new ArrayList<>();
                orderDetails.add(orderDetail);
                hashMap.put(market.getId(), orderDetails);
                markets.add(market);
            }
        }
        // flattening hashmap to list of MarketOrderDetails
        ArrayList<MarketOrderDetail> marketOrderDetails = new ArrayList<>();
        for(int i = 0; i<markets.size(); ++i){
            Market market = markets.get(i);
            MarketOrderDetail marketOrderDetail = new MarketOrderDetail(i,market.getId(),market,hashMap.get(market.getId()));
            marketOrderDetails.add(marketOrderDetail);
        }
        adapter.submitList(marketOrderDetails);     // submits that list to adapter
        mViewBinding.buttonShipperReceiveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: call API
//                mContainingActivity.getNavController().navigate(R.id.destination_shipper_maps);
                mContainingActivity.getNavController().navigate(R.id.destination_shipper_orders);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mContainingActivity.getmBottomNavBar().setVisibility(View.GONE);
        mContainingActivity.getmToolbar().setVisibility(View.VISIBLE);
        Menu menu = mContainingActivity.getmToolbar().getMenu();
        for(int i = 0; i<menu.size();++i) {
            menu.getItem(i).setVisible(false);
        }
    }

    // Template code
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mViewBinding = null;
    }
}