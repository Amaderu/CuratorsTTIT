package com.example.curatorsttit.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.curatorsttit.NavigationHost;
import com.example.curatorsttit.R;
import com.example.curatorsttit.databinding.FragmentLoginBinding;
import com.example.curatorsttit.models.User;
import com.example.curatorsttit.network.ApiService;
import com.example.curatorsttit.ui.main.MainFragment;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class LoginFragment extends Fragment {
    FragmentLoginBinding binding;
    AlertDialog.Builder builder;
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
        builder = new AlertDialog.Builder(requireContext());
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        //return inflater.inflate(R.layout.fragment_login, container, false);
        randomFraze();
        View.OnKeyListener keyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                EditText editText = ((EditText)view);
                if (!editText.getText().toString().isEmpty()) {
                    editText.setError(null); //Clear the error
                }
                return false;
            }
        };
        binding.login.setOnKeyListener(keyListener);
        binding.password.setOnKeyListener(keyListener);

        binding.logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                awaitlog();
                getUser(login, password);
            }
        });
        return binding.getRoot();
    }

    private void randomFraze() {
        String[] frazes = getResources().getStringArray(R.array.frazes);
        int max = frazes.length, offset = 0, index;
        Random rnd = new Random();
        index = rnd.nextInt(max - offset) + offset;
        binding.textView2.setText(frazes[index]);
    }

    //TODO доделать до конца
    private void shprefs() throws GeneralSecurityException, IOException {
        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        String masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
        Context context = requireContext();
        SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                "shared_preferences_filename",
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
        // storing a value;
        sharedPreferences
                .edit()
                .putString("some_key", "some_data")
                .apply();
        // reading a value
        sharedPreferences.getString("some_key", "some_default_value");
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                awaitlog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mockAuth();
                    }
                }, 5000l);

            }
        });

    }
    void mockAuth(){
        binding.expandElements.setVisibility(View.VISIBLE);
        binding.expandElements2.setVisibility(View.GONE);
        if(!binding.login.getText().toString().equals("curator") &&
                !binding.login.getText().toString().equals("curator")){
            reportAuth("Произошла ошибка");
            return;
        }
        (requireActivity()).getPreferences(Context.MODE_PRIVATE).edit().putString(getString(R.string.user_key),"username").commit();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.user_key), "username");
        bundle.putInt("CURATOR_ID", 2);
        Fragment toFragment = new MainFragment();
        toFragment.setArguments(bundle);
        ((NavigationHost) getActivity()).navigateTo(toFragment, false); // Navigate to the next Fragment
    }


    private void awaitlog() {
        /*InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity()..getWindowToken(), 0);*/
        login = binding.login.getText().toString();
        password = binding.password.getText().toString();
        if (!validateFields(login, password)) return;
        binding.expandElements.setVisibility(View.GONE);
        binding.expandElements2.setVisibility(View.VISIBLE);
    }

    private boolean validateFields(String username, String password) {
        if (!validateLogin(username) | !validatePass(password)) {
            return false;
        } else {
            binding.login.setError(null);
            binding.password.setError(null);
        }
        return true;
    }

    private boolean validatePass(String password) {
        if (password.isEmpty()) {
            binding.password.setError("Пустое поле");
            return false;
        } else {
            binding.password.setError(null);
        }
        return true;
    }

    private boolean validateLogin(String username) {
        if (username.isEmpty()) {
            binding.login.setError("Пустое поле");
            return false;
        } else {
            binding.login.setError(null);
        }
        return true;
    }

    void showToast(String message) {
        Toast toast = new Toast(requireContext());
        View view = getLayoutInflater().inflate(R.layout.toast_layout, null);
        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText(message);
        tvMessage.setTextColor(Color.YELLOW);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    private void getUser(String username, String password) {
        ApiService.getInstance().getApi().auth(username, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.d("Authorize response", "onResponse: запрос был успешным!");
                    reportAuth("Вы успешно авторизированны");
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
                    (requireActivity()).getPreferences(Context.MODE_PRIVATE).edit().putString(getString(R.string.user_key), username).commit();// Navigate to the next Fragment
                    (requireActivity()).getPreferences(Context.MODE_PRIVATE).edit().putInt("CURATOR_ID", response.body().getId()).commit();// Navigate to the next Fragment
                    Toast.makeText(requireContext(), "Вы вошли в систему", Toast.LENGTH_LONG).show();

                } else {
                    Log.d("Authorize response", "onResponse: запрос был не успешным");
                    reportAuth("Произошла ошибка");
                    binding.expandElements.setVisibility(View.VISIBLE);
                    binding.expandElements2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("Authorize response", "onFailure: +");
                Toast.makeText(requireContext(), "Произошла ошибка", Toast.LENGTH_LONG).show();
                binding.expandElements.setVisibility(View.VISIBLE);
                binding.expandElements2.setVisibility(View.GONE);
            }
        });
    }

    private void reportAuth(String msg) {
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Ошибка").setMessage(msg)
                .setPositiveButton("Ок", null);
        builder.show();
        //showToast(msg);
    }
}
