package com.example.wemarketandroid.views.buyer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wemarketandroid.R;
import com.example.wemarketandroid.databinding.FragmentBuyerCheckoutBinding;
import com.example.wemarketandroid.models.CartItem;
import com.example.wemarketandroid.viewmodels.buyer.CartSharedViewModel;
import com.example.wemarketandroid.viewmodels.buyer.CheckoutViewModel;
import com.example.wemarketandroid.viewmodels.buyer.ViewModelHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckoutFragment extends Fragment implements IUseToolbarOnlyTitle, IHideBottomNavBar{

    private MainActivity mContainingActivity;
    private FragmentBuyerCheckoutBinding mViewBinding;
    private CartSharedViewModel mCartSharedViewModel;
    private CheckoutViewModel mViewModel;
    private SavedStateHandle mSavedStateHandle;
    private FusedLocationProviderClient locationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public static final String IS_CHECKOUT_CONFIRMED = "isCheckoutConfirmed";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewBinding = FragmentBuyerCheckoutBinding.inflate(inflater,container,false);
        View rootView = mViewBinding.getRoot();
        mContainingActivity = (MainActivity)requireActivity();
        mCartSharedViewModel = new ViewModelProvider(requireActivity()).get(CartSharedViewModel.class);
        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(CheckoutViewModel.class);
        mCartSharedViewModel.setIsCheckoutConfirmed(false);     // sets confirm state
        locationProviderClient = LocationServices.getFusedLocationProviderClient(mContainingActivity);
        // registers custom back button called handler
        // TODO: fix needs many tries
//        mContainingActivity.getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                mCartSharedViewModel.setIsCheckoutConfirmed(false);     // sets confirm state
//                setEnabled(true);
//                mContainingActivity.getNavController().popBackStack();
//            }
//        });
        // registers add items button click handler
        mViewBinding.buttonBuyerCheckoutAddItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigates to choose food page
                mContainingActivity.getNavController().navigate(R.id.destination_buyer_choose_food);
                mCartSharedViewModel.setIsCheckoutConfirmed(null);

            }
        });
        // defines remove button click handler
        CheckoutViewModel.OnCartItemClicked onCartItemClicked = new CheckoutViewModel.OnCartItemClicked() {
            @Override
            public void onItemClick(CartItem cartItem) {
                mCartSharedViewModel.removeFromCart(cartItem.getId());
            }
        };
        // setups recycler view
        mViewBinding.recyclerBuyerCheckoutCart.setAdapter(mViewModel.getCartItemViewHolderAdapter(onCartItemClicked));
        mViewBinding.recyclerBuyerCheckoutCart.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));
        ViewModelHelper.addItemDivider(getContext(), mViewBinding.recyclerBuyerCheckoutCart,LinearLayoutManager.VERTICAL);
        // observes cart items
        mCartSharedViewModel.getCartItems().observe(getViewLifecycleOwner(), new Observer<HashMap<Integer, CartItem>>() {
            @Override
            public void onChanged(HashMap<Integer, CartItem> integerCartItemHashMap) {
                ArrayList<CartItem> cartItemArrayList = new ArrayList<>(integerCartItemHashMap.values());
                mViewModel.getCartItemViewHolderAdapter(onCartItemClicked).submitList(cartItemArrayList);
            }
        });
        // observes cart total cost
        mCartSharedViewModel.getCartTotalCost().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                String string = String.format("%,d VND",integer);
                mViewBinding.textBuyerCheckoutPaymentItemsCost.setText(string);
                mViewBinding.includeBuyerBottomBar.buttonBuyerIncludeBottomBarItemsCost.setText(string);
            }
        });
        // renders checkout bottom bar
        ViewModelHelper.transformBottomBarCheckout(mViewBinding.includeBuyerBottomBar);
        // registers place order button click handler
        mViewBinding.includeBuyerBottomBar.buttonBuyerIncludeBottomBarItemCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // confirms order
                mCartSharedViewModel.setIsCheckoutConfirmed(true);  // sets cart order confirmed
                String address = mViewBinding.textInputEditTextBuyerCheckoutDeliveryNotes.getText().toString();
                //TODO: assert address not blank!
                mCartSharedViewModel.getmAddressLiveData().postValue(address);
                getLatestLocation();    // request GPS permission and saves current location
                mContainingActivity.getNavController().popBackStack();  // returns to previous fragment

            }
        });

        return rootView;
    }

    private void getLatestLocation(){
        // checks if any location permission is granted
        if(ActivityCompat.checkSelfPermission(mContainingActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(mContainingActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        // gets and shows current location on map
        locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location==null){
                    Toast.makeText(mContainingActivity,"Last location is null",Toast.LENGTH_SHORT).show();
                } else{
                    mCartSharedViewModel.getmLocationLiveData().postValue(location);    // saves current GPS location
                }
            }
        });
    }

    private void requestLocationPermission(){
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }


    /**
     * Actively asks permission then show locations on map
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode != LOCATION_PERMISSION_REQUEST_CODE) return;
        // try displaying locations on map again
        getLatestLocation();
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

    @Override
    public void hideBottomNavBar(BottomNavigationView bottomNavigationView) {
        
    }

    @Override
    public void useToolbarOnlyTitle(Toolbar toolbar) {

    }
}