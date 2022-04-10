package com.example.curatorsttit.network;

import com.example.curatorsttit.models.Group;
import com.example.curatorsttit.models.Person;
import com.example.curatorsttit.models.Student;
import com.example.curatorsttit.models.StudentData;
import com.example.curatorsttit.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("/api/Users")
    Call<List<User>> getAllUsers();
    @GET("/api/Users")
    Call<User> auth(@Query("login") String login, @Query("passsword") String password);
    @GET("/api/Persons")
    Call<List<Person>> getAllPersons();
    @GET("/api/Persons")
    Call<List<Person>> getStudentsByGroup(@Query("groupId") int groupId);

    @GET("/api/Students")
    Call<StudentData> getStudentDataById(@Query("personId") int personId);

    @GET("/api/Groups")
    Call<List<Group>> getGroupsByCuratorId(@Query("userId") int userId);

}
