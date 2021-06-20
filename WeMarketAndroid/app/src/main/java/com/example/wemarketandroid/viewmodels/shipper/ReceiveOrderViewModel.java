package com.example.wemarketandroid.viewmodels.shipper;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wemarketandroid.databinding.ItemBuyerOrderBinding;
import com.example.wemarketandroid.databinding.ItemBuyerOrdersFoodsDetailBinding;
import com.example.wemarketandroid.databinding.ItemShipperMarketBinding;
import com.example.wemarketandroid.databinding.ItemShipperMarketFoodBinding;
import com.example.wemarketandroid.models.Delivery;
import com.example.wemarketandroid.models.Food;
import com.example.wemarketandroid.models.Market;
import com.example.wemarketandroid.models.MarketOrderDetail;
import com.example.wemarketandroid.models.OrderDetail;
import com.example.wemarketandroid.repository.Repo;
import com.example.wemarketandroid.viewmodels.buyer.OrdersViewModel;
import com.example.wemarketandroid.viewmodels.buyer.ViewModelHelper;

import java.util.List;

public class ReceiveOrderViewModel extends AndroidViewModel {
    private Repo mRepo;
    private Context mContext;
    private static OrderDetailsAdapter ORDER_DETAILS_ADAPTER;
    private static MarketOrderDetailAdapter MARKET_ODER_DETAIL_ADAPTER;

    private class OrderDetailsViewHolder extends RecyclerView.ViewHolder {
        ItemShipperMarketFoodBinding binding;
        public OrderDetailsViewHolder(@NonNull View itemView, ItemShipperMarketFoodBinding binding) {
            super(itemView);
            this.binding = binding;
        }
        public void bindTo(OrderDetail orderDetail){
            Food food = orderDetail.getFood();
            binding.imageShipperOrdersFoodsDetailFoodImage.setImageResource(Integer.parseInt(food.getImageUri()));
            binding.textShipperOrderFoodsDetailFoodName.setText(food.getName());
            binding.textShipperOrderFoodsDetailMarketName.setText("From: "+food.getMarket().getName());
            binding.textShipperOrderFoodsDetailFoodQuantity.setText(String.format("%,d kg",orderDetail.getKg()));
            binding.textShipperOrderFoodsDetailFoodPrice.setText(String.format("%,d VND",orderDetail.getKg()*food.getPrice()));
        }
    }

    public class OrderDetailsAdapter extends ListAdapter<OrderDetail, OrderDetailsViewHolder> {

        protected OrderDetailsAdapter(@NonNull DiffUtil.ItemCallback<OrderDetail> diffCallback) {
            super(diffCallback);
        }

        @NonNull
        @Override
        public OrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemShipperMarketFoodBinding binding = ItemShipperMarketFoodBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            OrderDetailsViewHolder viewHolder = new OrderDetailsViewHolder(binding.getRoot(), binding);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull OrderDetailsViewHolder holder, int position) {
            OrderDetail orderDetail = getItem(position);
            holder.bindTo(orderDetail);
        }
    }

    public class MarketOrderDetailViewHolder extends RecyclerView.ViewHolder{
        private ItemShipperMarketBinding binding;
        private RecyclerView.RecycledViewPool viewPool;
        public MarketOrderDetailViewHolder(@NonNull View itemView, ItemShipperMarketBinding binding, RecyclerView.RecycledViewPool viewPool) {
            super(itemView);
            this.binding = binding;
            this.viewPool = viewPool;
        }
        public void bindTo(MarketOrderDetail marketOrderDetail, int prefetchCount){
            Market market = marketOrderDetail.getMarket();
            binding.textShipperItemMarketName.setText(market.getName());
            binding.textShipperItemMarketAddress.setText(market.getAddress());
            binding.textShipperItemMarketDistance.setText(market.getDistance()+"km");
            // binding recycle view
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            layoutManager.setInitialPrefetchItemCount(prefetchCount);
            OrderDetailsAdapter adapter = new OrderDetailsAdapter(ViewModelHelper.getModelDiffCallback(OrderDetail.class));
            binding.recyclerShipperItemMarketFood.setAdapter(adapter);
            binding.recyclerShipperItemMarketFood.setLayoutManager(layoutManager);
            binding.recyclerShipperItemMarketFood.setRecycledViewPool(viewPool);
            adapter.submitList(marketOrderDetail.getMarketOrderDetails());
        }
    }
    public class MarketOrderDetailAdapter extends ListAdapter<MarketOrderDetail, MarketOrderDetailViewHolder>{
        private RecyclerView.RecycledViewPool viewPool;

        protected MarketOrderDetailAdapter(DiffUtil.ItemCallback<MarketOrderDetail> callback) {
            super(callback);
            viewPool = new RecyclerView.RecycledViewPool();
        }

        @NonNull
        @Override
        public MarketOrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemShipperMarketBinding binding = ItemShipperMarketBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new MarketOrderDetailViewHolder(binding.getRoot(), binding, viewPool);
        }

        @Override
        public void onBindViewHolder(@NonNull MarketOrderDetailViewHolder holder, int position) {
            MarketOrderDetail orderDetail = getItem(position);
            holder.bindTo(orderDetail, getItemCount());
        }
    }


    public ReceiveOrderViewModel(Application application){
        super(application);
        mRepo = Repo.getInstance();
        mContext = application.getApplicationContext();
    }
    public OrderDetailsAdapter getOrderDetailsAdapter(){
        if(ORDER_DETAILS_ADAPTER==null){
            ORDER_DETAILS_ADAPTER = new OrderDetailsAdapter(ViewModelHelper.getModelDiffCallback(OrderDetail.class));
        }
        return ORDER_DETAILS_ADAPTER;
    }
    public MarketOrderDetailAdapter getMarketOrderDetailAdapter(){
        if(MARKET_ODER_DETAIL_ADAPTER==null){
            MARKET_ODER_DETAIL_ADAPTER = new MarketOrderDetailAdapter(ViewModelHelper.getModelDiffCallback(MarketOrderDetail.class));
        }
        return MARKET_ODER_DETAIL_ADAPTER;
    }



    public Repo getmRepo() {
        return mRepo;
    }

}
