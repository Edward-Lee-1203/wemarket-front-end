package com.example.wemarketandroid.viewmodels.buyer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wemarketandroid.databinding.ItemBuyerBucketBinding;
import com.example.wemarketandroid.models.CartItem;
import com.example.wemarketandroid.repository.Repo;
import com.squareup.picasso.Picasso;

public class CheckoutViewModel extends ViewModel {
    private Repo mRepo;
    private MutableLiveData<Boolean> mIsKeepSocialDistancing;
    private MutableLiveData<String> mDeliveryAddress;
    private static CartItemViewHolderAdapter CART_ITEM_VIEW_HOLDER_ADAPTER;
    
    public interface OnCartItemClicked{
        void onItemClick(CartItem cartItem);
    }

    private class CartItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemBuyerBucketBinding binding;
        private CartItem mCartItem;
        private OnCartItemClicked mOnCartItemClicked;

        public CartItemViewHolder(@NonNull View itemView, ItemBuyerBucketBinding binding, OnCartItemClicked onCartItemClicked) {
            super(itemView);
            this.binding = binding;
            mOnCartItemClicked = onCartItemClicked;
            itemView.setOnClickListener(this);
            binding.fabItemBuyerBuckerRemove.setOnClickListener(this);
        }

        public void bindTo(CartItem cartItem){
            mCartItem = cartItem;
            // debug code
//            binding.includeImageBadgeBottom.imageIncludeImageBadgeBottomFoodImage.setImageResource(Integer.parseInt(cartItem.getFood().getUrlImg()));
            Picasso.get().load(cartItem.getFood().getUrlImg()).into(binding.includeImageBadgeBottom.imageIncludeImageBadgeBottomFoodImage);

            binding.textBuyerItemMarketFoodName.setText(cartItem.getFood().getName());
            binding.textItemBuyerBucketPrice.setText(String.format("%,d",cartItem.getFood().getPrice()));
            ViewModelHelper.bindIncludeQuantityPriceExchange(binding.includeBuyerQuantityPriceExchange, cartItem.getQuantity(), cartItem.getPrice(), cartItem.getDiscountPrice());
        }


        public void onClick(View view) {
            if(view.getId()==binding.fabItemBuyerBuckerRemove.getId()){
                mOnCartItemClicked.onItemClick(mCartItem);
            }
        }
    }

    public class CartItemViewHolderAdapter extends ListAdapter<CartItem, CartItemViewHolder> {

        private OnCartItemClicked mOnCartItemClicked;

        public CartItemViewHolderAdapter(DiffUtil.ItemCallback<CartItem> callback, OnCartItemClicked onCartItemClicked){
            super(callback);
            mOnCartItemClicked = onCartItemClicked;
        }

        @NonNull
        @Override
        public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemBuyerBucketBinding binding = ItemBuyerBucketBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            View view = binding.getRoot();
            CartItemViewHolder viewHolder = new CartItemViewHolder(view,binding, mOnCartItemClicked);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
            CartItem cartItem = getItem(position);
            holder.bindTo(cartItem);
        }
    }

    public CheckoutViewModel(){
        mRepo = Repo.getInstance();
        mIsKeepSocialDistancing = new MutableLiveData<>();
        mIsKeepSocialDistancing.postValue(true);
        mDeliveryAddress = new MutableLiveData<>();
        // TODO: use geolocation to detect user address
    }

    public void setIsKeepSocialDistancing(boolean isKeepSocialDistancing){
        mIsKeepSocialDistancing.postValue(isKeepSocialDistancing);
    }
    public LiveData<Boolean> getIsKeepSocialDistancing(){ return mIsKeepSocialDistancing; }

    public void setDeliveryAddress(String address){
        mDeliveryAddress.postValue(address);
    }
    public LiveData<String> getDeliveryAddress(){ return mDeliveryAddress; }

    public CartItemViewHolderAdapter getCartItemViewHolderAdapter(OnCartItemClicked onCartItemClicked){
        if(CART_ITEM_VIEW_HOLDER_ADAPTER==null){
            CART_ITEM_VIEW_HOLDER_ADAPTER = new CartItemViewHolderAdapter(ViewModelHelper.getModelDiffCallback(CartItem.class),onCartItemClicked);
        }
        return CART_ITEM_VIEW_HOLDER_ADAPTER;
    }
}
