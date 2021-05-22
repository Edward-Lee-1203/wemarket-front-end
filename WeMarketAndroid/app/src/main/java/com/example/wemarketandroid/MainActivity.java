package com.example.wemarketandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    /**
     * User needs a default delivery address
     * Items in cart checkout should have market name on the side
     * "delivery" doesn't have delivery fee
     */
}