package com.example.wemarketandroid.viewmodels.buyer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wemarketandroid.databinding.ItemBuyerHomeControlsBinding;
import com.example.wemarketandroid.databinding.ItemBuyerPromoBinding;
import com.example.wemarketandroid.models.buyer.Filter;
import com.example.wemarketandroid.models.buyer.Food;
import com.example.wemarketandroid.models.buyer.User;
import com.example.wemarketandroid.repository.Repo;
import com.squareup.picasso.Picasso;

import java.util.List;


public class HomeViewModel extends ViewModel {
    private Repo mRepo;
    private static FilterViewHolderAdapter FILTERVIEWHOLDER_ADAPTER;
    private static PromoViewHolderAdapter PROMOVIEWHOLDER_ADAPTER;
    private LiveData<User> mUserLiveData;
    private LiveData<List<Food>> mFoodLiveData;

    
    private class FilterViewHolder extends RecyclerView.ViewHolder{

        private ItemBuyerHomeControlsBinding itemBinding;
        public FilterViewHolder(@NonNull View itemView, ItemBuyerHomeControlsBinding binding) {
            super(itemView);
            this.itemBinding = binding;
        }

        public ItemBuyerHomeControlsBinding getItemBinding() {
            return itemBinding;
        }
    }
    private class PromoViewHolder extends RecyclerView.ViewHolder {

        private ItemBuyerPromoBinding itemBinding;
        public PromoViewHolder(@NonNull View itemView, ItemBuyerPromoBinding binding) {
            super(itemView);
            this.itemBinding = binding;
        }
        public void bindTo(Food food){
            // production code
//            Picasso.get().load(food.getImageUri()).placeholder(R.drawable.icon_chicken_legs).into(itemBinding.imageBuyerPromoPhoto, new Callback() {
//                @Override
//                public void onSuccess() {
//                    Log.d("PicassoLoad","success");
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    Log.d("PicassoLoad",e.toString());
//                }
//            });
            // debug code
            itemBinding.imageBuyerPromoPhoto.setImageResource(Integer.parseInt(food.getImageUri()));

            itemBinding.includeItemBuyerPromo.textBuyerPromoFoodName.setText(food.getName());
            itemBinding.includeItemBuyerPromo.textBuyerPromoFoodPriceCurrent.setText(food.getPrice()+"");
            itemBinding.includeItemBuyerPromo.textBuyerPromoMarketName.setText(food.getMarket().getName());
            itemBinding.includeItemBuyerPromo.textBuyerPromoFoodPriceBefore.setText(""+(food.getPrice()-food.getDiscount()*food.getPrice()));
        }

        public ItemBuyerPromoBinding getItemBinding() {
            return itemBinding;
        }
    }



    public class PromoViewHolderAdapter extends ListAdapter<Food,PromoViewHolder>{

        public PromoViewHolderAdapter(DiffUtil.ItemCallback<Food> callback){
            super(callback);
        }
        @NonNull
        @Override
        public PromoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemBuyerPromoBinding binding = ItemBuyerPromoBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new PromoViewHolder(binding.getRoot(),binding);
        }

        @Override
        public void onBindViewHolder(@NonNull PromoViewHolder holder, int position) {
            Food food = getItem(position);
            holder.bindTo(food);
        }
    }

    public class FilterViewHolderAdapter extends RecyclerView.Adapter<FilterViewHolder>{
        private Filter[] data;

        public FilterViewHolderAdapter(Filter[] data) {
            this.data = data;
        }

        @NonNull
        @Override
        public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemBuyerHomeControlsBinding binding = ItemBuyerHomeControlsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            View view = binding.getRoot();
            return new FilterViewHolder(view,binding);
        }

        @Override
        public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
            Filter filter = data[position];
            Picasso.get().load(filter.getResId()).into(holder.getItemBinding().imageItemBuyerHomeControls);
            holder.getItemBinding().textItemBuyerHomeControls.setText(filter.getLabel());
        }

        @Override
        public int getItemCount() {
            return data.length;
        }
    }


    public HomeViewModel() {
        mRepo = Repo.getInstance();
        mUserLiveData = mRepo.getUser();
        mFoodLiveData = mRepo.getFoodList();
    }
    public FilterViewHolderAdapter getFilterViewHolderAdapter(){
        if(FILTERVIEWHOLDER_ADAPTER==null){
            FILTERVIEWHOLDER_ADAPTER = new FilterViewHolderAdapter(mRepo.getHomeFilters());
        }
        return FILTERVIEWHOLDER_ADAPTER;
    }
    public PromoViewHolderAdapter getPromoViewHolderAdapter(){
        if(PROMOVIEWHOLDER_ADAPTER==null){
            PROMOVIEWHOLDER_ADAPTER = new PromoViewHolderAdapter(ViewModelHelper.getModelDiffCallback(Food.class));
        }
        return PROMOVIEWHOLDER_ADAPTER;
    }
    public LiveData<User> getmUserLiveData(){ return mUserLiveData; }
    public LiveData<List<Food>> getmFoodLiveData(){ return mFoodLiveData; }
}
