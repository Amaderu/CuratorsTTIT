package com.example.curatorsttit.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.curatorsttit.data.local.entity.UserEntity;

import java.util.List;

@Dao
public interface UserDao {
        @Query("SELECT * FROM users")
        LiveData<List<UserEntity>> loadAllUsers();

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertAll(List<UserEntity> users);

        @Query("select * from users where id = :userId")
        LiveData<UserEntity> loadUser(int userId);

        @Query("select * from users where id = :userId")
        UserEntity loadUserSync(int userId);

        @Query("SELECT users.* , persons.surname, persons.name, persons.patronymic FROM users INNER JOIN persons ON (users.personId = persons.id)")
        LiveData<List<UserEntity>> userInf();
        /*@Query("SELECT users.* FROM products JOIN productsFts ON (products.id = productsFts.rowid) "
                + "WHERE productsFts MATCH :query")
        LiveData<List<UserEntity>> searchAllProducts(String query);*/
}
