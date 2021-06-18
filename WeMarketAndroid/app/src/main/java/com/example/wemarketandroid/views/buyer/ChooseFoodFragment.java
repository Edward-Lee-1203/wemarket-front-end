package com.example.wemarketandroid.views.buyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wemarketandroid.R;
import com.example.wemarketandroid.databinding.FragmentBuyerChooseFoodBinding;
import com.example.wemarketandroid.models.Food;
import com.example.wemarketandroid.models.Market;
import com.example.wemarketandroid.repository.Repo;
import com.example.wemarketandroid.viewmodels.buyer.CartSharedViewModel;
import com.example.wemarketandroid.viewmodels.buyer.ChooseFoodViewModel;
import com.example.wemarketandroid.viewmodels.buyer.OnDialogResult;
import com.example.wemarketandroid.viewmodels.buyer.ViewModelHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ChooseFoodFragment extends Fragment implements IHideBottomNavBar, IUseToolbarOnlyTitle, OnDialogResult {

    private final String TAG = "ChooseFoodFragment";
    private MainActivity mContainingActivity;
    private FragmentBuyerChooseFoodBinding mViewBinding;
    private ChooseFoodViewModel mViewModel;
    private CartSharedViewModel mCartSharedViewModel;
    private Repo mRepo;
    private NavController mNavController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewBinding = FragmentBuyerChooseFoodBinding.inflate(inflater,container,false);
        View rootView = mViewBinding.getRoot();
        mContainingActivity = (MainActivity)getActivity();
        mRepo = Repo.getInstance();
        mNavController = mContainingActivity.getNavController();
        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(ChooseFoodViewModel.class);
        mCartSharedViewModel = new ViewModelProvider(requireActivity()).get(CartSharedViewModel.class);
        // setups filters recycle view
        mViewBinding.recyclerBuyerQuickFilters.setAdapter(mViewModel.getChooseFoodFilterViewHolderAdapter());
        mViewBinding.recyclerBuyerQuickFilters.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));
        ViewModelHelper.addItemDivider(getContext(), mViewBinding.recyclerBuyerQuickFilters,LinearLayoutManager.HORIZONTAL);
        // TODO: implement filter foods on filter click
        // defines click listeners
        ChooseFoodViewModel.MarketClickListener marketClickListener = new ChooseFoodViewModel.MarketClickListener() {
                @Override
                public void onItemClick(Market market) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("marketId",market.getId());
                    mNavController.navigate(R.id.action_chooseFoodFragment_to_marketDetailsFragment,bundle);
                }
        };
        ChooseFoodViewModel.FoodClickListener foodClickListener = new ChooseFoodViewModel.FoodClickListener() {
            @Override
            public void onItemClick(Food food) {
                // shows food dialog
                ChooseFoodDialogFragment dialogFragment = ChooseFoodDialogFragment.newInstance(food.getId());
                dialogFragment.setTargetFragment(ChooseFoodFragment.this,0);
                dialogFragment.show(getParentFragmentManager(),TAG);
            }
        };
        // setups market recycle view
        mViewBinding.recyclerBuyerTodayFoods.setAdapter(mViewModel.getMarketItemViewHolderAdapter(getViewLifecycleOwner(),marketClickListener, foodClickListener));
        mViewBinding.recyclerBuyerTodayFoods.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));
        ViewModelHelper.addItemDivider(getContext(), mViewBinding.recyclerBuyerTodayFoods,LinearLayoutManager.VERTICAL);
        // observes market list
        Observer<List<Market>> marketListObserver = new Observer<List<Market>>() {
            @Override
            public void onChanged(List<Market> markets) {
                mViewModel.getMarketItemViewHolderAdapter(getViewLifecycleOwner(),marketClickListener, foodClickListener).submitList(markets);
            }
        };
        mRepo.getMarketList().observe(getViewLifecycleOwner(),marketListObserver);
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
                if(mCartSharedViewModel.getCartItemCount().getValue()>0) {
                    mNavController.navigate(R.id.destination_buyer_success);
                } else{
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
        mContainingActivity.getmToolbar().setVisibility(View.VISIBLE);
        Menu menu = mContainingActivity.getmToolbar().getMenu();
        for(int i = 0; i<menu.size();++i) {
            menu.getItem(i).setVisible(true);
        }
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
    
    public void useToolbarOnlyTitle(Toolbar toolbar) {
        
    }
    @Override
    public void hideBottomNavBar(BottomNavigationView bottomNavigationView) {
        
    }

//    TODO: dynamically create bottom linear layout
//    TODO: checkout listener
}