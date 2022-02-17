package com.example.curatorsttit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.curatorsttit.adapters.OnSlideAdapter;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ActionBar actionBar;
    Toolbar toolbar;
    ViewPager viewPager;
    LinearLayout dots;
    OnSlideAdapter slideAdapter;


    public static int CURRENT_FRAGMENT;
    public static int LAST_FRAGMENT;
    private SharedPreferences prefs;

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
        findViewById(R.id.button).setOnClickListener(this);
        viewPager = findViewById(R.id.slider);
        slideAdapter = new OnSlideAdapter(this);
        /*viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return false;
            }
        });
        viewPager.setCurrentItem(1,false);*/
        viewPager.setAdapter(slideAdapter);

    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment fragment;
        //SecondFragment fragment = new SecondFragment();

        //if(CURRENT_FRAGMENT == LAST_FRAGMENT)
            switch (CURRENT_FRAGMENT) {
                case R.id.fragment_second:
                    CURRENT_FRAGMENT = R.id.fragment_main;
                    fragment =  new MainFragment();
                    break;
                case R.id.fragment_main:
                    CURRENT_FRAGMENT = R.id.fragment_second;
                    fragment = new SecondFragment();
                    break;
                default:
                    CURRENT_FRAGMENT = R.id.fragment_main;
                    fragment =  new MainFragment();
                    break;
            }
        loadFragment(fragment);
        //whichFragment(R.id.fragment_second);
        //NumberFormat.getInstance().format(123121L);
    }

    // Метод для создания фрагмента по его id
    @SuppressLint("NonConstantResourceId")
    private Fragment whichFragment(int id) {
        switch (id) {
            case R.id.fragment_main:
                CURRENT_FRAGMENT = R.id.fragment_main;
                return new MainFragment();
            case R.id.fragment_second:
                CURRENT_FRAGMENT = R.id.fragment_second;
                return new SecondFragment();
            default:
                CURRENT_FRAGMENT = R.id.fragment_main;
                return new MainFragment();
        }
    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.mainFrame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}