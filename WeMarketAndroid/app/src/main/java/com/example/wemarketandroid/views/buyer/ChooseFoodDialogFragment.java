package com.example.wemarketandroid.views.buyer;

import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.wemarketandroid.databinding.DialogChooseFoodBinding;
import com.example.wemarketandroid.models.buyer.Food;
import com.example.wemarketandroid.repository.Repo;
import com.example.wemarketandroid.viewmodels.buyer.OnDialogResult;
import com.example.wemarketandroid.viewmodels.buyer.ViewModelHelper;

public class ChooseFoodDialogFragment extends DialogFragment {

    private DialogChooseFoodBinding mViewBinding;
    private Repo mRepo;
    private LiveData<Food> mFood;
    private TextWatcher mQuantityTextWatcher, mCostTextWatcher;
    private static final String FOOD_ID_KEY = "foodId";

    public ChooseFoodDialogFragment(){}

    public static ChooseFoodDialogFragment newInstance(int foodId){
        ChooseFoodDialogFragment dialogFragment = new ChooseFoodDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FOOD_ID_KEY,foodId);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = DialogChooseFoodBinding.inflate(inflater,container,false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRepo = Repo.getInstance();
        mFood = mRepo.getFoodById(getArguments().getInt(FOOD_ID_KEY));
        // custom text watchers
        mQuantityTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String string = editable.toString();
                if(!string.isEmpty() && mViewBinding.includeDialogFoodDetails.includeBuyerQuantityPriceExchange.textInputEditTextBuyerDialogFoodAmount.hasFocus()) {
                    String[] hundreds = string.split(",");
                    string = "";
                    for(int i = 0; i<hundreds.length; ++i) string+= hundreds[i];
                    int quantity = Integer.parseInt(string);
                    int cost = quantity * mFood.getValue().getPrice();
                    mViewBinding.includeDialogFoodDetails.includeBuyerQuantityPriceExchange.textInputEditTextBuyerDialogCostAmount.setText(String.format("%,d", cost));
                }
            }
        };
        mCostTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String string = editable.toString();
                if(!string.isEmpty() && mViewBinding.includeDialogFoodDetails.includeBuyerQuantityPriceExchange.textInputEditTextBuyerDialogCostAmount.hasFocus()) {
                    String[] hundreds = string.split(",");
                    string = "";
                    for(int i = 0; i<hundreds.length; ++i) string+= hundreds[i];
                    int cost = Integer.parseInt(string);
                    int quantity = (int) Math.floor(cost / mFood.getValue().getPrice());
                    mViewBinding.includeDialogFoodDetails.includeBuyerQuantityPriceExchange.textInputEditTextBuyerDialogFoodAmount.setText(String.format("%,d", quantity));
                }
            }
        };
        // observes food item
        mFood.observe(getViewLifecycleOwner(), new Observer<Food>() {
            @Override
            public void onChanged(Food food) {
                // TODO: move the binding textinput edittext to helper class
                mViewBinding.imageDialogFoodImage.setImageResource(Integer.parseInt(food.getImageUri()));
                mViewBinding.includeDialogFoodDetails.textBuyerDialogFoodName.setText(food.getName());
                ViewModelHelper.bindIncludeFoodPricing(mViewBinding.includeDialogFoodDetails.includeFoodPricing,food.getBasePrice(),food.getPrice());
                mViewBinding.includeDialogFoodDetails.includeBuyerQuantityPriceExchange.textInputEditTextBuyerDialogFoodAmount.setText(1+"");
                mViewBinding.includeDialogFoodDetails.includeBuyerQuantityPriceExchange.textInputEditTextBuyerDialogCostAmount.setText(String.format("%,d",food.getPrice()));
                mViewBinding.includeDialogFoodDetails.includeBuyerQuantityPriceExchange.textInputEditTextBuyerDialogFoodAmount.addTextChangedListener(mQuantityTextWatcher);
                mViewBinding.includeDialogFoodDetails.includeBuyerQuantityPriceExchange.textInputEditTextBuyerDialogCostAmount.addTextChangedListener(mCostTextWatcher);
            }
        });
        // defines click handler for add to cart button
        mViewBinding.includeDialogFoodDetails.buttonBuyerDialogAddToCartMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(mViewBinding.includeDialogFoodDetails.includeBuyerQuantityPriceExchange.textInputEditTextBuyerDialogFoodAmount.getText().toString());
                int cost = quantity*mFood.getValue().getPrice();
//                Toast.makeText(getContext(),String.format("Buying %s for %d kg",mFood.getValue().getName(),quantity),Toast.LENGTH_SHORT).show();
                OnDialogResult handler = (OnDialogResult)getTargetFragment();
                handler.handle(mFood.getValue().getId(),cost);
                dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        double scale = 1;
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * scale), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }
}
