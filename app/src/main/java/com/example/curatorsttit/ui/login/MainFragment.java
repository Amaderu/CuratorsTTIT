package com.example.curatorsttit.ui.login;

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

    /*// TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    *//**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     *//*
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/
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