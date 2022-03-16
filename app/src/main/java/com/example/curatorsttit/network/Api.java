package com.example.curatorsttit.network;

import com.example.curatorsttit.models.Student;
import com.example.curatorsttit.models.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    @GET("user19/api//Students")
    Call<List<Student>> getStudentByGroupId(@Query("idGroup") int id);

    @GET("user19/api/Users")
    Call<List<Users>> getAllUsers();

    @GET("user19/api/Users")
    Call<Users> auth(@Query("login") String login, @Query("passsword") String password);

    /*
    @GET("/quotes")
    Call<List<Quotes>> getQuotes();

    @GET("/feelings")
    Call<List<Feelings>> getFeelings();

    @POST("/user/login")
    Call<User> authorize(@Field("email") String email, @Field("password") String password);
    */
}
