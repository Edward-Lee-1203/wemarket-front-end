package com.example.wemarketandroid.views.buyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wemarketandroid.R;
import com.example.wemarketandroid.databinding.FragmentBuyerChooseFoodBinding;
import com.example.wemarketandroid.models.buyer.Market;
import com.example.wemarketandroid.repository.Repo;
import com.example.wemarketandroid.viewmodels.buyer.CartSharedViewModel;
import com.example.wemarketandroid.viewmodels.buyer.ChooseFoodViewModel;
import com.example.wemarketandroid.viewmodels.buyer.RecyclerViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ChooseFoodFragment extends Fragment implements IHideBottomNavBar, IUseToolbarOnlyTitle {
    
    private MainActivity mContainingActivity;
    private FragmentBuyerChooseFoodBinding mViewBinding;
    private MenuItem[] mHiddenMenuItem;
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
        Menu menu = mContainingActivity.getmToolbar().getMenu();
        mHiddenMenuItem = new MenuItem[]{menu.getItem(0), menu.getItem(1)};
        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(ChooseFoodViewModel.class);
        mCartSharedViewModel = new ViewModelProvider(requireActivity()).get(CartSharedViewModel.class);
        mRepo = Repo.getInstance();
        mNavController = mContainingActivity.getNavController();
        // setups filters recycle view
        mViewBinding.recyclerBuyerQuickFilters.setAdapter(mViewModel.getChooseFoodFilterViewHolderAdapter());
        mViewBinding.recyclerBuyerQuickFilters.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));
        RecyclerViewHelper.addItemDivider(getContext(), mViewBinding.recyclerBuyerQuickFilters,LinearLayoutManager.HORIZONTAL);
        // TODO: implement filter foods on filter click
        // setups market recycle view
        ChooseFoodViewModel.MarketClickListener marketClickListener = new ChooseFoodViewModel.MarketClickListener() {
                @Override
                public void onItemClick(Market market) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("marketId",market.getId());
                    mNavController.navigate(R.id.action_chooseFoodFragment_to_marketDetailsFragment,bundle);
                    // TODO: handle geting market id from arguments (bundle) and binding market details in the destination
                }
        };
        mViewBinding.recyclerBuyerTodayFoods.setAdapter(mViewModel.getMarketItemViewHolderAdapter(getViewLifecycleOwner(),marketClickListener));
        mViewBinding.recyclerBuyerTodayFoods.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));
        RecyclerViewHelper.addItemDivider(getContext(), mViewBinding.recyclerBuyerTodayFoods,LinearLayoutManager.VERTICAL);

        Observer<List<Market>> marketListObserver = new Observer<List<Market>>() {
            @Override
            public void onChanged(List<Market> markets) {
                mViewModel.getMarketItemViewHolderAdapter(getViewLifecycleOwner(),marketClickListener).submitList(markets);
            }
        };
        mRepo.getMarketList().observe(getViewLifecycleOwner(),marketListObserver);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mContainingActivity.getmBottomNavBar().setVisibility(View.GONE);
        for(MenuItem item : mHiddenMenuItem) item.setVisible(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        mContainingActivity.getmBottomNavBar().setVisibility(View.VISIBLE);
        for(MenuItem item : mHiddenMenuItem) item.setVisible(true);
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