package com.example.wemarketandroid.viewmodels.buyer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wemarketandroid.databinding.ItemBuyerMarketFoodBinding;
import com.example.wemarketandroid.models.Food;
import com.example.wemarketandroid.repository.Repo;

public class MarketDetailsViewModel extends ViewModel {

    private Repo mRepo;
    private FoodItemViewHolderAdapter FOOD_ITEM_VIEW_HOLDER_ADAPTER;

    private class FoodItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemBuyerMarketFoodBinding binding;
        private Food mFood;
        private ChooseFoodViewModel.FoodClickListener mFoodClickListener;

        public FoodItemViewHolder(@NonNull View itemView, ItemBuyerMarketFoodBinding binding, ChooseFoodViewModel.FoodClickListener foodClickListener) {
            super(itemView);
            this.binding = binding;
            mFoodClickListener = foodClickListener;
            itemView.setOnClickListener(this);
            binding.imageButtonMarketFoodAddToCart.setOnClickListener(this);
        }

        public void bindTo(Food food){
            mFood = food;
            // debug code
            binding.includeImageBadgeBottom.imageIncludeImageBadgeBottomFoodImage.setImageResource(Integer.parseInt(food.getImageUri()));
            ViewModelHelper.bindIncludeFoodPricing(binding.includeMarketFoodFoodPricing,food.getBasePrice(),food.getPrice());
            binding.textBuyerItemMarketFoodName.setText(food.getName());
        }


        public void onClick(View view) {
            if(view.getId()==binding.imageButtonMarketFoodAddToCart.getId()){
                mFoodClickListener.onItemClick(mFood);
            }
        }
    }

    public class FoodItemViewHolderAdapter extends ListAdapter<Food, FoodItemViewHolder> {

        private ChooseFoodViewModel.FoodClickListener mFoodClickListener;

        public FoodItemViewHolderAdapter(DiffUtil.ItemCallback<Food> callback, ChooseFoodViewModel.FoodClickListener foodClickListener){
            super(callback);
            mFoodClickListener = foodClickListener;
        }

        @NonNull
        @Override
        public FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemBuyerMarketFoodBinding binding = ItemBuyerMarketFoodBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            View view = binding.getRoot();
            FoodItemViewHolder viewHolder = new FoodItemViewHolder(view,binding, mFoodClickListener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull FoodItemViewHolder holder, int position) {
            Food food = getItem(position);
            holder.bindTo(food);
        }
    }
    public MarketDetailsViewModel(){
        mRepo = Repo.getInstance();
    }

    public FoodItemViewHolderAdapter getFoodItemViewHolderAdapter(ChooseFoodViewModel.FoodClickListener foodClickListener){
        if(FOOD_ITEM_VIEW_HOLDER_ADAPTER==null){
            FOOD_ITEM_VIEW_HOLDER_ADAPTER = new FoodItemViewHolderAdapter(ViewModelHelper.getModelDiffCallback(Food.class),foodClickListener);
        }
        return FOOD_ITEM_VIEW_HOLDER_ADAPTER;
    }
    public Repo getRepo(){ return mRepo; }


}
