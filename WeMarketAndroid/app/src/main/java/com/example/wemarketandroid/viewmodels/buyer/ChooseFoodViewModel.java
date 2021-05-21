package com.example.wemarketandroid.viewmodels.buyer;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wemarketandroid.databinding.FragmentBuyerChooseFoodBinding;
import com.example.wemarketandroid.databinding.ItemBuyerFoodFilterBinding;
import com.example.wemarketandroid.databinding.ItemBuyerHomeControlsBinding;
import com.example.wemarketandroid.databinding.ItemBuyerMarketBinding;
import com.example.wemarketandroid.databinding.ItemBuyerMarketFoodBinding;
import com.example.wemarketandroid.models.buyer.Filter;
import com.example.wemarketandroid.models.buyer.Food;
import com.example.wemarketandroid.models.buyer.Market;
import com.example.wemarketandroid.repository.Repo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChooseFoodViewModel extends AndroidViewModel {

    private static ChooseFoodFilterViewHolderAdapter CHOOSE_FOOD_FILTER_VIEW_HOLDER_ADAPTER;
    private static MarketItemViewHolderAdapter MARKET_ITEM_VIEW_HOLDER_ADAPTER;
    private Context mContext;
    private Repo mRepo;

    public ChooseFoodViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mRepo = Repo.getInstance();
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

        public FoodItemViewHolder(@NonNull View itemView, ItemBuyerMarketFoodBinding binding, MarketClickListener marketClickListener) {
            super(itemView);
            this.binding = binding;
            mMarketClickListener = marketClickListener;
            itemView.setOnClickListener(this);
            binding.imageButtonMarketFoodAddToCart.setOnClickListener(this);
        }

        public void bindTo(Food food){
            mFood = food;
            // debug code
            binding.includeImageBadgeBottom.imageIncludeImageBadgeBottomFoodImage.setImageResource(Integer.parseInt(food.getImageUri()));

            binding.includeMarketFoodFoodPricing.textBuyerFoodPriceCurrent.setText(String.format("%,d",food.getPrice()));
            binding.includeMarketFoodFoodPricing.textBuyerFoodPriceBefore.setText(String.format("%,d",food.getPrice()-(int)Math.floor(food.getPrice()*food.getDiscount())));
        }


        @Override
        public void onClick(View view) {
            // TODO: check if we can ommit this checking, i.e. click anywhere inside this item view is considered add to cart also
            if(view.getId()==binding.imageButtonMarketFoodAddToCart.getId()){
                // TODO: opens add food to cart dialog
                Toast.makeText(mContext,mFood.getName()+" clicked",Toast.LENGTH_SHORT).show();
            } else{
                // executes the click callback
                mMarketClickListener.onItemClick(mFood.getMarket());
            }
        }
    }

    public class FoodItemViewHolderAdapter extends ListAdapter<Food, FoodItemViewHolder>{

        private MarketClickListener mMarketClickListener;

        public FoodItemViewHolderAdapter(DiffUtil.ItemCallback<Food> callback, MarketClickListener marketClickListener){
            super(callback);
            mMarketClickListener = marketClickListener;
        }

        @NonNull
        @Override
        public FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemBuyerMarketFoodBinding binding = ItemBuyerMarketFoodBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            View view = binding.getRoot();
            FoodItemViewHolder viewHolder = new FoodItemViewHolder(view,binding,mMarketClickListener);
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

        public MarketItemViewHolder(@NonNull View itemView, ItemBuyerMarketBinding binding, MarketClickListener marketClickListener, LifecycleOwner lifecycleOwner) {
            super(itemView);
            this.binding = binding;
            this.mLifecycleOwner = lifecycleOwner;
            mMarketClickListener = marketClickListener;
        }

        public void bindTo(LiveData<Market> market, int itemCount, RecyclerView.RecycledViewPool viewPool){
            // setups nested recycle view
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
            linearLayoutManager.setInitialPrefetchItemCount(itemCount);
            FoodItemViewHolderAdapter adapter = new FoodItemViewHolderAdapter(RecyclerViewHelper.getModelDiffCallback(Food.class), mMarketClickListener);
            binding.recyclerBuyerItemMarketFood.setAdapter(adapter);
            binding.recyclerBuyerItemMarketFood.setLayoutManager(linearLayoutManager);
            binding.recyclerBuyerItemMarketFood.setRecycledViewPool(viewPool);
            // observes food item changes
            market.observe(mLifecycleOwner,new Observer<Market>() {
                @Override
                public void onChanged(Market market) {
                    binding.textBuyerItemMarketName.setText(market.getName());
                    binding.textBuyerItemMarketAddress.setText(market.getAddress());
                    adapter.submitList(market.getFoodList());
                }
            });
        }
    }

    public class MarketItemViewHolderAdapter extends ListAdapter<Market, MarketItemViewHolder>{

        private RecyclerView.RecycledViewPool viewPool;
        private LifecycleOwner mLifecycleOwner;
        private MarketClickListener mMarketClickListener;

        public MarketItemViewHolderAdapter(DiffUtil.ItemCallback<Market> callback, LifecycleOwner lifecycleOwner, MarketClickListener marketClickListener){
            super(callback);
            viewPool = new RecyclerView.RecycledViewPool();
            mLifecycleOwner = lifecycleOwner;
            mMarketClickListener = marketClickListener;
        }

        @NonNull
        @Override
        public MarketItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemBuyerMarketBinding binding = ItemBuyerMarketBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            View view = binding.getRoot();
            return new MarketItemViewHolder(view,binding, mMarketClickListener,mLifecycleOwner);
        }

        @Override
        public void onBindViewHolder(@NonNull MarketItemViewHolder holder, int position) {
            Market market = getItem(position);
            LiveData<Market> marketLiveData = Transformations.map(mRepo.getMarketList(), new Function<List<Market>, Market>() {
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
    public MarketItemViewHolderAdapter getMarketItemViewHolderAdapter(LifecycleOwner lifecycleOwner, MarketClickListener marketClickListener){
        if(MARKET_ITEM_VIEW_HOLDER_ADAPTER==null || MARKET_ITEM_VIEW_HOLDER_ADAPTER.mLifecycleOwner != lifecycleOwner){
            MARKET_ITEM_VIEW_HOLDER_ADAPTER = new MarketItemViewHolderAdapter(RecyclerViewHelper.getModelDiffCallback(Market.class),lifecycleOwner, marketClickListener);
        }
        return MARKET_ITEM_VIEW_HOLDER_ADAPTER;
    }

}
