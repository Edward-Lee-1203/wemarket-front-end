package com.example.wemarketandroid.views.buyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.wemarketandroid.R;
import com.example.wemarketandroid.databinding.FragmentBuyerChooseFoodBinding;
import com.example.wemarketandroid.databinding.FragmentBuyerHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChooseFoodFragment extends Fragment implements IHideBottomNavBar, IUseToolbarOnlyTitle {
    
    private MainActivity mContainingActivity;
    private FragmentBuyerChooseFoodBinding mViewBinding;
    private MenuItem[] mHiddenMenuItem;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewBinding = FragmentBuyerChooseFoodBinding.inflate(inflater,container,false);
        View rootView = mViewBinding.getRoot();

        mContainingActivity = (MainActivity)getActivity();
        Menu menu = mContainingActivity.getmToolbar().getMenu();
        mHiddenMenuItem = new MenuItem[]{menu.getItem(0), menu.getItem(1)};
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mContainingActivity.getmBottomNavBar().setVisibility(View.GONE);
        for(MenuItem item : mHiddenMenuItem) item.setVisible(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        mContainingActivity.getmBottomNavBar().setVisibility(View.VISIBLE);
        for(MenuItem item : mHiddenMenuItem) item.setVisible(true);
    }

    // Template code
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mViewBinding = null;
    }
    
    public void useToolbarOnlyTitle(Toolbar toolbar) {
        
    }
    @Override
    public void hideBottomNavBar(BottomNavigationView bottomNavigationView) {
        
    }

//    TODO: dynamically create bottom linear layout
//    TODO: checkout listener
}