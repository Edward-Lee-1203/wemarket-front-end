package com.example.wemarketandroid.views.buyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wemarketandroid.R;
import com.example.wemarketandroid.databinding.FragmentBuyerChooseFoodBinding;
import com.example.wemarketandroid.databinding.FragmentBuyerMarketDetailsBinding;
import com.example.wemarketandroid.models.buyer.Food;
import com.example.wemarketandroid.models.buyer.Market;
import com.example.wemarketandroid.repository.Repo;
import com.example.wemarketandroid.viewmodels.buyer.CartSharedViewModel;
import com.example.wemarketandroid.viewmodels.buyer.ChooseFoodViewModel;
import com.example.wemarketandroid.viewmodels.buyer.MarketDetailsViewModel;
import com.example.wemarketandroid.viewmodels.buyer.OnDialogResult;
import com.example.wemarketandroid.viewmodels.buyer.ViewModelHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MarketDetailsFragment extends Fragment implements IUseToolbarOnlyTitle, IHideBottomNavBar, OnDialogResult {

    private final String TAG = "MarketDetailsFragment";
    private MainActivity mContainingActivity;
    private FragmentBuyerMarketDetailsBinding mViewBinding;
    private MarketDetailsViewModel mViewModel;
    private CartSharedViewModel mCartSharedViewModel;
    private LiveData<Market> mMarket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewBinding = FragmentBuyerMarketDetailsBinding.inflate(inflater,container,false);
        View rootView = mViewBinding.getRoot();
        mContainingActivity = (MainActivity)getActivity();
        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()).create(MarketDetailsViewModel.class);
        mCartSharedViewModel = new ViewModelProvider(requireActivity()).get(CartSharedViewModel.class);
        mMarket = mViewModel.getRepo().getMarketById(getArguments().getInt("marketId"));
        // registers floating back button click handler
        mViewBinding.fabBuyerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do pop nav stack
                mContainingActivity.getNavController().popBackStack();
            }
        });
        // creates on food item click handler
        ChooseFoodViewModel.FoodClickListener foodClickListener = new ChooseFoodViewModel.FoodClickListener() {
            @Override
            public void onItemClick(Food food) {
                // shows add food dialog
                ChooseFoodDialogFragment dialogFragment = ChooseFoodDialogFragment.newInstance(food.getId());
                dialogFragment.setTargetFragment(MarketDetailsFragment.this,0);
                dialogFragment.show(getParentFragmentManager(),TAG);
            }
        };
        // setups recycler view
        mViewBinding.recyclerMarketFoods.setAdapter(mViewModel.getFoodItemViewHolderAdapter(foodClickListener));
        mViewBinding.recyclerMarketFoods.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));
        ViewModelHelper.addItemDivider(getContext(), mViewBinding.recyclerMarketFoods,LinearLayoutManager.VERTICAL);
        // observes market object
        mMarket.observe(getViewLifecycleOwner(), new Observer<Market>() {
            @Override
            public void onChanged(Market market) {
                // bind data to view
                mViewBinding.includeMarketDetails.textIncludeBuyerMarketDetailsName.setText(market.getName());
                mViewBinding.includeMarketDetails.textIncludeBuyerMarketDetailsAddress.setText(market.getAddress());
                mViewBinding.includeMarketDetails.textIncludeBuyerMarketDetailsOpenTime.setText(String.format("%d %s - %d %s",market.getOpenTime(),market.getOpenTime()<=12?"am":"pm",market.getCloseTime(),market.getCloseTime()<=12?"am":"pm"));
                // submit food list to adapter
                mViewModel.getFoodItemViewHolderAdapter(foodClickListener).submitList(market.getFoodList());
            }
        });
        // observes cart content
        mCartSharedViewModel.getCartItemCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mViewBinding.includeBuyerBottomBar.buttonBuyerIncludeBottomBarItemCounter.setText(integer+(integer<1?" item":" items"));
            }
        });
        mCartSharedViewModel.getCartTotalCost().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mViewBinding.includeBuyerBottomBar.buttonBuyerIncludeBottomBarItemsCost.setText(String.format("%,d",integer));
            }
        });
        // registers checkout button click handler
        mViewBinding.includeBuyerBottomBar.buttonBuyerIncludeBottomBarCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCartSharedViewModel.getCartItemCount().getValue()>0){
                    mContainingActivity.getNavController().navigate(R.id.destination_buyer_success);
                } else {
                    Toast.makeText(getContext(),"Please add at least 1 food to cart first",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void handle(Food food, int price){
        mCartSharedViewModel.addToCart(food,price);
    }

    @Override
    public void onResume() {
        super.onResume();
        mContainingActivity.getmBottomNavBar().setVisibility(View.GONE);
        mContainingActivity.getmToolbar().setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
//        mContainingActivity.getmBottomNavBar().setVisibility(View.VISIBLE);
//        for(MenuItem item : mHiddenMenuItem) item.setVisible(true);
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
//    TODO: dynamically create the linear layout for the bottom bar
//    TODO: checkout listener
}