package com.example.curatorsttit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.StaticLayout;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.example.curatorsttit.adapters.OnSlideAdapter;
import com.example.curatorsttit.adapters.StudentListViewAdapter;
import com.example.curatorsttit.databinding.ActivityMainBinding;
import com.example.curatorsttit.listeners.StudentInfoFragment;
import com.example.curatorsttit.ui.documents.DocumentsFragment;
import com.example.curatorsttit.ui.login.LoginFragment;
import com.example.curatorsttit.ui.login.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationHost {
    ActionBar actionBar;
    Toolbar toolbar;
    ViewPager viewPager;
    LinearLayout dots;
    OnSlideAdapter slideAdapter;
    Fragment toFragment;

    SearchView editsearch;
    StudentListViewAdapter adapter;
    public static int CURRENT_FRAGMENT;
    public static int LAST_FRAGMENT;
    private SharedPreferences prefs;
    ActivityMainBinding binding;

    static{
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
    }


    void login() {
        prefs = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.user_key), "user");
        editor.apply();
    }

    void getUserfromPrefs() {
        /*prefs = EncryptedSharedPreferences.create(
                sharedPrefsFile,
                mainKeyAlias,
                getApplicationContext(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );*/
        SharedPreferences.Editor sharedPrefsEditor = prefs.edit();
        // Edit the user's shared preferences...
        sharedPrefsEditor.apply();

        String username = prefs.getString(getString(R.string.user_key), null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        });
        String username = getPreferences(Context.MODE_PRIVATE).getString(getString(R.string.user_key), null);
        if (username != null){
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.user_key),username);
            /*toFragment =  whichFragment(R.id.fragment_main);
            if(toFragment != null){
                toFragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainFrame, toFragment, String.valueOf(R.id.fragment_main))
                        .commit();
            }*/
            toFragment = new MainFragment();
            toFragment.setArguments(bundle);
            navigateTo(toFragment, false);

        }
        else {
            toFragment = new LoginFragment();
            navigateTo(toFragment, false);
        }

        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        // Get ActionBar
        //Activity.
        //actionBar = getSupportActionBar();
        // Set below attributes to add logo in ActionBar.
        //actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setTitle("");
        //toolbar = (Toolbar) Toolbar.inflate(getApplicationContext(),R.layout.toolbar,findViewById(R.id.toolbar));
        //loadFragment(whichFragment(R.id.fragment_login));


    }
    /*private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 41;
    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getBaseContext().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }
    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            AppCompatActivity.requestPermissions(getBaseContext(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }*/
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 41;
    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
    /**/


    @Override
    protected void onStart() {
        super.onStart();
        /*if (CURRENT_FRAGMENT == R.id.fragment_login) {
            return;
        }*/

    }

    @Override
    public void onClick(View view) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment fragment;
        //SecondFragment fragment = new SecondFragment();
        //if(CURRENT_FRAGMENT == LAST_FRAGMENT)
        switch (CURRENT_FRAGMENT) {
            case R.id.fragment_main:
                CURRENT_FRAGMENT = R.id.fragment_main;
                fragment = new MainFragment();
                break;
            case R.id.fragment_student_list:
                CURRENT_FRAGMENT = R.id.fragment_student_list;
                fragment = new StudentListFragment();
                break;
            case R.id.fragment_student_info:
                CURRENT_FRAGMENT = R.id.fragment_student_info;
                fragment = new StudentInfoFragment();
                break;
            default:
                CURRENT_FRAGMENT = R.id.fragment_login;
                fragment = new LoginFragment();
                break;
        }
        loadFragment(fragment);
        //whichFragment(R.id.fragment_second);
        //NumberFormat.getInstance().format(123121L);
    }


    public void show(View view) {
        View view2 = view.getRootView();
        //view2 = view2.findViewById(view.getId());
        RelativeLayout layout = null;
        switch (view.getId()) {
            case R.id.commonInf:
                layout = (RelativeLayout) view2.findViewById(R.id.expandable);
                break;
            case R.id.medInf:
                layout = (RelativeLayout) view2.findViewById(R.id.expandable2);
                break;
            case R.id.documents:
                layout = (RelativeLayout) view2.findViewById(R.id.expandable3);
                break;
            case R.id.family:
                layout = (RelativeLayout) view2.findViewById(R.id.expandable4);
                break;
            case R.id.sicknessСertificates:
                layout = (RelativeLayout) view2.findViewById(R.id.expandable5);
                break;
            default: {
                //TODO доделать для остальных блокоф инфы
            }
            break;
        }
        if (layout != null) {
            TransitionManager.beginDelayedTransition(layout, new AutoTransition());
            layout.setVisibility(layout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        }
        //Toast.makeText(this, "Show more", Toast.LENGTH_SHORT).show();
    }

    // Метод для создания фрагмента по его id
    @SuppressLint("NonConstantResourceId")
    public Fragment whichFragment(int id) {
        //if (binding.mainFrame.getTag().equals(CURRENT_FRAGMENT)) return null;
        switch (id) {
            //TODO Доделать правильный навигационный переход
            case R.id.fragment_student_info:
                CURRENT_FRAGMENT = R.id.fragment_student_info;
                return new StudentInfoFragment();
            case R.id.nav_list:
                CURRENT_FRAGMENT = R.id.fragment_student_list;
                return new StudentListFragment();
            case R.id.fragment_login:
                CURRENT_FRAGMENT = R.id.fragment_login;
                return new LoginFragment();
            case R.id.fragment_main:
                CURRENT_FRAGMENT = R.id.fragment_main;
                return new MainFragment();
            case R.id.fragment_documents:
                CURRENT_FRAGMENT = R.id.fragment_documents;
                return new DocumentsFragment();
            case R.id.nav_documents:
                CURRENT_FRAGMENT = R.id.fragment_documents;
                return new DocumentsFragment();
            default:
                CURRENT_FRAGMENT = 0;
                return null;
        }
    }


    public void loadFragment(Fragment fragment) {
        if (fragment == null) return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        //transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        //if(getSupportFragmentManager().findFragmentByTag("StudentInfoFragment") == null)
        transaction.replace(R.id.container, fragment, String.valueOf(CURRENT_FRAGMENT));
        if (CURRENT_FRAGMENT == R.id.fragment_student_info)
            transaction.addToBackStack("StudentInfoFragment");
        transaction.commit();
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        if (fragment == null) return;
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainFrame, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(fragment.getTag());
        }

        transaction.commit();
    }
}