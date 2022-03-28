package com.example.curatorsttit.ui.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.curatorsttit.NavigationHost;
import com.example.curatorsttit.R;
import com.example.curatorsttit.StudentListFragment;
import com.example.curatorsttit.databinding.FragmentLoginBinding;
import com.example.curatorsttit.models.Users;
import com.example.curatorsttit.network.ApiService;

import java.util.Random;
import java.util.prefs.Preferences;

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
    private static final String LOGIN = "LOGIN";
    private static final String PASSWORD = "PASSWORD";

    private String login;
    private String password;

    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance(String login, String password) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(LOGIN, login);
        args.putString(PASSWORD, password);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            login = getArguments().getString(LOGIN);
            password = getArguments().getString(PASSWORD);
        }
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
        binding.login.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!binding.login.getText().toString().isEmpty()) {
                    binding.login.setError(null); //Clear the error
                }
                return false;
            }
        });
        binding.password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!binding.password.getText().toString().isEmpty()) {
                    binding.password.setError(null); //Clear the error
                }
                return false;
            }
        });

        binding.logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, password;
                username = binding.login.getText().toString();
                password = binding.password.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    if (username.isEmpty()) {
                        binding.login.setError("Пустое поле");
                    } else {
                        binding.login.setError(null);
                    }
                    if (password.isEmpty()) {
                        binding.password.setError("Пустое поле");
                    } else {
                        binding.password.setError(null);
                    }
                    //Toast.makeText(requireContext(), "Не все поля заполнены", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    binding.login.setError(null);
                    binding.password.setError(null);
                }
                binding.expandElements.setVisibility(View.GONE);
                binding.expandElements2.setVisibility(View.VISIBLE);
                ApiService.getInstance().getApi().auth(username, password).enqueue(new Callback<Users>() {
                    @Override
                    public void onResponse(Call<Users> call, Response<Users> response) {
                        if (response.isSuccessful()) {
                            Log.d("Authorize response", "onResponse: запрос был успешным!");
                            builder.setTitle("tittle").setMessage(String.valueOf(response.code()))
                                    .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                            SharedPreferences prefs = requireActivity().getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString(getString(R.string.user_key), username);
                            editor.apply();
                            builder.create().show();
                            Bundle bundle = new Bundle();
                            bundle.putString(getString(R.string.user_key), username);
                            bundle.putInt("CURATOR_ID", response.body().getId());
                            Fragment toFragment = new MainFragment();
                            toFragment.setArguments(bundle);

                            ((NavigationHost) getActivity()).navigateTo(toFragment, false);
                            (requireActivity()).getPreferences(Context.MODE_PRIVATE).edit().putString(getString(R.string.user_key),username).commit();// Navigate to the next Fragment
                            Toast.makeText(requireContext(), "Вы вошли в систему", Toast.LENGTH_LONG).show();

                        } else
                        {
                            Log.d("Authorize response", "onResponse: запрос был не успешным");
                            Toast.makeText(requireContext(), "Произошла ошибка", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Users> call, Throwable t) {
                        Log.d("Authorize response", "onFailure: +");
                        Toast.makeText(requireContext(), "Произошла ошибка", Toast.LENGTH_LONG).show();
                        binding.expandElements.setVisibility(View.VISIBLE);
                        binding.expandElements2.setVisibility(View.GONE);
                    }
                });
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        /*binding.logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (requireActivity()).getPreferences(Context.MODE_PRIVATE).edit().putString(getString(R.string.user_key),"username").commit();
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.user_key), "username");
                bundle.putInt("CURATOR_ID", 123);
                Fragment toFragment = new MainFragment();
                toFragment.setArguments(bundle);
                ((NavigationHost) getActivity()).navigateTo(toFragment, false); // Navigate to the next Fragment
            }
        });*/

    }

    @Override
    public void onPause() {
        super.onPause();

    }
}