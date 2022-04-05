package com.example.curatorsttit.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.curatorsttit.data.local.entity.RoleEntity;

import java.util.List;

@Dao
public interface RoleDao {
        @Query("SELECT * FROM roles")
        LiveData<List<RoleEntity>> loadAllRoles();

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertAll(List<RoleEntity> roles);

        @Query("select * from roles where id = :roleId")
        LiveData<RoleEntity> loadRole(int roleId);

        @Query("select * from roles where id = :roleId")
        RoleEntity loadRoleSync(int roleId);
}
