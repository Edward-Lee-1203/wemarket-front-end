package com.example.wemarketandroid.views.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.wemarketandroid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private BottomNavigationView mBottomNavBar;
    private BottomNavigationView.OnNavigationItemSelectedListener mBottomNavBarItemListener;
    private NavController mNavController;

    private void initComponents(){
        mToolbar = (Toolbar)findViewById(R.id.toolbar_buyer);
        mBottomNavBar = (BottomNavigationView) findViewById(R.id.bottom_navigation_buyer);
        mNavController = Navigation.findNavController(this,R.id.fragment_container_buyer);
        mBottomNavBarItemListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id!=R.id.bottomnavbar_search) {
                    mNavController.navigate(id);
                } else{
                    onSearchRequested();
                }
                return true;
            }
        };
    }
    private void handleIntent(Intent intent){
        if(intent.getAction().equals(Intent.ACTION_SEARCH)){
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
        mBottomNavBar.setOnNavigationItemSelectedListener(mBottomNavBarItemListener);
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_buyer, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.toolbar_buyer_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setQueryHint(getString(R.string.search_hint_buyer));
        searchView.setSubmitButtonEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // this method checks for appbar item click event
        switch (item.getItemId()) {
            case R.id.toolbar_buyer_search:  {
                onSearchRequested();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}