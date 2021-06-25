package com.example.wemarketandroid.viewmodels.shipper;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wemarketandroid.databinding.ItemShipperOrderBinding;
import com.example.wemarketandroid.databinding.ItemShipperOrdersFoodsDetailBinding;
import com.example.wemarketandroid.databinding.ItemShipperOrderBinding;
import com.example.wemarketandroid.models.Delivery;
import com.example.wemarketandroid.models.DeliveryStatus;
import com.example.wemarketandroid.models.Food;
import com.example.wemarketandroid.models.OrderDetail;
import com.example.wemarketandroid.models.Shipper;
import com.example.wemarketandroid.models.User;
import com.example.wemarketandroid.repository.Repo;
import com.example.wemarketandroid.viewmodels.buyer.ViewModelHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrdersViewModel extends AndroidViewModel {

    private Context mContext;
    private Repo mRepo;
    private static OrderAdapter ORDER_ADAPTER;
    private LiveData<List<Delivery>> mDeliveryListLiveData;
//    private LiveData<User> mUserLiveData;


    public OrdersViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mRepo = Repo.getInstance();
        mDeliveryListLiveData = mRepo.getDeliveryByShipper(mRepo.getmShipper().getValue());
//        mUserLiveData = mRepo.getUser();
    }


    public class OrderDetailsViewHolder extends RecyclerView.ViewHolder{
        private ItemShipperOrdersFoodsDetailBinding binding;

        public OrderDetailsViewHolder(@NonNull View itemView, ItemShipperOrdersFoodsDetailBinding binding) {
            super(itemView);
            this.binding = binding;
        }
        public void bindTo(OrderDetail orderDetail){
            // binds cart item to view
            Food food = orderDetail.getFood();
//            binding.imageShipperOrdersFoodsDetailFoodImage.setImageResource(Integer.parseInt(food.getImageUri()));
            Picasso.get().load(orderDetail.getFood().getUrlImg()).into(binding.imageShipperOrdersFoodsDetailFoodImage);
            binding.textShipperOrderFoodsDetailFoodName.setText(food.getName());
            binding.textShipperOrderFoodsDetailMarketName.setText("From: "+food.getMarket().getName());
            binding.textShipperOrderFoodsDetailFoodQuantity.setText(String.format("%,.1f kg",orderDetail.getKilogram()));
            binding.textShipperOrderFoodsDetailFoodPrice.setText(String.format("%,d VND",(int)Math.ceil(orderDetail.getKilogram()*food.getDiscountPrice())));

        }
    }
    public class OrderDetailsAdapter extends ListAdapter<OrderDetail, OrderDetailsViewHolder>{

        protected OrderDetailsAdapter(@NonNull DiffUtil.ItemCallback<OrderDetail> diffCallback) {
            super(diffCallback);
        }

        @NonNull
        @Override
        public OrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemShipperOrdersFoodsDetailBinding binding = ItemShipperOrdersFoodsDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
        private ItemShipperOrderBinding binding;
        private LifecycleOwner mLifecycleOwner;
        private View.OnClickListener onClickListener;

        public OrderViewHolder(@NonNull View itemView, ItemShipperOrderBinding binding, LifecycleOwner lifecycleOwner) {
            super(itemView);
            this.binding = binding;
            mLifecycleOwner = lifecycleOwner;
        }
        public void bindTo(Delivery delivery, RecyclerView.RecycledViewPool viewPool){
            // binds delivery object to view and recycler view
            binding.textShipperOrderItemOrderId.setText("Delivery No."+delivery.getId());
            binding.textShipperOrderItemOrderDate.setText("Date created: "+delivery.getDate());
            binding.textShipperOrderItemOrderStatus.setText(delivery.getDelivery());
            User user = delivery.getUser();
            binding.textShipperOrderItemShipperName.setText(String.format("Buyer: %s",user==null?"None":user.getName()));
            binding.textShipperOrderItemTotalPrice.setText(String.format("%,d VND",delivery.getOrder().getTotalPrice()));
            // reigsters button handler
            binding.buttonShipperOrderConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    completeDelivery(delivery);
                }
            });
            if(!delivery.getDelivery().equals(DeliveryStatus.DELIVERING)){
                // checks if confirmed and delivery
                binding.buttonShipperOrderConfirm.setVisibility(View.GONE);
            }

            // retrieves list of cart items
            mRepo.getOrderDetails(mContext);
            // setups recycler view
            LiveData<List<OrderDetail>> orderDetailList = mRepo.getOrderDetailsByOrderId(delivery.getOrder().getId());
            orderDetailList.observe(mLifecycleOwner, new Observer<List<OrderDetail>>() {
                @Override
                public void onChanged(List<OrderDetail> orderDetails) {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
                    OrderDetailsAdapter adapter = new OrderDetailsAdapter(ViewModelHelper.getModelDiffCallback(OrderDetail.class));
                    binding.recyclerShipperOrdersOrderDetail.setAdapter(adapter);
                    binding.recyclerShipperOrdersOrderDetail.setLayoutManager(linearLayoutManager);
                    binding.recyclerShipperOrdersOrderDetail.setRecycledViewPool(viewPool);
                    linearLayoutManager.setInitialPrefetchItemCount(orderDetails.size());
                    adapter.submitList(orderDetails);
                }
            });

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
            ItemShipperOrderBinding binding = ItemShipperOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
    public LiveData<List<Delivery>> getShipperDeliveryList(){
        return mDeliveryListLiveData;
    }


    public void completeDelivery(Delivery delivery){
        delivery.setConfirm(1);
        delivery.setDelivery(DeliveryStatus.DELIVERED.toString());
        mRepo.saveDelivery(delivery,mContext);
    }
//    public LiveData<User> getUserLiveData(){return mUserLiveData;}
}
