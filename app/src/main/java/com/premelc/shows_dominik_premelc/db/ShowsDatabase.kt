package com.premelc.shows_dominik_premelc.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ShowEntity::class,
        ReviewEntity::class
    ],
    version = 7
)
abstract class ShowsDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: ShowsDatabase? = null

        fun getDatabase(context: Context): ShowsDatabase {
            return INSTANCE ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context,
                    ShowsDatabase::class.java,
                    "shows_db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = database
                database
            }
        }
    }

    abstract fun showsDAO(): ShowsDao
    abstract fun reviewsDAO(): ReviewsDao
}