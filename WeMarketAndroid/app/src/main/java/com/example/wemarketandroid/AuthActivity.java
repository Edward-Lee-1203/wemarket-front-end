package com.example.wemarketandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

public class AuthActivity extends AppCompatActivity {

    private NavController mNavController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavController = Navigation.findNavController(this,R.id.nav_host_fragment_auth);
        mNavController.navigate(R.id.destination_login);
    }
    public NavController getNavController(){ return mNavController; }
    /**
     * User needs a default delivery address
     * Items in cart checkout should have market name on the side
     * "delivery" doesn't have delivery fee
     */
}