package com.example.wemarketandroid.views.buyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wemarketandroid.databinding.FragmentBuyerOrdersBinding;
import com.example.wemarketandroid.models.Delivery;
import com.example.wemarketandroid.viewmodels.buyer.OrdersViewModel;
import com.example.wemarketandroid.viewmodels.buyer.ViewModelHelper;

import java.util.List;

public class OrdersFragment extends Fragment {

    private OrdersViewModel mViewModel;
    private FragmentBuyerOrdersBinding mViewBinding;
    private MainActivity mContainingActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewBinding = FragmentBuyerOrdersBinding.inflate(inflater, container, false);
        View root = mViewBinding.getRoot();
        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(OrdersViewModel.class);
        mContainingActivity = (MainActivity)getActivity();
        // setups order recycler view
        mViewBinding.recyclerBuyerOrders.setAdapter(mViewModel.getOrderAdapter(getViewLifecycleOwner()));
        mViewBinding.recyclerBuyerOrders.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));
        ViewModelHelper.addItemDivider(getContext(), mViewBinding.recyclerBuyerOrders,LinearLayoutManager.VERTICAL);
        // debugging
//        mViewModel.getRepo().seedDeliveryList(getViewLifecycleOwner());
        // observes delivery list of this user
        mViewModel.getRepo().getDeliveryList().observe(getViewLifecycleOwner(), new Observer<List<Delivery>>() {
            @Override
            public void onChanged(List<Delivery> deliveries) {
                mViewModel.getOrderAdapter(getViewLifecycleOwner()).submitList(deliveries);
            }
        });
        return root;
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
    }

    // Template code
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mViewBinding = null;
    }
}