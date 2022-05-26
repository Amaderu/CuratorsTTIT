package com.example.curatorsttit.network;

import com.example.curatorsttit.models.Group;
import com.example.curatorsttit.models.Person;
import com.example.curatorsttit.models.Student;
import com.example.curatorsttit.models.StudentData;
import com.example.curatorsttit.models.User;
import com.example.curatorsttit.models.events.EventData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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


    //Events


    //c
    @POST("/api/Events/{eventId}")
    Call<EventData> postEventDataById(@Path("eventId") int eventId);

    //r
    @GET("/api/Events/{eventId}")
    Call<EventData> getEventDataById(@Query("eventId") int eventId);

    //u -all
    @PUT("/api/Events/{eventId}")
    Call<EventData> putEventDataById(@Path("eventId") int eventId, @Body EventData body);

    //u
    @PATCH("/api/Events/{eventId}")
    Call<EventData> patchEventDataById(@Path("eventId") int eventId, @Body EventData body);

    //d
    @DELETE("/api/Events/{eventId}")
    public Call<EventData> deleteEventDataById(@Path("eventId") int eventId);
}
