package com.premelc.shows_dominik_premelc.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(listUsers: List<UserEntity>)

    @Query("SELECT * FROM user WHERE id is :userId")
    fun getUser(userId: String):LiveData<UserEntity>
}