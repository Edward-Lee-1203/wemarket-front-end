package com.example.wemarketandroid.viewmodels.buyer;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wemarketandroid.R;
import com.example.wemarketandroid.databinding.IncludeBuyerBottomBarBinding;
import com.example.wemarketandroid.databinding.IncludeDialogFoodDetailsBinding;
import com.example.wemarketandroid.databinding.IncludeFoodPricingBinding;
import com.example.wemarketandroid.models.buyer.Food;
import com.example.wemarketandroid.models.buyer.IDiffable;

public class ViewModelHelper {
    public static void addItemDivider(Context context, RecyclerView recyclerView, int orientation){
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,orientation);
        int resId = orientation==LinearLayoutManager.HORIZONTAL?R.drawable.empty_tall_divider:R.drawable.empty_wide_divider;
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, resId));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public static <T extends IDiffable> DiffUtil.ItemCallback getModelDiffCallback(Class<T> tClass) {
        return new DiffUtil.ItemCallback<T>() {
            @Override
            public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                return oldItem.equals(newItem);
            }
        };
    }

    public static void bindIncludeFoodPricing(IncludeFoodPricingBinding viewBiding, int basePrice, int price){
        viewBiding.textBuyerFoodPriceBefore.setText(String.format("%,d",basePrice));
        viewBiding.textBuyerFoodPriceCurrent.setText(String.format("%,d",price));
    }

    public static void transformBottomBarCheckout(IncludeBuyerBottomBarBinding viewBinding) {
        viewBinding.buttonBuyerIncludeBottomBarItemCounter.setEnabled(true);
        viewBinding.buttonBuyerIncludeBottomBarItemCounter.setText("Place order");
        viewBinding.buttonBuyerIncludeBottomBarCheckout.setVisibility(View.INVISIBLE);
        viewBinding.buttonBuyerIncludeBottomBarCheckout.setEnabled(false);
    }
}
