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
    String username;
    SharedPreferences prefs;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            username = getArguments().getString("username");
            /*((Button)binding.container.findViewById(R.id.log_in)).setOnClickListener(l -> {
                String  name = savedInstanceState.getString("MyArg");
                //findNavController().navigate(R.id.fragmentTwo, bundle)
            });*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new StudentListFragment(), String.valueOf(R.id.fragment_student_list))
                .commit();
        if(binding.navBar != null){

            BottomNavigationView navbar = binding.navBar;
            // Создаем и устанавливаем слушатель событий на навбар
            navbar.setOnItemSelectedListener(item -> {
                // Загружаем нужный фрагмент
                MainActivity m = ((MainActivity)requireActivity());
                m.loadFragment(m.whichFragment(item.getItemId()));
                Toast.makeText(requireContext(),String.valueOf(item.getItemId()),Toast.LENGTH_LONG);
                return true;
            });
        }
    }


}