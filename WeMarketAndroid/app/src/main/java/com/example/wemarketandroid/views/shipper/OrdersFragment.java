package com.example.wemarketandroid.views.shipper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.wemarketandroid.databinding.FragmentShipperHomeBinding;

public class OrdersFragment extends Fragment {

    private MainActivity mContainingActivity;
    private FragmentShipperHomeBinding mViewBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewBinding = FragmentShipperHomeBinding.inflate(inflater,container,false);
        View rootView = mViewBinding.getRoot();
        mContainingActivity = (MainActivity) getActivity();
//        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()).create(HomeViewModel.class);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mContainingActivity.getmBottomNavBar().setVisibility(View.VISIBLE);
        mContainingActivity.getmToolbar().setVisibility(View.VISIBLE);
        Menu menu = mContainingActivity.getmToolbar().getMenu();
        for(int i = 0; i<menu.size();++i) {
            menu.getItem(i).setVisible(true);
        }
    }

    // Template code
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mViewBinding = null;
    }
}