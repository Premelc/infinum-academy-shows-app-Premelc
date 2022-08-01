package com.premelc.shows_dominik_premelc.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReviewsDao {
    @Query("SELECT * FROM review WHERE showId is :showId")
    fun getAllReviews(showId: Int): LiveData<List<ReviewEntity>>

    @Query("SELECT * FROM review WHERE showId is :showId")
    suspend fun getAllTheReviews(showId: Int): List<ReviewEntity>

    @Query("SELECT * FROM review WHERE id is :reviewId")
    fun getReview(reviewId: String): LiveData<ReviewEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(reviewList: List<ReviewEntity>)
}