package com.example.curatorsttit.network;

import com.example.curatorsttit.models.Group;
import com.example.curatorsttit.models.Person;
import com.example.curatorsttit.models.Student;
import com.example.curatorsttit.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("/api/Students")
    Call<List<Student>> getStudentByGroupId(@Query("idGroup") int id);

    @GET("/api/Users")
    Call<List<User>> getAllUsers();
    @GET("/api/Persons")
    Call<List<Person>> getStudentsByGroup(@Query("groupId") int groupId);
    @GET("/api/Groups")
    Call<List<Group>> getGroupsByCuratorId(@Query("userId") int userId);

    @GET("/api/Users")
    Call<User> auth(@Query("login") String login, @Query("passsword") String password);

    /*
    @GET("/quotes")
    Call<List<Quotes>> getQuotes();

    @GET("/feelings")
    Call<List<Feelings>> getFeelings();

    @POST("/user/login")
    Call<User> authorize(@Field("email") String email, @Field("password") String password);
    */
}
