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
import com.squareup.picasso.Picasso;

public class ChooseFoodDialogFragment extends DialogFragment {

    private DialogChooseFoodBinding mViewBinding;
    private Repo mRepo;
    private LiveData<Food> mFood;
    private static final String FOOD_ID_KEY = "foodId";

    public ChooseFoodDialogFragment(){}

    public static ChooseFoodDialogFragment newInstance(long foodId){
        ChooseFoodDialogFragment dialogFragment = new ChooseFoodDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(FOOD_ID_KEY,foodId);
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
        long foodId = getArguments().getLong(FOOD_ID_KEY);
        mFood = mRepo.getFoodById(foodId);
        // observes food item
        mFood.observe(getViewLifecycleOwner(), new Observer<Food>() {
            @Override
            public void onChanged(Food food) {
                // TODO: move the binding textinput edittext to helper class
//                mViewBinding.imageDialogFoodImage.setImageResource(Integer.parseInt(food.getImageUri()));
                if(food==null) return;
                Picasso.get().load(food.getUrlImg()).into(mViewBinding.imageDialogFoodImage);
                mViewBinding.includeDialogFoodDetails.textBuyerDialogFoodName.setText(food.getName());
                ViewModelHelper.bindIncludeFoodPricing(mViewBinding.includeDialogFoodDetails.includeFoodPricing,food.getPrice(),(int)(food.getPrice()-food.getPrice()*food.getDiscount()/100));
                ViewModelHelper.bindIncludeQuantityPriceExchange(mViewBinding.includeDialogFoodDetails.includeBuyerQuantityPriceExchange,1,food.getPrice(),(int)(food.getPrice()-food.getPrice()*food.getDiscount()/100));
            }
        });
        // defines click handler for add to cart button
        mViewBinding.includeDialogFoodDetails.buttonBuyerDialogAddToCartMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Food food = mFood.getValue();
                double quantity = Double.parseDouble(mViewBinding.includeDialogFoodDetails.includeBuyerQuantityPriceExchange.textInputEditTextBuyerDialogFoodAmount.getText().toString());
                int discountPrice = (int)(food.getPrice()-food.getPrice()*food.getDiscount()/100);
                int cost = (int)(quantity*discountPrice);
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
