package com.example.wemarketandroid.views.shipper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wemarketandroid.databinding.FragmentShipperHomeBinding;
import com.example.wemarketandroid.databinding.FragmentShipperOrdersBinding;
import com.example.wemarketandroid.models.Delivery;
import com.example.wemarketandroid.viewmodels.buyer.ChooseFoodViewModel;
import com.example.wemarketandroid.viewmodels.buyer.ViewModelHelper;
import com.example.wemarketandroid.viewmodels.shipper.OrdersViewModel;

import java.util.List;

public class OrdersFragment extends Fragment {

    private MainActivity mContainingActivity;
    private FragmentShipperOrdersBinding mViewBinding;
    private ChooseFoodViewModel mBuyerChooseFoodViewModel;
    private OrdersViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewBinding = FragmentShipperOrdersBinding.inflate(inflater,container,false);
        View rootView = mViewBinding.getRoot();
        mContainingActivity = (MainActivity) getActivity();
        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()).create(OrdersViewModel.class);
        mBuyerChooseFoodViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()).create(ChooseFoodViewModel.class);

        // bindings
        // setups filters recycle view
        mViewBinding.recyclerShipperQuickFilters.setAdapter(mBuyerChooseFoodViewModel.getChooseFoodFilterViewHolderAdapter());
        mViewBinding.recyclerShipperQuickFilters.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));
        ViewModelHelper.addItemDivider(getContext(), mViewBinding.recyclerShipperQuickFilters,LinearLayoutManager.HORIZONTAL);
        // setups orders recycle view
        OrdersViewModel.OrderAdapter adapter = mViewModel.getOrderAdapter(getViewLifecycleOwner());
        mViewBinding.recyclerShipperDeliveries.setAdapter(adapter);
        mViewBinding.recyclerShipperDeliveries.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));
        ViewModelHelper.addItemDivider(getContext(), mViewBinding.recyclerShipperDeliveries,LinearLayoutManager.VERTICAL);
        mViewModel.getShipperDeliveryList().observe(getViewLifecycleOwner(), new Observer<List<Delivery>>() {
            @Override
            public void onChanged(List<Delivery> deliveries) {
                adapter.submitList(deliveries);
            }
        });
        // inits lists
        mViewModel.getRepo().getDeliveryList(requireContext());
        mViewModel.getRepo().getOrderDetails(requireContext());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mContainingActivity.getmBottomNavBar().setVisibility(View.VISIBLE);
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