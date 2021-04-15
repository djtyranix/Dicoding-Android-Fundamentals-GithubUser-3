package com.nixstudio.githubuser3.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nixstudio.githubuser3.model.Favorite

@Database(entities = arrayOf(Favorite::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "favorite-user-database")
            .build()
    }

    abstract fun favoriteDao(): FavoriteDao
}