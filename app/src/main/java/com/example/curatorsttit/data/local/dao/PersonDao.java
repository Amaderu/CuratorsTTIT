package com.example.curatorsttit.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.curatorsttit.data.local.entity.PersonEntity;

import java.util.List;

@Dao
public interface PersonDao {
        @Query("SELECT * FROM persons")
        LiveData<List<PersonEntity>> loadAllPersons();

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertAll(List<PersonEntity> persons);

        @Query("select * from persons where id = :personId")
        LiveData<PersonEntity> loadPerson(int personId);

        @Query("select * from persons where id = :personId")
        PersonEntity loadPersonSync(int personId);
}
