package com.premelc.shows_dominik_premelc.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShowsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllShows(shows: List<ShowEntity>)

    @Query("SELECT * FROM show WHERE id IS :showId")
    suspend fun getShow(showId: String): ShowEntity

    @Query("SELECT * FROM show ")
    suspend fun getAllShows(): List<ShowEntity>

}