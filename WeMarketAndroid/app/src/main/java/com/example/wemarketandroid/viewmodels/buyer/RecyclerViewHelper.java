package com.example.wemarketandroid.viewmodels.buyer;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wemarketandroid.R;
import com.example.wemarketandroid.models.buyer.Food;
import com.example.wemarketandroid.models.buyer.IDiffable;

public class RecyclerViewHelper {
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
}
