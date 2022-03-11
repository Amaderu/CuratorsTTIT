package com.example.curatorsttit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ReactiveGuide;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.curatorsttit.adapters.OnSlideAdapter;
import com.example.curatorsttit.adapters.StudentListViewAdapter;
import com.example.curatorsttit.listeners.StudentInfoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ActionBar actionBar;
    Toolbar toolbar;
    ViewPager viewPager;
    LinearLayout dots;
    OnSlideAdapter slideAdapter;
    Fragment fragment;

    SearchView editsearch;
    StudentListViewAdapter adapter;
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
        //toolbar = (Toolbar) Toolbar.inflate(getApplicationContext(),R.layout.toolbar,findViewById(R.id.toolbar));

        //actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setLogo(R.drawable.ic_bird);
        //actionBar.setTitle("Dev2Qa.com");



        //findViewById(R.id.button).setOnClickListener(this);
        //viewPager = findViewById(R.id.slider);
        BottomNavigationView navbar = findViewById(R.id.nav_bar);

        // Создаем и устанавливаем слушатель событий на навбар
        navbar.setOnItemSelectedListener(item -> {
            // Загружаем нужный фрагмент
            loadFragment(whichFragment(item.getItemId()));
            return true;
        });
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
        //viewPager.setAdapter(slideAdapter);

    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment fragment;
        //SecondFragment fragment = new SecondFragment();

        //if(CURRENT_FRAGMENT == LAST_FRAGMENT)
            switch (CURRENT_FRAGMENT) {
                case R.id.fragment_student_info:
                    CURRENT_FRAGMENT = R.id.fragment_main;
                    fragment =  new MainFragment();
                    break;
                case R.id.fragment_main:
                    CURRENT_FRAGMENT = R.id.fragment_student_info;
                    fragment = new StudentInfoFragment();
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




    public void show(View view){
        View view2 = view.getRootView();
        //view2 = view2.findViewById(view.getId());
        RelativeLayout layout = null;
        switch (view.getId()){
            case R.id.commonInf:
                layout = (RelativeLayout)view2.findViewById(R.id.expandable);
                break;
            case R.id.medInf:
                layout = (RelativeLayout)view2.findViewById(R.id.expandable2);
                break;
            default:{
                //TODO Nothing
            }
            break;
        }
        if(layout != null){
            TransitionManager.beginDelayedTransition(layout, new AutoTransition());
            layout.setVisibility(layout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        }
        //Toast.makeText(this, "Show more", Toast.LENGTH_SHORT).show();
    }
    // Метод для создания фрагмента по его id
    @SuppressLint("NonConstantResourceId")
    private Fragment whichFragment(int id) {
        switch (id) {
            case R.id.nav_stud:
                CURRENT_FRAGMENT = R.id.fragment_student_info;
                return new StudentInfoFragment();
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
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        //transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        //if(getSupportFragmentManager().findFragmentByTag("StudentInfoFragment") == null)
        transaction.replace(R.id.mainFrame, fragment);
        if(CURRENT_FRAGMENT == R.id.fragment_student_info)
            transaction.addToBackStack("StudentInfoFragment");
        transaction.commit();
    }
}