package com.example.wemarketandroid.viewmodels.buyer;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wemarketandroid.R;
import com.example.wemarketandroid.databinding.IncludeBuyerBottomBarBinding;
import com.example.wemarketandroid.databinding.IncludeBuyerQuantityPriceExchangeBinding;
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
    
    public static void bindIncludeQuantityPriceExchange(IncludeBuyerQuantityPriceExchangeBinding viewBinding, int initialFoodAmount, int initialCostAmount, int price){
        // creates TextWatchers
        TextWatcher foodAmountTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String string = editable.toString();
                if(!string.isEmpty() && viewBinding.textInputEditTextBuyerDialogFoodAmount.hasFocus()) {
                    String[] hundreds = string.split(",");
                    string = "";
                    for(int i = 0; i<hundreds.length; ++i) string+= hundreds[i];
                    int quantity = Integer.parseInt(string);
                    int cost = quantity * price;
                    viewBinding.textInputEditTextBuyerDialogCostAmount.setText(String.format("%,d", cost));
                }
            }
        };
        TextWatcher costAmountTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String string = editable.toString();
                if(!string.isEmpty() && viewBinding.textInputEditTextBuyerDialogCostAmount.hasFocus()) {
                    String[] hundreds = string.split(",");
                    string = "";
                    for(int i = 0; i<hundreds.length; ++i) string+= hundreds[i];
                    int cost = Integer.parseInt(string);
                    int quantity = (int) Math.floor(cost / price);
                    viewBinding.textInputEditTextBuyerDialogFoodAmount.setText(String.format("%,d", quantity));
                }
            }
        };
        // binding
        viewBinding.textInputEditTextBuyerDialogFoodAmount.setText(String.format("%,d",initialFoodAmount));
        viewBinding.textInputEditTextBuyerDialogFoodAmount.addTextChangedListener(foodAmountTextWatcher);
        viewBinding.textInputEditTextBuyerDialogCostAmount.setText(String.format("%,d",initialCostAmount));
        viewBinding.textInputEditTextBuyerDialogCostAmount.addTextChangedListener(costAmountTextWatcher);

    }
}
