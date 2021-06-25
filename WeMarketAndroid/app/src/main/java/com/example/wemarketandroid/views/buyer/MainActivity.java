package com.example.wemarketandroid.views.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.wemarketandroid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private BottomNavigationView mBottomNavBar;
    private NavController mNavController;
    private SearchView mSearchView;

    private void initComponents(){
        /**
         * mToolbar
         */
        mToolbar = (Toolbar)findViewById(R.id.toolbar_buyer);
        mToolbar.inflateMenu(R.menu.menu_toolbar);
        mToolbar.setOnMenuItemClickListener(item->{
            switch (item.getItemId()) {
                case R.id.toolbar_search:  {
                    onSearchRequested();
                    return true;
                }
                default:
                    return super.onOptionsItemSelected(item);
            }
        });
        Menu menu = mToolbar.getMenu();
        mSearchView = (SearchView) mToolbar.getMenu().findItem(R.id.toolbar_search).getActionView();
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(true);
//        mSearchView.setQueryHint(getString(R.string.search_hint_buyer));
        mSearchView.setSubmitButtonEnabled(true);
        /**
         * mBottomNavBar
         */
        mBottomNavBar = (BottomNavigationView)findViewById(R.id.bottom_navigation_buyer);
        mBottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.bottomnavbar_search) {
                    mSearchView.setIconified(false);
                } else{
                    mNavController.navigate(id);
                }
                return true;
            }
        });
        /**
         * mNavController
         */
        mNavController = Navigation.findNavController(this,R.id.fragment_container_buyer);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.destination_buyer_home,R.id.destination_buyer_orders,R.id.destination_buyer_profile).build();
        NavigationUI.setupWithNavController(mToolbar,mNavController,appBarConfiguration);
    }
    private void handleIntent(Intent intent){
        if(intent.getAction()!=null && intent.getAction().equals(Intent.ACTION_SEARCH)){
            String query = intent.getStringExtra(SearchManager.QUERY);
            // TODO: handle food item query
            Log.d("MainActivity","Query: "+query);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_main);
        initComponents();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    public BottomNavigationView getmBottomNavBar() {
        return mBottomNavBar;
    }

    public Toolbar getmToolbar() {
        return mToolbar;
    }

    public NavController getNavController(){ return mNavController; }
}