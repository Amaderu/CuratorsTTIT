package com.example.curatorsttit.ui.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.curatorsttit.MainActivity;
import com.example.curatorsttit.R;
import com.example.curatorsttit.SplashActivity;
import com.example.curatorsttit.databinding.FragmentLoginBinding;
import com.example.curatorsttit.models.Users;
import com.example.curatorsttit.network.ApiService;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    FragmentLoginBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        //return inflater.inflate(R.layout.fragment_login, container, false);
        int max = 1, offset = 0, index;
        Random rnd = new Random();
        index = rnd.nextInt(max - offset) + offset;
        binding.textView2.setText(getResources().getStringArray(R.array.frazes)[index]);
        binding.logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, password;
                username = binding.login.getText().toString();
                password = binding.password.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(requireContext(), "Не все поля заполнены", Toast.LENGTH_LONG).show();
                    return;
                }
                binding.expandelements.setVisibility(View.GONE);
                binding.loading.setVisibility(View.VISIBLE);
                ApiService.getInstance().getApi().auth(username, password).enqueue(new Callback<Users>() {
                    @Override
                    public void onResponse(Call<Users> call, Response<Users> response) {
                        if (response.isSuccessful()) {
                            builder.setTitle("tittle").setMessage(String.valueOf(response.code()))
                                    .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                            builder.create().show();
                            Toast.makeText(requireContext(), "Вы вошли как в систему", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(requireContext(), "Произошла ошибка", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Users> call, Throwable t) {
                        Toast.makeText(requireContext(), "Произошла ошибка", Toast.LENGTH_LONG).show();
                        binding.expandelements.setVisibility(View.VISIBLE);
                        binding.loading.setVisibility(View.GONE);
                    }
                });
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();


    }

}