package com.example.curatorsttit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.curatorsttit.databinding.ActivitySplashBinding;
import com.example.curatorsttit.models.Users;
import com.example.curatorsttit.network.ApiService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    Intent intent;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        /*ImageView rocketImage = (ImageView) findViewById(R.id.progress);
        rocketImage.setBackgroundResource(R.drawable.ic_progress);

        rocketAnimation = rocketImage.getBackground();
        if (rocketAnimation instanceof Animatable) {
            ((Animatable)rocketAnimation).start();
        }*/
        ImageView backgroundImage  = binding.progressDrawable;
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        backgroundImage.startAnimation(rotateAnimation);
        if(binding.progressBar.getProgress()==100){
            handler.postDelayed(() -> startActivity(intent),3000l);
        }
        intent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ApiService.getInstance().getApi().auth("kokin", "QWEasd").enqueue(new Callback<Users>() {

            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if(response.isSuccessful()){
                    builder.setTitle("tittle").setMessage(String.valueOf(response.code()))
                            .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(intent);
                                }
                            });
                /*.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });*/
                    // Create the AlertDialog object and return it
                    builder.create().show();
                    Toast.makeText(SplashActivity.this,"Вы Вошли в систему",Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(SplashActivity.this,"Произошла ошибка",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(SplashActivity.this,"Ошибка - отсутствие интернета",Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}