package com.example.curatorsttit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    ActionBar actionBar;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        // Get ActionBar
        //Activity.
        //actionBar = getSupportActionBar();
        // Set below attributes to add logo in ActionBar.
        //actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setTitle("");
        //toolbar = (Toolbar) Toolbar.inflate(getApplicationContext(),R.layout.toolbar,findViewById(R.id.mainActivity));
        //setSupportActionBar(toolbar);
        //actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setLogo(R.drawable.ic_bird);
        //actionBar.setTitle("Dev2Qa.com");



    }
}