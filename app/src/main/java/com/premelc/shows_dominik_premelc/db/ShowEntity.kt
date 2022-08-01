package com.premelc.shows_dominik_premelc.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "show")
data class ShowEntity(
    @ColumnInfo(name = "id") @PrimaryKey val id: String,
    @ColumnInfo(name = "rating") val averageRating: Int?,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image") val imageUrl: String,
    @ColumnInfo(name = "reviewsNumber") val noOfReviews: Int,
    @ColumnInfo(name = "title") val title: String
)