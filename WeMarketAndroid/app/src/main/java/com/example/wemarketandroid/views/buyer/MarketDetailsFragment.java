package com.example.wemarketandroid.views.buyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.wemarketandroid.R;
import com.example.wemarketandroid.databinding.FragmentBuyerChooseFoodBinding;
import com.example.wemarketandroid.databinding.FragmentBuyerMarketDetailsBinding;

public class MarketDetailsFragment extends Fragment {

    private MainActivity mContainingActivity;
    private FragmentBuyerMarketDetailsBinding mViewBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewBinding = FragmentBuyerMarketDetailsBinding.inflate(inflater,container,false);
        View rootView = mViewBinding.getRoot();
        mContainingActivity = (MainActivity)getActivity();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        mContainingActivity.getSupportActionBar().hide();
        mContainingActivity.getmBottomNavBar().setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
//        mContainingActivity.getSupportActionBar().show();
        mContainingActivity.getmBottomNavBar().setVisibility(View.VISIBLE);
    }

    // Template code
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mViewBinding = null;
    }
//    TODO: implement up button on actionbar
//    TODO: inflate another actionbar layout
//    TODO: dynamically create the linear layout for the bottom bar
//    TODO: checkout listener
}