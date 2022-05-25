package com.example.curatorsttit;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.curatorsttit.databinding.ActivityMainBinding;
import com.example.curatorsttit.ui.students.StudentInfoFragment;
import com.example.curatorsttit.ui.documents.DocumentsFragment;
import com.example.curatorsttit.ui.login.LoginFragment;
import com.example.curatorsttit.ui.main.MainFragment;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationHost {
    private static final String CURATOR_ID = "CURATOR_ID";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 41;
    Fragment toFragment;
    public static int CURRENT_FRAGMENT;
    public static int LAST_FRAGMENT;
    ActivityMainBinding binding;
    private SharedPreferences sharedPreferences;
    private int curatorId;
    private String username;


    static{
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.StreamReaderImpl");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
    }

    private void getAppDataFromPrefs() throws GeneralSecurityException, IOException {
        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        String masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
        Context context = getApplicationContext();
        sharedPreferences = EncryptedSharedPreferences.create(
                "app_data",
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
        // reading a value
        username = sharedPreferences.getString(getString(R.string.user_key), "");
        curatorId = sharedPreferences.getInt(CURATOR_ID, -1);
        if(curatorId != -1 && !username.isEmpty())
            Log.i("LoadData", "getAppDataFromPrefs: successes");
    }
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
        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE
        });
        try {
            getAppDataFromPrefs();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!username.isEmpty() && curatorId != -1){
            toFragment = new MainFragment();
            navigateTo(toFragment, false);

        }
        else {
            toFragment = new LoginFragment();
            navigateTo(toFragment, false);
        }

    }


    public void show(View view) {
        View view2 = view.getRootView();
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
            }
            break;
        }
        if (layout != null) {
            TransitionManager.beginDelayedTransition(layout, new AutoTransition());
            layout.setVisibility(layout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        }
    }

    @SuppressLint("NonConstantResourceId")
    public Fragment whichFragment(int id) {
        //if (binding.mainFrame.getTag().equals(CURRENT_FRAGMENT)) return null;
        switch (id) {
            //TODO Доделать правильный навигационный переход
//            case R.id.fragment_student_info:
//                CURRENT_FRAGMENT = R.id.fragment_student_info;
//                return new StudentInfoFragment();
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
        // TODO: 31.03.2022  Сделать нормальный переход
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
    @Override
    public void openActivivty(Fragment fragment, boolean addToBackstack) {
        if (fragment == null) return;
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(fragment.getTag());
        }

        transaction.commit();
    }

}