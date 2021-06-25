package com.example.wemarketandroid.viewmodels.buyer;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wemarketandroid.databinding.ItemBuyerFoodFilterBinding;
import com.example.wemarketandroid.databinding.ItemBuyerMarketBinding;
import com.example.wemarketandroid.databinding.ItemBuyerMarketFoodBinding;
import com.example.wemarketandroid.models.Filter;
import com.example.wemarketandroid.models.Food;
import com.example.wemarketandroid.models.Market;
import com.example.wemarketandroid.repository.Repo;

import java.util.List;

public class ChooseFoodViewModel extends AndroidViewModel {

    private static ChooseFoodFilterViewHolderAdapter CHOOSE_FOOD_FILTER_VIEW_HOLDER_ADAPTER;
    private static MarketItemViewHolderAdapter MARKET_ITEM_VIEW_HOLDER_ADAPTER;
    private Context mContext;
    private Repo mRepo;
    private LiveData<List<Market>> mMarketListLiveData;

    public ChooseFoodViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mRepo = Repo.getInstance();
        mMarketListLiveData = mRepo.getMarketList(mContext);
    }

    private class ChooseFoodFilterViewHolder extends RecyclerView.ViewHolder{
        // TODO: add click listener for these filter. Consider create a callback just like market click listener

        private ItemBuyerFoodFilterBinding binding;

        public ChooseFoodFilterViewHolder(@NonNull View itemView, ItemBuyerFoodFilterBinding binding, View.OnClickListener onClickListener) {
            super(itemView);
            this.binding = binding;
            if(onClickListener!=null) {
                itemView.setOnClickListener(onClickListener);
            }
        }
        public void bindTo(Filter filter){
            binding.buttonBuyerFoodFilter.setText(filter.getLabel());
            binding.buttonBuyerFoodFilter.setCompoundDrawablesRelativeWithIntrinsicBounds(filter.getResId(),0,0,0);
        }
    }


    public class ChooseFoodFilterViewHolderAdapter extends RecyclerView.Adapter<ChooseFoodFilterViewHolder>{
        private Filter[] data;

        public ChooseFoodFilterViewHolderAdapter(Filter[] data) {
            this.data = data;
        }

        @NonNull
        @Override
        public ChooseFoodFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemBuyerFoodFilterBinding binding = ItemBuyerFoodFilterBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            View view = binding.getRoot();
            return new ChooseFoodFilterViewHolder(view,binding, null);
        }

        @Override
        public void onBindViewHolder(@NonNull ChooseFoodFilterViewHolder holder, int position) {
            Filter filter = data[position];
            holder.bindTo(filter);
        }

        @Override
        public int getItemCount() {
            return data.length;
        }
    }

    public interface MarketClickListener{
        void onItemClick(Market market);
    }
    public interface FoodClickListener{
        void onItemClick(Food food);
    }

    private class FoodItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemBuyerMarketFoodBinding binding;
        private Food mFood;
        private MarketClickListener mMarketClickListener;
        private FoodClickListener mFoodClickListener;

        public FoodItemViewHolder(@NonNull View itemView, ItemBuyerMarketFoodBinding binding, MarketClickListener marketClickListener, FoodClickListener foodClickListener) {
            super(itemView);
            this.binding = binding;
            mMarketClickListener = marketClickListener;
            mFoodClickListener = foodClickListener;
            itemView.setOnClickListener(this);
            binding.imageButtonMarketFoodAddToCart.setOnClickListener(this);
        }

        public void bindTo(Food food){
            mFood = food;
            // debug code
//            binding.includeImageBadgeBottom.imageIncludeImageBadgeBottomFoodImage.setImageResource(Integer.parseInt(food.getImageUri()));
//            Picasso
            binding.textBuyerItemMarketFoodName.setText(food.getName());
            ViewModelHelper.bindIncludeFoodPricing(binding.includeMarketFoodFoodPricing, food.getPrice(), ((int)food.getPrice()-food.getPrice()*food.getDiscount()/100));
        }


        public void onClick(View view) {
            if(view.getId()==binding.imageButtonMarketFoodAddToCart.getId()){
                mFoodClickListener.onItemClick(mFood);
            } else{
                mMarketClickListener.onItemClick(mFood.getMarket());
            }
        }
    }

    public class FoodItemViewHolderAdapter extends ListAdapter<Food, FoodItemViewHolder>{

        private MarketClickListener mMarketClickListener;
        private FoodClickListener mFoodClickListener;

        public FoodItemViewHolderAdapter(DiffUtil.ItemCallback<Food> callback, MarketClickListener marketClickListener, FoodClickListener foodClickListener){
            super(callback);
            mMarketClickListener = marketClickListener;
            mFoodClickListener = foodClickListener;
        }

        @NonNull
        @Override
        public FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemBuyerMarketFoodBinding binding = ItemBuyerMarketFoodBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            View view = binding.getRoot();
            FoodItemViewHolder viewHolder = new FoodItemViewHolder(view,binding,mMarketClickListener, mFoodClickListener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull FoodItemViewHolder holder, int position) {
            Food food = getItem(position);
            holder.bindTo(food);
        }
    }

    private class MarketItemViewHolder extends RecyclerView.ViewHolder{

        private ItemBuyerMarketBinding binding;
        private LifecycleOwner mLifecycleOwner;
        private MarketClickListener mMarketClickListener;
        private FoodClickListener mFoodClickListener;

        public MarketItemViewHolder(@NonNull View itemView, ItemBuyerMarketBinding binding, MarketClickListener marketClickListener, FoodClickListener foodClickListener, LifecycleOwner lifecycleOwner) {
            super(itemView);
            this.binding = binding;
            this.mLifecycleOwner = lifecycleOwner;
            mMarketClickListener = marketClickListener;
            mFoodClickListener = foodClickListener;
        }

        public void bindTo(LiveData<Market> market, int itemCount, RecyclerView.RecycledViewPool viewPool){
            // setups nested recycle view
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
            linearLayoutManager.setInitialPrefetchItemCount(itemCount);
            FoodItemViewHolderAdapter adapter = new FoodItemViewHolderAdapter(ViewModelHelper.getModelDiffCallback(Food.class), mMarketClickListener, mFoodClickListener);
            binding.recyclerBuyerItemMarketFood.setAdapter(adapter);
            binding.recyclerBuyerItemMarketFood.setLayoutManager(linearLayoutManager);
            binding.recyclerBuyerItemMarketFood.setRecycledViewPool(viewPool);
            // observes food item changes
            market.observe(mLifecycleOwner,new Observer<Market>() {
                @Override
                public void onChanged(Market market) {
                    binding.textBuyerItemMarketName.setText(market.getName());
                    binding.textBuyerItemMarketAddress.setText(market.getAddress());
                    mRepo.getFoodsByMarketId(market.getId()).observe(mLifecycleOwner, new Observer<List<Food>>() {
                        @Override
                        public void onChanged(List<Food> foods) {
                            adapter.submitList(foods);
                        }
                    });
                }
            });
        }
    }

    public class MarketItemViewHolderAdapter extends ListAdapter<Market, MarketItemViewHolder>{

        private RecyclerView.RecycledViewPool viewPool;
        private LifecycleOwner mLifecycleOwner;
        private MarketClickListener mMarketClickListener;
        private FoodClickListener mFoodClickListener;

        public MarketItemViewHolderAdapter(DiffUtil.ItemCallback<Market> callback, LifecycleOwner lifecycleOwner, MarketClickListener marketClickListener, FoodClickListener foodClickListener){
            super(callback);
            viewPool = new RecyclerView.RecycledViewPool();
            mLifecycleOwner = lifecycleOwner;
            mMarketClickListener = marketClickListener;
            mFoodClickListener = foodClickListener;
        }

        @NonNull
        @Override
        public MarketItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemBuyerMarketBinding binding = ItemBuyerMarketBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            View view = binding.getRoot();
            return new MarketItemViewHolder(view,binding, mMarketClickListener, mFoodClickListener,mLifecycleOwner);
        }

        @Override
        public void onBindViewHolder(@NonNull MarketItemViewHolder holder, int position) {
            Market market = getItem(position);
            LiveData<Market> marketLiveData = Transformations.map(mMarketListLiveData, new Function<List<Market>, Market>() {
                @Override
                public Market apply(List<Market> input) {
                    int indx = input.indexOf(market);
                    return input.get(indx);
                }
            });
            holder.bindTo(marketLiveData, getItemCount(), viewPool);
        }
    }

    public ChooseFoodFilterViewHolderAdapter getChooseFoodFilterViewHolderAdapter(){
        if(CHOOSE_FOOD_FILTER_VIEW_HOLDER_ADAPTER==null){
            CHOOSE_FOOD_FILTER_VIEW_HOLDER_ADAPTER = new ChooseFoodFilterViewHolderAdapter(mRepo.getChooseFoodFilters());
        }
        return CHOOSE_FOOD_FILTER_VIEW_HOLDER_ADAPTER;
    }
    // TODO: observe cart food list and display on bottom bar
    public MarketItemViewHolderAdapter getMarketItemViewHolderAdapter(LifecycleOwner lifecycleOwner, MarketClickListener marketClickListener, FoodClickListener foodClickListener){
        if(MARKET_ITEM_VIEW_HOLDER_ADAPTER==null || MARKET_ITEM_VIEW_HOLDER_ADAPTER.mLifecycleOwner != lifecycleOwner){
            MARKET_ITEM_VIEW_HOLDER_ADAPTER = new MarketItemViewHolderAdapter(ViewModelHelper.getModelDiffCallback(Market.class),lifecycleOwner, marketClickListener, foodClickListener);
        }
        return MARKET_ITEM_VIEW_HOLDER_ADAPTER;
    }

    public LiveData<List<Market>> getMarketListLiveData(){return mMarketListLiveData;}

}
