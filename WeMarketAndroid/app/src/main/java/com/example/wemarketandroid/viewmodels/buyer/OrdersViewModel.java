package com.example.wemarketandroid.viewmodels.buyer;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wemarketandroid.databinding.ItemBuyerOrderBinding;
import com.example.wemarketandroid.databinding.ItemBuyerOrdersFoodsDetailBinding;
import com.example.wemarketandroid.models.Delivery;
import com.example.wemarketandroid.models.Food;
import com.example.wemarketandroid.models.OrderDetail;
import com.example.wemarketandroid.repository.Repo;

import java.util.List;

public class OrdersViewModel extends AndroidViewModel {

    private Context mContext;
    private Repo mRepo;
    private static OrderAdapter ORDER_ADAPTER;


    public OrdersViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mRepo = Repo.getInstance();
    }


    public class OrderDetailsViewHolder extends RecyclerView.ViewHolder{
        private ItemBuyerOrdersFoodsDetailBinding binding;

        public OrderDetailsViewHolder(@NonNull View itemView, ItemBuyerOrdersFoodsDetailBinding binding) {
            super(itemView);
            this.binding = binding;
        }
        public void bindTo(OrderDetail orderDetail){
            // binds cart item to view
            Food food = orderDetail.getFood();
            binding.imageBuyerOrdersFoodsDetailFoodImage.setImageResource(Integer.parseInt(food.getImageUri()));
            binding.textBuyerOrderFoodsDetailFoodName.setText(food.getName());
            binding.textBuyerOrderFoodsDetailMarketName.setText("From: "+food.getMarket().getName());
            binding.textBuyerOrderFoodsDetailFoodQuantity.setText(String.format("%,d kg",orderDetail.getKg()));
            binding.textBuyerOrderFoodsDetailFoodPrice.setText(String.format("%,d VND",orderDetail.getKg()*food.getPrice()));
        }
    }
    public class OrderDetailsAdapter extends ListAdapter<OrderDetail, OrderDetailsViewHolder>{

        protected OrderDetailsAdapter(@NonNull DiffUtil.ItemCallback<OrderDetail> diffCallback) {
            super(diffCallback);
        }

        @NonNull
        @Override
        public OrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemBuyerOrdersFoodsDetailBinding binding = ItemBuyerOrdersFoodsDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            OrderDetailsViewHolder viewHolder = new OrderDetailsViewHolder(binding.getRoot(), binding);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull OrderDetailsViewHolder holder, int position) {
            OrderDetail orderDetail = getItem(position);
            holder.bindTo(orderDetail);
        }
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{
        private ItemBuyerOrderBinding binding;
        private LifecycleOwner mLifecycleOwner;

        public OrderViewHolder(@NonNull View itemView, ItemBuyerOrderBinding binding, LifecycleOwner lifecycleOwner) {
            super(itemView);
            this.binding = binding;
            mLifecycleOwner = lifecycleOwner;
        }
        public void bindTo(Delivery delivery, RecyclerView.RecycledViewPool viewPool){
            // binds delivery object to view and recycler view
            binding.textBuyerOrderItemOrderId.setText("Delivery No."+delivery.getId());
            binding.textBuyerOrderItemOrderDate.setText("Date created: "+delivery.getDate());
            binding.textBuyerOrderItemOrderStatus.setText(delivery.isConfirmed()?"Completed":"Delivering");
            binding.textBuyerOrderItemShipperName.setText("Shipper: "+delivery.getShipper().getName());
            binding.textBuyerOrderItemTotalPrice.setText(String.format("%,d VND",delivery.getOrder().getTotalPrice()));

            // retrieves list of cart items
            List<OrderDetail> orderDetailList = delivery.getOrder().getOrderDetailList();
            // setups recycler view
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
            linearLayoutManager.setInitialPrefetchItemCount(orderDetailList.size());
            OrderDetailsAdapter adapter = new OrderDetailsAdapter(ViewModelHelper.getModelDiffCallback(OrderDetail.class));
            adapter.submitList(orderDetailList);
            binding.recyclerBuyerOrdersOrderDetail.setAdapter(adapter);
            binding.recyclerBuyerOrdersOrderDetail.setLayoutManager(linearLayoutManager);
            binding.recyclerBuyerOrdersOrderDetail.setRecycledViewPool(viewPool);

            // TODO: uses see shipper button clicked callback to handle click event

        }
    }
    public class OrderAdapter extends ListAdapter<Delivery, OrderViewHolder>{

        private RecyclerView.RecycledViewPool viewPool;
        private LifecycleOwner mLifecycleOwner;

        protected OrderAdapter(@NonNull DiffUtil.ItemCallback<Delivery> diffCallback, LifecycleOwner lifecycleOwner) {
            super(diffCallback);
            mLifecycleOwner = lifecycleOwner;
            viewPool = new RecyclerView.RecycledViewPool();
        }

        @NonNull
        @Override
        public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemBuyerOrderBinding binding = ItemBuyerOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            OrderViewHolder viewHolder = new OrderViewHolder(binding.getRoot(), binding, mLifecycleOwner);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
            Delivery delivery = getItem(position);
            holder.bindTo(delivery, viewPool);
        }
    }

    public OrderAdapter getOrderAdapter(LifecycleOwner lifecycleOwner){
        if(ORDER_ADAPTER==null){
            ORDER_ADAPTER = new OrderAdapter(ViewModelHelper.getModelDiffCallback(Delivery.class), lifecycleOwner);
        }
        return ORDER_ADAPTER;
    }
    public Repo getRepo(){ return mRepo; }
}
