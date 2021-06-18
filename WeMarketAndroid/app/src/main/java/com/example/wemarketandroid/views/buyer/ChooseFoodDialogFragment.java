package com.example.wemarketandroid.views.buyer;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.wemarketandroid.databinding.DialogChooseFoodBinding;
import com.example.wemarketandroid.models.Food;
import com.example.wemarketandroid.repository.Repo;
import com.example.wemarketandroid.viewmodels.buyer.OnDialogResult;
import com.example.wemarketandroid.viewmodels.buyer.ViewModelHelper;

public class ChooseFoodDialogFragment extends DialogFragment {

    private DialogChooseFoodBinding mViewBinding;
    private Repo mRepo;
    private LiveData<Food> mFood;
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
        // observes food item
        mFood.observe(getViewLifecycleOwner(), new Observer<Food>() {
            @Override
            public void onChanged(Food food) {
                // TODO: move the binding textinput edittext to helper class
                mViewBinding.imageDialogFoodImage.setImageResource(Integer.parseInt(food.getImageUri()));
                mViewBinding.includeDialogFoodDetails.textBuyerDialogFoodName.setText(food.getName());
                ViewModelHelper.bindIncludeFoodPricing(mViewBinding.includeDialogFoodDetails.includeFoodPricing,food.getBasePrice(),food.getPrice());
                ViewModelHelper.bindIncludeQuantityPriceExchange(mViewBinding.includeDialogFoodDetails.includeBuyerQuantityPriceExchange,1,food.getPrice(),food.getPrice());
            }
        });
        // defines click handler for add to cart button
        mViewBinding.includeDialogFoodDetails.buttonBuyerDialogAddToCartMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(mViewBinding.includeDialogFoodDetails.includeBuyerQuantityPriceExchange.textInputEditTextBuyerDialogFoodAmount.getText().toString());
                int cost = quantity*mFood.getValue().getPrice();
                OnDialogResult handler = (OnDialogResult)getTargetFragment();
                handler.handle(mFood.getValue(),cost);
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
