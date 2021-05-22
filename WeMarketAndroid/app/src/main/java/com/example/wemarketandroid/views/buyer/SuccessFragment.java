package com.example.wemarketandroid.views.buyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;

import com.example.wemarketandroid.R;
import com.example.wemarketandroid.databinding.FragmentBuyerSuccessBinding;
import com.example.wemarketandroid.models.buyer.Market;
import com.example.wemarketandroid.models.buyer.User;
import com.example.wemarketandroid.viewmodels.buyer.CartSharedViewModel;
import com.example.wemarketandroid.viewmodels.buyer.UserSharedViewModel;

import java.util.List;

public class SuccessFragment extends Fragment {

    private MainActivity mContainingActivity;
    private FragmentBuyerSuccessBinding mViewBinding;
    private CartSharedViewModel mCartSharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewBinding = FragmentBuyerSuccessBinding.inflate(inflater,container,false);
        View root = mViewBinding.getRoot();
        mContainingActivity = (MainActivity)getActivity();
        mCartSharedViewModel = new ViewModelProvider(getActivity()).get(CartSharedViewModel.class);
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
                    // TODO: sends user straight to orders page at home fragment
                    navController.navigate(startDestination, null, navOptions);
                    mCartSharedViewModel.clearCart();   // clears cart content
                } else{
                    mCartSharedViewModel.clearCart();   // clears cart content
                }
            }
        });
        // registers check order button click handler
        mViewBinding.buttonBuyerSuccessCheckOrderStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: goes to home orders page
                NavController navController = mContainingActivity.getNavController();
                int startDestination = navController.getGraph().getStartDestination();
                NavOptions navOptions = new NavOptions.Builder().setPopUpTo(startDestination, true).build();
                // TODO: sends user straight to orders page at home fragment
                navController.navigate(startDestination, null, navOptions);
            }
        });
        // TODO: test the whole thing again

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