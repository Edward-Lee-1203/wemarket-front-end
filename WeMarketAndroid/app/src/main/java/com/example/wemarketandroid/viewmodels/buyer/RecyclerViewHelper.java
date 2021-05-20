package com.example.wemarketandroid.viewmodels.buyer;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wemarketandroid.R;

public class RecyclerViewHelper {
    public static void addItemDivider(RecyclerView recyclerView, int orientation){
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),orientation);
        int resId = orientation==LinearLayoutManager.HORIZONTAL?R.drawable.empty_tall_divider:R.drawable.empty_wide_divider;
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), resId));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
}
