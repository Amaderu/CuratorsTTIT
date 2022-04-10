package com.example.curatorsttit.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.curatorsttit.MainActivity;
import com.example.curatorsttit.R;
import com.example.curatorsttit.StudentListFragment;
import com.example.curatorsttit.databinding.FragmentLoginBinding;
import com.example.curatorsttit.databinding.FragmentMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainFragment extends Fragment {
    FragmentMainBinding binding;
    SharedPreferences prefs;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fragment toFragment = new StudentListFragment();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container, toFragment, toFragment.getTag())
                .commit();
        if(binding.navBar != null){
            BottomNavigationView navbar = binding.navBar;
            navbar.setOnItemSelectedListener(item -> {
                MainActivity m = ((MainActivity)requireActivity());
                m.loadFragment(m.whichFragment(item.getItemId()));
                Toast.makeText(requireContext(),String.valueOf(item.getItemId()),Toast.LENGTH_LONG);
                return true;
            });
        }
    }


}