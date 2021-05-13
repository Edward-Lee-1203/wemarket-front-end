package com.example.wemarketandroid.views.buyer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wemarketandroid.R;
import com.example.wemarketandroid.databinding.FragmentBuyerHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentBuyerHomeBinding mViewBinding;
//    private NavController mNavController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewBinding = FragmentBuyerHomeBinding.inflate(inflater,container,false);
        View rootView = mViewBinding.getRoot();
        mViewBinding.textBuyerSeeAll.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_chooseFoodFragment);
        });


        return rootView;
    }

    // Template code
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mViewBinding = null;
    }
}